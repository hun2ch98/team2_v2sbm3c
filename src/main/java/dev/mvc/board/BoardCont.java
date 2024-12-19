package dev.mvc.board;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.member.MemberVO;
import dev.mvc.board.Board;
import dev.mvc.board.BoardVO;
import dev.mvc.diary.DiaryProcInter;
import dev.mvc.board.BoardVOMenu;
import dev.mvc.diary.DiaryVO;
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
  @Qualifier("dev.mvc.diary.DiaryProc") 
  private DiaryProcInter diaryProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") 
  private MemberProcInter memberProc;
  
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
//    ArrayList<BoardVOMenu> menu = this.diaryProc.menu();
//    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }
  
  /**
   * 게시글 생성
   * @param BoardVO
   * @param boardno
   * @return
   */
  @GetMapping(value = "/create")
  public String create(Model model, 
      @ModelAttribute("boardVO") BoardVO boardVO, 
      @RequestParam(name = "memberno", defaultValue = "1") int memberno) {

      boardVO.setMemberno(memberno); // 기본값 설정
      model.addAttribute("BoardVO", boardVO); // 수정된 BoardVO 전달

      return "/board/create"; // /templates/contents/create.html
  }

  /**
   * 처리 
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpServletRequest request, 
                       HttpSession session, 
                       Model model, 
                       @ModelAttribute("boardVO") BoardVO boardVO,
                       RedirectAttributes ra) {

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

      int memberno = 1; 
      boardVO.setMemberno(memberno); 

      int cnt = this.boardProc.create(boardVO);
      if (cnt == 1) {
          ra.addAttribute("boardno", boardVO.getBoardno()); 
          ra.addAttribute("now_page", 1); 
          return "redirect:/board/list_by_boardno"; 
      } else {
          ra.addFlashAttribute("code", "create_fail");
          return "redirect:/board/msg"; 
      }
  }
  
  /**
   * 게시글 목록 페이지
   * @param boardno
   * @param model
   * @return
   */
  @GetMapping("/list_by_boardno")
  public String listByBoardno(@RequestParam(name = "boardno", defaultValue = "0") int boardno, Model model) {
      ArrayList<BoardVO> list = this.boardProc.list_by_boardno(boardno);
      model.addAttribute("list", list);
      return "/board/list_by_boardno";
  }

  
  /**
   * 전체 목록
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
    // System.out.println("-> list_all");
//    ArrayList<DiaryVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);

//    if (this.memberProc.isMemberAdmin(session)) { // 관리자만 조회 가능
      ArrayList<BoardVO> list = this.boardProc.list_all(); // 모든 목록

      // Thymeleaf는 CSRF(크로스사이트) 스크립팅 해킹 방지 자동 지원
      // for문을 사용하여 객체를 추출, Call By Reference 기반의 원본 객체 값 변경
//      for (ContentsVO contentsVO : list) {
//        String title = contentsVO.getTitle();
//        String content = contentsVO.getContent();
//        
//        title = Tool.convertChar(title);  // 특수 문자 처리
//        content = Tool.convertChar(content); 
//        
//        contentsVO.setTitle(title);
//        contentsVO.setContent(content);  
//
//      }

      model.addAttribute("list", list);
      return "/board/list_all";

//    } else {
//      return "redirect:/member/login_cookie_need";
//
//    }

  }
  
  /**
   * 유형 3
   * 카테고리별 목록 + 검색 + 페이징 http://localhost:9091/contents/list_by_cateno?cateno=5
   * http://localhost:9091/contents/list_by_cateno?cateno=6
   * 
   * @return
   */
//  @GetMapping(value = "/list_by_boardno")
//  public String list_by_boardno_search_paging(
//      HttpSession session, 
//      Model model, 
//      @RequestParam(name = "memberno", defaultValue = "1") int memberno,
//      @RequestParam(name = "word", defaultValue = "") String word,
//      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
//
//    // System.out.println("-> cateno: " + cateno);
//
////    ArrayList<BoardVOMenu> menu = this.boardProc.menu();
////    model.addAttribute("menu", menu);
//
//    MemberVO memberVO = this.memberProc.read(memberno);
//    model.addAttribute("memberVO", memberVO);
//
//    word = Tool.checkNull(word).trim();
//
//    HashMap<String, Object> map = new HashMap<>();
//    map.put("memberno", memberno);
//    map.put("word", word);
//    map.put("now_page", now_page);
//
//    ArrayList<BoardVO> list = this.boardProc.list_by_boardno_search_paging(map);
//    model.addAttribute("list", list);
//
//    // System.out.println("-> size: " + list.size());
//    model.addAttribute("word", word);
//
//    int search_count = this.boardProc.list_by_cateno_search_count(map);
//    String paging = this.boardProc.pagingBox(cateno, now_page, word, "/contents/list_by_cateno", search_count,
//        Board.RECORD_PER_PAGE, Board.PAGE_PER_BLOCK);
//    model.addAttribute("paging", paging);
//    model.addAttribute("now_page", now_page);
//
//    model.addAttribute("search_count", search_count);
//
//    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
//    int no = search_count - ((now_page - 1) * Board.RECORD_PER_PAGE);
//    model.addAttribute("no", no);
//
//    return "/contents/list_by_cateno_search_paging"; // /templates/contents/list_by_cateno_search_paging.html
//  }

  
  /**
   * 게시글 조회 
   * @return
   */
  @GetMapping(value = "/read")
  public String read(Model model, 
      @RequestParam(name="boardno", defaultValue = "0") int boardno, 
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);

    BoardVO boardVO = this.boardProc.read(boardno);

