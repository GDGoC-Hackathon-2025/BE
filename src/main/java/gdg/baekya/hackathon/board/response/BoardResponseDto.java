package gdg.baekya.hackathon.board.response;

import gdg.baekya.hackathon.board.domain.Board;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String title;
    private String category;
    private String contents;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static BoardResponseDto fromEntity(Board board) {
        return new BoardResponseDto(
                board.getId(),
                board.getTitle(),
                board.getCategory().toString(), // 카테고리 문자열로 변환
                board.getContent(),
                board.getCreatedDate(),
                board.getEndDate()
        );
    }
}
