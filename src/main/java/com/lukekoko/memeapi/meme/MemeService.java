package com.lukekoko.memeapi.meme;

import com.google.gson.*;
import com.lukekoko.memeapi.reddit.RedditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@AllArgsConstructor
public class MemeService {
    private final RedditService redditService;

    private final MemeRepository memeRepository;

    private static final Random random = new Random();

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public String getPost(String subreddit) {
        String url = "https://oauth.reddit.com/r/" + subreddit + "/hot?limit=20";
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
            return gson.toJson(memes);
        } catch (Exception ex) {
            log.error("Error occurred: ", ex);
            return gson.toJson("hehe");
        }
    }

    public String getRandom() {
        List<Meme> memes = new ArrayList<>();
        memeRepository.findAll().forEach(memes::add);

        return gson.toJson(memes.get(random.nextInt(memes.size())));
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
                .upvotes(obj.get("ups").getAsInt())
                .spoiler(obj.get("spoiler").getAsBoolean())
                .build();
    }
}
