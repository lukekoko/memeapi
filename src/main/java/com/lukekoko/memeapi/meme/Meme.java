package com.lukekoko.memeapi.meme;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.*;
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
