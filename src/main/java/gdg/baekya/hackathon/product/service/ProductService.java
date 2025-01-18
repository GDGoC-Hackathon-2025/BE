package gdg.baekya.hackathon.product.service;

import gdg.baekya.hackathon.page.request.PageRequest;
import gdg.baekya.hackathon.page.response.PageResponse;
import gdg.baekya.hackathon.product.controller.request.AddRequest;
import gdg.baekya.hackathon.product.controller.request.ProductReqeust;
import gdg.baekya.hackathon.product.service.response.ProductResponse;

public interface ProductService {

    // 민원에서 펀딩 만들기
    ProductResponse createProduct(ProductReqeust reqeust);

    // 펀딩 리스트 가져오기
    PageResponse<ProductResponse> findAll(PageRequest pageRequest);

    // 특정 카테고리에 있는 아이템 조회하기
    PageResponse<ProductResponse> findByCategory(String category, PageRequest pageRequest);

    // 펀딩 상세정보 가져오기
    ProductResponse getProduct(Long productId);

    // 펀딩 좋아요 누르기
    ProductResponse addLike(AddRequest addRequest);

    // 좋아요가 많은 순서대로 조회하기
//    PageResponse<ProductResponse> findByLike(String category, PageRequest pageRequest);

}
