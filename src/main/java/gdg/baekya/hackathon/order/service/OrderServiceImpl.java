package gdg.baekya.hackathon.order.service;

import gdg.baekya.hackathon.order.controller.request.OrderRequest;
import gdg.baekya.hackathon.order.service.response.TossOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    @Override
    public TossOrderResponse order(OrderRequest orderRequest) {
        return null;
    }
}
