package dev.mvc.team2;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.mvc.board.Board;


public class WebMvcConfiguration implements WebMvcConfigurer{
  
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    
      // C:/kd/deploy/team2/board/storage ->  /board/storage -> http://localhost:9093/board/storage;
      registry.addResourceHandler("/board/storage/**").addResourceLocations("file:///" +  Board.getUploadDir());
      
      System.out.println("Upload Directory: " + Board.getUploadDir());

      
  }

}
