package gdg.baekya.hackathon.board.domain;

import gdg.baekya.hackathon.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BoardReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private int Like;

    // 생성자
    public static BoardReaction of(Board board, Member member) {
        BoardReaction reaction = BoardReaction.builder()
                .board(board)
                .member(member)
                .build();

        board.addReaction(reaction);
        return reaction;
    }
}