//    String title = contentsVO.getTitle();
//    String content = contentsVO.getContent();
//    
//    title = Tool.convertChar(title);  // 특수 문자 처리
//    content = Tool.convertChar(content); 
//    
//    contentsVO.setTitle(title);
//    contentsVO.setContent(content);  

    long size1 = boardVO.getSize1();
    String size1_label = Tool.unit(size1);
    boardVO.setSize1_label(size1_label);

    model.addAttribute("boardVO", boardVO);

    MemberVO memberVO = this.memberProc.read(boardVO.getMemberno());
    model.addAttribute("memberVO", memberVO);

    // 조회에서 화면 하단에 출력
    // ArrayList<ReplyVO> reply_list = this.replyProc.list_contents(contentsno);
    // mav.addObject("reply_list", reply_list);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

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
      @RequestParam(name="word", defaultValue="") String word,
      @RequestParam(name="now_page", defaultValue="1") int now_page) {
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

//    if (this.memberProc.isMember(session)) { // 관리자로 로그인한경우
      BoardVO boardVO = this.boardProc.read(boardno);
      model.addAttribute("boardVO", boardVO);

      MemberVO memberVO = this.memberProc.read(boardVO.getMemberno());
      model.addAttribute("memberVO", memberVO);

      return "/board/update_text"; // /templates/contents/update_text.html
      // String content = "장소:\n인원:\n준비물:\n비용:\n기타:\n";
      // model.addAttribute("content", content);

//    } else {
////      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
////      return "redirect:/contents/msg"; // @GetMapping(value = "/read")
//      return "member/login_cookie_need";
//    }

  }

  /**
   * 게시글 수정 처리
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(
          HttpSession session, 
          Model model, 
          @ModelAttribute("boardVO") BoardVO boardVO, 
          RedirectAttributes ra,
          @RequestParam(name = "search_word", defaultValue = "") String search_word, 
          @RequestParam(name = "now_page", defaultValue = "0") int now_page) {

      // Redirect 시 검색어 및 현재 페이지를 유지하기 위한 파라미터 추가
      ra.addAttribute("word", search_word);
      ra.addAttribute("now_page", now_page);

      // bcontent 값 검증
      if (boardVO.getBcontent() == null || boardVO.getBcontent().trim().isEmpty()) {
          ra.addFlashAttribute("message", "내용은 필수 입력 사항입니다.");
          ra.addFlashAttribute("code", "update_fail");
          return "redirect:/board/msg"; // 실패 시 msg 페이지로 이동
      }

      // 글 수정 처리
      try {
          int cnt = this.boardProc.update_text(boardVO); // 글 수정
          if (cnt > 0) { // 수정 성공
              ra.addAttribute("boardno", boardVO.getBoardno());
              return "redirect:/board/read"; // 성공 시 게시글 조회 페이지로 이동
          } else { // 수정 실패
              ra.addFlashAttribute("message", "게시글 수정에 실패했습니다.");
              ra.addFlashAttribute("code", "update_fail");
              return "redirect:/board/msg"; // 실패 시 msg 페이지로 이동
          }
      } catch (Exception e) {
          e.printStackTrace();
          ra.addFlashAttribute("message", "글 수정 중 오류가 발생했습니다.");
          ra.addFlashAttribute("code", "update_fail");
          return "redirect:/board/msg"; // 오류 발생 시 msg 페이지로 이동
      }
  }


  
  /**
   * 파일 수정 폼 http://localhost:9091/contents/update_file?contentsno=1
   * 
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, 
         @RequestParam(name="boardno", defaultValue="0") int boardno,
         @RequestParam(name="word", defaultValue="") String word, 
         @RequestParam(name="now_page", defaultValue="1") int now_page) {
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    BoardVO boardVO = this.boardProc.read(boardno);
    model.addAttribute("boardVO", boardVO);

    MemberVO memberVO = this.memberProc.read(boardVO.getMemberno());
    model.addAttribute("memberVO", memberVO);


    return "/board/update_file";

  }

  /**
   * 파일 수정 처리 http://localhost:9091/contents/update_file
   * 
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, RedirectAttributes ra,
                            @ModelAttribute("boardVO") BoardVO boardVO,
                            @RequestParam(name="word", defaultValue="") String word, 
                            @RequestParam(name="now_page", defaultValue="1") int now_page) {

//    if (this.memberProc.isMember(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      BoardVO boardVO_old = boardProc.read(boardVO.getBoardno());

      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      String file1saved = boardVO_old.getFile1saved(); // 실제 저장된 파일명
      String thumb1 = boardVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = Board.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/contents/storage/

      Tool.deleteFile(upDir, file1saved); // 실제 저장된 파일삭제
      Tool.deleteFile(upDir, thumb1); // preview 이미지 삭제
      // -------------------------------------------------------------------
      // 파일 삭제 종료
      // -------------------------------------------------------------------

      // -------------------------------------------------------------------
      // 파일 전송 시작
      // -------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = boardVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명
      size1 = mf.getSize(); // 파일 크기

      if (size1 > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        file1saved = Upload.saveFileSpring(mf, upDir);

        if (Tool.isImage(file1saved)) { // 이미지인지 검사
          // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
          thumb1 = Tool.preview(upDir, file1saved, 250, 200);
        }

      } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
        file1 = "";
        file1saved = "";
        thumb1 = "";
        size1 = 0;
      }

      boardVO.setFile1(file1);
      boardVO.setFile1saved(file1saved);
      boardVO.setThumb1(thumb1);
      boardVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      this.boardProc.update_file(boardVO); // Oracle 처리
      ra.addAttribute ("boardno", boardVO.getBoardno());
      ra.addAttribute("memberno", boardVO.getMemberno());
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/board/read";
//    } else {
//      ra.addAttribute("url", "/member/login_cookie_need"); 
//      return "redirect:/board/post2get"; // GET
//    }
  }
  
  /**
   * 파일 삭제 폼
   * http://localhost:9091/contents/delete?contentsno=1
   * 
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
                               @RequestParam(name="memberno", defaultValue="0") int memberno, 
                               @RequestParam(name="boardno", defaultValue="0") int boardno, 
                               @RequestParam(name="word", defaultValue="") String word, 
                               @RequestParam(name="now_page", defaultValue="1") int now_page) {
//    if (this.memberProc.isMember(session)) { // 로그인한경우
      model.addAttribute("memberno", memberno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
//      ArrayList<CateVOMenu> menu = this.cateProc.menu();
//      model.addAttribute("menu", menu);
      
      BoardVO boardVO = this.boardProc.read(boardno);
      model.addAttribute("boardVO", boardVO);

      MemberVO memberVO = this.memberProc.read(boardVO.getMemberno());
      model.addAttribute("memberVO", memberVO);
      
      return "/board/delete"; // forward
      
//    } else {
//      ra.addAttribute("url", "/member/login_cookie_need");
//      return "redirect:/board/msg"; 
//    }

  }
  
  /**
   * 삭제 처리 http://localhost:9091/contents/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra,
      @RequestParam(name="memberno", defaultValue="0") int memberno, 
      @RequestParam(name="boardno", defaultValue="0") int boardno, 
      @RequestParam(name="word", defaultValue="") String word, 
      @RequestParam(name="now_page", defaultValue="1") int now_page) {
    // -------------------------------------------------------------------
    // 파일 삭제 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
    BoardVO boardVO_read = boardProc.read(boardno);
        
    String file1saved = boardVO_read.getFile1saved();
    String thumb1 = boardVO_read.getThumb1();
    
    String uploadDir = Board.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
    Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
    // -------------------------------------------------------------------
    // 파일 삭제 종료
    // -------------------------------------------------------------------
        
    this.boardProc.delete(boardno); // DBMS 삭제
        
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------    
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("memberno", memberno);
    map.put("word", word);
    
//    if (this.boardProc.list_by_cateno_search_count(map) % Board.RECORD_PER_PAGE == 0) {
//      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
//      if (now_page < 1) {
//        now_page = 1; // 시작 페이지
//      }
//    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("memberno", memberno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/board/list_by_boardno";    
    
  }   


}
