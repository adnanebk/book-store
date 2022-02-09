package com.cloud_steam.bookstore.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {

  @Id private UUID id = UUID.randomUUID();
  private String text;

  public Comment(String text) {
    this.text = text;
  }
}
