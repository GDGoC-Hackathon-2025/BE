package gdg.baekya.hackathon.product.service;

import gdg.baekya.hackathon.product.controller.request.ProductReqeust;
import gdg.baekya.hackathon.product.service.response.ProductResponse;

public interface ProductService {

    // 민원에서 펀딩 만들기
    ProductResponse createProduct(ProductReqeust reqeust);

}
