package dev.mvc.surveyitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.mvc.survey.SurveyProcInter;

@RequestMapping(value = "/surveyitem")
@Controller
public class ItemCont {
  
  @Autowired
  @Qualifier("dev.mvc.surveyitem.ItemProc")
  private ItemProcInter itemProc;
  
  @Autowired
  @Qualifier("dev.mvc.survey.SurveyProc")
  private SurveyProcInter surveyProc;
  
  public ItemCont() {
    System.out.println("-> ItemCont created.");
  }

}
