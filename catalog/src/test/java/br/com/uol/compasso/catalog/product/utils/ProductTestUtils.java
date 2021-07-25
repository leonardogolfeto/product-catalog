package br.com.uol.compasso.catalog.product.utils;

import br.com.uol.compasso.catalog.product.domain.Product;
import br.com.uol.compasso.catalog.product.domain.ProductDTO;
import br.com.uol.compasso.catalog.product.visitor.SpecificationVisitorManager;
import java.math.BigDecimal;
import java.util.Optional;

public class ProductTestUtils {

    public static final String PRODUCT_NAME = "cat";
    public static final String PRODUCT_DESCRIPTION = "amazing";

    public static ProductDTO mockProductDTO() {
        return ProductDTO
                .builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(BigDecimal.TEN)
                .build();
    }

    public static Product mockProductEntity() {
        return Product
                .builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(BigDecimal.TEN)
                .build();
    }

    public static Optional<Product> mockProduct() {
        return Optional.of(Product
                .builder()
                .id(1L)
                .build()
        );
    }

    public static SpecificationVisitorManager mockSpecificationVisitorManager(String searchText, BigDecimal minimalPrice, BigDecimal maximumPrice) {
        return new SpecificationVisitorManager(searchText, minimalPrice, maximumPrice);
    }

}
