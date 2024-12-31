package dev.mvc.illustration;

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
    
    @GetMapping("/list_by_illustno_search_paging_grid")
    public String listByIllustNoSearchPagingGrid(@RequestParam(name = "illustno", defaultValue = "1") int illustno,
                                                 @RequestParam(name = "now_page", defaultValue = "1") int nowPage, 
                                                 @RequestParam(name = "word", defaultValue = "") String word, 
                                                 HttpSession session, Model model) {
        // 별명 조회
      session.setAttribute("illustno", illustno);
      int memberno = (int) session.getAttribute("memberno");
      String nickname = memberProc.getNickname(memberno);
      model.addAttribute("nickname", nickname);

      Date ddate = illustrationProc.getDiaryDateByIllustNo(illustno);
      model.addAttribute("ddate", ddate);

      // 일러스트 목록 조회
      ArrayList<IllustrationVO> list = illustrationProc.listByIllustNoSearchPaging(illustno, word, nowPage);
      model.addAttribute("list", list);

      // 페이징 처리
      int searchCount = illustrationProc.searchCount(illustno, word);
      String paging = illustrationProc.pagingBox(illustno, searchCount, nowPage, word);
      model.addAttribute("paging", paging);

      model.addAttribute("illustno", illustno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", nowPage);

      return "/illustration/list_by_illustno_search_paging_grid";
  }
    
  @GetMapping("/diary/list_by_date_range")
  public String listByDateRange( Model model,
          @RequestParam(value = "start_date", required = false) String start_date,
          @RequestParam(value = "end_date", required = false) String end_date) {
      List<DiaryVO> diaryList = illustrationProc.listByDateRange(start_date, end_date);
      model.addAttribute("diaryList", diaryList);
      model.addAttribute("start_date", start_date);
      model.addAttribute("end_date", end_date);
      return "/diary/list_by_date_range"; // Thymeleaf view name
  }

    
}
