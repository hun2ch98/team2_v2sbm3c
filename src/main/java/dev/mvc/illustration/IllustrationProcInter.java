package dev.mvc.illustration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.mvc.diary.DiaryVO;

public interface IllustrationProcInter {

    int create(IllustrationVO illustrationVO);

    List<Map<String, Object>> listAllWithDiaryDetails();
    
    /**
     * 일러스트 번호로 일러스트 정보 조회
     * @param illustno
     * @return IllustrationVO
     */
    IllustrationVO read(int illustno);

    /**
     * 일기 번호로 일기 정보 조회
     * @param diaryno
     * @return DiaryVO
     */
    DiaryVO readDiary(int diaryno);
    
    int delete(int illustno);
    
}
