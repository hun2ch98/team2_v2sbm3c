package dev.mvc.illustration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import dev.mvc.diary.DiaryVO;

@Component("dev.mvc.illustration.IllustrationProc")
public class IllustrationProc implements IllustrationProcInter {

    @Autowired
    private IllustrationDAOInter illustrationDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public int create(IllustrationVO illustrationVO) {
        return illustrationDAO.create(illustrationVO);
    }

    /**
     * Illustration과 Diary 데이터 가져오기
     *
     * @return illustration 및 diary 데이터를 포함한 리스트
     */
    public List<Map<String, Object>> listAllWithDiaryDetails() {
        return illustrationDAO.listAllWithDiaryDetails();
    }

    @Override
    public IllustrationVO read(int illustno) {
        String sql = "SELECT * FROM illustration WHERE illustno = ?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, illustno);

        IllustrationVO illustrationVO = new IllustrationVO();
        illustrationVO.setIllustno((int) map.get("illustno"));
        illustrationVO.setIllust_thumb((String) map.get("illust_thumb"));
        illustrationVO.setIllust_size((int) map.get("illust_size"));
        illustrationVO.setDiaryno((int) map.getOrDefault("diaryno", 0));

        return illustrationVO;
    }

    @Override
    public DiaryVO readDiary(int diaryno) {
        String sql = "SELECT * FROM diary WHERE diaryno = ?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, diaryno);

        DiaryVO diaryVO = new DiaryVO();
        diaryVO.setDiaryno((int) map.get("diaryno"));
        diaryVO.setTitle((String) map.get("title"));
        diaryVO.setDdate((java.sql.Date) map.get("ddate"));

        return diaryVO;
    }
    
    @Override
    public int delete(int illustno) {
      int cnt = this.illustrationDAO.delete(illustno);
      return cnt;
    }
    
    @Override
    public int update(IllustrationVO illustrationVO) {
    	int cnt = this.illustrationDAO.update(illustrationVO);
    	return cnt;
    }
    
}
