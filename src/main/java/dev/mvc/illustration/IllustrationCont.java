package dev.mvc.illustration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.diary.DiaryProcInter;
import dev.mvc.diary.DiaryVO;
import dev.mvc.log.LogProcInter;
import dev.mvc.log.LogVO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@Controller
@RequestMapping("/illustration")
public class IllustrationCont {

  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.diary.DiaryProc") // @Service("dev.mvc.member.MemberProc")
  private DiaryProcInter diaryProc;
  
  @Autowired
  @Qualifier("dev.mvc.illustration.IllustrationProc")
  private IllustrationProcInter illustrationProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/illustration/list_all";

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
  
  /**
   * 등록 폼
   */
  @GetMapping("/create")
  public String create(Model model, @ModelAttribute("illustrationVO") IllustrationVO illustrationVO) {
      // Diary 테이블에서 날짜 리스트를 가져옵니다.
      List<Date> availableDates = diaryProc.getAvailableDates(); // DiaryProc 호출
      availableDates.sort(Comparator.naturalOrder());
      model.addAttribute("availableDates", availableDates);
      model.addAttribute("illustrationVO", illustrationVO);
      return "/illustration/create"; // /templates/illustration/create.html
  }

  /**
   * Create: 그림, 날짜별로 등록
   */
  @PostMapping("/create")
  public String create(@ModelAttribute IllustrationVO illustrationVO, 
                       @RequestParam("selectedDate") String selectedDate, // 선택된 날짜
                       RedirectAttributes ra, HttpSession session, 
                       Model model, HttpServletRequest request) {
    
    int memberno = (int) session.getAttribute("memberno");
      if (memberProc.isMemberAdmin(session)) {
          String illust = "";
          String illust_saved = "";
          String illust_thumb = "";

          String upDir = Illustration.getUploadDir();

          MultipartFile mf = illustrationVO.getIllustMF();
          illust = mf.getOriginalFilename();

          long illust_size = mf.getSize();

          if (illust_size > 0) {
              if (Tool.checkUploadFile(illust)) {
                  illust_saved = Upload.saveFileSpring(mf, upDir);

                  if (Tool.isImage(illust_saved)) {
                      illust_thumb = Tool.preview(upDir, illust_saved, 200, 150);
                  }
                  illustrationVO.setIllust(illust);
                  illustrationVO.setIllust_saved(illust_saved);
                  illustrationVO.setIllust_thumb(illust_thumb);
                  illustrationVO.setIllust_size(illust_size);
              } else {
                  ra.addFlashAttribute("code", "check_upload_file_fail");
                  ra.addFlashAttribute("url", "/illustration/msg");
                  ra.addFlashAttribute("cnt", 0);
                  return "redirect:/illustration/msg";
              }
          }

          // ddate로 diaryno 가져오기
          java.sql.Date ddate = java.sql.Date.valueOf(selectedDate); // String -> Date 변환
          int diaryno = diaryProc.getDiaryNoByDate(ddate);
          illustrationVO.setDiaryno(diaryno);

          int cnt = this.illustrationProc.create(illustrationVO);

          if (cnt == 1) {
            logAction("create", "illustration", memberno, "파일명=" + illustrationVO.getIllust(), request, "Y");
              ra.addAttribute("illustno", illustrationVO.getIllustno());
              return "redirect:/illustration/list_all";
          } else {
            logAction("create", "illustration", memberno, "파일명=" + illustrationVO.getIllust(), request, "N");
              ra.addFlashAttribute("code", "check_upload_file_fail");
              ra.addFlashAttribute("url", "/illustration/msg");
              ra.addFlashAttribute("cnt", 0);
              return "redirect:/illustration/msg";
          }
      } else {
          return "redirect:/member/login_cookie_need";
      }
  }

