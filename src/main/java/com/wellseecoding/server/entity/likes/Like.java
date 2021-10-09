package com.wellseecoding.server.entity.likes;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "likes")
public class Like {
    @EmbeddedId
    private LikeId likeId;
}
