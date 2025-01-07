package dev.mvc.noticegood;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/noticegood")
public class NoticegoodCont {
  public NoticegoodCont() {
    System.out.println("-> NoticegoodCont created.");
  }
}
