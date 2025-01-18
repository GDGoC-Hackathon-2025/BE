package gdg.baekya.hackathon.board.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardDetailResponseDto {
    private Long id;
    private String title;
    private LocalDateTime startDate;
    private String category;
    private Long likes;
    private String contents;
    private List<CommentDto> comments;
}
