package dev.mvc.emotion;

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

import dev.mvc.board.Board;
import dev.mvc.diary.DiaryProcInter;
import dev.mvc.diary.DiaryVO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.member.MemberVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/emotion")
public class EmotionCont {

  @Autowired
  @Qualifier("dev.mvc.emotion.EmotionProc")
  private EmotionProcInter emotionProc;
  
  @Autowired
  @Qualifier("dev.mvc.diary.DiaryProc") 
  private DiaryProcInter diaryProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") 
  private MemberProcInter memberProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/emotion/list_by_emono";
  
  public EmotionCont() {
    System.out.println("-> EmotionCont created.");
  }
  
  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue="") String url) {
//    ArrayList<emotionVOMenu> menu = this.diaryProc.menu();
//    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }
  
  /**
   * 감정 생성
   * @param emotionVO
   * @param emono
   * @return
   */
  @GetMapping(value = "/create")
  public String create(Model model, 
      @ModelAttribute("emotionVO") EmotionVO emotionVO, 
      @RequestParam(name = "memberno", defaultValue = "1") int memberno) {

	  emotionVO.setMemberno(memberno); // 기본값 설정
      model.addAttribute("EmotionVO", emotionVO); // 수정된 emotionVO 전달

      return "/emotion/create"; // /templates/contents/create.html
  }

  /**
   * 등록 처리 
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpServletRequest request, 
                       HttpSession session, 
                       Model model, 
                       @ModelAttribute("emotionVO") EmotionVO emotionVO,
                       RedirectAttributes ra) {
	  
	  if (memberProc.isMember(session)) { // 회원 로그인한경우
		  // ------------------------------------------------------------------------------
	      // 파일 전송 코드 시작
	      // ------------------------------------------------------------------------------
	        String file1 = ""; 
	        String file1saved = ""; 
	        String thumb1 = ""; 
	  
	        String upDir = Emotion.getUploadDir(); 
	        
	  
	        MultipartFile mf = emotionVO.getFile1MF(); 
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

	            emotionVO.setFile1(file1); // 순수 원본 파일명
	            emotionVO.setFile1saved(file1saved); // 저장된 파일명(파일명 중복 처리)
	            emotionVO.setThumb1(thumb1); // 원본이미지 축소판
	            emotionVO.setSize1(size1); // 파일 크기

	          } else { // 전송 못하는 파일 형식
	            ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 할 수 없는 파일
	            ra.addFlashAttribute("cnt", 0); // 업로드 실패
	            ra.addFlashAttribute("url", "/emotion/msg"); // msg.html, redirect parameter 적용
	            return "redirect:/emotion/msg"; // Post -> Get - param...
	          }
	        } else { // 글만 등록하는 경우
	          System.out.println("-> 글만 등록");
	        }

	        // ------------------------------------------------------------------------------
	        // 파일 전송 코드 종료
	        // ------------------------------------------------------------------------------
	        int memberno = 1; 
	        emotionVO.setMemberno(memberno); 
	  
	        int cnt = this.emotionProc.create(emotionVO);
	        if (cnt == 1) {
	            ra.addAttribute("emono", emotionVO.getEmono()); 
	            ra.addAttribute("now_page", 1); 
	            return "redirect:/emotion/list_by_emono"; 
	        } else {
	            ra.addFlashAttribute("code", "create_fail");
	            return "redirect:/emotion/msg"; 
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

      ArrayList<EmotionVO> list = this.emotionProc.list_all(); // 모든 목록

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
      return "/emotion/list_all";

  }
  
  /**
   * 감정 목록(회원)
   * @param model
   * @param emono
   * @param now_page
   * @return
   */
  @GetMapping(value = "/list_by_emono")
  public String list_by_emono(
          Model model,
          @RequestParam(name = "emono", defaultValue = "0") int emono,
          @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

//    if (this.diaryProc.isdiary(session)) {  // 회원 조회
        
        int record_per_page = 10; 
    
        HashMap<String, Object> map = new HashMap<>();
        map.put("startRow", (now_page - 1) * record_per_page + 1);
        map.put("endRow", now_page * record_per_page);
        map.put("emono", emono);
        
        ArrayList<EmotionVO> list = this.emotionProc.list_by_emono(emono);
        if (list == null || list.isEmpty()) {
            model.addAttribute("message", "등록된 감정이 없습니다.");
        } else {
            model.addAttribute("list", list);
        }
  
        model.addAttribute("now_page", now_page);
        return "/emotion/list_by_emono";

    //  } else {
    //  return "redirect:/diary/login_cookie_need";
    //}
    }


//  
//  /**
//   * 유형 3
//   * 카테고리별 목록 + 검색 + 페이징 http://localhost:9091/contents/list_by_cateno?cateno=5
//   * http://localhost:9091/contents/list_by_cateno?cateno=6
//   * 
//   * @return
//   */
//  @GetMapping(value = "/list_by_emono_search_paging")
//  public String list_by_emono_search_paging(
//      HttpSession session, 
//      Model model, 
//      @ModelAttribute("emotionVO") EmotionVO emotionVO,
//      @RequestParam(name = "emono", defaultValue = "0") int emono,
//      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
//
//      int record_per_page = 10;
//      int startRow = (now_page - 1) * record_per_page + 1;
//      int endRow = now_page * record_per_page;
//
//      int diaryno = (int) session.getAttribute("diaryno");
//      DiaryVO diaryVO = this.diaryProc.read(diaryno);
////      if (diaryVO == null) {
////          diaryVO = new DiaryVO();
////          diaryVO.setDiaryno(0);
////          model.addAttribute("message", "일기 정보가 없습니다.");
////      }
//      int memberno = (int) session.getAttribute("memberno");
//      MemberVO memberVO = this.memberProc.read(memberno);
//      if (memberVO == null) {
//          memberVO = new MemberVO();
//          memberVO.setMemberno(0);
//          model.addAttribute("message", "회원 정보가 없습니다.");
//      }
////      model.addAttribute("diaryVO", diaryVO);
//      model.addAttribute("memberVO", memberVO);
//      model.addAttribute("emono", emono);
//      model.addAttribute("now_page", now_page);
//
//      HashMap<String, Object> map = new HashMap<>();
////      map.put("diaryno", diaryno);
//      map.put("now_page", now_page);
//      map.put("startRow", startRow);
//      map.put("endRow", endRow);
//
//      ArrayList<EmotionVO> list = this.emotionProc.list_by_emono_search_paging(map);
//      if (list == null || list.isEmpty()) {
//          model.addAttribute("message", "게시물이 없습니다.");
//      } else {
//          model.addAttribute("list", list);
//      }
//
////      int search_count = this.emotionProc.count_by_emono_search(map);
////      String paging = this.emotionProc.pagingBox(diaryno, b, now_page, "/emotion/list_by_emono_search_paging", search_count,
////    		  Emotion.RECORD_PER_PAGE, Emotion.PAGE_PER_BLOCK);
////      model.addAttribute("paging", paging);
////      model.addAttribute("now_page", now_page);
////      model.addAttribute("search_count", search_count);
//
//
////      int no = search_count - ((now_page - 1) * Emotion.RECORD_PER_PAGE);
////      model.addAttribute("no", no);
//
//      return "/emotion/list_by_emono_search_paging"; // /templates/emotion/list_by_emono_search_paging.html
//  }

//  /**
//   * 카테고리별 목록 + 검색 + 페이징 + Grid
//   * @return
//   */
//  @GetMapping(value = "/list_by_emono_search_paging_grid")
//  public String list_by_emono_search_paging_grid(HttpSession session, 
//      Model model, 
//      @RequestParam(name = "diaryno", defaultValue = "0") int diaryno,
//      @RequestParam(name = "emotion_cate", defaultValue = "1") String emotion_cate,
//      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
//
//
////  ArrayList<emotionVOMenu> menu = this.emotionProc.menu();
////  model.addAttribute("menu", menu);
//
//   DiaryVO diaryVO = this.diaryProc.read(diaryno);
//    model.addAttribute("diaryVO", diaryVO);
//
//    emotion_cate = Tool.checkNull(emotion_cate).trim();
//
//    HashMap<String, Object> map = new HashMap<>();
//    map.put("diaryno", diaryno);
//    map.put("emotion_cate", emotion_cate);
//    map.put("now_page", now_page);
//
//    ArrayList<EmotionVO> list = this.emotionProc.list_by_emono_search_paging(map);
//    model.addAttribute("list", list);
//    
//    model.addAttribute("emotion_cate", emotion_cate);
//
//    int search_count = this.emotionProc.count_by_emono_search(map);
//    String paging = this.emotionProc.pagingBox(diaryno, now_page, emotion_cate, "/emotion/list_by_emono", search_count,
//    		Emotion.RECORD_PER_PAGE, Emotion.PAGE_PER_BLOCK);
//    model.addAttribute("paging", paging);
//    model.addAttribute("now_page", now_page);
//
//    model.addAttribute("search_count", search_count);
//
//    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
//    int no = search_count - ((now_page - 1) * Emotion.RECORD_PER_PAGE);
//    model.addAttribute("no", no);
//
//    // /templates/contents/list_by_cateno_search_paging_grid.html
//    return "/emotion/list_by_emono_search_paging_grid";
//  }

  
  /**
   * 감정 조회 
   * @return
   */
  @GetMapping(value = "/read")
  public String read(Model model, 
      @RequestParam(name="emono", defaultValue = "0") int emono, 
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);

	  EmotionVO emotionVO = this.emotionProc.read(emono);

//    String title = contentsVO.getTitle();
//    String content = contentsVO.getContent();
//    
//    title = Tool.convertChar(title);  // 특수 문자 처리
//    content = Tool.convertChar(content); 
//    
//    contentsVO.setTitle(title);
//    contentsVO.setContent(content);  
//
//    long size1 = emotionVO.getSize1();
//    String size1_label = Tool.unit(size1);
//    emotionVO.setSize1_label(size1_label);

    model.addAttribute("emotionVO", emotionVO);
    long size1 = emotionVO.getSize1();
    String size1_label = Tool.unit(size1);
    emotionVO.setSize1_label(size1_label);

    MemberVO memberVO = this.memberProc.read(emotionVO.getMemberno());
    model.addAttribute("memberVO", memberVO);

    // 조회에서 화면 하단에 출력
    // ArrayList<ReplyVO> reply_list = this.replyProc.list_contents(contentsno);
    // mav.addObject("reply_list", reply_list);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    return "/emotion/read";
  }
  
  /**
   * 감정 수정 폼 
   */
  @GetMapping(value = "/update_text")
  public String update_text(HttpSession session, 
      Model model, 
      @RequestParam(name="emono", defaultValue="") int emono, 
      RedirectAttributes ra, 
      @RequestParam(name="word", defaultValue="") String word,
      @RequestParam(name="now_page", defaultValue="1") int now_page) {
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

//    if (this.diaryProc.isdiary(session)) { // 회원 로그인한경우
    
      EmotionVO emotionVO = this.emotionProc.read(emono);
      model.addAttribute("emotionVO", emotionVO);

      DiaryVO diaryVO = this.diaryProc.read(emotionVO.getDiaryno());
      model.addAttribute("diaryVO", diaryVO);

      return "/emotion/update_text"; // /templates/contents/update_text.html
      // String content = "장소:\n인원:\n준비물:\n비용:\n기타:\n";
      // model.addAttribute("content", content);

//    } else {
////      ra.addAttribute("url", "/diary/login_cookie_need"); // /templates/diary/login_cookie_need.html
////      return "redirect:/contents/msg"; // @GetMapping(value = "/read")
//      return "diary/login_cookie_need";
//    }

  }

  /**
   * 감정 수정 처리
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(
          HttpSession session, 
          Model model, 
          @ModelAttribute("emotionVO") EmotionVO emotionVO, 
          RedirectAttributes ra,
          @RequestParam(name = "search_word", defaultValue = "") String search_word, 
          @RequestParam(name = "now_page", defaultValue = "0") int now_page) {

      // Redirect 시 검색어 및 현재 페이지를 유지하기 위한 파라미터 추가
      ra.addAttribute("word", search_word);
      ra.addAttribute("now_page", now_page);

      // bcontent 값 검증
      if (emotionVO.getExplan() == null || emotionVO.getExplan().trim().isEmpty()) {
          ra.addFlashAttribute("message", "내용은 필수 입력 사항입니다.");
          ra.addFlashAttribute("code", "update_fail");
          return "redirect:/emotion/msg"; // 실패 시 msg 페이지로 이동
      }

      // 글 수정 처리
      try {
          int cnt = this.emotionProc.update_text(emotionVO); // 글 수정
          if (cnt > 0) { // 수정 성공
              ra.addAttribute("emono", emotionVO.getEmono());
              return "redirect:/emotion/read"; // 성공 시 감정 조회 페이지로 이동
          } else { // 수정 실패
              ra.addFlashAttribute("message", "감정 수정에 실패했습니다.");
              ra.addFlashAttribute("code", "update_fail");
              return "redirect:/emotion/msg"; // 실패 시 msg 페이지로 이동
          }
      } catch (Exception e) {
          e.printStackTrace();
          ra.addFlashAttribute("message", "글 수정 중 오류가 발생했습니다.");
          ra.addFlashAttribute("code", "update_fail");
          return "redirect:/emotion/msg"; // 오류 발생 시 msg 페이지로 이동
      }
  }


  
  /**
   * 파일 수정 폼 http://localhost:9091/contents/update_file?contentsno=1
   * 
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, 
         @RequestParam(name="emono", defaultValue="0") int emono,
         @RequestParam(name="word", defaultValue="") String word, 
         @RequestParam(name="now_page", defaultValue="1") int now_page) {
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    EmotionVO emotionVO = this.emotionProc.read(emono);
    model.addAttribute("emotionVO", emotionVO);

    DiaryVO diaryVO = this.diaryProc.read(emotionVO.getDiaryno());
    model.addAttribute("diaryVO", diaryVO);


    return "/emotion/update_file";

  }

  /**
   * 파일 수정 처리 http://localhost:9091/emotion/update_file
   * 
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, RedirectAttributes ra,
                            @ModelAttribute("emotionVO") EmotionVO emotionVO,
                            @RequestParam(name="word", defaultValue="") String word, 
                            @RequestParam(name="now_page", defaultValue="1") int now_page) {

//    if (this.diaryProc.isdiary(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
	  EmotionVO emotionVO_old = emotionProc.read(emotionVO.getEmono());

      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
//      String file1saved = emotionVO_old.getFile1saved(); // 실제 저장된 파일명
//      String thumb1 = emotionVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
//      long size1 = 0;
//
//      String upDir = emotion.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/contents/storage/
//
//      Tool.deleteFile(upDir, file1saved); // 실제 저장된 파일삭제
//      Tool.deleteFile(upDir, thumb1); // preview 이미지 삭제
      // -------------------------------------------------------------------
      // 파일 삭제 종료
      // -------------------------------------------------------------------

      // -------------------------------------------------------------------
      // 파일 전송 시작
      // -------------------------------------------------------------------
//      String file1 = ""; // 원본 파일명 image
//
//      // 전송 파일이 없어도 file1MF 객체가 생성됨.
//      // <input type='file' class="form-control" name='file1MF' id='file1MF'
//      // value='' placeholder="파일 선택">
//      MultipartFile mf = emotionVO.getFile1MF();
//
//      file1 = mf.getOriginalFilename(); // 원본 파일명
//      size1 = mf.getSize(); // 파일 크기
//
//      if (size1 > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
//        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
//        file1saved = Upload.saveFileSpring(mf, upDir);
//
//        if (Tool.isImage(file1saved)) { // 이미지인지 검사
//          // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
//          thumb1 = Tool.preview(upDir, file1saved, 250, 200);
//        }
//
//      } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
//        file1 = "";
//        file1saved = "";
//        thumb1 = "";
//        size1 = 0;
//      }
//
//      emotionVO.setFile1(file1);
//      emotionVO.setFile1saved(file1saved);
//      emotionVO.setThumb1(thumb1);
//      emotionVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      this.emotionProc.update_file(emotionVO); // Oracle 처리
      ra.addAttribute ("emono", emotionVO.getEmono());
      ra.addAttribute("diaryno", emotionVO.getDiaryno());
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/emotion/read";
//    } else {
//      ra.addAttribute("url", "/diary/login_cookie_need"); 
//      return "redirect:/emotion/post2get"; // GET
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
                               @RequestParam(name="diaryno", defaultValue="0") int diaryno, 
                               @RequestParam(name="emono", defaultValue="0") int emono, 
                               @RequestParam(name="word", defaultValue="") String word, 
                               @RequestParam(name="now_page", defaultValue="1") int now_page) {
//    if (this.diaryProc.isdiary(session)) { // 로그인한경우
      model.addAttribute("diaryno", diaryno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
//      ArrayList<CateVOMenu> menu = this.cateProc.menu();
//      model.addAttribute("menu", menu);
      
      EmotionVO emotionVO = this.emotionProc.read(emono);
      model.addAttribute("emotionVO", emotionVO);

      DiaryVO diaryVO = this.diaryProc.read(emotionVO.getDiaryno());
      model.addAttribute("diaryVO", diaryVO);
      
      return "/emotion/delete"; // forward
      
//    } else {
//      ra.addAttribute("url", "/diary/login_cookie_need");
//      return "redirect:/emotion/msg"; 
//    }

  }
  
  /**
   * 삭제 처리 http://localhost:9091/contents/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra,
      @RequestParam(name="diaryno", defaultValue="0") int diaryno, 
      @RequestParam(name="emono", defaultValue="0") int emono, 
      @RequestParam(name="word", defaultValue="") String word, 
      @RequestParam(name="now_page", defaultValue="1") int now_page) {
    // -------------------------------------------------------------------
    // 파일 삭제 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
//	EmotionVO emotionVO_read = emotionProc.read(emono);
//        
//    String file1saved = emotionVO_read.getFile1saved();
//    String thumb1 = emotionVO_read.getThumb1();
//    
//    String uploadDir = emotion.getUploadDir();
//    Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
//    Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
    // -------------------------------------------------------------------
    // 파일 삭제 종료
    // -------------------------------------------------------------------
        
    this.emotionProc.delete(emono); // DBMS 삭제
        
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------    
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("diaryno", diaryno);
    map.put("word", word);
    
//    if (this.emotionProc.list_by_cateno_search_count(map) % emotion.RECORD_PER_PAGE == 0) {
//      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
//      if (now_page < 1) {
//        now_page = 1; // 시작 페이지
//      }
//    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("diaryno", diaryno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/emotion/list_by_emono_search_paging";    
    
  }   


}

