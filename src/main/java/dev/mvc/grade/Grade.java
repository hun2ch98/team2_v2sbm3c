package dev.mvc.grade;

public class Grade {
  
  /** 페이지당 출력할 레코드 갯수 */
  public static int RECORD_PER_PAGE = 8;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public static int PAGE_PER_BLOCK = 10;
  
  public static synchronized String getUploadDir() {
    String osName = System.getProperty("os.name").toLowerCase();
    String path = "";

    if (osName.contains("win")) { // Windows
      //      C:\kd\deploy\team2\grade\storage
      path = "C:\\kd\\deploy\\team2\\grade\\storage\\";
      // System.out.println("Windows: " + path);
    } else if (osName.contains("mac")) { // MacOS
      path = "/Users/yourusername/deploy/team2/grade/storage/";
      // System.out.println("MacOS: " + path);
    } else { // Linux
      path = "/home/ubuntu/deploy/team2/grade/storage/";
      // System.out.println("Linux: " + path);
    }

    return path;
  }

}


 