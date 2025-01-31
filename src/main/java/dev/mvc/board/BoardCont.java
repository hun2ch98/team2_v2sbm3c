package dev.mvc.board;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.member.MemberVO;
import dev.mvc.board.Board;
import dev.mvc.board.BoardVO;
import dev.mvc.boardgood.BoardgoodProcInter;
import dev.mvc.boardgood.BoardgoodVO;
import dev.mvc.log.LogProcInter;
import dev.mvc.log.LogVO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/board")
public class BoardCont {

  @Autowired
  @Qualifier("dev.mvc.board.BoardProc")
  private BoardProcInter boardProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") 
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.boardgood.BoardgoodProc")
  private BoardgoodProcInter boardgoodProc;

  /** 페이징 목록 주소 */
  private String list_file_name = "/board/list_by_boardno";
  
  @Autowired
  @Qualifier("dev.mvc.log.LogProc")
  private LogProcInter logProc;

  private void logAction(String action, String table, int memberno, String details, HttpServletRequest request, String is_success) {
      LogVO logVO = new LogVO();
      logVO.setMemberno(memberno);
      logVO.setTable_name(table);
      logVO.setAction(action);
      logVO.setDetails(details);
      logVO.setIp(getClientIp(request)); // IP 주소 설정
      logVO.setIs_success(is_success);
      logProc.create(logVO); // Log 테이블에 삽입
  }

