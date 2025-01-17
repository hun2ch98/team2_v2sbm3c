package dev.mvc.diary;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.member.MemberProcInter;
import dev.mvc.member.MemberVO;
import dev.mvc.schedule.ScheduleVO;
import dev.mvc.diarygood.DiaryGoodProcInter;
import dev.mvc.diarygood.DiaryGoodVO;
import dev.mvc.emotion.EmotionProcInter;
import dev.mvc.emotion.EmotionVO;
import dev.mvc.illustration.Illustration;
import dev.mvc.illustration.IllustrationProcInter;
import dev.mvc.illustration.IllustrationVO;
import dev.mvc.log.LogProcInter;
import dev.mvc.log.LogVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import dev.mvc.weather.WeatherProcInter;
import dev.mvc.weather.WeatherVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/diary")
public class DiaryCont {
	
  @Autowired
  @Qualifier("dev.mvc.diary.DiaryProc")
  private DiaryProcInter diaryProc;

  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") 
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.illustration.IllustrationProc")
  private IllustrationProcInter illustrationProc;  // IllustrationProc 인터페이스
  
  @Autowired
  @Qualifier("dev.mvc.diarygood.DiaryGoodProc")
  private DiaryGoodProcInter diaryGoodProc;
  
  @Autowired
  @Qualifier("dev.mvc.emotion.EmotionProc")
  private EmotionProcInter emotionProc;
  
  @Autowired
@Qualifier("dev.mvc.weather.WeatherProc")
  private WeatherProcInter weatherProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 5;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/diary/list_by_diaryno_search_paging";
  
  @Autowired
  @Qualifier("dev.mvc.log.LogProc")
  private LogProcInter logProc;

  private void logAction(String action, String table, int memberno, String details, HttpServletRequest request, String is_success) {
      LogVO logVO = new LogVO();
      logVO.setMemberno(memberno);
      logVO.setTable_name(table);
      logVO.setAction(action);
      logVO.setDetails(details);
      logVO.setIp(getClientIp(request)); // IP 주소 설정
      logVO.setIs_success(is_success);
      logProc.create(logVO); // Log 테이블에 삽입
  }

