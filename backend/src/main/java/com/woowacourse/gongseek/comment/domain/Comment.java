package com.woowacourse.gongseek.comment.domain;


import com.woowacourse.gongseek.article.domain.Article;
import com.woowacourse.gongseek.member.domain.Member;
import java.time.LocalDateTime;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @CreatedDate
    private LocalDateTime createdAt;

    public Comment(String content, Member member, Article article) {
        this.content = new Content(content);
        this.member = member;
        this.article = article;
    }

    public void updateContent(String content) {
        this.content = new Content(content);
    }

    public boolean isMember(Member member) {
        return this.getMember().equals(member);
    }

    public String getContent() {
        return content.getContent();
    }
}
