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
        List<Meme> memes = memeRepository.findAllBySubreddit(subreddit.toLowerCase());
        memes.removeIf(Objects::isNull);
        if (memes.isEmpty()) {
            getPosts(subreddit, "hot");
            memes = memeRepository.findAllBySubreddit(subreddit.toLowerCase());
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
            redditService
                    .doGetRequest(url)
                    .thenApply(
                            response -> {
                                JsonObject jsonObject =
                                        JsonParser.parseString(response).getAsJsonObject();
                                JsonArray jsonArray =
                                        jsonObject
                                                .getAsJsonObject("data")
                                                .getAsJsonArray("children");

                                List<Meme> memes = new ArrayList<>();
                                for (final JsonElement element : jsonArray) {
                                    JsonObject obj =
                                            element.getAsJsonObject().getAsJsonObject("data");
                                    addMeme(memes, obj);
                                }
                                memeRepository.saveAll(memes);
                                return memes;
                            })
                    .get();
        } catch (InterruptedException ex) {
            log.error("thread interrupted");
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            log.error("Error occurred: ", ex);
        } finally {
            lock.unlock();
        }
    }

    private void addMeme(List<Meme> memes, JsonObject obj) {
        String memeUrl = obj.get("url").getAsString();
        if (!memeUrl.endsWith(".jpg")
                && !memeUrl.endsWith(".png")
                && !memeUrl.endsWith(".gif")
                && !memeUrl.endsWith(".jpeg")) {
            return;
        }
        try {
            Meme meme = createMeme(obj);
            if (memeRepository.findById(meme.getId()).isEmpty()) {
                memes.add(meme);
            }
        } catch (NullPointerException ex) {
            log.info("error with this post: {}", obj);
        }
    }

    private Meme createMeme(JsonObject obj) {
        log.debug(obj.toString());
        return Meme.builder()
                .id(obj.get("id").getAsString())
                .author(obj.get("author").getAsString())
                .postLink("https://redd.it/" + obj.get("id").getAsString())
                .url(obj.get("url").getAsString())
                .subreddit(obj.get("subreddit").getAsString().toLowerCase())
                .title(obj.get("title").getAsString())
                .nsfw(obj.get("over_18").getAsBoolean())
                .spoiler(obj.get("spoiler").getAsBoolean())
                .build();
    }
}
