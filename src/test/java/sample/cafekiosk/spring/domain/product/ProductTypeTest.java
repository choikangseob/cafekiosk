package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductTypeTest {

    @Test
    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    public void containsStockType(){
        // given
        ProductType givenType = ProductType.HANDMADE;
        ProductType givenType2 = ProductType.BOTTLE;
        ProductType givenType3 = ProductType.BAKERY;
        // when
        boolean result = ProductType.containsStockType(givenType);
        boolean result2 = ProductType.containsStockType(givenType2);
        boolean result3 = ProductType.containsStockType(givenType3);
        // then
        assertThat(result).isFalse();
        assertThat(result2).isTrue();
        assertThat(result3).isTrue();
    }

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @CsvSource({"HANDMADE,false","BOTTLE,true","BAKERY,true"})
    @ParameterizedTest
    public void containsStockType4(ProductType productType,boolean expected){
        //when
        boolean result = ProductType.containsStockType(productType);
        //then
        assertThat(result).isEqualTo(expected);

    }

}
