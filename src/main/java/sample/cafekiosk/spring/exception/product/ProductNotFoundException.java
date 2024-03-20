package sample.cafekiosk.spring.exception.product;

import sample.cafekiosk.spring.exception.CustomException;
import sample.cafekiosk.spring.exception.ErrorCode;

public class ProductNotFoundException extends CustomException {
    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FIND);
    }
}
