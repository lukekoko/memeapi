package com.lukekoko.memeapi;

import com.lukekoko.memeapi.meme.MemeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class MemeapiApplicationTests {

    @Autowired
    MemeController controller;
    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
}
