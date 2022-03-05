package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.model.User;

import java.util.Optional;
import java.util.Set;

public interface UrlService {
  Url save(Url url, User urlOwner);

  Optional<Url> findByFullUrl(String fullUrl);

  Optional<Url> findByShortUrl(String shortUrl);

  Set<Url> findAll();

  void delete(Url url);
}
