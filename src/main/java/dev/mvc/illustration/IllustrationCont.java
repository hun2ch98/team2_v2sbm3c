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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.diary.DiaryProcInter;
import dev.mvc.diary.DiaryVO;
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
              ra.addAttribute("illustno", illustrationVO.getIllustno());
              return "redirect:/illustration/list_all";
          } else {
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
  public String read(Model model, 
                     @PathVariable("illustno") int illustno, 
                     @RequestParam(name="now_page", defaultValue = "1") int now_page) {
      IllustrationVO illustrationVO = this.illustrationProc.read(illustno);
      model.addAttribute("illustrationVO", illustrationVO);

      if (illustrationVO.getDiaryno() > 0) {
          DiaryVO diaryVO = this.illustrationProc.readDiary(illustrationVO.getDiaryno());
          model.addAttribute("diaryVO", diaryVO);
      }
      
      long size1 = illustrationVO.getIllust_size();
      String illust_size_label = Tool.unit(size1);
      illustrationVO.setIllust_size_label(illust_size_label);
      model.addAttribute("now_page", now_page);

      return "/illustration/read";
  }
  

  @GetMapping("/list_all")
  public String listAll(Model model, @RequestParam(name="illustno", defaultValue="1") int illustno, 
      @RequestParam(value = "title", required = false, defaultValue = "") String title,
      @RequestParam(value = "start_date", required = false, defaultValue = "") String start_date,
      @RequestParam(value = "end_date", required = false, defaultValue = "") String end_date,
      @RequestParam(value = "now_page", required = false, defaultValue = "1") int nowPage) {
    
      title = title.trim();
      start_date = start_date.trim();
      end_date = end_date.trim();
      
      int startNum = (nowPage - 1) * record_per_page + 1;
      int endNum = nowPage * record_per_page;
      int searchCount = illustrationProc.countSearchResults(title, start_date, end_date);
   // 날짜가 비어있는 경우 전체 목록 조회
      if (start_date.isEmpty() && end_date.isEmpty()) {
        List<Map<String, Object>> list = illustrationProc.listAllWithDiaryDetails();
        model.addAttribute("illustrations", list);
        System.out.println(list);
      } else {
        ArrayList<IllustrationVO> list = illustrationProc.list_search_paging(title, start_date, end_date, startNum, endNum);
        model.addAttribute("illustrations", list);
        System.out.println(list);
      }
      
   // 날짜 값 출력 (디버깅)
      
      
      
      System.out.println("start_date: " + start_date);
      System.out.println("end_date: " + end_date);

      System.out.println("startNum: " + startNum + ", endNum: " + endNum);
      
      String paging = illustrationProc.pagingBox(nowPage, title, start_date, end_date, list_file_name, searchCount, record_per_page, page_per_block);
      
      model.addAttribute("illustno", illustno);
      model.addAttribute("title", title);
      model.addAttribute("start_date", start_date);
      model.addAttribute("end_date", end_date);
      model.addAttribute("paging", paging);
      model.addAttribute("search_count", searchCount);
      model.addAttribute("now_page", nowPage);
      
      return "/illustration/list_all";
  }


  @GetMapping(path="/delete/{illustno}")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
                                  @PathVariable("illustno") int illustno, 
                                  @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMemberAdmin(session)) {
      model.addAttribute("illustno", illustno);
      model.addAttribute("now_page", now_page);
      
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
  public String delete(RedirectAttributes ra, Model model, 
      @RequestParam(name="illustno", defaultValue = "0") int illustno,
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    IllustrationVO illustrationVO_read = illustrationProc.read(illustno);
        
    String file1saved = illustrationVO_read.getIllust_saved();
    String thumb1 = illustrationVO_read.getIllust_thumb();
    
    String uploadDir = Illustration.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
    Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
        
    this.illustrationProc.delete(illustno); // DBMS 글 삭제
        
//    HashMap<String, Object> map = new HashMap<String, Object>();
//    map.put("illustno", illustno);
//    
//    ra.addAttribute("illustno", illustno);
//    ra.addAttribute("now_page", now_page);
//    
    return "redirect:/illustration/list_all";    
    
  }   
  

  @GetMapping(path="/update/{illustno}")
  public String update(HttpSession session, Model model, 
      @PathVariable("illustno") int illustno,
      @RequestParam(name="now_page", defaultValue="0") int now_page) {
    IllustrationVO illustrationVO = this.illustrationProc.read(illustno);
    model.addAttribute(illustrationVO);
    model.addAttribute(now_page);
    
    return "/illustration/update";
  }
  
  
  @PostMapping(value="/update")
  public String update(HttpSession session, Model model,RedirectAttributes ra, 
      @ModelAttribute("illustrationVO") IllustrationVO illustrationVO, 
      @RequestParam(name="now_page", defaultValue="0") int now_page, 
      @RequestParam(name="illustno", defaultValue="0") int illustno ) {
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
      return "redirect:/illustration/list_all";
    } else {
        
        return "/member/login_cookie_need"; // GET
      }
  }
  
  @RequestMapping("/diary/read")
  public String getDiaryIllustrations(@RequestParam int diaryno, Model model) {
      // `diaryno`를 바탕으로 일러스트 데이터 가져오기
      List<IllustrationVO> illustrationList = illustrationProc.getIllustrationsByDiaryNo(diaryno);
      model.addAttribute("illustrationList", illustrationList);  // 데이터를 Model에 추가

      return "diary/read";  // "diary/read" 템플릿으로 이동
  }
  
  
  
}