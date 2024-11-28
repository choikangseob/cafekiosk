package sample.cafekiosk.spring.api.service.order.response;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.order.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse {

    private Long id;

    private int totalPrice;

    private LocalDateTime registeredDateTime;

    private List<ProductResponse> products;

    public static OrderResponse of(Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .registeredDateTime(order.getRegisteredDateTime())
                .products(order.getOrderProducts().stream()
                        .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                        .collect(Collectors.toList()))
                .build();
    }


    @Builder
    public OrderResponse(Long id, List<ProductResponse> products, int totalPrice, LocalDateTime registeredDateTime) {
        this.id = id;
        this.products = products;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
    }
}
