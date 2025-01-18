package gdg.baekya.hackathon.product.service;

import gdg.baekya.hackathon.board.domain.Board;
import gdg.baekya.hackathon.board.domain.BoardRepository;
import gdg.baekya.hackathon.member.domain.Member;
import gdg.baekya.hackathon.member.domain.MemberRepository;
import gdg.baekya.hackathon.page.request.PageRequest;
import gdg.baekya.hackathon.page.response.PageResponse;
import gdg.baekya.hackathon.product.controller.request.AddRequest;
import gdg.baekya.hackathon.product.controller.request.ProductReqeust;
import gdg.baekya.hackathon.product.domain.*;
import gdg.baekya.hackathon.category.domain.Category;
import gdg.baekya.hackathon.product.service.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductReactionRepository productReactionRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ProductImageService productImageService;
    // 펀딩 만들기
    @Override
    public ProductResponse createProduct(ProductReqeust reqeust) {

        // 민원 정보 가져오기
        Board board = boardRepository.findById(reqeust.getBoardId())
                .orElseThrow(() -> new NoSuchElementException("해당 민원이 존재하지 않습니다."));

        // 중복 체크: 해당 boardId로 이미 생성된 Product가 있는지 확인
        if (productRepository.existsByBoardId(reqeust.getBoardId())) {
            throw new IllegalStateException("해당 민원에 대한 상품이 이미 존재합니다. boardId: " + reqeust.getBoardId());
        }

        // 새로운 상품 생성
        Product newOne = Product.of(board, reqeust.getPname(), reqeust.getPdesc(), reqeust.getPrice(),
                reqeust.getGoalPrice(), reqeust.getCreatedAt(), reqeust.getEndAt());
        Product product = productRepository.save(newOne);

        // 사진 저장하기
        List<String> uploads = productImageService.saveFiles(product, reqeust.getFiles());
        ProductResponse response = ProductResponse.from(product);
        response.setUploadFileNames(uploads);

        return response;
    }


    @Override
    public PageResponse<ProductResponse> findAll(PageRequest pageRequest) {

        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPage()-1, pageRequest.getSize(), Sort.by("id").descending());
        Page<Object[]> result = productRepository.selectList(pageable);

        List<ProductResponse> dtoList = result.get().map(arr -> {
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            String imageStr = (productImage != null) ? productImage.getFileName() : "No image found";
            ProductResponse dto = ProductResponse.from(product);
            dto.setUploadFileNames(Collections.singletonList(imageStr));

            return dto;
        }).toList();

        long total = result.getTotalElements();
        return new PageResponse<>(dtoList, pageRequest, total);
    }

    @Override
    public PageResponse<ProductResponse> findByCategory(String category, PageRequest pageRequest) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPage()-1, pageRequest.getSize(), Sort.by("id").descending());
        Page<Object[]> result = productRepository.findByCategoryWithImages(pageable, Category.valueOf(category));

        List<ProductResponse> dtoList = result.get().map(arr -> {
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            String imageStr = (productImage != null) ? productImage.getFileName() : "No image found";
            ProductResponse dto = ProductResponse.from(product);
            dto.setUploadFileNames(Collections.singletonList(imageStr));

            return dto;
        }).toList();

        long total = result.getTotalElements();
        return new PageResponse<>(dtoList, pageRequest, total);
    }

    @Override
    public ProductResponse getProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("해당 상품이 없습니다"));


        List<ProductImage> productImages = productRepository.selectProductImages(productId);

        ProductResponse productResponse = ProductResponse.from(product);

        productResponse.setUploadFileNames(productImages.stream().map(ProductImage::getFileName).collect(Collectors.toList()));

        return productResponse;

    }
    @Override
    public ProductResponse addLike(AddRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));

        // 중복 여부 확인 (좋아요가 이미 눌려졌다면 취소)
        ProductReaction existingReaction = productReactionRepository.findByMemberAndProduct(member, product);

        if (existingReaction != null) {
            // 좋아요 취소 (이미 좋아요가 눌려있는 경우)
            productReactionRepository.delete(existingReaction);
        } else {
            // 좋아요 추가 (좋아요가 눌려있지 않다면)
            ProductReaction reaction = ProductReaction.of(product, member);
            productReactionRepository.save(reaction);
        }

        // 변경된 상품 정보 반환
        Product result = productRepository.findById(request.getProductId()).orElseThrow();

        return ProductResponse.from(result);
    }
}
