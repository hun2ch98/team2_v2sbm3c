package dev.mvc.member;

import org.springframework.web.multipart.MultipartFile;

import dev.mvc.member.MemberVO.CreateValidationGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MemberVO {
  
  
  public interface UpdateValidationGroup {}
  public interface CreateValidationGroup {}
  
  /** 회원 번호 */
  private int memberno = 0;
  
  /** 등급 번호 */
  private Integer gradeno;
  
  /** 아이디(이메일) */
  @NotEmpty(message="아이디 입력은 필수입니다.")
  private String id = "";
  
  /** 패스워드 */
  @NotEmpty(groups = CreateValidationGroup.class, message="비밀번호 입력은 필수입니다.")
  private String passwd = "";
  
  /** 이메일 주소 */
  @NotEmpty(message="이메일 입력은 필수입니다.")
  private String email="";
  
  /** 회원 성명 */
  @NotEmpty(message="이름 입력은 필수입니다.")
  private String name = "";
  
  /** 회원 별명 */
  @NotEmpty(message="닉네임 입력은 필수입니다.")
  @Size(min=2, max=12, message="닉네은 최소 2자에서 최대 12자입니다.")
  private String nickname = "";
  
  /** 생년월일 */
  @NotEmpty(message="생년월일 입력은 필수입니다.")
  private String birth = "";
  
  /** 우편 번호 */
  @NotEmpty(message="우편번호 입력은 필수입니다.")
  private String zipcode = "";
  /** 주소 1 */
  private String address1 = "";
  /** 주소 2 */
  private String address2 = "";
  
  /** 작성 댓글수 */
  private int replycnt = 0;
  
  /** 가입일 */
  private String mdate = "";
  
  /** 등급 */
  private int grade = 0;
  
  // -----------------------------------------------------------------------------------
  /** 파일 업로드 관련 */
  private MultipartFile file1MF = null;
  /** 메인 이미지 크기 단위,  파일 크기 */
  private String size1_label = "";
  /** 프로필 이미지 */
  private String pf_img = "";
  /** 실제 저장된 프로필 이미지 */
  private String file1saved = "";
  /** 프로필 이미지 preview */
  private String thumb1 = "";
  /** 메인 이미지 크기 */
  private long size1 = 0;
  // -----------------------------------------------------------------------------------
  
//  /** 등록된 패스워드 */
//  private String old_passwd = "";
  /** id 저장 여부 */
  private String id_save = "";
  /** passwd 저장 여부 */
  private String passwd_save = "";
  /** 이동할 주소 저장 */
  private String url_address = "";
}

