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
public class MinPriceSpecificationVisitor implements Visitor{

    @Override
    public Specification<Product> accept(SpecificationVisitorManager manager) {
        return manager.addMinPriceClause(this);
    }

    @Nullable
    Specification<Product> apply(@Nullable BigDecimal minPrice, @Nullable Specification<Product> spec) {

        if(minPrice != null) spec = compose(spec, minPrice);

        return spec;
    }

    private static Specification<Product> compose(@Nullable Specification<Product> spec, BigDecimal minPrice) {

        log.debug("Composing specification by Minimal Price {}", minPrice);

        Specification<Product> minPriceSpecification = minPrice(minPrice);

        if (spec == null) {
            return where(minPriceSpecification);
        }
        return spec.and(minPriceSpecification);
    }

    private static Specification<Product> minPrice(BigDecimal minPrice) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }
}
