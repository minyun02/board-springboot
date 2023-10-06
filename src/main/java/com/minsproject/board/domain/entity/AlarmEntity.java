package com.minsproject.board.domain.entity;

import com.minsproject.board.domain.constant.AlarmType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@Table(name = "alarm", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@SQLDelete(sql = "UPDATE alarm SET removed_at = NOW() WHERE id = ?")
@Where(clause = "removed_at is NULL")
@Entity
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Column(name = "from_user_id")
    private Integer fromUserId;

    @Column(name = "target_id")
    private Integer targetId;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @PrePersist
    void registeredAt() { this.registeredAt = LocalDateTime.now(); }

    @PreUpdate
    void checkedAt() { this.checkedAt = LocalDateTime.now(); }

    protected AlarmEntity() {}

    private AlarmEntity(UserEntity user, AlarmType alarmType, Integer fromUserId, Integer targetId) {
        this.user = user;
        this.alarmType = alarmType;
        this.fromUserId = fromUserId;
        this.targetId = targetId;
    }

    public static AlarmEntity of(UserEntity user, AlarmType alarmType, Integer fromUserId, Integer targetId) {
        return new AlarmEntity(user, alarmType, fromUserId, targetId);
    }
}
