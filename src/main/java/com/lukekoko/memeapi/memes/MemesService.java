package com.lukekoko.memeapi.memes;

import com.google.gson.*;
import com.lukekoko.memeapi.reddit.RedditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MemesService {
    private final RedditService redditService;

    public String getPost(String subreddit) {
        String url = "https://oauth.reddit.com/r/" + subreddit + "/hot?limit=5";
        try {
            String response = redditService.doGetRequest(url);

            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonObject("data").getAsJsonArray("children");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonArray memes = new JsonArray();
            for (final JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject().getAsJsonObject("data");
                Memes meme = createMeme(obj);
                memes.add(gson.toJsonTree(meme));
            }
            return gson.toJson(memes);
        } catch (Exception ex) {
            log.error("Error occurred: ", ex);
            return "error occurred";
        }
    }

    private Memes createMeme(JsonObject obj) {
        log.info(obj.toString());
        return Memes.builder()
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
