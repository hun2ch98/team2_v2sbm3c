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
   * @param model
   * @param BoardVO
   * @param boardno
   * @return
   */
  @GetMapping(value = "/create")
  public String create(Model model, 
      @ModelAttribute("boardVO") BoardVO boardVO, 
      @RequestParam(name="memberno", defaultValue="0") int memberno) {
//    ArrayList<DiaryVOMenu> menu = this.diaryProc.menu();  // 일기 메뉴 클래스 생성 필요
//    model.addAttribute("menu", menu);

    MemberVO memberVO = this.memberProc.read(memberno); // 카테고리 정보를 출력하기위한 목적
    model.addAttribute("MemberVO", memberVO);

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

    if (memberProc.isMember(session)) { // 로그인한경우
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image
      String file1saved = ""; // 저장된 파일명, image
      String thumb1 = ""; // preview image

      String upDir = Board.getUploadDir(); // 파일을 업로드할 폴더 준비
      // upDir = upDir + "/" + 한글을 제외한 카테고리 이름
      System.out.println("-> upDir: " + upDir);

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = boardVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명 산출, 01.jpg
      System.out.println("-> 원본 파일명 산출 file1: " + file1);

      long size1 = mf.getSize(); // 파일 크기
      if (size1 > 0) { // 파일 크기 체크, 파일을 올리는 경우
        if (Tool.checkUploadFile(file1) == true) { // 업로드 가능한 파일인지 검사
          // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg, spring_2.jpg...
          file1saved = Upload.saveFileSpring(mf, upDir);

          if (Tool.isImage(file1saved)) { // 이미지인지 검사
            // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
            thumb1 = Tool.preview(upDir, file1saved, 200, 150);
          }

          boardVO.setFile1(file1); // 순수 원본 파일명
          boardVO.setFile1saved(file1saved); // 저장된 파일명(파일명 중복 처리)
          boardVO.setThumb1(thumb1); // 원본이미지 축소판
          boardVO.setSize1(size1); // 파일 크기

        } else { // 전송 못하는 파일 형식
          ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 할 수 없는 파일
          ra.addFlashAttribute("cnt", 0); // 업로드 실패
          ra.addFlashAttribute("url", "/board/msg"); // msg.html, redirect parameter 적용
          return "redirect:/board/msg"; // Post -> Get - param...
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }

      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------

      // Call By Reference: 메모리 공유, Hashcode 전달
      int memberno = (int) session.getAttribute("memberno"); // memberno FK
      boardVO.setMemberno(memberno);
      int cnt = this.boardProc.create(boardVO);

      // ------------------------------------------------------------------------------
      // PK의 return
      // ------------------------------------------------------------------------------
      // System.out.println("--> contentsno: " + contentsVO.getContentsno());
      // mav.addObject("contentsno", contentsVO.getContentsno()); // redirect
      // parameter 적용
      // ------------------------------------------------------------------------------

      if (cnt == 1) {
        // type 1, 재업로드 발생
        // return "<h1>파일 업로드 성공</h1>"; // 연속 파일 업로드 발생

        // type 2, 재업로드 발생
        // model.addAttribute("cnt", cnt);
        // model.addAttribute("code", "create_success");
        // return "contents/msg";

        // type 3 권장
        // return "redirect:/contents/list_all"; // /templates/contents/list_all.html

        // System.out.println("-> contentsVO.getCateno(): " + contentsVO.getCateno());
        // ra.addFlashAttribute("diaryno", contentsVO.getCateno()); // controller ->
        // controller: X

        ra.addAttribute("memberno", boardVO.getMemberno()); // controller -> controller: O
        return "redirect:/board/list_by_diaryno";

        // return "redirect:/contents/list_by_cateno?cateno=" + contentsVO.getCateno();
        // // /templates/contents/list_by_cateno.html
      } else {
        ra.addFlashAttribute("code", "create_fail"); // DBMS 등록 실패
        ra.addFlashAttribute("cnt", 0); // 업로드 실패
        ra.addFlashAttribute("url", "/board/msg"); // msg.html, redirect parameter 적용
        return "redirect:/board/msg"; // Post -> Get - param...
      }
    } else { // 로그인 실패 한 경우
      return "redirect:/member/login_cookie_need"; // /member/login_cookie_need.html
    }
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

    if (this.memberProc.isMemberAdmin(session)) { // 관리자만 조회 가능
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

    } else {
      return "redirect:/member/login_cookie_need";

    }

  }
  
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

    if (this.memberProc.isMember(session)) { // 관리자로 로그인한경우
      BoardVO boardVO = this.boardProc.read(boardno);
      model.addAttribute("boardVO", boardVO);

      MemberVO memberVO = this.memberProc.read(boardVO.getMemberno());
      model.addAttribute("memberVO", memberVO);

      return "/board/update_text"; // /templates/contents/update_text.html
      // String content = "장소:\n인원:\n준비물:\n비용:\n기타:\n";
      // model.addAttribute("content", content);

    } else {
//      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
//      return "redirect:/contents/msg"; // @GetMapping(value = "/read")
      return "member/login_cookie_need";
    }

  }

  /**
   * 게시글 수정 처리 
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(HttpSession session, 
      Model model, 
      @ModelAttribute("boardVO") BoardVO boardVO, 
      RedirectAttributes ra,
      @RequestParam(name="search_word", defaultValue="") String search_word, // contentsVO.word와 구분 필요
      @RequestParam(name="now_page", defaultValue="0") int now_page) {
      ra.addAttribute("word", search_word);
      ra.addAttribute("now_page", now_page);

    if (this.memberProc.isMember(session)) { // 관리자 로그인 확인
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("contentsno", boardVO.getBoardno());

//      if (this.boardProc.password_check(map) == 1) { // 패스워드 일치
        this.boardProc.update_text(boardVO); // 글수정

        // mav 객체 이용
        ra.addAttribute("boardno", boardVO.getBoardno());
        ra.addAttribute("memberno", boardVO.getMemberno());
        return "redirect:/board/read"; // @GetMapping(value = "/read")

      } else { // 패스워드 불일치
        ra.addFlashAttribute("code", "passwd_fail"); // redirect -> forward -> html
        ra.addFlashAttribute("cnt", 0);
        ra.addAttribute("url", "/board/msg"); // msg.html, redirect parameter 적용

        return "redirect:/board/post2get"; // @GetMapping(value = "/msg")
      }
//    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
//      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
//      return "redirect:/board/post2get"; // @GetMapping(value = "/msg")
//    }

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

    if (this.memberProc.isMember(session)) {
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
    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/board/post2get"; // GET
    }
  }
  
  /**
   * 파일 삭제 폼
   * http://localhost:9091/contents/delete?contentsno=1
   * 
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
                               @RequestParam(name="memberno", defaultValue="") int memberno, 
                               @RequestParam(name="boardno", defaultValue="0") int boardno, 
                               @RequestParam(name="word", defaultValue="") String word, 
                               @RequestParam(name="now_page", defaultValue="1") int now_page) {
    if (this.memberProc.isMember(session)) { // 로그인한경우
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
      
    } else {
      ra.addAttribute("url", "/member/login_cookie_need");
      return "redirect:/board/msg"; 
    }

  }
  
  /**
   * 삭제 처리 http://localhost:9091/contents/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra,
      @RequestParam(name="memberno", defaultValue="") int memberno, 
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
    
    return "redirect:/board/list_by_cateno";    
    
  }   


}
