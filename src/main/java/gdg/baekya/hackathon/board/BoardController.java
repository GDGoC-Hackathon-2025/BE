package gdg.baekya.hackathon.board;

import gdg.baekya.hackathon.board.domain.Board;
import gdg.baekya.hackathon.board.request.BoardResponseDto;
import gdg.baekya.hackathon.board.request.WriteBoardRequest;
import gdg.baekya.hackathon.board.service.BoardService;
import gdg.baekya.hackathon.common.response.ApiResponse;
import gdg.baekya.hackathon.member.domain.PrincipalDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/complain")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @PostMapping("")
    public ApiResponse<Object> createBoard(@AuthenticationPrincipal PrincipalDetails principalDetails, @Validated @ModelAttribute WriteBoardRequest writeBoardRequest){
        return ApiResponse.ok(boardService.createBoard(principalDetails, writeBoardRequest));
    }

    @GetMapping("/favorite")
    public ApiResponse<List<BoardResponseDto>> showFavoriteBoard(){
        return ApiResponse.ok(boardService.showFavoriteBoard());
    }
}
