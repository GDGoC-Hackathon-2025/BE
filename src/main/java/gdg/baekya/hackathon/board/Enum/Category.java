package gdg.baekya.hackathon.board.Enum;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

    WORK("일자리"),
    FACI("시설");

    private final String category;

    @JsonValue
    public String getCategory() {
        return category;
    }
}
