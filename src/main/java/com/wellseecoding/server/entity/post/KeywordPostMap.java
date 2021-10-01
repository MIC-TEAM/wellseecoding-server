package com.wellseecoding.server.entity.post;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "keyword_post_map")
public class KeywordPostMap {
    @Id
    @GeneratedValue
    private Long id;
    private String keyword;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;
}
