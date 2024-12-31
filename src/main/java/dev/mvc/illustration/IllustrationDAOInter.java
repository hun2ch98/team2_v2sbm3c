package dev.mvc.illustration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import dev.mvc.diary.DiaryVO;

@Mapper
public interface IllustrationDAOInter {

    int create(IllustrationVO illustrationVO);

    ArrayList<IllustrationVO> list_all();

    ArrayList<IllustrationVO> list_by_illustno(int illustno);

    IllustrationVO read(int illustno);

    int update_text(IllustrationVO illustrationVO);

    int update_file(IllustrationVO illustrationVO);

    int delete(int illustno);

    int list_by_illustno_search_count(Map<String, Object> paramMap);

    ArrayList<IllustrationVO> list_by_illustno_search_paging(Map<String, Object> paramMap);

    int count_by_illustno(int illustno);

    Date getDiaryDateByIllustNo(int illustno);

    ArrayList<IllustrationVO> listByIllustNoSearchPaging(HashMap<String, Object> paramMap);

    int searchCount(Map<String, Object> paramMap);

    int countByDateRange(HashMap<String, Object> paramMap);
    
}
