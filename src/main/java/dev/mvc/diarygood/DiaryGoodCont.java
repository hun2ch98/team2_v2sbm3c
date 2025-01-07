package dev.mvc.diarygood;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/diarygood")
public class DiaryGoodCont {

  public DiaryGoodCont() {
    System.out.println("-> DiaryGoodCont Creadted");
  }
  
  
  
}
