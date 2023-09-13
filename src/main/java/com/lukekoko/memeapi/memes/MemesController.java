package com.lukekoko.memeapi.memes;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MemesController {

    private final MemesService memesService;

    @GetMapping(value = "/meme", produces = "application/json")
    public String getMeme(
            @RequestParam(name = "subreddit", required = false, defaultValue = "memes")
                    String subreddit) {
        return memesService.getPost(subreddit);
    }
}
