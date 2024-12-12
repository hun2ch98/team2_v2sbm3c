package dev.mvc.emotion;

public interface EmotionProcInter {
  
  /**
   * 감정 생성
   * @param emotio1nVO
   * @return
   */
  public int create(EmotionVO emotio1nVO);
  
  /**
   * 감정 조회
   * @param emno
   * @return
   */
  public EmotionVO read(int emno);
  
  /**
   * 수정
   * @param emotio1nVO
   * @return
   */
  public int update(EmotionVO emotio1nVO);
  
  /**
   * 감정 삭제
   * @param emno
   * @return
   */
  public int delete(int emno);
  
}
