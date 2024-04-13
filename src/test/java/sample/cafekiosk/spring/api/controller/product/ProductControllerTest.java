package sample.cafekiosk.spring.api.controller.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sample.cafekiosk.spring.ControllerTestSupport;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductUpdateRequest;
import sample.cafekiosk.spring.api.service.product.request.ProductUpdateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.STOP_SELLING;

class ProductControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("신규 상품을 생성")
    void createProduct() throws Exception {
        // given
        MockMultipartFile mockFile = getMockMultipartFile("uploadImage");

        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(ProductType.HANDMADE)
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(4000)
            .build();
        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", requestJson.getBytes());

        // when// then
        mockMvc.perform(
                multipart("/api/products/new")
                    .file("uploadImage", mockFile.getBytes())
                    .file(jsonFile)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
    void createProductWithoutType() throws Exception {
        // given
        MockMultipartFile mockFile = getMockMultipartFile("uploadImage");

        ProductCreateRequest request = ProductCreateRequest.builder()
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(4000)
            .build();

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", requestJson.getBytes());

        // when// then
        mockMvc.perform(
                multipart("/api/products/new")
                    .file("mainImage", mockFile.getBytes())
                    .file(jsonFile)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 판매상태는 필수값이다.")
    void createProductWithoutSellingStatus() throws Exception {
        // given
        MockMultipartFile mockFile = getMockMultipartFile("uploadImage");

        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(ProductType.HANDMADE)
            .name("아메리카노")
            .price(4000)
            .build();

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", requestJson.getBytes());

        // when// then
        mockMvc.perform(
                multipart("/api/products/new")
                    .file("mainImage", mockFile.getBytes())
                    .file(jsonFile)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 이름은 필수값이다.")
    void createProductWithoutName() throws Exception {
        // given
        MockMultipartFile mockFile = getMockMultipartFile("uploadImage");

        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(ProductType.HANDMADE)
            .sellingStatus(SELLING)
            .price(4000)
            .build();

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", requestJson.getBytes());

        // when// then
        mockMvc.perform(
                multipart("/api/products/new")
                    .file("mainImage", mockFile.getBytes())
                    .file(jsonFile)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 가격은 양수여야 합니다.")
    void createProductWithoutPrice() throws Exception {
        // given
        MockMultipartFile mockFile = getMockMultipartFile("uploadImage");

        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(ProductType.HANDMADE)
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(0)
            .build();

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", requestJson.getBytes());

        // when// then
        mockMvc.perform(
                multipart("/api/products/new")
                    .file("mainImage", mockFile.getBytes())
                    .file(jsonFile)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("판매 상품 조회")
    void getSellingProduct() throws Exception {
        // given

        List<ProductResponse> result = List.of();
        when(productService.getSellingProducts()).thenReturn(result);

        // when// then
        mockMvc.perform(
                get("/api/product/selling")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("등록된 상품을 수정한다")
    void updateProduct() throws Exception {
        // given
        MockMultipartFile mockFile = getMockMultipartFile("updateImage");

        ProductUpdateRequest request = ProductUpdateRequest.builder()
            .type(ProductType.HANDMADE)
            .sellingStatus(STOP_SELLING)
            .name("아메리카노")
            .price(1000)
            .build();

        BDDMockito.given(productService.updateProduct(any(Long.class), any(ProductUpdateServiceRequest.class), any(MultipartFile.class)))
            .willReturn(ProductResponse.builder()
                .id(1L)
                .type(request.getType())
                .productNumber("001")
                .sellingStatus(STOP_SELLING)
                .name(request.getName())
                .price(request.getPrice())
                .build());

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", requestJson.getBytes());

        // when // then
        mockMvc.perform(
                multipart("/api/products/{productId}", 1L)
                    .file("updateImage", mockFile.getBytes())
                    .file(jsonFile)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").exists())
            .andDo(print());
    }

    @Test
    @DisplayName("등록된 상품 삭제")
    void deleteProduct() throws Exception {

        // void 를 명시적으로 표현, 반환 값이 없을 때
        BDDMockito.doNothing().when(productService).deleteProduct(1L);

        // when // then
        mockMvc.perform(delete("/api/products/{productId}", 1L))
            .andExpect(status().isOk())
            .andDo(print());

        // 메소드 호출 확인
        verify(productService).deleteProduct(1L);
    }

    private static MockMultipartFile getMockMultipartFile(String name) throws IOException {
        byte[] imageContent = Files.readAllBytes(Paths.get("src/test/resources/test.jpg"));

        return new MockMultipartFile(name, "test.jpg", "image/jpg", imageContent);
    }
}