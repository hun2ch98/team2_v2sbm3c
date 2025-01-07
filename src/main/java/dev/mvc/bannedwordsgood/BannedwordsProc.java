package dev.mvc.bannedwordsgood;

import java.util.ArrayList;

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
	
	@Override
    public ArrayList<BannedwordsgoodVO> list_all() {
        ArrayList<BannedwordsgoodVO> list = this.bannedwordsgoodDAO.list_all();
        return list;
    }

}
