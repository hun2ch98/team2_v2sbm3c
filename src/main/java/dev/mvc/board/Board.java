package dev.mvc.board;

import java.io.File;

// 파일 업로드 경로는 war 외부의 절대경로를 지정해야 파일이 손실되지 않습니다. 
// 만약 이렇게 안하면 war 생성시마다 업로드 경로가 초기화 되어 등록된 모든 파일이 삭제됩니다. ★
public class Board {
  /** 페이지당 출력할 레코드 갯수 */
  public static int RECORD_PER_PAGE = 8;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public static int PAGE_PER_BLOCK = 10;

  public static synchronized String getUploadDir() {
    String osName = System.getProperty("os.name").toLowerCase();
    String path = "";

    if (osName.contains("win")) { 
//      C:\kd\deploy\team2\board\storage
      path = "C:\\kd\\deploy\\team2\\board\\storage\\";
    } else if (osName.contains("mac")) { // MacOS
      path = "/Users/yourusername/deploy/team2/board/storage/";
    } else { // Linux
      path = "/home/ubuntu/deploy/team2/board/storage/";
    }

    return path;
  }


}



 