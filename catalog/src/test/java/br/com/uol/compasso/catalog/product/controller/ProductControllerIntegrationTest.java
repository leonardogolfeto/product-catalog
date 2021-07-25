package br.com.uol.compasso.catalog.product.controller;

import static br.com.uol.compasso.catalog.product.utils.ProductTestUtils.PRODUCT_DESCRIPTION;
import static br.com.uol.compasso.catalog.product.utils.ProductTestUtils.PRODUCT_NAME;
import static br.com.uol.compasso.catalog.product.utils.ProductTestUtils.mockProductDTO;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.uol.compasso.catalog.exception.handler.ErrorResponse;
import br.com.uol.compasso.catalog.product.AbstractIntegrationTest;
import br.com.uol.compasso.catalog.product.domain.Product;
import br.com.uol.compasso.catalog.product.domain.ProductDTO;
import br.com.uol.compasso.catalog.product.repository.ProductRepository;
import br.com.uol.compasso.catalog.product.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class ProductControllerIntegrationTest extends AbstractIntegrationTest {

    public static final String LOCALHOST = "http://localhost:";
    public static final String PRODUCTS = "/products/";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldGetProductExpectingSuccess() {

        ProductDTO productDTO = productService.insertProduct(mockProductDTO());

        String getProductUrl = LOCALHOST + port + PRODUCTS + productDTO.getId();

        ProductDTO result = restTemplate.getForObject(getProductUrl, ProductDTO.class);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(PRODUCT_NAME);
        assertThat(result.getDescription()).isEqualTo(PRODUCT_DESCRIPTION);
        assertThat(result.getPrice()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void shouldGetProductExpectingResourceNotFound() {

        String getProductUrl = LOCALHOST + port + PRODUCTS + "9999999999";

        ResponseEntity<String> response = restTemplate.getForEntity(getProductUrl, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNullOrEmpty();

    }

    @Test
    void shouldInsertProductExpectingSuccess() {

        String insertProductUrl = LOCALHOST + port + PRODUCTS ;

        ProductDTO dto = mockProductDTO();

        ProductDTO result = restTemplate.postForObject(insertProductUrl, dto, ProductDTO.class);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(PRODUCT_NAME);
        assertThat(result.getDescription()).isEqualTo(PRODUCT_DESCRIPTION);
        assertThat(result.getPrice()).isEqualTo(BigDecimal.TEN);

        dto = productService.findProduct(result.getId());

        assertThat(dto.getId()).isNotNull();
        assertThat(dto.getName()).isEqualTo(PRODUCT_NAME);
        assertThat(dto.getDescription()).isEqualTo(PRODUCT_DESCRIPTION);
        assertThat(dto.getPrice()).isEqualTo(BigDecimal.TEN);

    }

    @Test
    void shouldInsertProductExpectingValidationErrorOnName() {

        String insertProductUrl = LOCALHOST + port + PRODUCTS ;

        ProductDTO dto = mockProductDTO();
        dto.setName(null);

        ErrorResponse result = restTemplate.postForObject(insertProductUrl, dto, ErrorResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(400);
        assertThat(result.getMessage()).contains("name");
    }

    @Test
    void shouldInsertProductExpectingValidationErrorOnDescription() {

        String insertProductUrl = LOCALHOST + port + PRODUCTS ;

        ProductDTO dto = mockProductDTO();
        dto.setDescription(null);

        ErrorResponse result = restTemplate.postForObject(insertProductUrl, dto, ErrorResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(400);
        assertThat(result.getMessage()).contains("description");

    }

    @Test
    void shouldInsertProductExpectingValidationErrorOnPrice() {

        String insertProductUrl = LOCALHOST + port + PRODUCTS ;

        ProductDTO dto = mockProductDTO();
        dto.setPrice(null);

        ErrorResponse result = restTemplate.postForObject(insertProductUrl, dto, ErrorResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(400);
        assertThat(result.getMessage()).contains("price");

    }


    @Test
    void shouldUpdateProductExpectingSuccess() {

        ProductDTO productDTO = productService.insertProduct(mockProductDTO());

        String updateProductUrl = LOCALHOST + port + PRODUCTS + productDTO.getId() ;

        productDTO.setName("dog");
        productDTO.setDescription("angry");
        productDTO.setPrice(BigDecimal.ONE);

        restTemplate.put(updateProductUrl, productDTO);

        productDTO = productService.findProduct(productDTO.getId());

        assertThat(productDTO.getId()).isNotNull();
        assertThat(productDTO.getName()).isEqualTo("dog");
        assertThat(productDTO.getDescription()).isEqualTo("angry");
        assertThat(productDTO.getPrice()).isEqualTo(BigDecimal.ONE);

    }

    @Test
    void shouldUpdateProductExpectingResourceNotFound() {

        String updateProductUrl = LOCALHOST + port + PRODUCTS + "999999" ;

        ResponseEntity<String> response = restTemplate.getForEntity(updateProductUrl, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNullOrEmpty();

    }

    @Test
    void shouldListProducts() {

        productRepository.deleteAll();

        productService.insertProduct(mockProductDTO());
        productService.insertProduct(mockProductDTO());
        productService.insertProduct(mockProductDTO());
        productService.insertProduct(mockProductDTO());

        String listProductUrl = LOCALHOST + port + PRODUCTS ;

        List products = restTemplate.getForObject(listProductUrl, List.class);

        assertThat(products).hasSize(4);
    }

    @Test
    void shouldDeleteProduct() {

        ProductDTO productDTO = productService.insertProduct(mockProductDTO());

        String deleteProductUrl = LOCALHOST + port + PRODUCTS + productDTO.getId() ;

        restTemplate.delete(deleteProductUrl);

        Optional<Product> optional = productRepository.findById(productDTO.getId());

        assertThat(optional).isEmpty();
    }

    @Test
    void shouldSearchProductByName() {

        ProductDTO productDTO = mockProductDTO();
        productDTO.setName("lion");

        productService.insertProduct(productDTO);

        String searchProductUrl = LOCALHOST + port + PRODUCTS + "search?q=lion" ;

        List products = restTemplate.getForObject(searchProductUrl, List.class);

        assertThat(products)
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void shouldSearchProductByDescription() {

        ProductDTO productDTO = mockProductDTO();
        productDTO.setDescription("beautiful");

        productService.insertProduct(productDTO);

        String searchProductUrl = LOCALHOST + port + PRODUCTS + "search?q=beautiful" ;

        List products = restTemplate.getForObject(searchProductUrl, List.class);

        assertThat(products)
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void shouldSearchProductByMinPrice() {

        ProductDTO productDTO = mockProductDTO();
        productDTO.setPrice(BigDecimal.valueOf(777L));

        productService.insertProduct(productDTO);

        String searchProductUrl = LOCALHOST + port + PRODUCTS + "search?min_price=776" ;

        List products = restTemplate.getForObject(searchProductUrl, List.class);

        assertThat(products)
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void shouldSearchProductByMaxPrice() {

        ProductDTO productDTO = mockProductDTO();
        productDTO.setPrice(BigDecimal.valueOf(0.2));

        productService.insertProduct(productDTO);

        String searchProductUrl = LOCALHOST + port + PRODUCTS + "search?max_price=0.5" ;

        List products = restTemplate.getForObject(searchProductUrl, List.class);

        assertThat(products)
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void shouldSearchProductByAllParameters() {

        ProductDTO productDTO = mockProductDTO();
        productDTO.setName("cake");
        productDTO.setPrice(BigDecimal.valueOf(1100));

        productService.insertProduct(productDTO);

        String searchProductUrl = LOCALHOST + port + PRODUCTS + "search?q=cak&min_price=1000&max_price=1200" ;

        List products = restTemplate.getForObject(searchProductUrl, List.class);

        assertThat(products)
                .isNotEmpty()
                .hasSize(1);
    }

}