package dev.mvc.grade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/grade")
public class GradeCont {
  
  @Autowired
  @Qualifier("dev.mvc.grade.GradeProc")
  private GradeProcInter gradeProc;
  
  /**
   * 등급 등록 폼
   * @return
   */
  @GetMapping("/create")
  public String createForm() {
      return "/grade/create";
  }
  
  /**
   * 등급 등록 처리
   * @param gradeVO
   * @return
   */
  @PostMapping("/create")
  public String create(GradeVO gradeVO) {
      gradeProc.create(gradeVO);
      return "redirect:/grade/list";
  }
  
  /**
   * 등급 목록
   * @param model
   * @return
   */
  @GetMapping("/list")
  public String list(Model model) {
      List<GradeVO> list = gradeProc.list();
      model.addAttribute("list", list);
      return "/grade/list";
  }
  
  /**
   * 등급 상세보기
   * @param gradeno
   * @param model
   * @return
   */
  @GetMapping("/read/{gradeno}")
  public String read(@PathVariable int gradeno, Model model) {
      GradeVO gradeVO = gradeProc.read(gradeno);
      model.addAttribute("gradeVO", gradeVO);
      return "/grade/read";
  }
  
  /**
   * 등급 수정 처리
   * @param gradeVO
   * @return
   */
  @PostMapping("/update")
  public String update(GradeVO gradeVO) {
      gradeProc.update(gradeVO);
      return "redirect:/grade/list";
  }
  
  /**
   * 등급 삭제 처리
   * @param gradeno
   * @return
   */
  @PostMapping("/delete")
  public String delete(@RequestParam int gradeno) {
      gradeProc.delete(gradeno);
      return "redirect:/grade/list";
  }
}
