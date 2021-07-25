package br.com.uol.compasso.catalog.product.visitor;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import br.com.uol.compasso.catalog.product.domain.Product;
import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class SpecificationVisitorManager {

    private final String searchText;
    private final BigDecimal minimalPrice;
    private final BigDecimal maximumPrice;

    @Getter
    private Specification<Product> spec;

    @Nullable
    Specification<Product> addSearchTextClause(SearchTextSpecificationVisitor visitor) {
        this.spec = visitor.apply(searchText, this.spec);
        return this.spec;
    }

    @Nullable
    Specification<Product> addMinPriceClause(MinPriceSpecificationVisitor visitor) {
        this.spec = visitor.apply(minimalPrice, this.spec);
        return this.spec;
    }

    @Nullable
    Specification<Product> addMaxPriceClause(MaxPriceSpecificationVisitor visitor) {
        this.spec = visitor.apply(maximumPrice, this.spec);
        return this.spec;
    }

}
