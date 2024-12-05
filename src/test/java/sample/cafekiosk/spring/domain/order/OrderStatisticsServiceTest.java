package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import sample.cafekiosk.spring.api.service.order.OrderStatisticsService;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class OrderStatisticsServiceTest extends IntegrationTestSupport {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;



    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다")
    public void sendOrderStatisticsMail (){
        // given
        LocalDateTime now = LocalDateTime.of(2024, 12, 3, 0, 0);

        Product product = createProduct(ProductType.HANDMADE,"001",1000);
        Product product2 = createProduct(ProductType.HANDMADE,"002",2000);
        Product product3 = createProduct(ProductType.HANDMADE,"003",3000);


        List<Product> products = List.of(product, product2, product3);

        productRepository.saveAll(products);


        Order order1 = createPaymentCompletedOrder(products, LocalDateTime.of(2024,12,3,23,59,59));
        Order order2 = createPaymentCompletedOrder(products, now);
        Order order3 = createPaymentCompletedOrder(products, LocalDateTime.of(2024,12,4,23,59,59));
        Order order4 = createPaymentCompletedOrder(products, LocalDateTime.of(2024,12,5,0,0));

        //stubbing
        when(mailSendClient.sendEmail(any(String.class),any(String.class),any(String.class),any(String.class)))
                .thenReturn(true);

        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2024, 12, 3), "test@test.com");
        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 6000원 입니다");
    }

    private Order createPaymentCompletedOrder(List<Product> products, LocalDateTime now) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(now)
                .build();
        return orderRepository.save(order);

    }

    private Product createProduct(ProductType type, String productNumber, int price){

        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("메뉴 이름")
                .build();
    }

}