  @GetMapping(path="/read/{illustno}")
  public String read(Model model, HttpSession session, 
                     @PathVariable("illustno") int illustno, HttpServletRequest request,
                     @RequestParam(value = "title", required = false, defaultValue = "") String title,
                     @RequestParam(value = "start_date", required = false, defaultValue = "") String start_date,
                     @RequestParam(value = "end_date", required = false, defaultValue = "") String end_date,
                     @RequestParam(name="now_page", defaultValue = "1") int now_page) {
      IllustrationVO illustrationVO = this.illustrationProc.read(illustno);
      model.addAttribute("illustrationVO", illustrationVO);

      int memberno = (int) session.getAttribute("memberno");
      if (illustrationVO.getDiaryno() > 0) {
          DiaryVO diaryVO = this.illustrationProc.readDiary(illustrationVO.getDiaryno());
          model.addAttribute("diaryVO", diaryVO);
      }
      
      long size1 = illustrationVO.getIllust_size();
      String illust_size_label = Tool.unit(size1);
      illustrationVO.setIllust_size_label(illust_size_label);
      model.addAttribute("now_page", now_page);
      model.addAttribute("title", title);
      model.addAttribute("start_date", start_date);
      model.addAttribute("end_date", end_date);
      logAction("read", "illustration", memberno, "파일명=" + illustrationVO.getIllust(), request, "Y");
      return "/illustration/read";
  }
  

  @GetMapping(value="/list_all")
  public String listAll(Model model, @RequestParam(name="illustno", defaultValue="1") int illustno, 
      @RequestParam(value = "title", required = false, defaultValue = "") String title,
      @RequestParam(value = "start_date", required = false, defaultValue = "") String start_date,
      @RequestParam(value = "end_date", required = false, defaultValue = "") String end_date,
      @RequestParam(value = "now_page", required = false, defaultValue = "1") int now_page) {
    
      title = title.trim();
      start_date = start_date.trim();
      end_date = end_date.trim();
      
      int startNum = (now_page - 1) * record_per_page + 1;
      int endNum = now_page * record_per_page;
      int searchCount = illustrationProc.countSearchResults(title, start_date, end_date);
   
      String paging = illustrationProc.pagingBox(now_page, title, start_date, end_date, list_file_name, searchCount, record_per_page, page_per_block);
      System.out.println("Generated Paging HTML: " + paging);
      
      model.addAttribute("illustno", illustno);
      model.addAttribute("title", title);
      model.addAttribute("start_date", start_date);
      model.addAttribute("end_date", end_date);
      model.addAttribute("paging", paging);
      model.addAttribute("search_count", searchCount);
      System.out.println("-> searchCount : " + searchCount);
      model.addAttribute("now_page", now_page);
      
      System.out.println("start_date: " + start_date);
      System.out.println("end_date: " + end_date);
      System.out.println("startNum: " + startNum + ", endNum: " + endNum);
      
      if (title.isEmpty() && start_date.isEmpty() && end_date.isEmpty()) {
        List<Map<String, Object>> illustrations = illustrationProc.listAllWithDiaryDetails();
        model.addAttribute("illustrations", illustrations);
        System.out.println(illustrations);
      } else {
      	List<Map<String, Object>> illustrations = illustrationProc.list_search_paging(title, now_page, record_per_page, startNum, endNum, start_date, end_date);
      	model.addAttribute("illustrations", illustrations);
        System.out.println(illustrations);
      }
      
      return "/illustration/list_all";
  }

  

