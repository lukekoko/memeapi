package com.lukekoko.memeapi.meme;

import com.google.gson.annotations.SerializedName;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value="Meme", timeToLive = 60L * 10L)
public class Meme implements Serializable {
    @Id
    private String id;

    @SerializedName("postLink")
    private String postLink;

    @Indexed
    @SerializedName("subreddit")
    private String subreddit;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("author")
    private String author;

    @SerializedName("nsfw")
    private Boolean nsfw;

    @SerializedName("spoiler")
    private Boolean spoiler;

}
