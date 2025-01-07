package dev.mvc.surveygood;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/SurveygoodCont")
public class SurveygoodCont {
  public SurveygoodCont() {
    System.out.println(" ->SurveygoodCont created.");
  }

}
