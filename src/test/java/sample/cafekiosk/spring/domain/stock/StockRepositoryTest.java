package sample.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
public class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Test
    @DisplayName("상품번호 리스트로 재고를 조회한다")
    public void findAllByProductNumberIn (){
        // given
        Stock stock1 = Stock.create(001L, 1);
        Stock stock2 = Stock.create(002L, 2);
        Stock stock3 = Stock.create(003L, 3);

        stockRepository.saveAll(List.of(stock1, stock2, stock3));

        // when
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(List.of(001L,002L));
        // then
        assertThat(stocks).hasSize(2)
        .extracting("productNumber","quantity")
                .containsExactlyInAnyOrder(
                        tuple(001L,1),
                        tuple(002L,2)
                );


    }
}
