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
    
//  /**
//   * 평점 전체 목록(관리자)
//   * @param session
//   * @param model
//   * @return
//   */
//  @GetMapping(value = "/list_all")
//  public String list_all(HttpSession session, Model model) {
//    
//	    ArrayList<scoreVO> list = this.scoreProc.list_all(); // 평점 모든 목록
//    
//	    model.addAttribute("list", list);
//	    return "/score/list_all";
//  }

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
      @RequestParam(name = "jumsu", defaultValue = "") String jumsu,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

      int record_per_page = 10;
      int startRow = (now_page - 1) * record_per_page + 1;
      int endRow = now_page * record_per_page;

      int memberno = (int) session.getAttribute("memberno");
      MemberVO memberVO = this.memberProc.read(memberno);
      if (memberVO == null) {
          memberVO = new MemberVO();
          memberVO.setMemberno(0);
          model.addAttribute("message", "회원 정보가 없습니다.");
      }
      jumsu = Tool.checkNull(jumsu).trim();

      model.addAttribute("memberVO", memberVO);
      model.addAttribute("scoreno", scoreno);
      model.addAttribute("jumsu", jumsu);
      model.addAttribute("now_page", now_page);

      HashMap<String, Object> map = new HashMap<>();
      map.put("memberno", memberno);
      map.put("jumsu", jumsu);
      map.put("now_page", now_page);
      map.put("startRow", startRow);
      map.put("endRow", endRow);

      ArrayList<ScoreVO> list = this.scoreProc.list_by_scoreno_search_paging(map);
      if (list == null || list.isEmpty()) {
          model.addAttribute("message", "게시물이 없습니다.");
      } else {
          model.addAttribute("list", list);
      }

      int search_count = this.scoreProc.count_by_scoreno_search(map);
      String paging = this.scoreProc.pagingBox(now_page, jumsu, memberno, search_count,
          record_per_page, page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      model.addAttribute("search_count", search_count);

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
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    
	ScoreVO scoreVO = this.scoreProc.read(scoreno);
   
    model.addAttribute("scoreVO", scoreVO);
    
    MemberVO memberVO = this.memberProc.read(scoreVO.getMemberno());
    model.addAttribute("memberVO", memberVO);
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
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
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    
    model.addAttribute("word", word);
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
      @RequestParam(name = "search_word", defaultValue = "") String search_word,
      @RequestParam(name = "now_page", defaultValue = "0") int now_page) {

      // Redirect 시 검색어 및 현재 페이지를 유지하기 위한 파라미터 추가
      ra.addAttribute("word", search_word);
      ra.addAttribute("now_page", now_page);
      
      // 평점 값 검증 (jumsu가 0.5 단위로 입력되었는지 확인)
      if (scoreVO.getJumsu() == null || scoreVO.getJumsu().trim().isEmpty()) {
          ra.addFlashAttribute("message", "평점은 필수 입력 사항입니다.");
          ra.addFlashAttribute("code", "update_fail");
          return "redirect:/score/msg"; // 실패 시 msg 페이지로 이동
      }
      
      Float jumsu = null;
      try {
          // 문자열로 된 평점 값을 Float로 변환
          jumsu = Float.parseFloat(scoreVO.getJumsu().trim());
          // 0.5단위 체크
          if (jumsu % 0.5 != 0 || jumsu > 5) {
              ra.addFlashAttribute("message", "평점은 0.5단위로 입력해야 하며 최대 5점까지 가능합니다.");
              ra.addFlashAttribute("code", "update_fail");
              return "redirect:/score/msg"; // 유효하지 않은 평점 입력 시
          }
      } catch (NumberFormatException e) {
          ra.addFlashAttribute("message", "유효하지 않은 평점 값입니다.");
          ra.addFlashAttribute("code", "update_fail");
          return "redirect:/score/msg"; // 예외 발생 시 오류 처리
      }

      // scoreVO에 검증된 jumsu 값 설정
      scoreVO.setJumsu(String.valueOf(jumsu));

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
