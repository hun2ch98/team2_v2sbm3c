package dev.mvc.diary;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.member.MemberProcInter;
import dev.mvc.emotion.EmotionVO;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/diary")
public class DiaryCont {
	
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
  private String list_file_name = "/diary/list_by_diaryno_search_paging";

  public DiaryCont() {
    System.out.println("-> DiaryCont created.");
  }

  /**
   * 등록 폼
   * 
   * @param model
   * @return
   */
  // http://localhost:9093/diary/create
  @GetMapping(value = "/create")
  public String create(Model model, 
                                  @RequestParam(name="title", defaultValue="오늘의 제목") String title, 
                                  @RequestParam(name="emotion", defaultValue="0") int emotion, 
                                  @RequestParam(name="summary", defaultValue="오늘의 일기") String summary) {
    // create method에 사용될 테이블
    // summary를 가져올 테이블
    DiaryVO diaryVO = new DiaryVO();
    EmotionVO emotionVO = new EmotionVO();
    
    model.addAttribute("diaryVO", diaryVO);
    model.addAttribute("emotionVO", emotionVO);

    diaryVO.setTitle(title);
    diaryVO.setSummary(summary);
    diaryVO.setEmno(emotion);
    
    return "/diary/create"; // /templates/diary/create.html
  }

  /**
   * 등록 처리, http://localhost:9093/diary/create
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @param diaryVO        Form 태그 값 -> 검증 -> diaryVO 자동 저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  // contentCont의 create 처리 과정보고 추가해야함. 
  @PostMapping(value = "/create")
  public String create(Model model, HttpSession session,
                       @Valid @ModelAttribute("diaryVO") DiaryVO diaryVO, 
                       BindingResult bindingResult, RedirectAttributes ra) {
      if (bindingResult.hasErrors()) { 
          // 에러 발생 시 폼으로 돌아가기
          return "/diary/create";
      }

      // 제목 및 내용 트림 처리
      diaryVO.setTitle(diaryVO.getTitle().trim());
      diaryVO.setSummary(diaryVO.getSummary().trim());

      // ddate 설정
      if (diaryVO.getDdate() == null) {
          diaryVO.setDdate(Date.valueOf(LocalDate.now())); // 현재 날짜로 설정
      }

      // 세션에서 memberno 가져오기
      Integer memberno = (Integer) session.getAttribute("memberno");
      if (memberno == null) {
          ra.addFlashAttribute("message", "로그인이 필요합니다.");
          return "redirect:/member/login";
      }
      diaryVO.setMemberno(memberno);

      // DB 저장 로직 호출
      int cnt = diaryProc.create(diaryVO);
      System.out.println("-> create_cnt: " + cnt);

      if (cnt == 1) {
          return "redirect:/diary/list_by_diaryno_search_paging";
      } else {
          model.addAttribute("code", "create_fail");
          return "/diary/msg";
      }
  }

  
  @GetMapping("/list_by_diaryno_search_paging")
  public String listSearch(@RequestParam(value = "title", required = false, defaultValue = "") String title,
                           @RequestParam(value = "start_date", required = false, defaultValue = "") String startDate,
                           @RequestParam(value = "end_date", required = false, defaultValue = "") String endDate,
                           @RequestParam(value = "now_page", required = false, defaultValue = "1") int nowPage,
                           Model model) {
      title = title.trim();
      startDate = startDate.trim();
      endDate = endDate.trim();

      int startNum = (nowPage - 1) * record_per_page + 1;
      int endNum = nowPage * record_per_page;

      int searchCount = diaryProc.countSearchResults(title, startDate, endDate);
      ArrayList<DiaryVO> diaryList = diaryProc.list_search_paging(title, startDate, endDate, startNum, endNum);

      String paging = diaryProc.pagingBox(nowPage, title, startDate, endDate, list_file_name, searchCount, record_per_page, page_per_block);

      model.addAttribute("diaryList", diaryList);
      model.addAttribute("title", title);
      model.addAttribute("start_date", startDate);
      model.addAttribute("end_date", endDate);
      model.addAttribute("paging", paging);
      model.addAttribute("search_count", searchCount);
      model.addAttribute("now_page", nowPage);

      return "/diary/list_by_diaryno_search_paging";
  }

  /**
   * 삭제 처리
   */
  @PostMapping(value = "/delete")
  public String deleteProcess(HttpSession session,
                              @RequestParam(name = "diaryno") int diaryno,
                              @RequestParam(name = "title", defaultValue = "") String title,
                              @RequestParam(name = "date", defaultValue = "") String date,
                              @RequestParam(name = "now_page", defaultValue = "1") int nowPage,
                              RedirectAttributes ra) {
      if (this.memberProc.isMemberAdmin(session)) {
          // 삭제할 Diary 조회
          DiaryVO diaryVO = this.diaryProc.read(diaryno);

          if (diaryVO != null) {
              // 삭제 수행
              int cnt = this.diaryProc.delete(diaryno);

              if (cnt == 1) {
                  // 삭제 성공 시 검색 조건 유지
                  ra.addAttribute("title", title);
                  ra.addAttribute("date", date);

                  // 마지막 페이지 처리 (빈 페이지 방지)
                  int searchCount = this.diaryProc.list_search_count(title, date);
                  if (searchCount % this.record_per_page == 0) {
                      nowPage = Math.max(nowPage - 1, 1); // 최소 페이지는 1
                  }
                  ra.addAttribute("now_page", nowPage);

                  return "redirect:/diary/list_by_diaryno_search_paging";
              }
          }
          // 삭제 실패 시 처리
          ra.addFlashAttribute("msg", "삭제 실패");
          return "redirect:/diary/list_by_diaryno_search_paging";
      } else {
          return "redirect:/member/login_cookie_need";
      }
  }


  

