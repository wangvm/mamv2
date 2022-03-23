package edu.cuz.mamv2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @author VM
 */
@MapperScan("edu.cuz.mamv2.mapper")
@SpringBootApplication
public class MamApplication {

    public static void main(String[] args) {
        SpringApplication.run(MamApplication.class, args);
    }

}
