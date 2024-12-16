package dev.mvc.emotion;

public interface EmotionDAOInter {
  
  /**
   * 감정 생성
   * @param emotionVO
   * @return
   */
  public int create(EmotionVO emotionVO);
  
  /**
   * 감정 조회
   * @param emno
   * @return
   */
  public EmotionVO read(int emno);
  
  /**
   * 수정
   * @param emotionVO
   * @return
   */
  public int update(EmotionVO emotionVO);
  
  /**
   * 감정 삭제
   * @param emno
   * @return
   */
  public int delete(int emno);
  
}
