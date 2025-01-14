package dev.mvc.learningdata;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/learningdata")
public class LearningdataCont {
	
	  @Autowired
	  @Qualifier("dev.mvc.learningdata.LearningdataProc")
	  private LearningdataProcInter learningdataProc;
	  
	  @Autowired
	  @Qualifier("dev.mvc.member.MemberProc") 
	  private MemberProcInter memberProc;
	  
	  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
	  public int record_per_page = 10;

	  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
	  public int page_per_block = 10;

	  /** 페이징 목록 주소 */
	  private String list_file_name = "/learningdata/list_by_datano_search_paging";
	  
	  public LearningdataCont() {
	    System.out.println("-> LearningdataCont created.");
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
	   * 학습 데이터 등록 폼
	   * @param model
	   * @param LearningdataVO
	   * @return
	   */
	  @GetMapping(value = "/create")
	  public String create(Model model,
			  HttpSession session,
	      @ModelAttribute("learningdataVO") LearningdataVO learningdataVO) { 
		
		  int memberno =(int) session.getAttribute("memberno");
		  learningdataVO.setMemberno(memberno); 
		  model.addAttribute("learningdataVO", learningdataVO); // 수정된 LearningdataVO 전달

		  return "/learningdata/create";
	  }
	  
	  /**
	   * 학습 데이터 등록 처리
	   * @param request
	   * @param session
	   * @param model
	   * @param LearningdataVO
	   * @param ra
	   * @return
	   */
	  @PostMapping(value = "/create")
	  public String create(HttpServletRequest request,
	                       HttpSession session,
	                       Model model,
	                       @ModelAttribute("LearningdataVO") LearningdataVO learningdataVO,
	                       RedirectAttributes ra) {
		  int memberno = 1; 
		  learningdataVO.setMemberno(memberno); 
	  
		  int cnt = this.learningdataProc.create(learningdataVO);
		  if (cnt == 1) {
			  ra.addAttribute("learningdataVO", learningdataVO.getDatano()); 
			  ra.addAttribute("now_page", 1); 
			  return "redirect:/learningdata/list_by_datano_search_paging"; 
		  } else {
			  ra.addFlashAttribute("code", "create_fail");
			  return "redirect:/learningdata/msg"; 
		  }
	  }
	  
	  /**
	   * 유형 3
	   * 학습 데이터별 목록 + 검색 + 페이징
	   * 
	   * @return
	   */
	  @GetMapping(value = "/list_by_datano_search_paging")
	  public String list_by_datano_search_paging(
	      HttpSession session,
	      Model model,
	      @ModelAttribute("learningdataVO") LearningdataVO learningdataVO,
	      @RequestParam(name = "datano", defaultValue = "0") int datano,
	      @RequestParam(name = "ques", defaultValue = "") String ques,
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
	      ques = Tool.checkNull(ques).trim();
	      model.addAttribute("memberVO", memberVO);
	      model.addAttribute("datano", datano);
	      model.addAttribute("ques", ques);
	      model.addAttribute("now_page", now_page);

	      HashMap<String, Object> map = new HashMap<>();
	      map.put("memberno", memberno);
	      map.put("ques", ques);
	      map.put("now_page", now_page);
	      map.put("startRow", startRow);
	      map.put("endRow", endRow);

	      ArrayList<LearningdataVO> list = this.learningdataProc.list_by_datano_search_paging(map);
	      if (list == null || list.isEmpty()) {
	          model.addAttribute("message", "게시물이 없습니다.");
	      } else {
	          model.addAttribute("list", list);          
	          model.addAttribute("ques", ques);
	      }

	      int search_count = this.learningdataProc.count_by_datano_search(map);
	      String paging = this.learningdataProc.pagingBox(now_page, ques, "/learningdata/list_by_datano_search_paging", search_count,
	          Learningdata.RECORD_PER_PAGE, Learningdata.PAGE_PER_BLOCK);
	      model.addAttribute("paging", paging);
	      model.addAttribute("ques", ques);
	      model.addAttribute("now_page", now_page);
	      model.addAttribute("search_count", search_count);

	      int no = search_count - ((now_page - 1) * Learningdata.RECORD_PER_PAGE);
	      model.addAttribute("no", no);

	      return "/learningdata/list_by_datano_search_paging"; // /templates/board/list_by_boardno_search_paging.html
	 
	  }
	  
	  /**
	   * 학습 데이터 조회
	   * @return
	   */
	  @GetMapping(value = "/read")
	  public String read(HttpSession session, Model model,
	      @RequestParam(name = "datano", defaultValue = "0") int datano,
	      @RequestParam(name = "word", defaultValue = "") String word,
	      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
	    
	    LearningdataVO learningdataVO = this.learningdataProc.read(datano);
	   
	    model.addAttribute("learningdataVO", learningdataVO);
	    
	    MemberVO memberVO = this.memberProc.read(learningdataVO.getMemberno());
	    model.addAttribute("memberVO", memberVO);
	    
	    model.addAttribute("word", word);
	    model.addAttribute("now_page", now_page);
	    
	    return "/learningdata/read";
	  }
	  
	  /**
	   * 학습 데이터 수정 폼
	   * @return
	   */
	  @GetMapping(value = "/update_text")
	  public String update_text(HttpSession session,
	      Model model, 
	      @RequestParam(name = "datano", defaultValue = "0") int datano,
	      RedirectAttributes ra,
	      @RequestParam(name = "word", defaultValue = "") String word,
	      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
	    
	    model.addAttribute("word", word);
	    model.addAttribute("now_page", now_page);
	    
	    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우

	    	LearningdataVO learningdataVO = this.learningdataProc.read(datano);
//	    	System.out.println("-> LearningdataVO.getdatano() : " + LearningdataVO.getdatano());
	        model.addAttribute("learningdataVO", learningdataVO);
	        
	        MemberVO memberVO = this.memberProc.read(learningdataVO.getMemberno());
	        model.addAttribute("memberVO", memberVO);
	        
	        return "/learningdata/update_text";
	    } else {
	        ra.addAttribute("url", "/member/login_cookie_need"); // /templates/diary/login_cookie_need.html
//	        return "redirect:/contents/msg"; // @GetMapping(value = "/read")
	        return "member/login_cookie_need";
	      }
	  }
	  
