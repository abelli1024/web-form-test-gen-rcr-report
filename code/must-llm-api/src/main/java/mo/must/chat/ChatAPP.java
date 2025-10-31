package mo.must.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableAsync
@SpringBootApplication
@MapperScan(basePackages = "mo.must.chat.dao")
@EnableScheduling
public class ChatAPP {
    public static void main(String[] args) {
        SpringApplication.run(ChatAPP.class, args);
    }

}
