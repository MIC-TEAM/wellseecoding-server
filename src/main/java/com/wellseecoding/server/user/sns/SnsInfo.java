package com.wellseecoding.server.user.sns;

import com.wellseecoding.server.user.User;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "sns_info")
public class SnsInfo {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @EmbeddedId
    private SnsInfoKey key;
}
