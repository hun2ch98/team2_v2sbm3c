package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.dto.SearchDTO;

public interface MemberDAOInter {
  
  /**
   * 중복 아이디 검사
   * @param id
   * @return
   */
  public int checkID(String id);
  
  /**
   * 이메일 중복 검사
   * @param email
   * @return
   */
  public int checkEMAIL(String email);
  
  /**
   * 회원 가입
   * @param memberVO
   * @return
   */
  public int create(MemberVO memberVO);
  
  /**
   * 검색 회원 수
   * @param memberVO
   * @return
   */
  public int list_search_count(SearchDTO searchDTO);
  
  /**
   * 회원 검색 + 페이징 목록
   * @param memberVO
   * @return
   */
  public ArrayList<MemberVO> list_search_paging(SearchDTO searchDTO);

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
   * 회원 정보 수정 처리
   * @param memberVO
   * @return
   */
  public int update(MemberVO memberVO);
  
  /**
   * 파일 수정
   * @param memberVO
   * @return
   */
  public int update_file(MemberVO memberVO);
  
  /**
   * 회원 탈퇴 처리 -> grade(등급) 99: 탈퇴 회원 번호로 변경
   * @param memberVO
   * @return
   */
  public int unsub_delete(MemberVO memberVO);
  
  /**
   * 로그인 처리
   */
  public int login(HashMap<String, Object> map);
  
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
   * 문자 인증 성공 시 비밀번호 수정 처리
   * @param map
   * @return 수정한 비밀번호 개수
   */
  public int update_passwd_find(HashMap<String, Object> map);
  
  /**
   * 이메일, 이름 입력받아서 일치하는 회원이 있는지 검사
   * @param email
   * @param name
   * @return
   */
  public int find_id_check(HashMap<String, String> map);
  
  /**
   * 아이디, 이메일 입력받아서 일치하는 회원이 있는지 검사
   * @param id
   * @param email
   * @return
   */
  public int find_passwd(String id, String phone);
}
