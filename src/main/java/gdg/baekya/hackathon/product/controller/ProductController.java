package gdg.baekya.hackathon.product.controller;

import gdg.baekya.hackathon.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 펀딩 작성하기

}
