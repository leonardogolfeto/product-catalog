package br.com.uol.compasso.catalog.product.mapper;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.Test;

import br.com.uol.compasso.catalog.product.domain.Product;
import br.com.uol.compasso.catalog.product.domain.ProductDTO;
import java.math.BigDecimal;

class ProductMapperTest {

    @Test
    void shouldMapProductToDTO(){

        Product product = new Product(1L, "cat", "amazing", BigDecimal.TEN);

        ProductDTO productDTO = ProductMapper.toDTO(product);

        assertThat(productDTO.getId()).isEqualTo(product.getId());
        assertThat(productDTO.getName()).isEqualTo(product.getName());
        assertThat(productDTO.getDescription()).isEqualTo(product.getDescription());
        assertThat(productDTO.getPrice()).isEqualTo(product.getPrice());

    }

    @Test
    void shouldMapProductDTOToEntity(){

        ProductDTO dto = new ProductDTO(1L, "cat", "amazing", BigDecimal.TEN);

        Product product = ProductMapper.toEntity(dto);

        assertThat(product.getId()).isEqualTo(dto.getId());
        assertThat(product.getName()).isEqualTo(dto.getName());
        assertThat(product.getDescription()).isEqualTo(dto.getDescription());
        assertThat(product.getPrice()).isEqualTo(dto.getPrice());

    }

}