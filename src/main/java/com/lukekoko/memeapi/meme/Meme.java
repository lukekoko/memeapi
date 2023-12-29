package com.lukekoko.memeapi.meme;

import com.google.gson.annotations.SerializedName;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "Meme",
        indexes = {@Index(name = "idx_meme_id", columnList = "id")})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Meme")
public class Meme implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
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

    @SerializedName("nsfw")
    private Boolean nsfw;

    @SerializedName("spoiler")
    private Boolean spoiler;

    @SerializedName("createdDate")
    @CreationTimestamp
    private LocalDateTime createdDate;
}
