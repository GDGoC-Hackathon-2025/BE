package gdg.baekya.hackathon.board.service;

import gdg.baekya.hackathon.board.domain.Board;
import gdg.baekya.hackathon.board.domain.BoardImage;
import gdg.baekya.hackathon.board.domain.BoardImageRepository;
import gdg.baekya.hackathon.board.domain.BoardReaction;
import gdg.baekya.hackathon.board.domain.BoardReactionRepository;
import gdg.baekya.hackathon.board.domain.BoardRepository;
import gdg.baekya.hackathon.board.response.BoardDetailResponseDto;
import gdg.baekya.hackathon.board.response.BoardResponseDto;
import gdg.baekya.hackathon.board.request.WriteBoardRequest;
import gdg.baekya.hackathon.board.response.CommentDto;
import gdg.baekya.hackathon.member.domain.Member;
import gdg.baekya.hackathon.member.domain.MemberRepository;
import gdg.baekya.hackathon.member.domain.PrincipalDetails;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final MemberRepository memberRepository;
    private final BoardReactionRepository boardReactionRepository;

    @Transactional
    public Object createBoard(PrincipalDetails principalDetails, WriteBoardRequest writeBoardRequest) {
        Long userId = principalDetails.getId();
        MultipartFile image = writeBoardRequest.getImage();
        Member member = memberRepository.findById(userId).orElseThrow(NullPointerException::new);
        Board board = Board.of(
                writeBoardRequest.getTitle(),
                writeBoardRequest.getContents(),
                member,
                writeBoardRequest.getEndDate(),
                writeBoardRequest.getCategory()
        );
        boardRepository.save(board);
        if(image != null){
            String fileName = saveImage(image);
            BoardImage boardImage = BoardImage.of(
                    board,
                    fileName
            );
            boardImageRepository.save(boardImage);
        }
        return null;
    }

    private String saveImage(MultipartFile image) {
        String uploadDir = "C:\\Users\\김영록\\IdeaProjects\\BE\\src\\main\\resources\\static\\images";
        try {
            // 원본 파일명에서 확장자 추출
            String originalFilename = image.getOriginalFilename(); // ex) "sample.png"
            String ext = "";
            if (originalFilename.contains(".")) {
                ext = originalFilename.substring(originalFilename.lastIndexOf('.')); // ".png"
            }
            // 중복되지 않는 고유 이름(예: UUID) 생성
            String uniqueName = UUID.randomUUID().toString().replaceAll("-", "") + ext;
            // 예) "374e8e585eb3458380b50b41cfb743bf.png"
            // 저장할 실제 파일 경로
            File destinationFile = new File(uploadDir, uniqueName);

            // 상위 디렉토리가 없으면 생성
            if (!destinationFile.getParentFile().exists()) {
                destinationFile.getParentFile().mkdirs();
            }

            // MultipartFile -> 실제 파일로 저장
            image.transferTo(destinationFile);

            // DB에 저장할 이미지 경로나 URL
            // 예: /images/uniqueName 형태로 저장해서 웹에서 접근 가능
            String storePath = "/images/" + uniqueName;

            // 만약 BoardImage Entity가 있다면 생성 후 저장
            // 예: BoardImage boardImage = new BoardImage(board, storePath);
            // boardImageRepository.save(boardImage);

            // 또는 Board 엔티티에 리스트 필드가 있다면 추가
            // board.addImagePath(storePath);
            return uniqueName;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 중 오류 발생", e);
        }
    }


    public List<BoardResponseDto> showFavoriteBoard() {
        List<Board> boards = boardRepository.findTop3ByOrderByViewCountDesc(); // 조회수 기준 상위 3개
        return boards.stream()
                .map(BoardResponseDto::fromEntity) // DTO로 변환
                .collect(Collectors.toList());
    }

    public BoardDetailResponseDto showBoardDetail(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시판을 찾을 수 없습니다. ID: " + boardId));

        List<CommentDto> commentDtos = board.getComments().stream()
                .map(comment -> new CommentDto(
                        comment.getMember().getUsername(), // 작성자 이름
                        comment.getContent(),             // 댓글 내용
                        comment.getCreatedAt()            // 작성 날짜
                ))
                .collect(Collectors.toList());

        long likeCount = board.getReactions().stream()
                .filter(BoardReaction::isLikes)
                .count();

        return new BoardDetailResponseDto(
                board.getId(),
                board.getTitle(),
                board.getCreatedDate(), // 카테고리 문자열 변환
                board.getCategory().getCategory(),
                likeCount,
                board.getContent(),
                commentDtos
        );
    }

    public List<BoardResponseDto> showBoardList(Long page) {
        int pageSize = 6; // 페이지당 표시할 데이터 수
        Pageable pageable = PageRequest.of(page.intValue() - 1, pageSize, Sort.by("createdDate").descending());

        // 데이터베이스에서 페이지네이션된 Board 목록 가져오기
        Page<Board> boardPage = boardRepository.findAll(pageable);

        // Board 엔티티를 DTO로 변환하여 리스트로 반환
        return boardPage.stream()
                .map(BoardResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public Object makeLike(PrincipalDetails principalDetails, Long boardId) {
        Long userId = principalDetails.getId();
        // Member와 Board를 데이터베이스에서 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid memberId: " + userId));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid boardId: " + boardId));

        // BoardReaction 조회
        Optional<BoardReaction> reactionOpt = boardReactionRepository.findByMemberAndBoard(member, board);

        if (reactionOpt.isPresent()) {
            // 이미 존재하면 likes 값을 반전
            BoardReaction reaction = reactionOpt.get();
            reaction.setLikes(!reaction.isLikes());
            boardReactionRepository.save(reaction);
        } else {
            // 존재하지 않으면 새로 생성
            BoardReaction newReaction = BoardReaction.builder()
                    .member(member)
                    .board(board)
                    .Likes(true) // 기본값 true로 설정
                    .build();
            boardReactionRepository.save(newReaction);
        }
        return null;
    }
}
