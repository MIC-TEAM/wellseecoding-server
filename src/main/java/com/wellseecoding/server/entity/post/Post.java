package com.wellseecoding.server.entity.post;

import com.wellseecoding.server.entity.tag.TagPostMap;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    @Builder.Default
    private Set<TagPostMap> tagPostMaps = new HashSet<>();
}
