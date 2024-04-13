package sample.cafekiosk.spring.api.controller.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductUpdateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

import java.io.IOException;
import java.util.List;

@RestController
@EnableWebSecurity
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 저장 POST
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/products/new")
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestPart(name = "request") ProductCreateRequest request,
                                                             @RequestPart(name = "uploadImage") MultipartFile uploadImage) throws IOException {
        return ApiResponse.ok(productService.createProduct(request.toServiceRequest(), uploadImage));
    }

    /**
     * 판매할 수 있는 상품 조회
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/api/product/selling")
    public ApiResponse<List<ProductResponse>> getSellingProducts() {
        return ApiResponse.ok(productService.getSellingProducts());
    }

    // 상품 수정
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/products/{productId}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable(name = "productId") Long id,
                                                      @Valid @RequestPart(name = "request") ProductUpdateRequest request,
                                                      @RequestPart(name = "updateImage") MultipartFile updateImage) throws IOException {
        return ApiResponse.ok(productService.updateProduct(id, request.toServiceRequest(), updateImage));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/api/products/{productId}")
    public void deleteProduct(@PathVariable(name = "productId") Long productId) {
        productService.deleteProduct(productId);
    }
}
