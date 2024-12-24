package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;

public interface MemberProcInter {
  /**
   * 중복 아이디 검사
   * @param id
   * @return 중복 아이디 갯수
   */
  public int checkID(String id);
  
  /**
   * 회원 가입
   * @param memberVO
   * @return
   */
  public int create(MemberVO memberVO);
  
  /**
   * 회원 전체 목록
   * @return
   */
  public ArrayList<MemberVO> list();

  /**
   * memberno로 회원 정보 조회
   * @param memberno
   * @return
   */
  public MemberVO read(int memberno);
  
  /**
   * id로 회원 정보 조회
   * @param id
   * @return
   */
  public MemberVO readById(String id);
  
  /**
   * 로그인된 회원 계정인지 검사합니다.
   * @param session
   * @return true: 사용자
   */
  public boolean isMember(HttpSession session);

  /**
   * 로그인된 회원 관리자 계정인지 검사합니다.
   * @param session
   * @return true: 사용자
   */
  public boolean isMemberAdmin(HttpSession session);

  /**
   * 수정 처리
   * @param memberVO
   * @return
   */
  public int update(MemberVO memberVO);
  
  /**
   * 프로필 이미지 수정
   * @param memberVO
   * @return
   */
  public int update_text(MemberVO memberVO);
  
  /**
   * 파일 수정
   * @param memberVO
   * @return
   */
  public int update_file(MemberVO memberVO);
 
  /**
   * 회원 삭제 처리
   * @param memberno
   * @return
   */
  public int delete(int memberno);
  
  /**
   * 현재 패스워드 검사
   * @param map
   * @return 0: 일치하지 않음, 1: 일치함
   */
  public int passwd_check(HashMap<String, Object> map);
  
  /**
   * 패스워드 변경
   * @param map
   * @return 변경된 패스워드 갯수
   */
  public int passwd_update(HashMap<String, Object> map);
  
  /**
   * 로그인 처리
   */
  public int login(HashMap<String, Object> map);
  
  /**
   * 회원번호별 검색 목록
   * @param hashMap
   * @return
   */
  public ArrayList<MemberVO> list_by_memberno_search(HashMap<String, Object> hashMap);

  /**
   * 회원번호별 검색된 레코드 갯수
   * @param hashMap
   * @return
   */
  public int list_by_memberno_search_count(HashMap<String, Object> hashMap);

  /**
   * 회원번호별 검색 목록 + 페이징
   * @param map
   * @return
   */
  public ArrayList<MemberVO> list_by_memberno_search_paging(HashMap<String, Object> map);

  
  /**
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param now_page 현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */ 
  public String pagingBox(int memberno, int now_page, String word, String list_file, int search_count, 
                                        int record_per_page, int page_per_block);


}
