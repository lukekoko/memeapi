package com.lukekoko.memeapi.meme;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class MemeController {

    private final MemeService memesService;

    @GetMapping(value = "/memes", produces = "application/json")
    public String getMeme(
            @RequestParam(name = "subreddit", required = false, defaultValue = "memes")
                    String subreddit) {
        log.debug("request for memes");
        return memesService.getMemes(subreddit);
    }

    @GetMapping(value = "/random", produces = "application/json")
    public String getRandom() {
        log.debug("request for random");
        return memesService.getRandom();
    }
}
