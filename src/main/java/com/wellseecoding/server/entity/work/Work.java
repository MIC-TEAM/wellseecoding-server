package com.wellseecoding.server.entity.work;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "work")
public class Work {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private String role;
    private String technology;
    private Long years;
}