  private String getClientIp(HttpServletRequest request) {
      String ip = request.getHeader("X-Forwarded-For");
      if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("Proxy-Client-IP");
      }
      if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("WL-Proxy-Client-IP");
      }
      if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getRemoteAddr();
      }
      return ip;
  }
  
  public BoardCont() {
    System.out.println("-> BoardCont created.");
  }
  
  
  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue="") String url) {

    return url; // forward, /templates/...
  }
  
  /**
   * 게시글 생성
   */
  @GetMapping(value = "/create")
  public String create(Model model, HttpSession session,
      @ModelAttribute("boardVO") BoardVO boardVO, 
      @RequestParam(name = "memberno", defaultValue = "1") int memberno) {
    if (memberProc.isMember(session)) { // 회원 로그인한경우

      boardVO.setMemberno(memberno); // 기본값 설정
      model.addAttribute("BoardVO", boardVO); // 수정된 BoardVO 전달

      return "/board/create"; // /templates/board/create.html
      }else { // 로그인 실패 한 경우
    return "redirect:/member/login_cookie_need"; // /member/login_cookie_need.html
    }
  }

  /**
   * 등록 처리 
   */
  @PostMapping(value = "/create")
  public String create(HttpServletRequest request, 
                       HttpSession session, 
                       Model model, 
                       @ModelAttribute("boardVO") BoardVO boardVO,
                       RedirectAttributes ra) {
    int memberno = (int) session.getAttribute("memberno");
    if (memberProc.isMember(session)) { // 회원 로그인한경우

      String file1 = ""; 
      String file1saved = ""; 
      String thumb1 = ""; 
      long size1 = 0;

      String upDir = Board.getUploadDir(); 

      MultipartFile mf = boardVO.getFile1MF(); 
      if (mf != null && !mf.isEmpty()) { 
          file1 = mf.getOriginalFilename();
          size1 = mf.getSize();

          if (Tool.checkUploadFile(file1)) { 
              file1saved = Upload.saveFileSpring(mf, upDir); 
              if (Tool.isImage(file1saved)) {
                  thumb1 = Tool.preview(upDir, file1saved, 200, 150); 
              }
          } else {
              ra.addFlashAttribute("code", "check_upload_file_fail");
              return "redirect:/board/msg"; 
          }
      }

      boardVO.setFile1(file1);
      boardVO.setFile1saved(file1saved);
      boardVO.setThumb1(thumb1);
      boardVO.setSize1(size1);
      boardVO.setMemberno(memberno); 

      int cnt = this.boardProc.create(boardVO);
      if (cnt == 1) {
        logAction("create", "board", memberno, "title=" + boardVO.getTitle(), request, "Y");
          ra.addAttribute("boardno", boardVO.getBoardno()); 
          ra.addAttribute("now_page", 1); 
          return "redirect:/board/list_by_boardno_search_paging"; 
      } else {
        logAction("create", "board", memberno, "title=" + boardVO.getTitle(), request, "N");
          ra.addFlashAttribute("code", "create_fail");
          return "redirect:/board/msg"; 
      }
    } else { // 로그인 실패 한 경우
      return "redirect:/member/login_cookie_need"; // /member/login_cookie_need.html
    }
  }
  
  
  /**
   * 카테고리별 목록 + 검색 + 페이징 
   */
  @GetMapping(value = "/list_by_boardno_search_paging")
  public String list_by_boardno_search_paging(
      HttpSession session, 
      Model model, 
      @ModelAttribute("boardVO") BoardVO boardVO,
      @RequestParam(name = "boardno", defaultValue = "0") int boardno,
      @RequestParam(name = "board_cate", defaultValue = "") String board_cate,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
   
    if (memberProc.isMember(session)) { // 회원 로그인한경우

      int memberno = (int) session.getAttribute("memberno");
      MemberVO memberVO = this.memberProc.read(memberno);
      if (memberVO == null) {
          memberVO = new MemberVO();
          memberVO.setMemberno(0);
          model.addAttribute("message", "회원 정보가 없습니다.");
      }
      board_cate = Tool.checkNull(board_cate).trim();
      model.addAttribute("memberVO", memberVO);
      model.addAttribute("boardno", boardno);
      model.addAttribute("board_cate", board_cate);
      model.addAttribute("now_page", now_page);

      HashMap<String, Object> map = new HashMap<>();
      map.put("memberno", memberno);
      map.put("board_cate", board_cate);
      map.put("now_page", now_page);
      
      ArrayList<BoardVO> list = this.boardProc.list_by_boardno_search_paging(map);
      if (list == null || list.isEmpty()) {
          model.addAttribute("message", "게시물이 없습니다.");
      } else {
          model.addAttribute("list", list);
      }

      int search_count = this.boardProc.count_by_boardno_search(map);
      String paging = this.boardProc.pagingBox(memberno, now_page, board_cate, "/board/list_by_boardno_search_paging", search_count,
          Board.RECORD_PER_PAGE, Board.PAGE_PER_BLOCK);
      model.addAttribute("paging", paging);
      model.addAttribute("search_count", search_count);


      int no = search_count - ((now_page - 1) * Board.RECORD_PER_PAGE);
      model.addAttribute("no", no);

      return "/board/list_by_boardno_search_paging"; // /templates/board/list_by_boardno_search_paging.html
      } else { // 로그인 실패 한 경우
      return "redirect:/member/login_cookie_need"; // /member/login_cookie_need.html
    }
  }
  
  /**
   * 게시글 조회 
   */
  @GetMapping(value = "/read")
  public String read(Model model, HttpSession session, HttpServletRequest request, 
                     @RequestParam(name = "boardno", defaultValue = "0") int boardno, 
                     @RequestParam(name = "board_cate", defaultValue = "") String board_cate, 
                     @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
      BoardVO boardVO = this.boardProc.read(boardno);
      int memberno = (int) session.getAttribute("memberno");
      if (boardVO == null) {
        logAction("read", "board", memberno, "title= null" , request, "N");
          model.addAttribute("errorMessage", "해당 게시글을 찾을 수 없습니다.");
          return "errorPage"; // 적절한 에러 페이지
      }

      long size1 = boardVO.getSize1();
      String size1_label = Tool.unit(size1);
      boardVO.setSize1_label(size1_label);

      MemberVO memberVO = this.memberProc.read(boardVO.getMemberno());
      model.addAttribute("boardVO", boardVO);
      model.addAttribute("memberVO", memberVO);

      model.addAttribute("board_cate", board_cate);
      model.addAttribute("now_page", now_page);
      logAction("read", "board", memberno, "title=" + boardVO.getTitle(), request, "Y");
      
      //추천 관련
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("boardno", boardno);
      
      int heart_cnt = 0;
      if (session.getAttribute("memberno") != null) {  //회원인 경우만 카운트 처리
          map.put("memberno", memberno);

          heart_cnt = this.boardgoodProc.heart_cnt(map);
       }
      model.addAttribute("heart_cnt", heart_cnt);

      return "/board/read";
  }


  /**
   * 게시글 수정 폼 
   */
  @GetMapping(value = "/update_text")
  public String update_text(HttpSession session, 
      Model model, 
      @RequestParam(name="boardno", defaultValue="") int boardno, 
      RedirectAttributes ra, 
      @RequestParam(name="board_cate", defaultValue="") String board_cate,
      @RequestParam(name="now_page", defaultValue="1") int now_page) {

    model.addAttribute("board_cate", board_cate);
    model.addAttribute("now_page", now_page);

    if (this.memberProc.isMember(session)) { // 회원 로그인한경우
    
      BoardVO boardVO = this.boardProc.read(boardno);
      model.addAttribute("boardVO", boardVO);

      MemberVO memberVO = this.memberProc.read(boardVO.getMemberno());
      model.addAttribute("memberVO", memberVO);

      return "/board/update_text";
    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "member/login_cookie_need";
    }

  }

  /**
   * 게시글 수정 처리
   */
  @PostMapping(value = "/update_text")
  public String update_text(
          HttpSession session, HttpServletRequest request, 
          Model model, 
          @ModelAttribute("boardVO") BoardVO boardVO, 
          RedirectAttributes ra,
          @RequestParam(name = "board_cate", defaultValue = "") String board_cate, 
          @RequestParam(name = "now_page", defaultValue = "0") int now_page) {

      // Redirect 시 검색어 및 현재 페이지를 유지하기 위한 파라미터 추가
      ra.addAttribute("board_cate", board_cate);
      ra.addAttribute("now_page", now_page);
      int memberno = (int) session.getAttribute("memberno");
      // bcontent 값 검증
      if (boardVO.getBcontent() == null || boardVO.getBcontent().trim().isEmpty()) {
        logAction("read", "board", memberno, "title=" + boardVO.getTitle(), request, "N");
          ra.addFlashAttribute("message", "내용은 필수 입력 사항입니다.");
          ra.addFlashAttribute("code", "update_fail");
          return "redirect:/board/msg"; // 실패 시 msg 페이지로 이동
      }

      // 글 수정 처리
      try {
        int cnt = this.boardProc.update_text(boardVO); // 글 수정
        if (cnt > 0) { // 수정 성공
          logAction("update_text", "board", memberno, "title=" + boardVO.getTitle(), request, "Y");
            ra.addAttribute("boardno", boardVO.getBoardno());
            return "redirect:/board/read"; // 성공 시 게시글 조회 페이지로 이동
        } else { // 수정 실패
          logAction("update_text", "board", memberno, "title=" + boardVO.getTitle(), request, "N");
            ra.addFlashAttribute("message", "게시글 수정에 실패했습니다.");
            ra.addFlashAttribute("code", "update_fail");
            return "redirect:/board/msg"; // 실패 시 msg 페이지로 이동
        }
      } catch (Exception e) {
        e.printStackTrace();
        logAction("update_text", "board", memberno, "title=" + boardVO.getTitle(), request, "N");
        ra.addFlashAttribute("message", "글 수정 중 오류가 발생했습니다.");
        ra.addFlashAttribute("code", "update_fail");
        return "redirect:/board/msg"; // 오류 발생 시 msg 페이지로 이동
      }
  }


  
  /**
   * 파일 수정 폼 
   */
  @GetMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, 
         @RequestParam(name="boardno", defaultValue="0") int boardno,
         @RequestParam(name="board_cate", defaultValue="") String board_cate, 
         @RequestParam(name="now_page", defaultValue="1") int now_page) {
    
    if (this.memberProc.isMember(session)) {
    
      model.addAttribute("board_cate", board_cate);
      model.addAttribute("now_page", now_page);
      
      BoardVO boardVO = this.boardProc.read(boardno);
      model.addAttribute("boardVO", boardVO);
  
      MemberVO memberVO = this.memberProc.read(boardVO.getMemberno());
      model.addAttribute("memberVO", memberVO);
  
  
      return "/board/update_file";
      } else { // 로그인 실패 한 경우
      return "redirect:/member/login_cookie_need"; // /member/login_cookie_need.html
    }

  }

  /**
   * 파일 수정 처리 
   */
  @PostMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, RedirectAttributes ra,
                            @ModelAttribute("boardVO") BoardVO boardVO, HttpServletRequest request,
                            @RequestParam(name="board_cate", defaultValue="") String board_cate, 
                            @RequestParam(name="now_page", defaultValue="1") int now_page) {

    int memberno = (int) session.getAttribute("memberno");
    if (this.memberProc.isMember(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      BoardVO boardVO_old = boardProc.read(boardVO.getBoardno());

      // 파일 삭제 시작
      String file1saved = boardVO_old.getFile1saved(); // 실제 저장된 파일명
      String thumb1 = boardVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = Board.getUploadDir(); 

      Tool.deleteFile(upDir, file1saved); // 실제 저장된 파일삭제
      Tool.deleteFile(upDir, thumb1); // preview 이미지 삭제

      // 파일 전송 시작
      String file1 = ""; 
      MultipartFile mf = boardVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명
      size1 = mf.getSize(); // 파일 크기

      if (size1 > 0) { 
        file1saved = Upload.saveFileSpring(mf, upDir);

        if (Tool.isImage(file1saved)) { 
          thumb1 = Tool.preview(upDir, file1saved, 250, 200);
        }

      } else { 
        file1 = "";
        file1saved = "";
        thumb1 = "";
        size1 = 0;
      }

      boardVO.setFile1(file1);
      boardVO.setFile1saved(file1saved);
      boardVO.setThumb1(thumb1);
      boardVO.setSize1(size1);

      this.boardProc.update_file(boardVO); // Oracle 처리
      ra.addAttribute ("boardno", boardVO.getBoardno());
      ra.addAttribute("memberno", boardVO.getMemberno());
      ra.addAttribute("board_cate", board_cate);
      ra.addAttribute("now_page", now_page);
      logAction("update_file", "board", memberno, "title=" + boardVO.getTitle(), request, "Y");
      return "redirect:/board/read";
    } else {
      logAction("update_file", "board", memberno, "title=" + boardVO.getTitle(), request, "N");
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/board/post2get"; // GET
    }
  }
  
  /**
   * 파일 삭제 폼
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
                               @RequestParam(name="memberno", defaultValue="0") int memberno, 
                               @RequestParam(name="boardno", defaultValue="0") int boardno, 
                               @RequestParam(name="board_cate", defaultValue="") String board_cate, 
                               @RequestParam(name="now_page", defaultValue="1") int now_page) {
    if (this.memberProc.isMember(session)) { // 로그인한경우
      model.addAttribute("memberno", memberno);
      model.addAttribute("board_cate", board_cate);
      model.addAttribute("now_page", now_page);
      
      BoardVO boardVO = this.boardProc.read(boardno);
      model.addAttribute("boardVO", boardVO);

      MemberVO memberVO = this.memberProc.read(boardVO.getMemberno());
      model.addAttribute("memberVO", memberVO);
      
      return "/board/delete"; 
      
    } else {
      ra.addAttribute("url", "/member/login_cookie_need");
      return "redirect:/board/msg"; 
    }

  }
  
  /**
   * 삭제 처리 
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra, HttpSession session, HttpServletRequest request,
      @RequestParam(name="boardno", defaultValue="0") int boardno, 
      @RequestParam(name="board_cate", defaultValue="") String board_cate, 
      @RequestParam(name="now_page", defaultValue="1") int now_page) {
    int memberno = (int) session.getAttribute("memberno");

    // 파일 삭제 시작
    BoardVO boardVO_read = boardProc.read(boardno);
        
    String file1saved = boardVO_read.getFile1saved();
    String thumb1 = boardVO_read.getThumb1();
    
    String uploadDir = Board.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved);  
    Tool.deleteFile(uploadDir, thumb1);    
        
    this.boardProc.delete(boardno); 
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("memberno", memberno);
    map.put("board_cate", board_cate);

    ra.addAttribute("memberno", memberno);
    ra.addAttribute("board_cate", board_cate);
    ra.addAttribute("now_page", now_page);
    logAction("delete", "board", memberno, "title=" + boardVO_read.getTitle(), request, "Y");
    return "redirect:/board/list_by_boardno_search_paging";    
    
  }   
  
  /**
   * 요청사항 추천 처리 
   */
  @PostMapping(value = "/good")
  @ResponseBody
  public String good(HttpSession session,
      Model model, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); 
    
    JSONObject src = new JSONObject(json_src); // String -> JSON
    int boardno = (int)src.get("boardno"); // 값 가져오기
    System.out.println("-> boardno: " + boardno);
    
    if (this.memberProc.isMember(session)) { // 회원 로그인 확인
      // 추천을 한 상태인지 확인
      int memberno = (int)session.getAttribute("memberno");
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("boardno", boardno);
      map.put("memberno", memberno);
      
      int good_cnt = this.boardgoodProc.heart_cnt(map);
      System.out.println("-> good_cnt: " + good_cnt);
      
      if(good_cnt == 1) {
        System.out.println("-> 추천 해제: " + boardno + ' ' + memberno);
        BoardgoodVO boardgoodVO = this.boardgoodProc.readByboardmember(map);
        
        this.boardgoodProc.delete(boardgoodVO.getGoodno());  // 추천 삭제
        this.boardProc.decreasegoodcnt(boardno);    // 카운트 감소
        
      }else {
        System.out.println("-> 추천: " + boardno + ' ' + memberno);
        
        BoardgoodVO boardgoodVO_new = new BoardgoodVO();
        boardgoodVO_new.setBoardno(boardno);
        boardgoodVO_new.setMemberno(memberno);
        
        this.boardgoodProc.create(boardgoodVO_new);
        this.boardProc.increasegoodcnt(boardno);
      }
      
      // 추천 여부가 변경되어 다시 새로운 값을 읽어옴
      int heart_cnt = this.boardgoodProc.heart_cnt(map);
      int goodcnt = this.boardProc.read(boardno).getGoodcnt();
      
      JSONObject result = new JSONObject();
      result.put("isMember", 1);  // 로그인:1, 비회원:0
      result.put("heart_cnt", heart_cnt);  // 추천 여부, 추천:1, 비추천:0
      result.put("goodcnt", goodcnt);   // 추천인수
      
      System.out.println("-> result.toString(): " + result.toString());
      return result.toString();

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      JSONObject result = new JSONObject();
      result.put("isMember", 1);  // 로그인:1, 비회원:0
      
      System.out.println("-> result.toString(): " + result.toString());
      return result.toString();
    }

  }  
  



}
