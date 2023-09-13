package com.lukekoko.memeapi.meme;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemeRepository extends CrudRepository<Meme, String> {}
