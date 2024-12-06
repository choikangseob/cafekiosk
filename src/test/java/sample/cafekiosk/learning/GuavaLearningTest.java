package sample.cafekiosk.learning;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GuavaLearningTest {


    @Test
    @DisplayName("주어진 개수만큼 리스트를 파티셔닝한다")
    public void partitionLearningTest (){
        // given
        List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);
        // when
        List<List<Integer>> partition = Lists.partition(integers, 3);
        // then
        assertThat(partition).hasSize(2)
                .isEqualTo(List.of(
                        List.of(1,2,3),List.of(4,5,6)
                ));
    }
}
