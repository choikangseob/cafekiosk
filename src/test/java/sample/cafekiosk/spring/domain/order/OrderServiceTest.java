package sample.cafekiosk.spring.domain.order;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
//@DataJpaTest
public class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrderService orderService;

/*    @AfterEach
    public void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }*/

    @Test
    @DisplayName("주문번호 리스트를 받아 주문번호를 생성한다.")
   public void createOrder(){
        // given
        Product product = createProduct(ProductType.HANDMADE,001L,1000);
        Product product2 = createProduct(ProductType.HANDMADE,002L,3000);
        Product product3 = createProduct(ProductType.HANDMADE,003L,5000);

        productRepository.saveAll(List.of(product, product2, product3));


        // when
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of(001L, 002L))
                .build();
        LocalDateTime registeredDateTime = LocalDateTime.now();
       OrderResponse orderResponse = orderService.createOrder(request,registeredDateTime);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime","totalPrice")
                .contains(registeredDateTime,4000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        tuple(001L,1000),
                        tuple(002L,3000)
                );
    }

    @Test
    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    public void createOrderWithStock(){
        // given
        Product product = createProduct(ProductType.BOTTLE,001L,1000);
        Product product2 = createProduct(ProductType.BAKERY,002L,3000);
        Product product3 = createProduct(ProductType.HANDMADE,003L,5000);

        productRepository.saveAll(List.of(product, product2, product3));


        Stock stock1 = Stock.create(001L,2);
        Stock stock2 = Stock.create(001L,2);
        stockRepository.saveAll(List.of(stock1, stock2));

        // when
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of(001L, 001L, 002L, 003L))
                .build();
        LocalDateTime registeredDateTime = LocalDateTime.now();
        OrderResponse orderResponse = orderService.createOrder(request,registeredDateTime);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime","totalPrice")
                .contains(registeredDateTime,10000);
        assertThat(orderResponse.getProducts()).hasSize(4)
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        tuple(001L,1000),
                        tuple(001L,1000),
                        tuple(002L,3000),
                        tuple(003L,5000)
                );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                .extracting("productNumber","quantity")
                .containsExactlyInAnyOrder(
                        tuple(001L,0),
                        tuple(002L,1)
                );
    }

    @Test
    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    public void createOrderWithDuplicateProductNumber(){
        // given
        Product product = createProduct(ProductType.HANDMADE,001L,1000);
        Product product2 = createProduct(ProductType.HANDMADE,002L,3000);
        Product product3 = createProduct(ProductType.HANDMADE,003L,5000);

        productRepository.saveAll(List.of(product, product2, product3));


        // when
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of(001L, 001L))
                .build();
        LocalDateTime registeredDateTime = LocalDateTime.now();
        OrderResponse orderResponse = orderService.createOrder(request,registeredDateTime);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime","totalPrice")
                .contains(registeredDateTime,2000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        tuple(001L,1000),
                        tuple(001L,1000)
                );
    }

    private Product createProduct(ProductType type, Long productNumber, int price){

        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("메뉴 이름")
                .build();
    }
}