	  /**
	   * 학습 데이터 수정 처리
	   * @return
	   */
	  @PostMapping(value = "/update_text")
	  public String update_text(
	      HttpSession session,
	      Model model,
	      @ModelAttribute("LearningdataVO") LearningdataVO learningdataVO,
	      RedirectAttributes ra,
	      @RequestParam(name = "datano", defaultValue = "") int datano,
	      @RequestParam(name = "search_word", defaultValue = "") String search_word,
	      @RequestParam(name = "now_page", defaultValue = "0") int now_page) {
		  
	      // Redirect 시 검색어 및 현재 페이지를 유지하기 위한 파라미터 추가
	      ra.addAttribute("word", search_word);
	      ra.addAttribute("now_page", now_page);
	      
	      // ques 값 검증
	      if (learningdataVO.getQues() == null || learningdataVO.getQues().trim().isEmpty()) {
	        ra.addFlashAttribute("message", "이유는 필수 입력 사항입니다.");
	        ra.addFlashAttribute("code", "update_fail");
	        return "redirect:/learningdata/msg"; // 실패 시 msg 페이지로 이동
	      }
	      
	   // ans 값 검증
	      if (learningdataVO.getAns() == null || learningdataVO.getAns().trim().isEmpty()) {
	        ra.addFlashAttribute("message", "이유는 필수 입력 사항입니다.");
	        ra.addFlashAttribute("code", "update_fail");
	        return "redirect:/learningdata/msg"; // 실패 시 msg 페이지로 이동
	      }
	      
	      // 학습 데이터 글 수정 처리
	      try {
	        int cnt = this.learningdataProc.update_text(learningdataVO); // 학습 데이터 글 수정
	        if (cnt > 0) { // 수정 성공
	          ra.addAttribute("datano", learningdataVO.getDatano());
	          return "redirect:/learningdata/read"; // 성공 시 게시글 조회 페이지로 이동
	        } else { // 수정 실패
	          ra.addFlashAttribute("message", "학습 데이터 글 수정에 실패했습니다.");
	          ra.addFlashAttribute("code", "update_fail");
	          return "redirect:/learningdata/msg"; // 실패 시 msg 페이지로 이동
	        }
	      } catch (Exception e) {
	        e.printStackTrace();
	        ra.addFlashAttribute("message", "학습 데이터 글 수정 중 오류가 발생했습니다.");
	        ra.addFlashAttribute("code", "update_fail");
	        return "redirect:/learningdata/msg"; // 오류 발생 시 msg 페이지로 이동
	    }
	  }
	  
