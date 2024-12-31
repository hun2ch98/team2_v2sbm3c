package dev.mvc.illustration;

import java.util.ArrayList;
import java.util.HashMap;

public interface IllustrationProcInter {

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

    /**
     * 페이징 박스 생성 메서드
     * @param now_page 현재 페이지
     * @param list_file_name 목록 파일 이름
     * @param search_count 검색된 레코드 수
     * @param record_per_page 페이지당 레코드 수
     * @param page_per_block 블럭당 페이지 수
     * @return 페이징 HTML 문자열
     */
    String pagingBox(int now_page, String list_file_name, int search_count, int record_per_page, int page_per_block);

    HashMap<String, Object> getDiaryInfoByIllustNo(int illustno); // Diary 정보를 가져오는 메소드 추가
}