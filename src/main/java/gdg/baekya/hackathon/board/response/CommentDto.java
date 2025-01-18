package gdg.baekya.hackathon.board.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentDto {
    private String name;
    private String contents;
    private LocalDateTime startDate;
}
