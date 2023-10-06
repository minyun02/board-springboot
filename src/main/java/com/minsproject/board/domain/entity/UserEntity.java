package com.minsproject.board.domain.entity;

import com.minsproject.board.domain.constant.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL")
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = null;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @PrePersist
    void registeredAt() { this.registeredAt = LocalDateTime.now(); }

    @PreUpdate
    void updatedAt() { this.updatedAt = LocalDateTime.now(); }

    public static UserEntity of(String username, String encodedPassword, String nickname) {
        UserEntity entity = new UserEntity();
        entity.setUsername(username);
        entity.setPassword(encodedPassword);
        entity.setNickname(nickname);
        return entity;
    }
}
