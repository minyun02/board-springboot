package com.minsproject.board.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Table(name = "hashtag",
        indexes = {
        @Index(columnList = "hashtag_name", unique = true),
        @Index(columnList = "registered_at"),
})
@SQLDelete(sql = "UPDATE hashtag SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL")
@Entity
public class HashtagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany()
    private Set<PostEntity> posts = new LinkedHashSet<>();

    @Setter
    @Column(name = "hashtag_name", nullable = false)
    private String hashtagName;

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

//    protected HashtagEntity() {}
    public HashtagEntity() {}
    private HashtagEntity(String hashtagName) { this.hashtagName = hashtagName; }

    public static HashtagEntity of(String hashtagName) { return new HashtagEntity(hashtagName); }
}
