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

    List<Map<String, Object>> listAllWithDiaryDetails();
    
    /**
     * 일러스트 번호로 일러스트 정보 조회
     * @param illustno
     * @return Map<String, Object>
     */
    Map<String, Object> read(int illustno);

    /**
     * 일기 번호로 일기 정보 조회
     * @param diaryno
     * @return Map<String, Object>
     */
    Map<String, Object> readDiary(int diaryno);
    
    public int delete(int illustno);
 
    public int update(IllustrationVO illustrationVO);
    
    int cntcount(int illustno);
    
    List<IllustrationVO> getIllustrationsByDiaryNo(int diaryno);
    
    public ArrayList<IllustrationVO> list_search_paging(Map<String, Object> map);

    int list_search_count(String title, String date);

    ArrayList<IllustrationVO> list_search(String title, String date);
    
}