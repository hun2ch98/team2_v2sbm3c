package dev.mvc.log;

import java.util.ArrayList;

import dev.mvc.diary.DiaryVO;

public interface LogProcInter {

	public int create(LogVO logVO);
	
	public ArrayList<LogVO> list_all();
	
	public int delete(int logno);
	
	public LogVO read(int logno);
	
	int countSearchResults(String table, String action, String ip, String is_success, String start_date, String end_date);
	
	 public ArrayList<LogVO> list_search_paging(String table, String action, String ip, String is_success, int  now_page, int record_per_page, int start_num, int end_num);
	
	 String pagingBox(int now_page, String table, String action, String ip, String is_success, String start_date, String end_date, String list_file_name, int search_count, int record_per_page,
	      int page_per_block);
}