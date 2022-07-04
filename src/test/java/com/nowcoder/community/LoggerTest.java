package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;



/**
 * @author sasayaya
 * @create 2022/7/4 22:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class LoggerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerTest.class);
    @Test
    public void testLogger(){
        System.out.println(LOGGER.getName());

        LOGGER.debug("debug log");
        LOGGER.info("info");
        LOGGER.warn(" warn");
        LOGGER.error(" error");


    }
}
