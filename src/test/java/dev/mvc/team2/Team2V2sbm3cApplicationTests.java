package dev.mvc.team2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.mvc.notice.NoticeDAOInter;
import dev.mvc.notice.NoticeVO;

@SpringBootTest
class Team2V2sbm3cApplicationTests {
  @Autowired // NoticeDAOInter를 구현한 클래스의 객체를 생성하여 noticeDAO 변수에 할당
  private NoticeDAOInter noticeDAO;

	@Test
	void contextLoads() {
	}
	
	@Test
	public void testCreate() {
	  NoticeVO noticeVO = new NoticeVO();
	  noticeVO.setTitle("공지 사항!");
	  
	  int cnt = this.noticeDAO.create(noticeVO);
	  System.out.println("-> cnt: " + cnt);
	}

}
