package gdg.baekya.hackathon.product.service;

import gdg.baekya.hackathon.page.request.PageRequest;
import gdg.baekya.hackathon.page.response.PageResponse;
import gdg.baekya.hackathon.product.controller.request.ProductReqeust;
import gdg.baekya.hackathon.product.service.response.ProductResponse;

public interface ProductService {

    // 민원에서 펀딩 만들기
    ProductResponse createProduct(ProductReqeust reqeust);

    // 펀딩 리스트 가져오기
    PageResponse<ProductResponse> findAll(ProductReqeust reqeust, PageRequest pageRequest);

    // 펀딩 상세정보 가져오기
    ProductResponse getProduct(Long productId);
}
