package com.lukekoko.memeapi.meme;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MemeController {

    private final MemeService memesService;

    @GetMapping(value = "/meme", produces = "application/json")
    public String getMeme(
            @RequestParam(name = "subreddit", required = false, defaultValue = "memes")
                    String subreddit) {
        return memesService.getPost(subreddit);
    }

    @GetMapping(value = "/random", produces = "application/json")
    public String getRandom() {
        return memesService.getRandom();
    }
}
