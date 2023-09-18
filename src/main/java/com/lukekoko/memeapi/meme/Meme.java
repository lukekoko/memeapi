package com.lukekoko.memeapi.meme;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

@Entity
@Table(
        name = "Meme",
        indexes = {@Index(name = "idx_meme_id", columnList = "id")})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// @RedisHash("Meme")
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

    @SerializedName("ups")
    private Integer upvotes;

    @SerializedName("nsfw")
    private Boolean nsfw;

    @SerializedName("spoiler")
    private Boolean spoiler;

    @SerializedName("preview")
    @ElementCollection
    private List<String> preview;
}