  @GetMapping(path="/delete/{illustno}")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
                                  @PathVariable("illustno") int illustno, 
                                  @RequestParam(value = "title", required = false, defaultValue = "") String title,
                                  @RequestParam(value = "start_date", required = false, defaultValue = "") String start_date,
                                  @RequestParam(value = "end_date", required = false, defaultValue = "") String end_date,
                                  @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMemberAdmin(session)) {
      model.addAttribute("illustno", illustno);
      model.addAttribute("now_page", now_page);
      model.addAttribute("title", title);
      model.addAttribute("start_date", start_date);
      model.addAttribute("end_date", end_date);
      
      IllustrationVO illustrationVO = this.illustrationProc.read(illustno);
      model.addAttribute("illustrationVO", illustrationVO);
      return "/illustration/delete";
    } else {
      return "/member/login_cookie_need"; 
    }
  }
  
  /**
   * 삭제 처리 http://localhost:9091/illustration/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra, Model model, HttpServletRequest request, HttpSession session, 
      @RequestParam(name="illustno", defaultValue = "0") int illustno,
      @RequestParam(value = "title", required = false, defaultValue = "") String title,
      @RequestParam(value = "start_date", required = false, defaultValue = "") String start_date,
      @RequestParam(value = "end_date", required = false, defaultValue = "") String end_date,
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    int memberno = (int) session.getAttribute("memberno");
    IllustrationVO illustrationVO_read = illustrationProc.read(illustno);
        
    String file1saved = illustrationVO_read.getIllust_saved();
    String thumb1 = illustrationVO_read.getIllust_thumb();
    
    String uploadDir = Illustration.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
    Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
        
    this.illustrationProc.delete(illustno); 
    model.addAttribute("title", title);
    model.addAttribute("start_date", start_date);
    model.addAttribute("end_date", end_date);
    logAction("delete", "illustration", memberno, "파일명=" + illustrationVO_read.getIllust(), request, "Y");
    return "redirect:/illustration/list_all";    
    
  }   
  

  @GetMapping(path="/update/{illustno}")
  public String update(HttpSession session, Model model, 
      @PathVariable("illustno") int illustno,
      @RequestParam(value = "title", required = false, defaultValue = "") String title,
      @RequestParam(value = "start_date", required = false, defaultValue = "") String start_date,
      @RequestParam(value = "end_date", required = false, defaultValue = "") String end_date,
      @RequestParam(name="now_page", defaultValue="0") int now_page) {
    IllustrationVO illustrationVO = this.illustrationProc.read(illustno);
    model.addAttribute(illustrationVO);
    model.addAttribute(now_page);
    model.addAttribute("title", title);
    model.addAttribute("start_date", start_date);
    model.addAttribute("end_date", end_date);
    
    return "/illustration/update";
  }
  
  
  @PostMapping(value="/update")
  public String update(HttpSession session, Model model,RedirectAttributes ra, HttpServletRequest request,
      @ModelAttribute("illustrationVO") IllustrationVO illustrationVO, 
      @RequestParam(value = "title", required = false, defaultValue = "") String title,
      @RequestParam(value = "start_date", required = false, defaultValue = "") String start_date,
      @RequestParam(value = "end_date", required = false, defaultValue = "") String end_date,
      @RequestParam(name="now_page", defaultValue="0") int now_page, 
      @RequestParam(name="illustno", defaultValue="0") int illustno ) {
    
    int memberno = (int) session.getAttribute("memberno");
    if (this.memberProc.isMemberAdmin(session)) {
      IllustrationVO illustrationVO_old = illustrationProc.read(illustrationVO.getIllustno());
      
      // 파일 삭제 시
      String illust_saved = illustrationVO_old.getIllust_saved();
      String illust_thumb = illustrationVO_old.getIllust_thumb();
      long illust_size = 0;
      
      String upDir = Illustration.getUploadDir();
      
      Tool.deleteFile(upDir, illust_saved);
      Tool.deleteFile(upDir, illust_thumb);
      // 파일 삭제 종료
      
      // 파일 전송 시작
      String illust = "";
      
      MultipartFile mf = illustrationVO.getIllustMF();
      
      illust = mf.getOriginalFilename();
      illust_size = mf.getSize();
      
      if (illust_size > 0) {
        illust_saved = Upload.saveFileSpring(mf, upDir);
        
        if (Tool.isImage(illust_saved)) {
          illust_thumb = Tool.preview(upDir, illust_saved, 250, 200);
        }
      } else {
        illust = "";
        illust_saved = "";
        illust_thumb = "";
        illust_size=0;
      }
      
      illustrationVO.setIllust(illust);
      illustrationVO.setIllust_saved(illust_saved);
      illustrationVO.setIllust_thumb(illust_thumb);
      illustrationVO.setIllust_size(illust_size);
      // 파일 전송 종료
      
      this.illustrationProc.update(illustrationVO);
      ra.addAttribute("illustno", illustrationVO.getIllustno());
      ra.addAttribute("now_page", now_page);
      model.addAttribute("illustno", illustno);
      model.addAttribute("now_page", now_page);
      model.addAttribute("title", title);
      model.addAttribute("start_date", start_date);
      model.addAttribute("end_date", end_date);
      logAction("update", "illustration", memberno, "파일명=" + illustrationVO.getIllust(), request, "Y");
      return "redirect:/illustration/list_all";
    } else {
      logAction("update", "illustration", memberno, "파일명=" + illustrationVO.getIllust(), request, "N");
        return "/member/login_cookie_need"; // GET
      }
  }
  
  @RequestMapping("/diary/read")
  public String getDiaryIllustrations(@RequestParam int diaryno, Model model) {
      List<IllustrationVO> illustrationList = illustrationProc.getIllustrationsByDiaryNo(diaryno);
      model.addAttribute("illustrationList", illustrationList);  // 데이터를 Model에 추가

      return "diary/read";  // "diary/read" 템플릿으로 이동
  }
  
  @GetMapping(value="/list_calendar")
  public String list_calendar(HttpSession session, Model model, 
      @RequestParam(name="year", defaultValue="0") int year, 
    @RequestParam(name="month", defaultValue="0") int month) {

      if (year == 0) {
        // 현재 날짜를 가져옴
        LocalDate today  = LocalDate.now();
        
        //연도와 월을 추출
        year = today.getYear();
        month = today.getMonthValue();
      }
      
      String month_str = String.format("%02d", month); // 두 자리 형식으로
    System.out.println("-> month: " + month_str);
  
    String date = year + "-" + month;
    System.out.println("-> date: " + date);
    
    model.addAttribute("year", year);
    model.addAttribute("month", month-1);  // javascript는 1월이 0임. 
      
    return "/illustration/list_calendar"; // /templates/calendar/list_calendar.html
  }
  
  
  @GetMapping(value="/list_calendar_day")
  @ResponseBody
  public String list_calendar_day(Model model, 
      @RequestParam(name="ddate", defaultValue = "") Date ddate) {
    
    System.out.println("-> ddate : " + ddate);
    
    ArrayList<DiaryIllustrationVO> list = this.illustrationProc.list_calendar_day(ddate);
    model.addAttribute(list);
    
    JSONArray calendar_list = new JSONArray();
    
    for (DiaryIllustrationVO diaryillustrationVO : list) {
      System.out.println("Illustno: " + diaryillustrationVO.getIllustno());
      System.out.println("Illust Thumb: " + diaryillustrationVO.getIllust_thumb());
      
      JSONObject calendar = new JSONObject();
      //diaryVO
      calendar.put("diaryno", diaryillustrationVO.getDiaryno());
      calendar.put("ddate", diaryillustrationVO);
      //emotionVO
      calendar.put("emono", diaryillustrationVO.getEmono());
      calendar.put("em_file1", diaryillustrationVO);
      //weatherVO
      calendar.put("weatherno", diaryillustrationVO.getWeatherno());
      calendar.put("we_file1", diaryillustrationVO.getWe_file1());
      //illustrationVO
      calendar.put("illustno", diaryillustrationVO.getIllustno());
      calendar.put("illust_thumb", diaryillustrationVO.getIllust_thumb());
      
      calendar_list.put(calendar);
    }
    return calendar_list.toString();
  }
  
  
  
  
}