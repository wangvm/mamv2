package edu.cuz.mamv2;

import edu.cuz.mamv2.entity.dto.ScenesDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author VM
 * @date 2022/4/10 16:45
 */
@SpringBootTest
public class POJOTest {

    @Test
    public void scenesDTOTest(){
        System.out.println(new ScenesDTO());
    }
}
