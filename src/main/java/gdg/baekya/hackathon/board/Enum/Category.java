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

    public static Category from(String text) {
        for (Category c : Category.values()) {
            if (c.category.equals(text)) {
                return c;
            }
        }
        // 못 찾으면 예외 발생 (적절한 예외를 던지거나, null 처리)
        throw new IllegalArgumentException("Unknown category: " + text);
    }
}
