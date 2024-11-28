package sample.cafekiosk.spring.api.controller.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class OrderCreateRequest {


    private List<Long> productNumbers;


    @Builder
    private OrderCreateRequest(List<Long> productNumbers) {
        this.productNumbers = productNumbers;
    }
}
