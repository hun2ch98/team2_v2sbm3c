package dev.mvc.illustration;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE illustration (
//    illustno            NUMBER(10)    NOT NULL    PRIMARY KEY,
//    illust              VARCHAR(100)    NOT NULL,
//    illust_saved        VARCHAR(100)  NOT NULL,
//    illust_size         NUMBER(10)    NULL,
//    illust_thumb        VARCHAR(100)  NULL,
//    conversationno      NUMBER(10),  
//    FOREIGN KEY (illustno) REFERENCES ILLUSTLATION(illustno)
//);

@Getter @Setter @ToString
public class IllustrationVO {
    /** 그림 번호 */
    private int illustno;
    
   
    /**
    이미지 파일
    <input type='file' class="form-control" name='file1MF' id='file1MF' 
               value='' placeholder="파일 선택">
    */
    private MultipartFile illustMF = null;
    /** 메인 이미지 크기 단위, 파일 크기 */
    private String illust_size_label = "";
    /** 메인 이미지 */
    private String illust = "";
    /** 실제 저장된 메인 이미지 */
    private String illust_saved = "";
    /** 메인 이미지 preview */
    private String illust_thumb = "";
    /** 메인 이미지 크기 */
    private long illust_size = 0;
    /** 대화 번호 */
    private int conversationno;
    /** 회원 번호 */
    private int memberno;

}

