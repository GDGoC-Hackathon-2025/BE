package gdg.baekya.hackathon.product.service;

import gdg.baekya.hackathon.board.domain.Board;
import gdg.baekya.hackathon.board.domain.BoardRepository;
import gdg.baekya.hackathon.page.request.PageRequest;
import gdg.baekya.hackathon.page.response.PageResponse;
import gdg.baekya.hackathon.product.controller.request.ProductReqeust;
import gdg.baekya.hackathon.product.domain.Product;
import gdg.baekya.hackathon.category.domain.Category;
import gdg.baekya.hackathon.product.domain.ProductImage;
import gdg.baekya.hackathon.product.domain.ProductRepository;
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
    private final BoardRepository boardRepository;

    // 펀딩 만들기
    @Override
    public ProductResponse createProduct(ProductReqeust reqeust) {

        // 작성페이지에서의 카테고리는 id를 바탕으로 민원 조회하는 것이 맞을 듯?

        // 민원 정보 가져오기
        Board board = boardRepository.findById(reqeust.getBoardId())
                .orElseThrow(() -> new NoSuchElementException("해당 민원이 존재하지 않습니다."));

        // 새로운 상품 생성
        Product newOne = Product.of(board, reqeust.getPname(), reqeust.getPdesc(), reqeust.getPrice(), reqeust.getGoalPrice(), reqeust.getCreatedAt(), reqeust.getEndAt());
        Product product = productRepository.save(newOne);
        //
        return ProductResponse.from(product);
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
}