	  /**
	   * 학습 데이터 삭제 폼
	   * @param session
	   * @param model
	   * @param ra
	   * @param datano
	   * @param word
	   * @param now_page
	   * @return
	   */
	  @GetMapping(value = "/delete")
	  public String delete(HttpSession session, Model model,
	         RedirectAttributes ra,
	         @RequestParam(name = "datano", defaultValue = "0") int datano,
	         @RequestParam(name = "memberno", defaultValue = "0") int memberno,
	         @RequestParam(name = "word", defaultValue = "") String word,
	         @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
			  model.addAttribute("datano", datano);
			  model.addAttribute("word", word);
			  model.addAttribute("now_page", now_page);
			    
			  LearningdataVO learningdataVO = this.learningdataProc.read(datano);
			  model.addAttribute("learningdataVO", learningdataVO);
			  
			  MemberVO  memberVO = this.memberProc.read(learningdataVO.getMemberno());
			  model.addAttribute("memberVO", memberVO);
			    
			  return "/learningdata/delete"; // forward
		
	  }
	  
	  /**
	   * 학습 데이터 삭제 처리
	   * @param ra
	   * @param datano
	   * @param word
	   * @param now_page
	   * @return
	   */
	  @PostMapping(value = "/delete")
	  public String delete(RedirectAttributes ra,
		  @RequestParam(name="memberno", defaultValue="0") int memberno, 
	      @RequestParam(name = "datano", defaultValue = "0") int datano,
	      @RequestParam(name = "word", defaultValue = "") String word,
	      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

	    this.learningdataProc.delete(datano); // DBMS 삭제
	    
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
	    
	    return "redirect:/learningdata/list_by_datano_search_paging";
	  }
	  
	  @GetMapping(value = "/download")
	  public ResponseEntity<String> exportCsv(HttpServletResponse response) throws IOException {
	      SimpleDateFormat date = new SimpleDateFormat("yyMMddHHmmSS");
	      String fileName = "learningdata_" + date.format(new Date()) + ".csv";

	      ArrayList<LearningdataVO> list = learningdataProc.findAll();
	      String[] dataColumn = {"질문", "답변"};

	      StringBuilder csvFile = new StringBuilder();

	      // Adding the header: Each column is now a row (질문, 답변)
	      csvFile.append(dataColumn[0]).append(',');
	      csvFile.append(dataColumn[1]).append('\n');

	      // Adding the data rows: Each row will represent a question and answer
	      for (LearningdataVO data : list) {
	          csvFile.append(data.getQues().replace(",", "/")).append(',');
	          csvFile.append(data.getAns().replace(",", "/")).append('\n');
	      }

	      HttpHeaders headers = new HttpHeaders();
	      headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	      headers.add("Content-Type", "text/csv; charset=MS949");

	      return new ResponseEntity<>(csvFile.toString(), headers, HttpStatus.CREATED);
	  }

	  
}
