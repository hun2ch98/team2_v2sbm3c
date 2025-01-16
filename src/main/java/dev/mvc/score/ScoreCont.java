package dev.mvc.score;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.diary.DiaryVO;
import dev.mvc.learningdata.Learningdata;
import dev.mvc.member.MemberProcInter;
import dev.mvc.member.MemberVO;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/score")
public class ScoreCont {
  @Autowired
  @Qualifier("dev.mvc.score.ScoreProc")
  private ScoreProcInter scoreProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") 
  private MemberProcInter memberProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/score/list_by_scoreno_search_paging";
  
  public ScoreCont() {
    System.out.println("-> ScoreCont created.");
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
   * 평점 등록 폼
   * @param model
   * @param scoreVO
   * @return
   */
  @GetMapping(value = "/create")
  public String create(Model model,
		  HttpSession session,
      @ModelAttribute("scoreVO") ScoreVO scoreVO) { 
	
	  int memberno =(int) session.getAttribute("memberno");
	  scoreVO.setMemberno(memberno); 
	  model.addAttribute("scoreVO", scoreVO); // 수정된 scoreVO 전달

	  return "/score/create";
  }
  
  /**
   * 평점 등록 처리
   * @param request
   * @param session
   * @param model
   * @param scoreVO
   * @param ra
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpServletRequest request,
                       HttpSession session,
                       Model model,
                       @ModelAttribute("scoreVO") ScoreVO scoreVO,
                       RedirectAttributes ra) {
	  int memberno = 1; 
	  scoreVO.setMemberno(memberno); 
  
	  int cnt = this.scoreProc.create(scoreVO);
	  if (cnt == 1) {
		  ra.addAttribute("scoreVO", scoreVO.getScoreno()); 
		  ra.addAttribute("now_page", 1); 
		  return "redirect:/score/list_by_scoreno_search_paging"; 
	  } else {
		  ra.addFlashAttribute("code", "create_fail");
		  return "redirect:/score/msg"; 
	  }
  }
    
  /**
   * 평점 전체 목록(관리자)
   * @param session
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
    
	    ArrayList<ScoreVO> list = this.scoreProc.list_all(); // 평점 모든 목록
    
	    model.addAttribute("list", list);
	    return "/score/list_all";
  }

  /**
   * 유형 3
   * 평점별 목록 + 검색 + 페이징
   * 
   * @return
   */
  @GetMapping(value = "/list_by_scoreno_search_paging")
  public String list_by_scoreno_search_paging(
      HttpSession session,
      Model model,
      @ModelAttribute("scoreVO") ScoreVO scoreVO,
      @RequestParam(name = "scoreno", defaultValue = "0") int scoreno,
      @RequestParam(value = "start_date", required = false, defaultValue = "") String startDate,
      @RequestParam(value = "end_date", required = false, defaultValue = "") String endDate,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

      // 날짜 값이 비어 있지 않도록 트림 처리
      startDate = startDate.trim();
      endDate = endDate.trim();

      // 페이징 설정
      int record_per_page = 10;
      int startRow = (now_page - 1) * record_per_page + 1;
      int endRow = now_page * record_per_page;

      // 쿼리 파라미터 맵 설정
      HashMap<String, Object> map = new HashMap<>();
      map.put("now_page", now_page);
      map.put("startRow", startRow);
      map.put("endRow", endRow);
      map.put("startDate", startDate);
      map.put("endDate", endDate);

      // 목록 가져오기
      ArrayList<ScoreVO> list = this.scoreProc.list_by_scoreno_search_paging(map);
      if (list == null || list.isEmpty()) {
          model.addAttribute("message", "검색 조건에 맞는 게시물이 없습니다.");
      } else {
          model.addAttribute("list", list);
      }


      // 검색된 글 수 가져오기
      int search_count = this.scoreProc.count_by_scoreno_search(map);
      String paging = this.scoreProc.pagingBox(now_page, scoreno,startDate,endDate, search_count,
    		  Score.RECORD_PER_PAGE, Score.PAGE_PER_BLOCK);
	      
      // 모델에 데이터 추가
      model.addAttribute("scoreno", scoreno);
      model.addAttribute("startDate", startDate);
      model.addAttribute("endDate", endDate);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      model.addAttribute("search_count", search_count);

      // 페이지 번호 계산
      int no = search_count - ((now_page - 1) * record_per_page);
      model.addAttribute("no", no);

      return "/score/list_by_scoreno_search_paging"; // /templates/score/list_by_scoreno_search_paging.html
  }

  /**
   * 평점 조회
   * @return
   */
  @GetMapping(value = "/read")
  public String read(HttpSession session, Model model,
      @RequestParam(name = "scoreno", defaultValue = "0") int scoreno,
      @RequestParam(name = "now_page", required = false) String now_page) {
      
      // now_page가 null 또는 "null"인 경우 기본값 1로 설정
      int currentPage = 1;
      if (now_page != null && !now_page.equals("null")) {
          try {
              currentPage = Integer.parseInt(now_page);
          } catch (NumberFormatException e) {
              currentPage = 1;  // 잘못된 형식인 경우 기본값 1로 설정
          }
      }

      // 평점 정보 조회
      ScoreVO scoreVO = this.scoreProc.read(scoreno);
      model.addAttribute("scoreVO", scoreVO);

      // 회원 정보 조회
      MemberVO memberVO = this.memberProc.read(scoreVO.getMemberno());
      model.addAttribute("memberVO", memberVO);

      // 현재 페이지 모델에 추가
      model.addAttribute("now_page", currentPage);
      
      return "/score/read";
  }

  /**
   * 평점 수정 폼
   * @return
   */
  @GetMapping(value = "/update_score")
  public String update_text(HttpSession session,
      Model model, 
      @RequestParam(name = "scoreno", defaultValue = "0") int scoreno,
      RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    
    model.addAttribute("now_page", now_page);
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우

    	ScoreVO scoreVO = this.scoreProc.read(scoreno);
//	    	System.out.println("-> scoreVO.getscoreno() : " + scoreVO.getscoreno());
        model.addAttribute("scoreVO", scoreVO);
        
        MemberVO memberVO = this.memberProc.read(scoreVO.getMemberno());
        model.addAttribute("memberVO", memberVO);
        
        return "/score/update_score";
    } else {
        ra.addAttribute("url", "/member/login_cookie_need"); // /templates/diary/login_cookie_need.html
//	        return "redirect:/contents/msg"; // @GetMapping(value = "/read")
        return "member/login_cookie_need";
      }
  }
  
