package gdg.baekya.hackathon.board.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/*
민원 작성하기 Request 객체 정의
* */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WriteBoardRequest {
    private MultipartFile image;
    @NotBlank(message = "누락된 값이 존재합니다.")
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;
    @NotBlank(message = "누락된 값이 존재합니다.")
    private String category;
    @NotBlank(message = "누락된 값이 존재합니다.")
    private String contents;
}
