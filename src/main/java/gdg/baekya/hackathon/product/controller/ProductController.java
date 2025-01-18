package gdg.baekya.hackathon.product.controller;

import gdg.baekya.hackathon.common.response.ApiResponse;
import gdg.baekya.hackathon.page.request.PageRequest;
import gdg.baekya.hackathon.page.response.PageResponse;
import gdg.baekya.hackathon.product.controller.request.ProductReqeust;
import gdg.baekya.hackathon.product.service.ProductImageService;
import gdg.baekya.hackathon.product.service.ProductService;
import gdg.baekya.hackathon.product.service.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/funding")
public class ProductController {

    private final ProductService productService;
    private final ProductImageService productImageService;

    // 펀딩 작성하기
    // 펀딩 추가
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<ProductResponse> create(@Validated ProductReqeust productRequest) {

        return ApiResponse.created(productService.createProduct(productRequest));
    }


    // 펀딩 상세 조회
    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> findById(@PathVariable Long id) {
        return ApiResponse.ok(productService.getProduct(id));
    }

    // 펀딩 사진 보기
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        return productImageService.getFile(fileName);
    }

    // 펀딩 전체 조회
    @GetMapping("/list")
    public ApiResponse<PageResponse<ProductResponse>> getList(PageRequest pageRequest) {
        return ApiResponse.ok(productService.findAll(pageRequest));
    }

}
