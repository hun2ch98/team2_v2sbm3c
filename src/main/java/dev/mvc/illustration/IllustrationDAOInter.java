package dev.mvc.illustration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import dev.mvc.diary.DiaryVO;

@Mapper
@Repository
public interface IllustrationDAOInter {

    int create(IllustrationVO illustrationVO);

    ArrayList<IllustrationVO> list_all();

    ArrayList<IllustrationVO> list_by_illustno(int illustno);

    IllustrationVO read(int illustno);

    int update_text(IllustrationVO illustrationVO);

    int update_file(IllustrationVO illustrationVO);

    int delete(int illustno);

    int list_by_illustno_search_count(HashMap<String, Object> hashMap);

    ArrayList<IllustrationVO> list_by_illustno_search_paging(HashMap<String, Object> map);

    int count_by_illustno(int illustno);

    Date getDiaryDateByIllustNo(int illustno);
    
    /**
     * 검색어와 페이지 번호에 따라 일러스트 목록을 가져옴
     * @param map
     * @return 일러스트 목록
     */
    ArrayList<IllustrationVO> listByIllustNoSearchPaging(HashMap<String, Object> map);
    
    public List<DiaryVO> listByDateRange(Map<String, Object> paramMap);

}
