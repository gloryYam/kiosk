package sample.cafekiosk.learning;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GuavaLearningTest {

    @Test
    @DisplayName("주어진 갯수만큼 List 를 파티셔닝한다.")
    void partitionLearningTest1() {
        // given
        List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

        // when
        List<List<Integer>> partition = Lists.partition(integers, 3);

        // then 
        assertThat(partition).hasSize(2)
            .isEqualTo(List.of(
                List.of(1, 2, 3),
                List.of(4, 5, 6)
            ));
    }

    @Test
    @DisplayName("멀티맵 기능 확인")
    void multiMapLearningTest1() {
        // given
        Multimap<String, String> multimap = ArrayListMultimap.create();

        multimap.put("커피", "아메리카노");
        multimap.put("커피", "카페라떼");
        multimap.put("커피", "아이스 바나나 라떼");
        multimap.put("베이커리", "소금빵");
        multimap.put("베이커리", "크루아상");
        // when

        Collection<String> strings = multimap.get("커피");

        // then
        assertThat(strings).hasSize(3)
            .isEqualTo(List.of("아메리카노", "카페라떼", "아이스 바나나 라떼"));
    }


    @DisplayName("멀티맵 기능 확인")
    @TestFactory
    Collection<DynamicTest> multiMapLearningTest() {
        // given
        Multimap<String, String> multimap = ArrayListMultimap.create();

        multimap.put("커피", "아메리카노");
        multimap.put("커피", "카페라떼");
        multimap.put("커피", "아이스 바나나 라떼");
        multimap.put("베이커리", "소금빵");
        multimap.put("베이커리", "크루아상");

        return List.of(
            DynamicTest.dynamicTest("1개 value 삭제", () -> {

                // when
                multimap.remove("커피", "아이스 바나나 라떼");

                // then
                Collection<String> results = multimap.get("커피");
                assertThat(results).hasSize(2)
                    .isEqualTo(List.of("아메리카노", "카페라떼"));
            }),
            DynamicTest.dynamicTest("1개 key 삭제 ", () -> {

                // when
                multimap.removeAll("커피");

                // then
                Collection<String> results = multimap.get("커피");
                assertThat(results).isEmpty();
            })
        );
    }
}
