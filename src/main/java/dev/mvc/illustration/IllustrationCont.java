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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
  private String list_file_name = "/illustration/list_by_illustno_search_paging_grid";

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
     * 파일 수정 폼 http://localhost:9091/contents/update_file?contentsno=1
     * 
     * @return
     */
    @GetMapping(value = "/update_file")
    public String update_file(HttpSession session, Model model, 
                                       @RequestParam(name="illustno", defaultValue = "0") int illustno,
                                       @RequestParam(name="now_page", defaultValue = "1") int now_page) {
      model.addAttribute("now_page", now_page);
      
      IllustrationVO illustrationVO = this.illustrationProc.read(illustno);
      model.addAttribute("illustrationVO", illustrationVO);
      return "/illustration/update_file";

    }

    /**
     * 파일 수정 처리 http://localhost:9091/contents/update_file
     * 
     * @return
     */
    @PostMapping(value = "/update_file")
    public String update_file(HttpSession session, Model model, RedirectAttributes ra,
                              @ModelAttribute("illustration") IllustrationVO illustrationVO,
                              @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
        if (this.memberProc.isMemberAdmin(session)) {
            IllustrationVO illustrationVO_old = illustrationProc.read(illustrationVO.getIllustno());

            // 기존 파일 삭제
            String upDir = Illustration.getUploadDir();
            Tool.deleteFile(upDir, illustrationVO_old.getIllust_saved());
            Tool.deleteFile(upDir, illustrationVO_old.getIllust_thumb());

            // 새 파일 업로드 처리
            MultipartFile mf = illustrationVO.getIllustMF();
            if (mf != null && !mf.isEmpty()) {
                String originalFileName = mf.getOriginalFilename();
                long fileSize = mf.getSize();

                if (fileSize > 0 && Tool.checkUploadFile(originalFileName)) {
                    String savedFileName = Upload.saveFileSpring(mf, upDir);
                    String thumbFileName = Tool.preview(upDir, savedFileName, 250, 200);

                    illustrationVO.setIllust(originalFileName);
                    illustrationVO.setIllust_saved(savedFileName);
                    illustrationVO.setIllust_thumb(thumbFileName);
                    illustrationVO.setIllust_size(fileSize);
                } else {
                    ra.addFlashAttribute("code", "invalid_file_type");
                    return "redirect:/illustration/msg";
                }
            } else {
                ra.addFlashAttribute("code", "no_file_uploaded");
                return "redirect:/illustration/update_file";
            }

            illustrationProc.update_file(illustrationVO);
            ra.addAttribute("illustno", illustrationVO.getIllustno());
            ra.addAttribute("now_page", now_page);
            return "redirect:/illustration/read";
        } else {
            ra.addAttribute("url", "/member/login_cookie_need");
            return "redirect:/illustration/msg";
        }
    }


    

  
  @GetMapping("/list_by_illustno_search_paging_grid")
  public String listByIllustNoSearchPagingGrid(HttpSession session, Model model,
                                                                    @RequestParam(value = "start_date", required = false, defaultValue = "2000-01-01") String startDate,
                                                                    @RequestParam(value = "end_date", required = false, defaultValue = "2030-12-31") String endDate,
                                                                    @RequestParam(value = "now_page", required = false, defaultValue = "1") int nowPage) {
  
    startDate = startDate.trim();
    endDate = endDate.trim();
    int startNum = (nowPage - 1) * record_per_page + 1;
    int endNum = nowPage * record_per_page;

    ArrayList<IllustrationVO> illustList = illustrationProc.listByIllustrationPaging(startNum, endNum);
    
    int totalCount = illustrationProc.countAllIllustrations(); 
    
    String paging = illustrationProc.pagingBox(nowPage, totalCount, record_per_page, page_per_block, list_file_name);

    model.addAttribute("illustList", illustList);
    model.addAttribute("start_date", startDate);
    model.addAttribute("end_date", endDate);
    model.addAttribute("paging", paging);
    model.addAttribute("search_count", totalCount);
    model.addAttribute("now_page", nowPage);

      return "/illustration/list_by_illustno_search_paging_grid";
  }
  
  
  /**
   * 파일 삭제 폼
   * http://localhost:9091/contents/delete?contentsno=1
   * 
   * @return
   */
  @GetMapping(value = "/delete/{illustno}")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
      @RequestParam(name="illustno", defaultValue = "0") int illustno,
      @RequestParam(name="start_date", defaultValue = "") String startDate, 
      @RequestParam(name="end_date", defaultValue = "") String endDate, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    if (this.memberProc.isMemberAdmin(session)) { 
      model.addAttribute("now_page", now_page);

      IllustrationVO illustrationVO = this.illustrationProc.read(illustno);
      model.addAttribute("illustrationVO", illustrationVO);

      return "/illustration/delete"; // forward
      
    } else {
      ra.addAttribute("url", "/admin/login_cookie_need");
      return "redirect:/illustration/msg"; 
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
      @RequestParam(name="start_date", defaultValue = "") String startDate, 
      @RequestParam(name="end_date", defaultValue = "") String endDate, 
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

    this.illustrationProc.delete(illustno); // DBMS 글 삭제

    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("illustno", illustno);
    map.put("startDate", startDate);
    map.put("endDate", endDate);
    
    int count = this.illustrationProc.list_by_illustno_search_count(map);
    if (count % Illustration.RECORD_PER_PAGE == 0) {
      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
      if (now_page < 1) {
        now_page = 1; // 시작 페이지
      }
    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("illustno", illustno);
    ra.addAttribute("startDate", startDate);
    ra.addAttribute("endDate", endDate);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/illustration/list_by_illustno_search_paging_grid";    
    
  }   
    
}
