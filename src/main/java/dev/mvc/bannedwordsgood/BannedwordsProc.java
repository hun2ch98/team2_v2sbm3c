package dev.mvc.bannedwordsgood;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.bannedwordsgood.BannedwordsgoodProc")
public class BannedwordsProc implements BannedwordsgoodProcInter {
	@Autowired
	BannedwordsgoodDAOInter bannedwordsgoodDAO;
	
	@Override
	public int create(BannedwordsgoodVO bannedwordsgoodVO) {
		int cnt = this.bannedwordsgoodDAO.create(bannedwordsgoodVO);
		return cnt;
	}

}
