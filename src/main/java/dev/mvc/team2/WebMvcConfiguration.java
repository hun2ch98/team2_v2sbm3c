package dev.mvc.team2;

<<<<<<< HEAD
<<<<<<< HEAD

=======
import org.springframework.context.annotation.Configuration;
>>>>>>> main
=======
import org.springframework.context.annotation.Configuration;
>>>>>>> upstream/main
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import dev.mvc.board.Board;
<<<<<<< HEAD
<<<<<<< HEAD
import dev.mvc.member.Member;

=======

@Configuration
>>>>>>> main
=======
import dev.mvc.member.Member;


@Configuration
>>>>>>> upstream/main
public class WebMvcConfiguration implements WebMvcConfigurer{
  
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    
      // C:/kd/deploy/team2/board/storage ->  /board/storage -> http://localhost:9093/board/storage;
      registry.addResourceHandler("/board/storage/**").addResourceLocations("file:///" +  Board.getUploadDir());
      
      System.out.println("Upload Directory: " + Board.getUploadDir());
      
      // C:/kd/deploy/team2/board/storage ->  /board/storage -> http://localhost:9093/board/storage;
      registry.addResourceHandler("/member/storage/**").addResourceLocations("file:///" +  Member.getUploadDir());

  }

}
