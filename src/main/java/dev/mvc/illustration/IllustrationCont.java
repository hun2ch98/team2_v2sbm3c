package dev.mvc.illustration;

import java.util.HashMap;

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

import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@Controller
@RequestMapping("/illustration")
public class IllustrationCont {

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
    public String create(@ModelAttribute IllustrationVO illustrationVO, RedirectAttributes ra) {
        MultipartFile mf = illustrationVO.getIllustMF();
        if (mf != null && !mf.isEmpty()) {
            String upDir = Illustration.getUploadDir();
            String originalFileName = mf.getOriginalFilename();
            long fileSize = mf.getSize();

            if (Tool.checkUploadFile(originalFileName)) { // 파일 형식 확인
                String savedFileName = Upload.saveFileSpring(mf, upDir);
                String thumbnailFileName = Tool.preview(upDir, savedFileName, 200, 150);

                illustrationVO.setIllust(originalFileName);
                illustrationVO.setIllust_saved(savedFileName);
                illustrationVO.setIllust_thumb(thumbnailFileName);
                illustrationVO.setIllust_size(fileSize);
            } else {
                ra.addFlashAttribute("code", "invalid_file_type");
                return "redirect:/illustration/msg";
            }
        }

        int result = illustrationProc.create(illustrationVO);
        if (result == 1) {
            ra.addAttribute("illustno", illustrationVO.getIllustno());
            return "redirect:/illustration/list_by_illustno";
        } else {
            ra.addFlashAttribute("code", "create_fail");
            return "redirect:/illustration/msg";
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
}
