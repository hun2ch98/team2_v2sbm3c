package dev.mvc.log;

import java.util.ArrayList;
import java.util.Map;

import dev.mvc.diary.DiaryVO;

public interface LogDAOInter {

	public int create(LogVO logVO);
	
	public ArrayList<LogVO> list_all();
	
	public int delete(int logno);
	
	public LogVO read(int logno);
	
	public ArrayList<LogVO> list_search_paging(Map<String, Object> map);
	
	
}