package br.com.uol.compasso.catalog.product.service;

import static br.com.uol.compasso.catalog.product.mapper.ProductMapper.toDTO;
import static br.com.uol.compasso.catalog.product.mapper.ProductMapper.toEntity;

import static java.util.stream.Collectors.toList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uol.compasso.catalog.product.domain.Product;
import br.com.uol.compasso.catalog.product.domain.ProductDTO;
import br.com.uol.compasso.catalog.exception.ResourceNotFoundException;
import br.com.uol.compasso.catalog.product.mapper.ProductMapper;
import br.com.uol.compasso.catalog.product.repository.ProductRepository;
import br.com.uol.compasso.catalog.product.visitor.SpecificationVisitorManager;
import br.com.uol.compasso.catalog.product.visitor.Visitor;
import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final List<Visitor> visitors;

    public ProductService(ProductRepository productRepository,
                          List<Visitor> visitors) {
        this.productRepository = productRepository;
        this.visitors = visitors;
    }

    public ProductDTO findProduct(Long id) {
        log.info("Searching product by id: {}", id);
        Product product = findById(id);

        return toDTO(product);
    }

    public List<ProductDTO> listAllProducts() {

        log.info("Listing all products");

        return productRepository
                .findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(toList());
    }


    public List<ProductDTO> searchProducts(BigDecimal minimalPrice, BigDecimal maximumPrice, String searchText) {

        log.info("Searching products by Minimal Price: {} Maximum Price: {} Search Text: {}", minimalPrice, maximumPrice, searchText);

        SpecificationVisitorManager specificationVisitorManager = buildSpecification(minimalPrice, maximumPrice, searchText);

        return productRepository
                .findAll(specificationVisitorManager.getSpec())
                .stream()
                .map(ProductMapper::toDTO)
                .collect(toList());

    }

    @Transactional
    public ProductDTO insertProduct(ProductDTO productDTO) {

        log.info("Inserting product");

        Product product = toEntity(productDTO);

        product = productRepository.save(product);

        return toDTO(product);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {

        log.info("Updating product");

        Product product = findById(id);

        productDTO.setId(product.getId());

        product = productRepository.save(toEntity(productDTO));

        return toDTO(product);
    }

    @Transactional
    public void deleteProduct(Long id) {

        Product product = findById(id);
        productRepository.delete(product);
    }

    private Product findById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    private SpecificationVisitorManager buildSpecification(BigDecimal minimalPrice, BigDecimal maximumPrice, String searchText) {

        log.debug("Building specification for Minimal Price: {} Maximum Price: {} Search Text: {}", minimalPrice, maximumPrice, searchText);

        SpecificationVisitorManager specificationVisitorManager = new SpecificationVisitorManager(searchText, minimalPrice, maximumPrice);
        visitors.forEach(visitor -> visitor.accept(specificationVisitorManager));

        return specificationVisitorManager;
    }

}
