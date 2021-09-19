package com.wellseecoding.server.entity.post;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private String name;
    private String deadline;
    private String schedule;
    private String summary;
    private String qualification;
    private String size;
}
