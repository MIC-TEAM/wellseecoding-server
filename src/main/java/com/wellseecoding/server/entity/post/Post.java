package com.wellseecoding.server.entity.post;

import com.wellseecoding.server.entity.group.Member;
import com.wellseecoding.server.entity.tag.TagPostMap;
import com.wellseecoding.server.entity.user.User;
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
    private String name;
    private String deadline;
    private String schedule;
    private String summary;
    private String qualification;
    private String size;
    private String link;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post", cascade = CascadeType.REMOVE)
    @Builder.Default
    private Set<TagPostMap> tagPostMaps = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post", cascade = CascadeType.REMOVE)
    @Builder.Default
    private Set<KeywordPostMap> keywordPostMaps = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post", cascade = CascadeType.REMOVE)
    @Builder.Default
    private Set<Member> members = new HashSet<>();
}
