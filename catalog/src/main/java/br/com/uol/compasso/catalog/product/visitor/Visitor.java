package br.com.uol.compasso.catalog.product.visitor;

import org.springframework.data.jpa.domain.Specification;

import br.com.uol.compasso.catalog.product.domain.Product;

public interface Visitor {

    Specification<Product> accept(SpecificationVisitorManager visitor);
}
