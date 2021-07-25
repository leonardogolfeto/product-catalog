package br.com.uol.compasso.catalog.product.visitor;

import static org.springframework.data.jpa.domain.Specification.where;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import br.com.uol.compasso.catalog.product.domain.Product;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MaxPriceSpecificationVisitor implements Visitor {

    @Override
    public Specification<Product> accept(SpecificationVisitorManager manager) {
        return manager.addMaxPriceClause(this);
    }

    @Nullable
    Specification<Product> apply(@Nullable BigDecimal maxPrice, @Nullable Specification<Product> spec) {

        if(maxPrice != null) spec = compose(spec, maxPrice);

        return spec;
    }

    static Specification<Product> maxPrice(BigDecimal maxPrice) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    private static Specification<Product> compose(@Nullable Specification<Product> spec, BigDecimal maxPrice) {

        log.debug("Composing specification by Maximum Price {}", maxPrice);

        Specification<Product> maxPriceSpecification = maxPrice(maxPrice);

        if(spec == null) {
            return where(maxPriceSpecification);
        }
        return spec.and(maxPriceSpecification);
    }
}
