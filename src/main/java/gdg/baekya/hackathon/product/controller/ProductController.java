package gdg.baekya.hackathon.product.controller;

import gdg.baekya.hackathon.common.response.ApiResponse;
import gdg.baekya.hackathon.product.controller.request.ProductReqeust;
import gdg.baekya.hackathon.product.service.ProductImageService;
import gdg.baekya.hackathon.product.service.ProductService;
import gdg.baekya.hackathon.product.service.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/funding")
public class ProductController {

    private final ProductService productService;
    private final ProductImageService productImageService;

    // 펀딩 작성하기
    // 상품 추가
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<ProductResponse> create(@Validated ProductReqeust productRequest) {

        return ApiResponse.created(productService.createProduct(productRequest));
    }
}
