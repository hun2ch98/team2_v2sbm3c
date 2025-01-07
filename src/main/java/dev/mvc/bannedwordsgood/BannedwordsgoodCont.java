package dev.mvc.bannedwordsgood;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/bannedwordsgood")
public class BannedwordsgoodCont {
	public BannedwordsgoodCont() {
		System.out.println("-> BannedwordsgoodCont created.");
	}
	
}
