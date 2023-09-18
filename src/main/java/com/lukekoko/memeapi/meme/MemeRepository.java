package com.lukekoko.memeapi.meme;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemeRepository extends JpaRepository<Meme, String> {
    @Override
    @CacheEvict(value = "findAll")
    List<Meme> findAll();
}
