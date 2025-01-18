package gdg.baekya.hackathon.comment.domain;


import gdg.baekya.hackathon.board.domain.Board;
import gdg.baekya.hackathon.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 댓글 내용
    private String content;

    // 생성된 날짜
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 생성자
    public static Comment of(Board board, Member member, String content) {

        Comment comment = Comment.builder()
                .board(board)
                .member(member)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        board.addComment(comment);

        return comment;
    }

}
