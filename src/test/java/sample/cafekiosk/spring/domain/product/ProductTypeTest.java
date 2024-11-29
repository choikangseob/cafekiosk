package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductTypeTest {

    @Test
    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    public void containsStockType(){
        // given
        ProductType givenType = ProductType.HANDMADE;
        // when
        boolean result = ProductType.containsStockType(givenType);
        // then
        assertThat(result).isFalse();

    }

}
