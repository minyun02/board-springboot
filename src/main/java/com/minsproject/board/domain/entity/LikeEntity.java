package com.minsproject.board.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@Table(name ="likes",
        indexes = {
        @Index(columnList = "registered_at")
})
@SQLDelete(sql = "UPDATE likes SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL")
@Entity
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = LocalDateTime.now();
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    protected LikeEntity() {}

    private LikeEntity(UserEntity user, PostEntity post) {
        this.user = user;
        this.post = post;
    }

    public static LikeEntity of(UserEntity user, PostEntity post) {
        return new LikeEntity(user, post);
    }
}
