package com.wellseecoding.server.user;

import com.wellseecoding.server.user.education.Education;
import com.wellseecoding.server.user.link.Link;
import com.wellseecoding.server.user.work.Work;
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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String email;
    @Column(name = "refresh_token")
    private String refreshToken;
    private String status;
    @Column(name = "about_me")
    private String aboutMe;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @Builder.Default
    private Set<Education> educations = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @Builder.Default
    private Set<Link> links = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @Builder.Default
    private Set<Work> works = new HashSet<>();
}