  private String getClientIp(HttpServletRequest request) {
      String ip = request.getHeader("X-Forwarded-For");
      if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("Proxy-Client-IP");
      }
      if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("WL-Proxy-Client-IP");
      }
      if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getRemoteAddr();
      }
      return ip;
  }


  public DiaryCont() {
    System.out.println("-> DiaryCont created.");
  }
  
  public static synchronized String preview2(String upDir, String _src, int width,  int height) {
    int RATIO = 0;
    int SAME = -1;
 
    File src = new File(upDir + "/" + _src); // 원본 파일 객체 생성
    String srcname = src.getName(); // 원본 파일명 추출
 
    // 순수 파일명 추출, mt.jpg -> mt 만 추출
    String _dest = srcname.substring(0, srcname.indexOf("."));
 
    // 축소 이미지 조합 /upDir/mt_t.jpg
    File dest = new File(upDir + "/" + _dest + "_t.jpg");
 
    Image srcImg = null;
 
    String name = src.getName().toLowerCase(); // 파일명을 추출하여 소문자로 변경
    // 이미지 파일인지 검사
    if (name.endsWith("jpg") || name.endsWith("bmp") || name.endsWith("png")
        || name.endsWith("gif")) {
      try {
        srcImg = ImageIO.read(src); // 메모리에 원본 이미지 생성
        int srcWidth = srcImg.getWidth(null); // 원본 이미지 너비 추출
        int srcHeight = srcImg.getHeight(null); // 원본 이미지 높이 추출
        int destWidth = -1, destHeight = -1; // 대상 이미지 크기 초기화
 
        if (width == SAME) { // width가 같은 경우
          destWidth = srcWidth;
        } else if (width > 0) {
          destWidth = width; // 새로운 width를 할당
        }
 
        if (height == SAME) { // 높이가 같은 경우
          destHeight = srcHeight;
        } else if (height > 0) {
          destHeight = height; // 새로운 높이로 할당
        }
 
        // 비율에 따른 크기 계산
        if (width == RATIO && height == RATIO) {
          destWidth = srcWidth;
          destHeight = srcHeight;
        } else if (width == RATIO) {
          double ratio = ((double) destHeight) / ((double) srcHeight);
          destWidth = (int) ((double) srcWidth * ratio);
        } else if (height == RATIO) {
          double ratio = ((double) destWidth) / ((double) srcWidth);
          destHeight = (int) ((double) srcHeight * ratio);
        }
 
        // 메모리에 대상 이미지 생성
        Image imgTarget = srcImg.getScaledInstance(destWidth, destHeight,
            Image.SCALE_SMOOTH);
        int pixels[] = new int[destWidth * destHeight];
        PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, destWidth,
            destHeight, pixels, 0, destWidth);
 
        pg.grabPixels();
 
        BufferedImage destImg = new BufferedImage(destWidth, destHeight,
            BufferedImage.TYPE_INT_RGB);
        destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);
 
        // 파일에 기록
        ImageIO.write(destImg, "jpg", dest);
 
        System.out.println(dest.getName() + " 이미지를 생성했습니다.");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
 
    return dest.getName();
  }

  /**
   * 등록 폼
   * 
   * @param model
   * @return
   */
  // http://localhost:9093/diary/create
  @GetMapping(value = "/create")
  public String create(Model model, 
                                  @RequestParam(name="title", defaultValue="오늘의 제목") String title, 
                                  @RequestParam(name="emono", defaultValue="1") int emono, 
                                  @RequestParam(name="weatherno", defaultValue="1") int weatherno, 
                                  @RequestParam(name="summary", defaultValue="오늘의 일기") String summary) {

    DiaryVO diaryVO = new DiaryVO();

    ArrayList<EmotionVO> emotions = this.emotionProc.image_list();
    ArrayList<WeatherVO> weathers = this.weatherProc.image_list();
    
    System.out.println("Emotions: " + emotions.size());
    System.out.println("Weathers: " + weathers.size());

    
    diaryVO.setTitle(title);
    diaryVO.setEmono(emono);
    diaryVO.setWeatherno(weatherno);
    diaryVO.setSummary(summary);
    
    model.addAttribute("diaryVO", diaryVO);
    model.addAttribute("emotions", emotions);
    model.addAttribute("weatherVO", weathers);

    
    return "/diary/create"; // /templates/diary/create.html
  }

  /**
   * 등록 처리, http://localhost:9093/diary/create
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @param diaryVO        Form 태그 값 -> 검증 -> diaryVO 자동 저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
   // contentCont의 create 처리 과정보고 추가해야함. 
//  @PostMapping(value = "/create")
//  public String create(Model model, HttpSession session, HttpServletRequest request,
//                       @Valid @ModelAttribute("diaryVO") DiaryVO diaryVO, 
//                       BindingResult bindingResult, RedirectAttributes ra) {
//    Integer memberno = (Integer) session.getAttribute("memberno");
//    if (bindingResult.hasErrors()) { 
//        // 에러 발생 시 폼으로 돌아가기
//      bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
//      logAction("read", "diary", memberno, "title=" + diaryVO.getTitle(), request, "N");
//      return "/diary/create";
//    }
//
//    // 제목 및 내용 트림 처리
//    diaryVO.setTitle(diaryVO.getTitle().trim());
//    diaryVO.setSummary(diaryVO.getSummary().trim());
//    diaryVO.setEmono(diaryVO.getEmono());
//    diaryVO.setWeatherno(diaryVO.getWeatherno());
//    
//
//    // ddate 설정
//    if (diaryVO.getDdate() == null) {
//        diaryVO.setDdate(Date.valueOf(LocalDate.now())); // 현재 날짜로 설정
//    }
//
//    // 세션에서 memberno 가져오기
//    
//    if (memberno == null) {
//        ra.addFlashAttribute("message", "로그인이 필요합니다.");
//        return "/member/login_cookie_need";
//    }
//    diaryVO.setMemberno(memberno);
//
//    // DB 저장 로직 호출
//    int cnt = diaryProc.create(diaryVO);
//    System.out.println("-> create_cnt: " + cnt);
//
//    if (cnt == 1) {
//      logAction("create", "diary", memberno, "title=" + diaryVO.getTitle(), request, "Y");
//      int diaryno = diaryVO.getDiaryno();
//      return "redirect:/diary/read/" + diaryno;
//    } else {
//      model.addAttribute("code", "create_fail");
//      logAction("create", "diary", memberno, "title=" + diaryVO.getTitle(), request, "N");
//      return "/diary/msg";
//    }
//  }
  
  @Transactional
  @PostMapping(value = "/create")
  public String create(Model model, HttpSession session, HttpServletRequest request,
                       @Valid @ModelAttribute("diaryVO") DiaryVO diaryVO, 
                       @ModelAttribute("illustrationVO") IllustrationVO illustrationVO,
                       BindingResult bindingResult, RedirectAttributes ra) {
    Integer memberno = (Integer) session.getAttribute("memberno");
    if (bindingResult.hasErrors()) { 
        // 에러 발생 시 폼으로 돌아가기
      bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
      logAction("read", "diary", memberno, "title=" + diaryVO.getTitle(), request, "N");
      return "/diary/create";
    }

    // 제목 및 내용 트림 처리
    diaryVO.setTitle(diaryVO.getTitle().trim());
    diaryVO.setSummary(diaryVO.getSummary().trim());
    diaryVO.setEmono(diaryVO.getEmono());
    diaryVO.setWeatherno(diaryVO.getWeatherno());
    

    // ddate 설정
    if (diaryVO.getDdate() == null) {
        diaryVO.setDdate(Date.valueOf(LocalDate.now())); // 현재 날짜로 설정
    }

    // 세션에서 memberno 가져오기
    
    if (memberno == null) {
        ra.addFlashAttribute("message", "로그인이 필요합니다.");
        return "/member/login_cookie_need";
    }
    diaryVO.setMemberno(memberno);

    // Hidden input에서 전달된 illust 값을 illust_file로 설정
    if (diaryVO.getIllust_file() != null && !diaryVO.getIllust_file().isEmpty()) {
        diaryVO.setIllust_file(diaryVO.getIllust_file());
        System.out.println("Illust file set: " + diaryVO.getIllust_file());
    } else {
        System.out.println("Illust is null or empty");
    }
    
    // DB 저장 로직 호출
    int diary_cnt = diaryProc.create(diaryVO);
    System.out.println("-> create_diary_cnt: " + diary_cnt);

    if (diary_cnt == 1) {
      int diaryno = diaryVO.getDiaryno();
      String illust_thumb = "";
      String upDir = Illustration.getUploadDir();
      String illust_file = diaryVO.getIllust_file();
      System.out.println("-> illust_file: " + illust_file);
      
      if (illust_file != null && !illust_file.isEmpty()) {
        File illustFile = new File(upDir, illust_file); // 파일 객체 생성
        System.out.println("Illustration file: " + illustFile.getAbsolutePath());
        illust_thumb = preview2(upDir, illust_file, 200, 150);
        String illust = illust_file;
        String illust_saved = illust_file;
        long illust_size = illust_file.length();
        illustrationVO.setDiaryno(diaryno);
        illustrationVO.setIllust(illust);
        illustrationVO.setIllust_saved(illust_saved);
        illustrationVO.setIllust_thumb(illust_thumb);
        illustrationVO.setIllust_size(illust_size);
        int illust_cnt = illustrationProc.create(illustrationVO);
        System.out.println("-> create_illust_cnt: " + illust_cnt);
      }
      
      return "redirect:/diary/read/" + diaryno;
    } else {
      model.addAttribute("code", "create_fail");
      logAction("create", "diary", memberno, "title=" + diaryVO.getTitle(), request, "N");
      return "/diary/msg";
    }
  }


  
  
  @GetMapping(path="/read/{diaryno}")
  public String read(HttpSession session, Model model, HttpServletRequest request,
      @PathVariable("diaryno") int diaryno,
      @RequestParam(name="now_page", defaultValue="1") int now_page) {
    
    
    // 일기 번호에 해당하는 일기 데이터 조회
    DiaryVO diaryVO = diaryProc.read(diaryno);  // DiaryProc에서 일기 데이터 조회
    this.diaryProc.increaseCnt(diaryno);
    
    // 일기 번호에 해당하는 일러스트 데이터 조회
    List<IllustrationVO> illustrationList = illustrationProc.getIllustrationsByDiaryNo(diaryno);  // IllustrationProc에서 일러스트 데이터 조회
    //System.out.println(illustrationList);
    // List<DiaryVO> diaryList = diaryProc.read(diaryno);
    
    // 모델에 일기 데이터와 일러스트 목록 추가
    model.addAttribute("diaryVO", diaryVO);  // 일기 데이터
    // model.addAttribute("diaryList", diaryList);  
    model.addAttribute("illustrationList", illustrationList);  // 일러스트 목록
    model.addAttribute("now_page", now_page);  // 현재 페이지
    
    //-----------------------------------------------------------------------------------
    // 좋아요 수 관련 코드
    //-----------------------------------------------------------------------------------
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("diaryno", diaryno);

    int heartCnt = 0;
    if (session.getAttribute("memberno") != null) {
      int memberno = (int) session.getAttribute("memberno");
      map.put("memberno", memberno);
      heartCnt = this.diaryGoodProc.heartCnt(map);
      logAction("read", "diary", memberno, "title=" + diaryVO.getTitle(), request, "Y");
    }
    
    model.addAttribute("heartCnt", heartCnt);
    //-----------------------------------------------------------------------------------
    
    
    return "/diary/read"; // /templates/diary/read.html
  }
  
  

  
  @GetMapping("/list_by_diaryno_search_paging")
  public String listSearch(@RequestParam(name="diaryno", defaultValue="1") int diaryno, 
                           @RequestParam(value = "title", required = false, defaultValue = "") String title,
                           @RequestParam(value = "start_date", required = false, defaultValue = "") String startDate,
                           @RequestParam(value = "end_date", required = false, defaultValue = "") String endDate,
                           @RequestParam(value = "now_page", required = false, defaultValue = "1") int nowPage,
                           Model model) {
      title = title.trim();
      startDate = startDate.trim();
      endDate = endDate.trim();

      int startNum = (nowPage - 1) * record_per_page + 1;
      int endNum = nowPage * record_per_page;

      int searchCount = diaryProc.countSearchResults(title, startDate, endDate);
      ArrayList<DiaryVO> diaryList = diaryProc.list_search_paging(title, startDate, endDate, startNum, endNum);

      String paging = diaryProc.pagingBox(nowPage, title, startDate, endDate, list_file_name, searchCount, record_per_page, page_per_block);

      model.addAttribute("diaryno", diaryno);
      model.addAttribute("diaryList", diaryList);
      model.addAttribute("title", title);
      model.addAttribute("start_date", startDate);
      model.addAttribute("end_date", endDate);
      model.addAttribute("paging", paging);
      model.addAttribute("search_count", searchCount);
      model.addAttribute("now_page", nowPage);

      return "/diary/list_by_diaryno_search_paging";
  }
  
  @GetMapping(path="/delete/{diaryno}")
  public String delete (HttpSession session, Model model, 
                                    @PathVariable("diaryno") int diaryno, 
                                    @RequestParam(value = "title", required = false, defaultValue = "") String title,
                                    @RequestParam(value = "start_date", required = false, defaultValue = "") String start_date,
                                    @RequestParam(value = "end_date", required = false, defaultValue = "") String end_date,
                                    @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMemberAdmin(session)) {
      model.addAttribute("diaryno", diaryno);
      model.addAttribute("now_page", now_page);
      model.addAttribute("title", title);
      model.addAttribute("start_date", start_date);
      model.addAttribute("end_date", end_date);
      
      DiaryVO diaryVO = this.diaryProc.read(diaryno);
      model.addAttribute("diaryVO", diaryVO);
      
      return "diary/delete";
    } else {
      return "/member/login_cookie_need";
    }
  }

  /**
   * 삭제 처리
   * 
   */
  @PostMapping(value = "/delete")
  public String deleteProcess(HttpSession session, Model model, HttpServletRequest request,
                              @RequestParam(name = "diaryno") int diaryno,
                              @RequestParam(name = "title", defaultValue = "") String title,
                              @RequestParam(value = "start_date", required = false, defaultValue = "") String startDate,
                              @RequestParam(value = "end_date", required = false, defaultValue = "") String endDate,
                              @RequestParam(name = "now_page", defaultValue = "1") int nowPage,
                              RedirectAttributes ra) {
      if (this.memberProc.isMemberAdmin(session)) {
        int memberno = (int) session.getAttribute("memberno");
        int startNum = (nowPage - 1) * record_per_page + 1;
        int endNum = nowPage * record_per_page;
        ArrayList<DiaryVO> diaryList = diaryProc.list_search_paging(title, startDate, endDate, startNum, endNum);
        model.addAttribute("diaryList", diaryList);
          // 삭제할 Diary 조회
          DiaryVO diaryVO = this.diaryProc.read(diaryno);

          if (diaryVO != null) {
              // 삭제 수행
            this.diaryGoodProc.f_delete(diaryno);
             int cnt = this.diaryProc.delete(diaryno);
             
             if (cnt == 1) {
                // 삭제 성공 시 검색 조건 유지
                ra.addAttribute("title", title);
                ra.addAttribute("start_date", startDate);
                ra.addAttribute("end_date", endDate);
                ra.addAttribute("diaryList", String.join(",", diaryList.stream().map(Object::toString).toList()));
              
                logAction("delete", "diary", memberno, "title=" + diaryVO.getTitle(), request, "Y");

                
                // 마지막 페이지 처리 (빈 페이지 방지)
                int searchCount = diaryProc.countSearchResults(title, startDate, endDate);
                if (searchCount % this.record_per_page == 0) {
                    nowPage = Math.max(nowPage - 1, 1); // 최소 페이지는 1
                }
                ra.addAttribute("now_page", nowPage);
                
                return "redirect:/diary/list_by_diaryno_search_paging";
             }
          }
          // 삭제 실패 시 처리
          ra.addFlashAttribute("msg", "삭제 실패");
          logAction("delete", "diary", memberno, "title=" + diaryVO.getTitle(), request, "N");
          return "redirect:/diary/list_by_diaryno_search_paging";
      } else {
          return "redirect:/member/login_cookie_need";
      }
  }


  

  /**
   * 수정 폼
   * http://localhost:9093/diary/update/1
   */
  @GetMapping(value = "/update/{diaryno}")
  public String update(HttpSession session, Model model, 
                       @PathVariable("diaryno") Integer diaryno, 
                       @RequestParam(value = "start_date", required = false, defaultValue = "") String startDate,
                       @RequestParam(value = "end_date", required = false, defaultValue = "") String endDate,
                       @RequestParam(name = "title", defaultValue = "") String title,  
                       @RequestParam(name = "now_page", defaultValue = "1") int nowPage) {
      if (this.memberProc.isMemberAdmin(session)) {
        
        int startNum = (nowPage - 1) * record_per_page + 1;
        int endNum = nowPage * record_per_page;
        
        DiaryVO diaryVO = this.diaryProc.read(diaryno); // 수정할 데이터를 조회
        ArrayList<DiaryVO> diaryList = diaryProc.list_search_paging(title, startDate, endDate, startNum, endNum);
        
        model.addAttribute("diaryVO", diaryVO);
        model.addAttribute("title", title); // 검색어 유지
        model.addAttribute("start_date", startDate); 
        model.addAttribute("end_date", endDate); 
        model.addAttribute("diaryList", diaryList); 
        model.addAttribute("now_page", nowPage); // 현재 페이지 유지

        return "/diary/update"; // 수정 폼으로 이동
    } else {
        return "redirect:/member/login_cookie_need"; // 권한이 없으면 로그인 페이지로 리다이렉트
    }
  }
  

  /**
   * 수정 처리
   * http://localhost:9093/diary/update
   */
  @PostMapping(value = "/update")
  public String update(HttpSession session, Model model, HttpServletRequest request,
                       @Valid @ModelAttribute("diaryVO") DiaryVO diaryVO, 
                       @RequestParam("diaryno") int diaryno,
                       BindingResult bindingResult, 
                       @RequestParam(name = "now_page", defaultValue = "1") int now_page, 
                       RedirectAttributes ra) {
    int memberno = (int) session.getAttribute("memberno");
      if (this.memberProc.isMember(session)) {
          if (bindingResult.hasErrors()) { // 폼 에러 처리
            DiaryVO diary1VO = diaryProc.read(diaryno);
            model.addAttribute("diaryVO", diary1VO); // 모델에 추가
            return "diary/update"; // update.html로 이동
          }

          DiaryVO existingDiary = this.diaryProc.read(diaryno);
          diaryVO.setDdate(existingDiary.getDdate());
          
          int cnt = this.diaryProc.update(diaryVO); // 데이터 업데이트
          if (cnt == 1) {
            
            logAction("update", "diary", memberno, "title=" + diaryVO.getTitle(), request, "Y");
              ra.addAttribute("now_page", now_page); // 페이지 번호 전달
              return "redirect:/diary/list_by_diaryno_search_paging"; // 목록 페이지로 리다이렉트
          } else {
              model.addAttribute("code", "update_fail");
              return "/diary/msg"; // 에러 메시지 출력 페이지
          }
      } else {
        logAction("update", "diary", memberno, "title=" + diaryVO.getTitle(), request, "N");
          return "redirect:/member/login_cookie_need"; // 권한이 없으면 로그인 페이지로 리다이렉트
      }
  }
  
  
  @PostMapping(value="/good")
  @ResponseBody
  public String good(HttpSession session, Model model, RedirectAttributes ra, 
      HttpServletRequest request,  @ RequestBody String json_src ) {
    System.out.println("-> json_src : "  + json_src);
    JSONObject src = new JSONObject(json_src); 
    
    int diaryno = (int)src.get("diaryno");
    System.out.println("->diaryno : " + diaryno);
    DiaryVO diaryVO = this.diaryProc.read(diaryno);
    
    if (this.memberProc.isMember(session)) {
      
      // 기존 추천 여부를 확인
      int memberno = (int)session.getAttribute("memberno");
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("diaryno", diaryno);
      map.put("memberno", memberno);
      
      int goodcnt = this.diaryGoodProc.heartCnt(map); 
      System.out.println("->goodcnt : " + goodcnt);
      
      if (goodcnt == 1) {
        // 추천 해제 작업
        System.out.println("-> 추천 해제 : " + diaryno + " " + memberno);
        DiaryGoodVO diaryGoodVO = this.diaryGoodProc.readByDiaryMember(map);
        this.diaryGoodProc.delete(diaryGoodVO.getGoodno()); // 추천 삭제
        this.diaryProc.decreaseGoodCnt(diaryno); // 카운트 감소
        logAction("decreaseGoodCnt", "diary", memberno, "title=" + diaryVO.getTitle(), request, "Y");
        
      } else {
        // 추천 작업
        System.out.println("-> 추천: " + diaryno + ' ' + memberno);
        DiaryGoodVO diaryGoodVO_new = new DiaryGoodVO(); // 새로운 객체 생성
        diaryGoodVO_new.setDiaryno(diaryno);
        diaryGoodVO_new.setMemberno(memberno);
        this.diaryGoodProc.create(diaryGoodVO_new); // 새로운 튜플 생성
        this.diaryProc.increaseGoodCnt(diaryno);
        logAction("increaseGoodCnt", "diary", memberno, "title=" + diaryVO.getTitle(), request, "Y");
      }
      
      int heartCnt = this.diaryGoodProc.heartCnt(map);
      int goodCnt = this.diaryProc.read(diaryno).getGoodcnt();
      
      JSONObject result = new JSONObject();
      result.put("isMember", 1); // 로그인 : 1 , 비회원 : 0
      result.put("heartCnt", heartCnt);
      result.put("goodCnt", goodCnt);
      
      System.out.println("-> result.toString(): " + result.toString());
      
      return result.toString();
      
    } else {
      JSONObject result = new JSONObject();
      result.put("isMember", 0); // 로그인 : 1 , 비회원 : 0
      System.out.println("-> result.toString(): " + result.toString());
      return result.toString();
    }
  }

  /**
   * 특정 날짜의 목록
   * @param session
   * @param model
   * @param date
   * @return
   */
  @GetMapping(value="/list_calendar")
  public String list_calendar(HttpSession session, Model model, 
      @RequestParam(name="year", defaultValue="0") int year, 
      @RequestParam(name="month", defaultValue="0") int month) {
    
    Integer memberno = (Integer) session.getAttribute("memberno");
    
    if (this.memberProc.isMember(session)) {
      MemberVO memberVO = memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);
      if (year == 0) {
        // 현재 날짜를 가져옴
        LocalDate today  = LocalDate.now();
        
        //연도와 월을 추출
        year = today.getYear();
        month = today.getMonthValue();
      }
      
      String month_str = String.format("%02d", month); // 두 자리 형식으로
    //System.out.println("-> month: " + month_str);
    
      String date = year + "-" + month;
    //    System.out.println("-> date: " + date);
      
      model.addAttribute("year", year);
      model.addAttribute("month", month-1);  // javascript는 1월이 0임. 
        
      return "/diary/list_calendar"; // /templates/calendar/list_calendar.html
    } else {
      return "/member/login_cookie_need";
    }
    
  }
  
  
  /**
   * 특정 날짜의 목록
   * 
   * @param model
   * @return
   */
  // http://localhost:9091/calendar/list_calendar_day?labeldate=2025-01-03
  @GetMapping(value = "/list_calendar_day")
  @ResponseBody
  public String list_calendar_day(Model model, @RequestParam(name="ddate", defaultValue = "") Date ddate) {
  
    // 원하는 포맷 설정
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Date를 String으로 변환
    String formattedDate = sdf.format(ddate);
    ArrayList<DiaryVO> list = this.diaryProc.list_calendar_day(formattedDate);
    model.addAttribute("list", list);

    JSONArray diary_list = new JSONArray();
    
    for (DiaryVO diaryVO: list) {
        JSONObject diaries = new JSONObject();
        diaries.put("diaryno", diaryVO.getDiaryno());
        diaries.put("ddate", sdf.format(diaryVO.getDdate()));
        diaries.put("title", diaryVO.getTitle());
        
        ArrayList<IllustrationVO> get_illust = illustrationProc.get_illust(diaryVO.getDiaryno());
        int size = get_illust.size();
        
        if (size == 0) {
          diaries.put("illust_thumb", "");
        } else {
          diaries.put("illust_thumb", get_illust.get(0).getIllust());
        }
        
        diary_list.put(diaries);
        
    }

    return diary_list.toString();
    
  }
}