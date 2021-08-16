package com.wellseecoding.server.user.sns;

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
public class SnsInfoKey implements Serializable {
    @Column(name = "sns_id", nullable = false)
    private String snsId;
    @Column(name = "sns_type", nullable = false)
    private String snsType;
}
