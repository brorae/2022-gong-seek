package com.woowacourse.gongseek.comment.application;

import com.woowacourse.gongseek.article.domain.Article;
import com.woowacourse.gongseek.article.domain.repository.ArticleRepository;
import com.woowacourse.gongseek.auth.presentation.dto.LoginMember;
import com.woowacourse.gongseek.comment.domain.Comment;
import com.woowacourse.gongseek.comment.domain.repository.CommentRepository;
import com.woowacourse.gongseek.comment.presentation.dto.CommentRequest;
import com.woowacourse.gongseek.comment.presentation.dto.CommentResponse;
import com.woowacourse.gongseek.member.domain.Member;
import com.woowacourse.gongseek.member.domain.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public void save(LoginMember loginMember, Long articleId, CommentRequest commentRequest) {
        Member member = findMember(loginMember);
        Article article = findArticle(articleId);
        Comment comment = new Comment(commentRequest.getContent(), member, article);

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findByArticleId(LoginMember loginMember, Long articleId) {
        Member member = findMember(loginMember);
        return commentRepository.findAllByArticleId(articleId).stream()
                .map(comment -> CommentResponse.of(comment, comment.isAuthor(member)))
                .collect(Collectors.toList());
    }

    public void update(LoginMember loginMember, Long commentId, CommentRequest updateRequest) {
        Member member = findMember(loginMember);
        Comment comment = findComment(commentId);

        if (!comment.isAuthor(member)) {
            throw new IllegalArgumentException("댓글을 작성한 회원만 수정할 수 있습니다.");
        }
        comment.updateContent(updateRequest.getContent());
    }

    public void delete(LoginMember loginMember, Long commentId) {
        Member member = findMember(loginMember);
        Comment comment = findComment(commentId);

        if (!comment.isAuthor(member)) {
            throw new IllegalArgumentException("댓글을 작성한 회원만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }

    private Member findMember(LoginMember loginMember) {
        return memberRepository.findById(loginMember.getPayload())
                .orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));
    }

    private Article findArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalStateException("게시글이 존재하지 않습니다."));
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("댓글이 존재하지 않습니다."));
    }
}

