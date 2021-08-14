package com.wellseecoding.server.user.sns;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@EqualsAndHashCode
@Embeddable
public class SnsInfoKey implements Serializable {
    @Column(name = "sns_id", nullable = false)
    private String snsId;
    @Column(name = "sns_type", nullable = false)
    private String snsType;
}
