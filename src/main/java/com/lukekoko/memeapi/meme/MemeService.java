package com.lukekoko.memeapi.meme;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lukekoko.memeapi.reddit.RedditService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@AllArgsConstructor
public class MemeService {
    private final RedditService redditService;

    private final MemeRepository memeRepository;

    private static final Random random = new Random();

    private static final Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).setPrettyPrinting().create();

    public String getRandom() {
        List<Meme> memes = memeRepository.findAll();
        if (memes.isEmpty()) {
            return gson.toJson("No memes :(");
        }
        return gson.toJson(memes.get(random.nextInt(memes.size())));
    }

    @Scheduled(initialDelay = 30 * 1000, fixedDelay = 3 * 60 * 1000)
    private void scheduledPosts() {
        log.info("fetching posts on schedule...");
        List<String> subreddits = new ArrayList<>(Arrays.asList("dankmemes", "memes", "okbuddyretard"));
        for (String subreddit : subreddits) {
            getPosts(subreddit, "new");
        }
    }

    private void getPosts(String subreddit, String listing) {
        log.info("fetching memes from {}", subreddit);
        String url = "https://oauth.reddit.com/r/" + subreddit + "/" + listing + "?limit=10";
        try {
            String response = redditService.doGetRequest(url);
            log.debug(response);
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonObject("data").getAsJsonArray("children");

            List<Meme> memes = new ArrayList<>();
            for (final JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject().getAsJsonObject("data");
                Meme meme = createMeme(obj);
                memes.add(meme);
            }
            memeRepository.saveAll(memes);
        } catch (Exception ex) {
            log.error("Error occurred: ", ex);
        }
    }

    private Meme createMeme(JsonObject obj) {
        log.debug(obj.toString());
        return Meme.builder()
                .id(obj.get("id").getAsString())
                .author(obj.get("author").getAsString())
                .postLink("https://redd.it/" + obj.get("id").getAsString())
                .url(obj.get("url_overridden_by_dest").getAsString())
                .subreddit(obj.get("subreddit").getAsString())
                .title(obj.get("title").getAsString())
                .nsfw(obj.get("over_18").getAsBoolean())
                .spoiler(obj.get("spoiler").getAsBoolean())
                .build();
    }
}
