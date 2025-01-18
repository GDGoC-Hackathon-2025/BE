package gdg.baekya.hackathon.product.service;

import gdg.baekya.hackathon.product.controller.request.ProductReqeust;
import gdg.baekya.hackathon.product.domain.ProductRepository;
import gdg.baekya.hackathon.product.service.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // 펀딩 만들기
    @Override
    public ProductResponse createProduct(ProductReqeust reqeust) {
        return null;
    }
}
