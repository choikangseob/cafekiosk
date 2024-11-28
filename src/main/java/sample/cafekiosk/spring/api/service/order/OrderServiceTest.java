package sample.cafekiosk.spring.api.service.order;




@ActiveProfiles("test")
@SpringBootTest
//@DataJpaTest
public class OrderServiceTest {

    private ProductRepository productRepository;

    private OrderService orderService;

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
                .contains(LocalDateTime.now(),4000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        tuple(001L,1000),
                        tuple(002L,3000)
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
                .contains(LocalDateTime.now(),2000);
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
