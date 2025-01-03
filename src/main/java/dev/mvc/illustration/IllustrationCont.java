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
  private String list_file_name = "/illustration/list_by_illustno_search_paging_grid";

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

  @GetMapping("/read")
  public String read(Model model, 
                     @RequestParam(name="illustno", defaultValue = "0") int illustno) {
      IllustrationVO illustrationVO = this.illustrationProc.read(illustno);
      model.addAttribute("illustrationVO", illustrationVO);

      if (illustrationVO.getDiaryno() > 0) {
          DiaryVO diaryVO = this.illustrationProc.readDiary(illustrationVO.getDiaryno());
          model.addAttribute("diaryVO", diaryVO);
      }

      return "/illustration/read";
  }
  

  @GetMapping("/list_all")
  public String listAll(Model model) {
      List<Map<String, Object>> list = illustrationProc.listAllWithDiaryDetails();
      System.out.println("Illustrations: " + list); // 로그 출력
      model.addAttribute("illustrations", list);
      return "/illustration/list_all";
  }


  @GetMapping(value="/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
                                  @RequestParam(name="illustno", defaultValue = "0") int illustno, 
                                  @RequestParam(name="title", defaultValue = "title") String title,
                                  @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMemberAdmin(session)) {
      model.addAttribute("illustno", illustno);
      model.addAttribute("title", title);
      model.addAttribute("now_page", now_page);
      
      IllustrationVO illustrationVO = this.illustrationProc.read(illustno);
      model.addAttribute(illustrationVO);
      return "/illustration/delete";
    } else {
      ra.addAttribute("url", "/admin/login_cookie_need");
      return "redirect:/contents/msg"; 
    }
  }
  
  /**
   * 삭제 처리 http://localhost:9091/contents/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra,
      @RequestParam(name="illustno", defaultValue = "0") int illustno,
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    // -------------------------------------------------------------------
    // 파일 삭제 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
    IllustrationVO illustrationVO_read = illustrationProc.read(illustno);
        
    String file1saved = illustrationVO_read.getIllust_saved();
    String thumb1 = illustrationVO_read.getIllust_thumb();
    
    String uploadDir = Illustration.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
    Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
    // -------------------------------------------------------------------
    // 파일 삭제 종료
    // -------------------------------------------------------------------
        
    this.illustrationProc.delete(illustno); // DBMS 글 삭제
        
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------    
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("illustno", illustno);
    map.put("word", word);
    
//    if (this.illustrationProc.list_all(map) % IllustrationProc.RECORD_PER_PAGE == 0) {
//      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
//      if (now_page < 1) {
//        now_page = 1; // 시작 페이지
//      }
//    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("illustno", illustno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/contents/list_all";    
    
  }   
  

    
  
  
  
  
  
  
}