  /**
   * 평점 수정 처리
   * @return
   */
  @PostMapping(value = "/update_score")
  public String update_text(
      HttpSession session,
      Model model,
      @ModelAttribute("scoreVO") ScoreVO scoreVO,
      RedirectAttributes ra,
      @RequestParam(name = "scoreno", defaultValue = "") int scoreno,
      @RequestParam(name = "total", defaultValue = "") int total,
      @RequestParam(name = "search_word", defaultValue = "") String search_word,
      @RequestParam(name = "now_page", defaultValue = "0") int now_page) {

      // Redirect 시 검색어 및 현재 페이지를 유지하기 위한 파라미터 추가
      ra.addAttribute("word", search_word);
      ra.addAttribute("now_page", now_page);

      // 평점 글 수정 처리
      try {
          int cnt = this.scoreProc.update_score(scoreVO); // 평점 글 수정
          if (cnt > 0) { // 수정 성공
              ra.addAttribute("scoreno", scoreVO.getScoreno());
              return "redirect:/score/read"; // 성공 시 게시글 조회 페이지로 이동
          } else { // 수정 실패
              ra.addFlashAttribute("message", "평점 글 수정에 실패했습니다.");
              ra.addFlashAttribute("code", "update_fail");
              return "redirect:/score/msg"; // 실패 시 msg 페이지로 이동
          }
      } catch (Exception e) {
          e.printStackTrace();
          ra.addFlashAttribute("message", "평점 글 수정 중 오류가 발생했습니다.");
          ra.addFlashAttribute("code", "update_fail");
          return "redirect:/score/msg"; // 오류 발생 시 msg 페이지로 이동
      }
  }

  /**
   * 평점 삭제 폼
   * @param session
   * @param model
   * @param ra
   * @param scoreno
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model,
         RedirectAttributes ra,
         @RequestParam(name = "scoreno", defaultValue = "0") int scoreno,
         @RequestParam(name = "memberno", defaultValue = "0") int memberno,
         @RequestParam(name = "word", defaultValue = "") String word,
         @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
		  model.addAttribute("scoreno", scoreno);
		  model.addAttribute("word", word);
		  model.addAttribute("now_page", now_page);
		    
		  ScoreVO scoreVO = this.scoreProc.read(scoreno);
		  model.addAttribute("scoreVO", scoreVO);
		  
		  MemberVO  memberVO = this.memberProc.read(scoreVO.getMemberno());
		  model.addAttribute("memberVO", memberVO);
		    
		  return "/score/delete"; // forward
	
  }
  
  /**
   * 평점 삭제 처리
   * @param ra
   * @param scoreno
   * @param word
   * @param now_page
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra,
	  @RequestParam(name="memberno", defaultValue="0") int memberno, 
      @RequestParam(name = "scoreno", defaultValue = "0") int scoreno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    this.scoreProc.delete(scoreno); // DBMS 삭제
    
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------    
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("memberno", memberno);
    map.put("word", word);
    
    ra.addAttribute("memberno", memberno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/score/list_by_scoreno_search_paging";
  }
  
}
