package sample.cafekiosk.spring.exception.product;

import sample.cafekiosk.spring.exception.custom.Custom404Exception;
import sample.cafekiosk.spring.exception.ErrorCode_404;

public class ProductNotFound404Exception extends Custom404Exception {
    public ProductNotFound404Exception() {
        super(ErrorCode_404.PRODUCT_NOT_FOUND);
    }
}
