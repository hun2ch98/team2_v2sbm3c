package dev.mvc.weather;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.weather.WeatherProc")
public class WeatherProc implements WeatherProcInter{  
	@Autowired
	private WeatherDAOInter weatherDAO;
	
	public WeatherProc() {
	    System.out.println("-> WeatherProc created.");
	}
	
	@Override
    public int create(WeatherVO weatherVO) {
        int cnt = this.weatherDAO.create(weatherVO);
        return cnt;
    }

    @Override
    public WeatherVO read(int weatherno) {
    	WeatherVO weatherVO = this.weatherDAO.read(weatherno);
        return weatherVO;
    }

    @Override
    public ArrayList<WeatherVO> list_all() {
        ArrayList<WeatherVO> list = this.weatherDAO.list_all();
        return list;
    }
    @Override
    public ArrayList<WeatherVO> image_list() {
      ArrayList<WeatherVO> image_list = this.weatherDAO.image_list();
      return image_list;
    }

    @Override
    public int update_text(WeatherVO weatherVO) {
        int cnt = this.weatherDAO.update_text(weatherVO);
        return cnt;
    }

    @Override
    public int update_file(WeatherVO weatherVO) {
        int cnt = this.weatherDAO.update_file(weatherVO);
        return cnt;
    }

    @Override
    public int delete(int weatherno) {
        int cnt = this.weatherDAO.delete(weatherno);
        return cnt;
    }

    @Override
    public ArrayList<WeatherVO> list_by_weatherno_search_paging(HashMap<String, Object> map) {
        // `now_page`를 기반으로 `startRow`와 `endRow`를 계산합니다.
        int now_page = (int) map.get("now_page");
        int record_per_page = 5; // 페이지당 레코드 수

        int startRow = (now_page - 1) * record_per_page + 1;
        int endRow = now_page * record_per_page;

        // 계산된 값을 `HashMap`에 추가합니다.
        map.put("startRow", startRow);
        map.put("endRow", endRow);

        // 데이터베이스 쿼리 실행
        ArrayList<WeatherVO> list = this.weatherDAO.list_by_weatherno_search_paging(map);
        return list;
    }

    @Override
    public int count_by_weatherno_search(HashMap<String, Object> map) {
        int cnt = this.weatherDAO.count_by_weatherno_search(map);
        return cnt;
    }

    @Override
    public String pagingBox(int memberno, int now_page, String type, String list_file, int search_count,
            int record_per_page, int page_per_block) {
        int total_page = (int) Math.ceil((double) search_count / record_per_page);
        int total_grp = (int) Math.ceil((double) total_page / page_per_block);
        int now_grp = (int) Math.ceil((double) now_page / page_per_block);

        int start_page = ((now_grp - 1) * page_per_block) + 1;
        int end_page = now_grp * page_per_block;

        StringBuffer str = new StringBuffer();
        str.append("<style type='text/css'>");
        str.append("  #paging {text-align: center; margin-top: 5px; font-size: 1em;}");
        str.append("  .span_box_1{border: 1px solid #cccccc; padding:1px 6px; margin:1px;}");
        str.append("  .span_box_2{background-color: #668db4; color: white; border: 1px solid #cccccc; padding:1px 6px; margin:1px;}");
        str.append("</style>");
        str.append("<div id='paging'>");

        // 이전 그룹 링크
        int _now_page = (now_grp - 1) * page_per_block;
        if (now_grp > 1) {
            str.append("<span class='span_box_1'><a href='" + list_file + "?memberno=" + memberno +
                    "&type=" + type + "&now_page=" + _now_page + "'>이전</a></span>");
        }

        // 현재 그룹의 페이지 링크
        for (int i = start_page; i <= end_page; i++) {
            if (i > total_page) break;
            if (i == now_page) {
                str.append("<span class='span_box_2'>" + i + "</span>");
            } else {
                str.append("<span class='span_box_1'><a href='" + list_file + "?memberno=" + memberno +
                        "&type=" + type + "&now_page=" + i + "'>" + i + "</a></span>");
            }
        }

        // 다음 그룹 링크
        _now_page = now_grp * page_per_block + 1;
        if (now_grp < total_grp) {
            str.append("<span class='span_box_1'><a href='" + list_file + "?memberno=" + memberno +
                    "&type=" + type + "&now_page=" + _now_page + "'>다음</a></span>");
        }

        str.append("</div>");
        return str.toString();
    }

    @Override
    public ArrayList<WeatherVO> list_by_weatherno(int weatherno) {
      HashMap<String, Object> map = new HashMap<>();
      map.put("weatherno", weatherno);
      ArrayList<WeatherVO> list = this.weatherDAO.list_by_weatherno(weatherno);
      return list;
    }

    @Override
    public ArrayList<WeatherVO> list_by_weatherno_search(HashMap<String, Object> hashMap) {
      ArrayList<WeatherVO> list = this.weatherDAO.list_by_weatherno_search(hashMap);
      return list;
    }
    
}
