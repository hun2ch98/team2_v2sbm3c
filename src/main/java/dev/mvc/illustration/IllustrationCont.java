package dev.mvc.illustration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
  @Qualifier("dev.mvc.illustration.IllustrationProc")
  private IllustrationProcInter illustrationProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/diary/list_by_illustno_search_paging_grid";

    /**
     * 등록 폼
     */
    @GetMapping("/create")
    public String create(Model model, @ModelAttribute("illustrationVO") IllustrationVO illustrationVO) {
        model.addAttribute("illustrationVO", illustrationVO);
        return "/illustration/create"; // /templates/illustration/create.html
    }

    /**
     * Create: 그림만 넣어도 처리
     */
    @PostMapping("/create")
    public String create(@ModelAttribute IllustrationVO illustrationVO, 
                                     RedirectAttributes ra, HttpSession session, 
                                     Model model,  HttpServletRequest request) {
      if (memberProc.isMemberAdmin(session)) {
        String illust = "";
        String illust_saved = "";
        String illust_thumb = "";
        
        String upDir = Illustration.getUploadDir();
        
        MultipartFile mf = illustrationVO.getIllustMF();
        illust = mf.getOriginalFilename();
        
        long illust_size = mf.getSize();
        
        if (illust_size > 0) {
          if (Tool.checkUploadFile(illust) == true) {
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
          }
        }
        
        int memberno = (int) session.getAttribute("memberno");
        illustrationVO.setMemberno(memberno);
        
        int cnt = this.illustrationProc.create(illustrationVO);
        
        if (cnt == 1) {
          ra.addAttribute("illustno", illustrationVO.getIllustno());
          return "redirect:/illustration/list_by_illustno_search_paging_grid";
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

    /**
     * Delete: 파일 및 레코드 삭제
     */
    @PostMapping("/delete")
    public String delete(@RequestParam int illustno, 
                         @RequestParam(name = "now_page", defaultValue = "1") int now_page,
                         RedirectAttributes ra) {
        IllustrationVO vo = illustrationProc.read(illustno);
        if (vo != null) {
            String upDir = Illustration.getUploadDir();
            Tool.deleteFile(upDir, vo.getIllust_saved());
            Tool.deleteFile(upDir, vo.getIllust_thumb());
            illustrationProc.delete(illustno);
        }

        ra.addAttribute("now_page", now_page);
        return "redirect:/illustration/list_by_illustno";
    }

    /**
     * Read: 일러스트 세부 정보 출력
     */
    @GetMapping("/read")
    public String read(@RequestParam int illustno, 
                       @RequestParam(name = "now_page", defaultValue = "1") int now_page,
                       Model model) {
        IllustrationVO illustrationVO = illustrationProc.read(illustno);
        if (illustrationVO != null) {
            model.addAttribute("illustrationVO", illustrationVO);
        }

        model.addAttribute("now_page", now_page);
        return "/illustration/read";
    }

  /**
   * Update_File: 기존 파일 갱신
   */
  @PostMapping("/update_file")
  public String updateFile(@ModelAttribute IllustrationVO illustrationVO, 
                           @RequestParam(name = "now_page", defaultValue = "1") int now_page,
                           RedirectAttributes ra) {
      IllustrationVO oldVO = illustrationProc.read(illustrationVO.getIllustno());
      if (oldVO != null) {
          String upDir = Illustration.getUploadDir();
          Tool.deleteFile(upDir, oldVO.getIllust_saved());
          Tool.deleteFile(upDir, oldVO.getIllust_thumb());
      }

      MultipartFile mf = illustrationVO.getIllustMF();
      if (mf != null && !mf.isEmpty()) {
          String upDir = Illustration.getUploadDir();
          String originalFileName = mf.getOriginalFilename();
          long fileSize = mf.getSize();

          if (Tool.checkUploadFile(originalFileName)) {
              String savedFileName = Upload.saveFileSpring(mf, upDir);
              String thumbnailFileName = Tool.preview(upDir, savedFileName, 250, 200);

              illustrationVO.setIllust(originalFileName);
              illustrationVO.setIllust_saved(savedFileName);
              illustrationVO.setIllust_thumb(thumbnailFileName);
              illustrationVO.setIllust_size(fileSize);
          } else {
              ra.addFlashAttribute("code", "invalid_file_type");
              return "redirect:/illustration/msg";
          }
      }

      illustrationProc.update_file(illustrationVO);
      ra.addAttribute("illustno", illustrationVO.getIllustno());
      ra.addAttribute("now_page", now_page);
      return "redirect:/illustration/read";
  }
    
//  @GetMapping("/list_by_illustno_search_paging_grid")
//  public String listByIllustNoSearchPagingGrid(HttpSession session, Model model,
//                                               @RequestParam(name = "start_date", required = false) String startDate,
//                                               @RequestParam(name = "end_date", required = false) String endDate,
//                                               @RequestParam(name = "now_page", defaultValue = "1") int nowPage) {
//    int memberno = (int) session.getAttribute("memberno");
//    String nickname = memberProc.getNickname(memberno);
//    model.addAttribute("nickname", nickname);
//
//    // 기본 날짜 설정
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    if (startDate == null || startDate.trim().isEmpty()) {
//        startDate = "2015-01-01";
//    }
//    if (endDate == null || endDate.trim().isEmpty()) {
//        endDate = LocalDate.now().format(formatter); // 오늘 날짜로 설정
//    }
//
//    // 페이징 처리
//    int startNum = (nowPage - 1) * record_per_page + 1;
//    int endNum = nowPage * record_per_page;
//
//    HashMap<String, Object> paramMap = new HashMap<>();
//    paramMap.put("start_date", startDate);
//    paramMap.put("end_date", endDate);
//    paramMap.put("startNum", startNum);
//    paramMap.put("endNum", endNum);
//
//    ArrayList<IllustrationVO> list = illustrationProc.listByIllustNoSearchPaging(paramMap);
//    model.addAttribute("list", list);
//
//    int searchCount = illustrationProc.countByDateRange(paramMap);
//    String paging = illustrationProc.pagingBox(nowPage, startDate, endDate, list_file_name, searchCount, record_per_page, page_per_block);
//
//    model.addAttribute("list", list);
//    model.addAttribute("paging", paging);
//
//    model.addAttribute("start_date", startDate);
//    model.addAttribute("end_date", endDate);
//    model.addAttribute("search_count", searchCount);
//    model.addAttribute("now_page", nowPage);
//    
//    IllustrationVO illustrationVO = new IllustrationVO();
//    model.addAttribute("illustrationVO", illustrationVO);
//
//    return "/illustration/list_by_illustno_search_paging_grid";
//  }
  
  @GetMapping("/list_by_illustno_search_paging_grid")
  public String listByIllustNoSearchPagingGrid(HttpSession session, Model model,
                                                                    @RequestParam(value = "start_date", required = false, defaultValue = "") String startDate,
                                                                    @RequestParam(value = "end_date", required = false, defaultValue = "") String endDate,
                                                                    @RequestParam(value = "now_page", required = false, defaultValue = "1") int nowPage) {
    if (this.memberProc.isMemberAdmin(session)) {
      startDate = startDate.trim();
      endDate = endDate.trim();
      int startNum = (nowPage - 1) * record_per_page + 1;
      int endNum = nowPage * record_per_page;
      
      int searchCount = illustrationProc.countSearchResults(startDate, endDate);
      ArrayList<IllustrationVO> illustList = illustrationProc.list_search_paging(startDate, endDate, startNum, endNum);

      String paging = illustrationProc.pagingBox(nowPage, startDate, endDate, list_file_name, searchCount, record_per_page, page_per_block);

      model.addAttribute("illustList", illustList);
      model.addAttribute("start_date", startDate);
      model.addAttribute("end_date", endDate);
      model.addAttribute("paging", paging);
      model.addAttribute("search_count", searchCount);
      model.addAttribute("now_page", nowPage);
      
      
      
      return "redirect:/diary/list_by_diaryno_search_paging_grid";
    } else {
      return "redirect:/member/login_cookie_need";
    }
    
  }
    
}
