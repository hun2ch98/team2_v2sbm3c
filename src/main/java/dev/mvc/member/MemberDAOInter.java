package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.board.BoardVO;

public interface MemberDAOInter {
  
  /**
   * 중복 아이디 검사
   * @param id
   * @return
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
  public ArrayList<MemberVO> list_all();

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
}
