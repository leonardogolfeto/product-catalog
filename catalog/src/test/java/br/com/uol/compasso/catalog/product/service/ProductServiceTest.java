package br.com.uol.compasso.catalog.product.service;

import static br.com.uol.compasso.catalog.product.utils.ProductTestUtils.PRODUCT_DESCRIPTION;
import static br.com.uol.compasso.catalog.product.utils.ProductTestUtils.PRODUCT_NAME;
import static br.com.uol.compasso.catalog.product.utils.ProductTestUtils.mockProduct;
import static br.com.uol.compasso.catalog.product.utils.ProductTestUtils.mockProductDTO;
import static br.com.uol.compasso.catalog.product.utils.ProductTestUtils.mockProductEntity;
import static br.com.uol.compasso.catalog.product.utils.ProductTestUtils.mockSpecificationVisitorManager;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import static java.util.Collections.singletonList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import br.com.uol.compasso.catalog.exception.ResourceNotFoundException;
import br.com.uol.compasso.catalog.product.domain.Product;
import br.com.uol.compasso.catalog.product.domain.ProductDTO;
import br.com.uol.compasso.catalog.product.repository.ProductRepository;
import br.com.uol.compasso.catalog.product.visitor.SpecificationVisitorManager;
import br.com.uol.compasso.catalog.product.visitor.Visitor;
import java.math.BigDecimal;
import java.util.Optional;

class ProductServiceTest {

    private ProductService subject;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Visitor visitor;

    @BeforeEach
    void setUp(){

        openMocks(this);

        subject = new ProductService(productRepository, singletonList(visitor));
    }

    @Test
    void shouldFindProductByIdExpectingSuccess(){

        when(productRepository.findById(10L)).thenReturn(mockProduct());

        subject.findProduct(10L);

        verify(productRepository).findById(10L);

    }

    @Test
    void shouldFindProductByIdExpectingResourceNotFound(){

        assertThrows(ResourceNotFoundException.class, () -> subject.findProduct(10L));

        verify(productRepository).findById(10L);

    }

    @Test
    void shouldListAllProductsExpectingSuccess(){

        subject.listAllProducts();

        verify(productRepository).findAll();

    }

    @Test
    void shouldSearchProductsByNameOrDescriptionExpectingSuccess(){

        subject.searchProducts(null, null, PRODUCT_NAME);

        verify(visitor).accept(mockSpecificationVisitorManager(PRODUCT_NAME, null, null));

    }

    @Test
    void shouldSearchProductsByMinimalPriceExpectingSuccess(){

        subject.searchProducts(BigDecimal.TEN, null, null);

        verify(visitor).accept(mockSpecificationVisitorManager(null, BigDecimal.TEN, null));

    }

    @Test
    void shouldSearchProductsByMaximumPriceExpectingSuccess(){

        subject.searchProducts(null, BigDecimal.ONE, null);

        verify(visitor).accept(mockSpecificationVisitorManager(null, null, BigDecimal.ONE));
    }

    @Test
    void shouldSearchProductsByAllParametersExpectingSuccess(){

        subject.searchProducts(BigDecimal.ONE, BigDecimal.TEN, PRODUCT_NAME);

        verify(visitor).accept(mockSpecificationVisitorManager(PRODUCT_NAME, BigDecimal.ONE, BigDecimal.TEN));

    }

    @Test
    void shouldInsertProductExpectingSuccess(){

        Product entity = mockProductEntity();
        ProductDTO dto = mockProductDTO();

        when(productRepository.save(entity)).thenReturn(entity);

        subject.insertProduct(dto);

        verify(productRepository).save(entity);

    }

    @Test
    void shouldUpdateProductExpectingSuccess(){

        Product entity = mockProductEntity();
        setField(entity, "id", 1L);

        ProductDTO dto = mockProductDTO();
        setField(entity, "id", 1L);

        when(productRepository.findById(1L)).thenReturn(mockProduct());
        when(productRepository.save(entity)).thenReturn(entity);

        subject.updateProduct(1L, dto);

        verify(productRepository).save(entity);

    }

    @Test
    void shouldUpdateProductExpectingResourceNotFound(){

        ProductDTO dto = mockProductDTO();

        assertThrows(ResourceNotFoundException.class, () -> subject.updateProduct(1L, dto));

    }

    @Test
    void shouldDeleteProductExpectingSuccess(){

        when(productRepository.findById(1L)).thenReturn(mockProduct());

        subject.deleteProduct(1L);

        verify(productRepository).delete(Product.builder().id(1L).build());
    }

    @Test
    void shouldDeleteProductExpectingResourceNotFound(){

        assertThrows(ResourceNotFoundException.class, () -> subject.deleteProduct(1L));

    }

}