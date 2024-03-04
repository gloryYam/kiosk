package sample.cafekiosk.spring.docs.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvcBuilderSupport;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sample.cafekiosk.spring.api.controller.order.OrderController;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.docs.RestDocsSupport;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerDocsTest extends RestDocsSupport {

    private final OrderService orderService = mock(OrderService.class);

    @Override
    protected Object initController() {
        return new OrderController(orderService);
    }

    @Test
    @DisplayName("주문 생성하는 API")
    void OrderCreate() throws Exception {
        // given
        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "002", "003"))
            .build();

        ProductResponse product = ProductResponse.builder()
            .id(1L)
            .productNumber("001")
            .sellingStatus(ProductSellingStatus.SELLING)
            .type(ProductType.HANDMADE)
            .name("아메리카노")
            .price(4000)
            .build();

        BDDMockito.given(orderService.createOrder(any(OrderCreateServiceRequest.class), any(LocalDateTime.class)))
            .willReturn(OrderResponse.builder()
                .id(1L)
                .totalPrice(4000)
                .registeredDateTime(LocalDateTime.of(2024, 3, 4, 10, 0))
                .products(List.of(product))
                .build());

        mockMvc.perform(post("/api/orders/new")
            .content(objectMapper.writeValueAsString(request))
            .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("order-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("productNumbers").type(JsonFieldType.ARRAY)
                        .description("상품 번호 리스트")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),

                    fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                        .description("ID"),
                    fieldWithPath("data.totalPrice").type(JsonFieldType.NUMBER)
                        .description("총 가격"),
                    fieldWithPath("data.registeredDateTime").type(JsonFieldType.ARRAY)
                        .description("주문 시간 "),
                    fieldWithPath("data.products").type(JsonFieldType.ARRAY)
                        .description("상품들"),

                    fieldWithPath("data.products[].id").type(JsonFieldType.NUMBER)
                        .description("ID"),
                    fieldWithPath("data.products[].productNumber").type(JsonFieldType.STRING)
                        .description("상품 번호"),
                    fieldWithPath("data.products[].type").type(JsonFieldType.STRING)
                        .description("상품 타입"),
                    fieldWithPath("data.products[].sellingStatus").type(JsonFieldType.STRING)
                        .description("상품 판매상태"),
                    fieldWithPath("data.products[].name").type(JsonFieldType.STRING)
                        .description("상품 이름"),
                    fieldWithPath("data.products[].price").type(JsonFieldType.NUMBER)
                        .description("상품 가격")
                )
                ));
    }
}
