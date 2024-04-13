package sample.cafekiosk.spring.api.service.product.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
public class ProductUpdateServiceRequest {

    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;
    private MultipartFile updateImageFile;

    @Builder
    public ProductUpdateServiceRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(String productNumber) {
        return Product.builder()
            .productNumber(productNumber)
            .type(type)
            .sellingStatus(sellingStatus)
            .name(name)
            .price(price)
            .build();
    }
}
