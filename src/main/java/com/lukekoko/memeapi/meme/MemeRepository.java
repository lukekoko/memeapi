package com.lukekoko.memeapi.meme;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MemeRepository extends ListCrudRepository<Meme, String> {
    List<Meme> findAllBySubreddit(String subreddit);
}
