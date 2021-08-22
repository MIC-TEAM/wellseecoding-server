package com.wellseecoding.server.user.sns;

import com.wellseecoding.server.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sns_info")
public class SnsInfo {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @EmbeddedId
    private SnsInfoKey key;
}
