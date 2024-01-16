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

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
@AllArgsConstructor
public class MemeService {
    private static final Random random = new Random();
    private static final Gson gson =
            Converters.registerLocalDateTime(new GsonBuilder()).setPrettyPrinting().create();
    private static final ReentrantLock lock = new ReentrantLock();
    private final RedditService redditService;
    private final MemeRepository memeRepository;

    public String getRandom(String subreddit) throws InterruptedException {
        log.info("getting random meme from {}", subreddit);
        List<Meme> memes = memeRepository.findAllBySubreddit(subreddit);
        memes.removeIf(Objects::isNull);
        if (memes.isEmpty()) {
            getPosts(subreddit, "hot");
            memes = memeRepository.findAllBySubreddit(subreddit);
        }
        return !memes.isEmpty()
                ? gson.toJson(memes.get(random.nextInt(memes.size())))
                : "No memes :(";
    }

    private void getPosts(String subreddit, String listing) throws InterruptedException {
        if (lock.isLocked()) {
            log.warn("Lock is locked, not fetching data");
            TimeUnit.SECONDS.sleep(3);
            return;
        }
        log.info("fetching {} memes from {}", listing, subreddit);
        String url = "https://oauth.reddit.com/r/" + subreddit + "/" + listing + "?limit=49";
        try {
            lock.lock();
            String response = redditService.doGetRequest(url);
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonObject("data").getAsJsonArray("children");

            List<Meme> memes = new ArrayList<>();
            for (final JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject().getAsJsonObject("data");
                Meme meme = createMeme(obj);
                if (memeRepository.findById(meme.getId()).isEmpty()) {
                    memes.add(meme);
                }
            }
            memeRepository.saveAll(memes);
        } catch (Exception ex) {
            log.error("Error occurred: ", ex);
        } finally {
            lock.unlock();
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
