package dev.mvc.illustration;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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

    @Select("SELECT d.title, d.ddate FROM diary d INNER JOIN illustration i ON d.diaryno = i.diaryno WHERE i.illustno = #{illustno}")
    HashMap<String, Object> getDiaryInfoByIllustNo(int illustno); // Diary 정보 조회 쿼리 추가
}
