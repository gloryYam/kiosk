package sample.cafekiosk.spring.api.controller.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductUpdateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 저장 POST
    @PostMapping("/api/products/new")
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        return ApiResponse.ok(productService.createProduct(request.toServiceRequest()));
    }

    /**
     * 판매할 수 있는 상품 조회
     */
    @GetMapping("/api/selling")
    public ApiResponse<List<ProductResponse>> getSellingProducts() {
        return ApiResponse.ok(productService.getSellingProducts());
    }

    // 상품 수정
    @PostMapping("/api/products/{productId}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable(name = "productId") Long id,
                                                      @Valid @RequestBody ProductUpdateRequest request) {
        return ApiResponse.ok(productService.updateProduct(id, request.toServiceRequest()));
    }

    @DeleteMapping("/api/products/{productId}")
    public void deleteProduct(@PathVariable(name = "productId") Long productId) {
        productService.deleteProduct(productId);
    }
}
