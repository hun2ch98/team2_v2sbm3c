package dev.mvc.weather;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE emotion (
//    weatherno      NUMBER(10)    NOT NULL    PRIMARY KEY,
//    w_type     VARCHAR(100)    NOT NULL,
//    w_image  VARCHAR(100)  NOT NULL
//  );

@Getter @Setter @ToString
public class WeatherVO {
  
  /** 감정 번호 */
  private int weatherno;
  
  /** 감정 종류 */
  private String w_type="";
  
  /** 감정 이미지 */
  private String w_img="";

}
