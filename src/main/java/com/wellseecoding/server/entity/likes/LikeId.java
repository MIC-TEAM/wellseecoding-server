package com.wellseecoding.server.entity.likes;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class LikeId implements Serializable {
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "post_id", nullable = false)
    private Long postId;
}
