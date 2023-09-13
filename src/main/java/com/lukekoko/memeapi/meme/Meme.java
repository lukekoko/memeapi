package com.lukekoko.memeapi.meme;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Meme")
public class Meme implements Serializable {
    private String id;

    @SerializedName("postLink")
    private String postLink;

    @SerializedName("subreddit")
    private String subreddit;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("author")
    private String author;

    @SerializedName("ups")
    private Integer upvotes;

    @SerializedName("nsfw")
    private Boolean nsfw;

    @SerializedName("spoiler")
    private Boolean spoiler;

    @SerializedName("preview")
    private List<String> preview;
}
