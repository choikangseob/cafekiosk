package sample.cafekiosk.spring.api.service.order;


public class OrderTest {

    @Test
    @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    public void calculateTotalPrice() {
        // given
        List<Product> products = List.of(
                createProduct(001L, 1000),
                createProduct(002L, 2000)
        );

        // when

        Order order = Order.create(products);
        // then

        assertThat(order.getTotalPrice()).isEqualTo(3000);
    }

    @Test
    @DisplayName("주문 생성 시 주문 상태는 INIT 이다.")
    public void init() {
        // given
        List<Product> products = List.of(
                createProduct(001L, 1000),
                createProduct(002L, 2000)
        );

        // when

        Order order = Order.create(products);
        // then

        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }

    @Test
    @DisplayName("주문 생성 시 주문 등록 시간을 기록한다.")
    public void registeredDateTime() {

        LocalDateTime registeredDateTime = LocalDateTime.now();
        // given
        List<Product> products = List.of(
                createProduct(001L, 1000),
                createProduct(002L, 2000)
        );

        // when

        Order order = Order.create(products,registeredDateTime);
        // then

        assertThat(order.getRegisteredDateTime()).isEqualto(registeredDateTime);
    }

    private Product createProduct(Long productNumber, int price) {

        return Product.builder()
                .type(ProductType.HANDMADE)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("메뉴 이름")
                .build();
    }
}
