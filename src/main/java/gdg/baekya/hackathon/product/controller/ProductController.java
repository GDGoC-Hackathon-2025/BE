package gdg.baekya.hackathon.product.controller;

import gdg.baekya.hackathon.common.response.ApiResponse;
import gdg.baekya.hackathon.page.request.PageRequest;
import gdg.baekya.hackathon.page.response.PageResponse;
import gdg.baekya.hackathon.product.controller.request.AddRequest;
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

    // 펀딩 전체 조회 및 카테고리별 펀딩 조회
    @GetMapping("/list")
    public ApiResponse<PageResponse<ProductResponse>> getList(
            @RequestParam(value = "category", required = false) String category,
            PageRequest pageRequest) {

        // 카테고리가 제공되지 않으면 전체 조회, 제공되면 해당 카테고리로 조회
        if (category == null || category.isEmpty()) {
            return ApiResponse.ok(productService.findAll(pageRequest));
        } else {
            return ApiResponse.ok(productService.findByCategory(category, pageRequest));
        }
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

    // 펀딩에 좋아요 누르기
    @PostMapping("/reaction")
    public ApiResponse<ProductResponse> addLike(@RequestBody AddRequest addRequest) {

        return ApiResponse.created(productService.addLike(addRequest));
    }

//    // 좋아요 순서 정렬
//    @GetMapping("/like")
//    public ApiResponse<PageResponse<ProductResponse>> likeList(@RequestParam(value = "category", required = true) String category,
//                                                               PageRequest pageRequest) {
//
//        return ApiResponse.created(productService.findByLike(category, pageRequest));
//    }

}
