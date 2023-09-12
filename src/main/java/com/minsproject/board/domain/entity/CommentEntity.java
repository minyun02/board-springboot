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
@ToString(callSuper = true)
@Table(name = "comment",
        indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "registered_at"),
})
@SQLDelete(sql = "UPDATE comment SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL")
@Entity
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @ManyToOne(optional = false)
//    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Setter
    @Column
    private String content;

    @Setter
    @Column
    private Integer parentCommentId;

    @OrderBy("registeredAt ASC")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "parentCommentId")
    private Set<CommentEntity> childComments = new LinkedHashSet<>();

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    protected CommentEntity() {}

    private CommentEntity(PostEntity post, UserEntity user, String content, Integer parentCommentId) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    public static CommentEntity of(PostEntity post, UserEntity user, String content) {
        return new CommentEntity(post, user, content, null);
    }

    public void addChildComment(CommentEntity child) {
        child.setParentCommentId(this.getId());
        this.getChildComments().add(child);
    }

    @PrePersist
    void registeredAt() {
        this.registeredAt = LocalDateTime.now();
    }

}
