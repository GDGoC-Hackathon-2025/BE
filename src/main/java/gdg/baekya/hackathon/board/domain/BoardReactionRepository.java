package gdg.baekya.hackathon.board.domain;

import gdg.baekya.hackathon.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReactionRepository extends JpaRepository<BoardReaction, Long> {

    Optional<BoardReaction> findByMemberAndBoard(Member member, Board board);
}
