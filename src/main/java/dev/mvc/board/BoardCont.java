package dev.mvc.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.mvc.member.MemberProcInter;

@Controller
@RequestMapping("/board")
public class BoardCont {

  @Autowired
  @Qualifier("dev.mvc.board.BoardProc")
  private BoardProcInter BoardProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") 
  private MemberProcInter memberProc;
  
  public BoardCont() {
    System.out.println("-> BoardCont created.");
  }
  
  

}
