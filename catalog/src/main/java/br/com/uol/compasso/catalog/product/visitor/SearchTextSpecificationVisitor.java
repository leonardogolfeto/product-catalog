package br.com.uol.compasso.catalog.product.visitor;

import static org.springframework.data.jpa.domain.Specification.where;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


import br.com.uol.compasso.catalog.product.domain.Product;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SearchTextSpecificationVisitor implements Visitor {

    @Override
    public Specification<Product> accept(SpecificationVisitorManager manager) {
        return manager.addSearchTextClause(this);
    }

    @Nullable
    Specification<Product> apply(@Nullable String searchText, @Nullable Specification<Product> spec) {

        if(searchText != null) spec = compose(spec, searchText);

        return spec;
    }

    private static Specification<Product> compose(@Nullable Specification<Product> spec, String searchText ) {

        log.debug("Composing specification by Search Text {}", searchText);

        Specification<Product> nameSpecification = name(searchText);
        Specification<Product> descriptionSpecification = description(searchText);

        if(spec == null) {
            return where(nameSpecification.or(descriptionSpecification));
        }
        return spec.and(nameSpecification.or(descriptionSpecification));

    }

    private static Specification<Product> name(String searchText) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + searchText + "%");
    }

    private static Specification<Product> description(String searchText) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + searchText + "%");
    }

}
