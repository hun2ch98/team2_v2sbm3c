package dev.mvc.illustration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import dev.mvc.diary.DiaryVO;

@Mapper
public interface IllustrationDAOInter {

    int create(IllustrationVO illustrationVO);

    ArrayList<IllustrationVO> list_all();

    ArrayList<IllustrationVO> list_by_illustno(int illustno);

    IllustrationVO read(int illustno);

    int update_file(IllustrationVO illustrationVO);

    int delete(int illustno);

    int list_by_illustno_search_count(Map<String, Object> paramMap);

    ArrayList<IllustrationVO> list_by_illustno_search_paging(Map<String, Object> paramMap);

    int count_by_illustno(int illustno);

    Date getDiaryDateByIllustNo(int illustno);

    ArrayList<IllustrationVO> listByIllustNoSearchPaging(HashMap<String, Object> paramMap);

    int searchCount(Map<String, Object> paramMap);

    int countByDateRange(HashMap<String, Object> paramMap);
    
    
    /**
     * 검색 갯수
     * @return
     */
    int list_search_count(@Param("date") String date);

    
    /**
     * 검색 + 페이징 목록
     * select id="list_search_paging" resultType="dev.mvc.diary.DiaryVO" parameterType="Map"
     * @param map
     * @return
     */
    public ArrayList<IllustrationVO> list_search_paging(Map<String, Object> map);

    

    public int cntcount(int illustno);
    
    public ArrayList<IllustrationVO> list_search(String date);
    

    ArrayList<IllustrationVO> listSearch(Map<String, Object> paramMap);
    
    //2323
    ArrayList<IllustrationVO> listByIllustrationPaging(HashMap<String, Object> map);
    int countAllIllustrations();
    String pagingBox(int nowPage, int totalCount, int recordPerPage, int pagePerBlock, String listFileName);

    
    int list_by_illustno_search_count(HashMap<String, Object> map);
    
}
