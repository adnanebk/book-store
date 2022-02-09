package com.cloud_steam.bookstore.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book {

  @Id private UUID id = UUID.randomUUID();
  private String name;
  @OneToMany private Set<Comment> comments;

  public Book(String name, Set<Comment> comments) {
    this.name = name;
    this.comments = comments;
  }
}
