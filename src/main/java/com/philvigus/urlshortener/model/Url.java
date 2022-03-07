package com.philvigus.urlshortener.model;

import com.philvigus.urlshortener.security.UniqueShortUrl;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "url")
public class Url {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  private String fullUrl;

  @UniqueShortUrl
  private String shortUrl;

  private long numberOfClicks;

  @PreRemove
  private void removeUrlFromUser() {
    user.getUrls().remove(this);
  }
}
