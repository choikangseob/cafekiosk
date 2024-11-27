package sample.cafekiosk.spring.api.service.product.response;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
public class ProductResponse {

    private long id;

    private long productNumber;

    private ProductType type;

    private ProductSellingStatus sellingStatus;

    private String name;

    private int price;


    @Builder
    public ProductResponse(long id, String name, ProductSellingStatus sellingStatus, ProductType type, long productNumber, int price) {
        this.id = id;
        this.name = name;
        this.sellingStatus = sellingStatus;
        this.type = type;
        this.productNumber = productNumber;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productNumber(product.getProductNumber())
                .type(product.getType())
                .sellingStatus(product.getSellingStatus())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
