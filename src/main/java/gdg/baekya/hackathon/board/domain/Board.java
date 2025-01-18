package gdg.baekya.hackathon.board.domain;


import gdg.baekya.hackathon.board.Enum.Category;
import gdg.baekya.hackathon.comment.domain.Comment;
import gdg.baekya.hackathon.member.domain.Member;
import gdg.baekya.hackathon.category.domain.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "board_id")
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
    private Member member;

    @ColumnDefault("0")
    @Column(name = "view_count",nullable = false)
    private int viewCount;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private Category category;

    @OneToMany(mappedBy = "board")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    @Builder.Default
    private List<BoardReaction> reactions = new ArrayList<>();

    // 카테고리
    @Enumerated(EnumType.STRING)
    private Category category;

    // 생성자
    public static Board of(String title, String content, Member member) {

        return Board.builder()
                .title(title)
                .content(content)
                .member(member)
                .createdDate(LocalDateTime.now())
                .build();
    }

    // 양방향
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void addReaction(BoardReaction reaction) {
        reactions.add(reaction);
    }

}