  /**
   * 수정 폼
   * http://localhost:9093/diary/update/1
   */
  @GetMapping(value = "/update/{diaryno}")
  public String update(HttpSession session, Model model, 
                       @PathVariable("diaryno") Integer diaryno, 
                       @RequestParam(name = "title", defaultValue = "") String title,  
                       @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
      if (this.memberProc.isMemberAdmin(session)) {
          DiaryVO diaryVO = this.diaryProc.read(diaryno); // 수정할 데이터를 조회
          model.addAttribute("diaryVO", diaryVO);

          model.addAttribute("title", title); // 검색어 유지
          model.addAttribute("now_page", now_page); // 현재 페이지 유지

          return "/diary/update"; // 수정 폼으로 이동
      } else {
          return "redirect:/member/login_cookie_need"; // 권한이 없으면 로그인 페이지로 리다이렉트
      }
  }
  

  /**
   * 수정 처리
   * http://localhost:9093/diary/update
   */
  @PostMapping(value = "/update")
  public String update(HttpSession session, Model model, 
                       @Valid @ModelAttribute("diaryVO") DiaryVO diaryVO, 
                       @RequestParam("diaryno") int diaryno,
                       BindingResult bindingResult, 
                       @RequestParam(name = "now_page", defaultValue = "1") int now_page, 
                       RedirectAttributes ra) {
      if (this.memberProc.isMemberAdmin(session)) {
          if (bindingResult.hasErrors()) { // 폼 에러 처리
            DiaryVO diary1VO = diaryProc.read(diaryno);
            model.addAttribute("diaryVO", diary1VO); // 모델에 추가
            return "diary/update"; // update.html로 이동
          }

          DiaryVO existingDiary = this.diaryProc.read(diaryno);
          diaryVO.setDdate(existingDiary.getDdate());
          
          int cnt = this.diaryProc.update(diaryVO); // 데이터 업데이트
          if (cnt == 1) {
              ra.addAttribute("now_page", now_page); // 페이지 번호 전달
              return "redirect:/diary/list_by_diaryno_search_paging"; // 목록 페이지로 리다이렉트
          } else {
              model.addAttribute("code", "update_fail");
              return "/diary/msg"; // 에러 메시지 출력 페이지
          }
      } else {
          return "redirect:/member/login_cookie_need"; // 권한이 없으면 로그인 페이지로 리다이렉트
      }
  }


 
  /**
   * 우선 순위 높임, 10 등 -> 1 등, http://localhost:9093/diary/update_seqno_forward/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_seqno_forward/{diaryno}")
  public String update_seqno_forward(Model model, @PathVariable("diaryno") Integer diaryno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "") int now_page, RedirectAttributes ra) {
    this.diaryProc.update_seqno_forward(diaryno);

    ra.addAttribute("word", word); // redirect로 데이터 전송
    ra.addAttribute("now_page", now_page); // redirect로 데이터 전송

    return "redirect:/diary/list_by_diaryno_search_paging"; // @GetMapping(value="/list_by_diaryno_search_paging")
  }

  /**
   * 우선 순위 낮춤, 1 등 -> 10 등, http://localhost:9093/diary/update_seqno_backward/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_seqno_backward/{diaryno}")
  public String update_seqno_backward(Model model, @PathVariable("diaryno") Integer diaryno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "") int now_page, RedirectAttributes ra) {
    this.diaryProc.update_seqno_backward(diaryno);

    ra.addAttribute("word", word); // redirect로 데이터 전송
    ra.addAttribute("now_page", now_page); // redirect로 데이터 전송

    return "redirect:/diary/list_by_diaryno_search_paging"; // @GetMapping(value="/list_by_diaryno_search_paging")
  }

  /**
   * 카테고리 공개 설정, http://localhost:9093/diary/update_visible_y/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_visible_y/{diaryno}")
  public String update_visible_y(HttpSession session, Model model, @PathVariable("diaryno") Integer diaryno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "") int now_page, RedirectAttributes ra) {
    
    if (this.memberProc.isMemberAdmin(session)) {
      this.diaryProc.update_visible_y(diaryno);

      ra.addAttribute("word", word); // redirect로 데이터 전송
      ra.addAttribute("now_page", now_page); // redirect로 데이터 전송

      return "redirect:/diary/list_by_diaryno_search_paging"; // @GetMapping(value="/list_by_diaryno_search_paging")
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
  }

  /**
   * 카테고리 비공개 설정, http://localhost:9093/diary/update_visible_n/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_visible_n/{diaryno}")
  public String update_visible_n(HttpSession session, Model model, @PathVariable("diaryno") Integer diaryno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "") int now_page, RedirectAttributes ra) {
    
    if (this.memberProc.isMemberAdmin(session)) {
      this.diaryProc.update_visible_n(diaryno);

      ra.addAttribute("word", word); // redirect로 데이터 전송
      ra.addAttribute("now_page", now_page); // redirect로 데이터 전송

      return "redirect:/diary/list_by_diaryno_search_paging"; // @GetMapping(value="/list_by_diaryno_search_paging")
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
  }

}