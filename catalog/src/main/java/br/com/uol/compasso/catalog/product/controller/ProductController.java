package br.com.uol.compasso.catalog.product.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.uol.compasso.catalog.product.domain.ProductDTO;
import br.com.uol.compasso.catalog.product.service.ProductService;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductDTO insertProduct(@Valid @RequestBody ProductDTO product){
        return productService.insertProduct(product);
    }

    @GetMapping
    public List<ProductDTO> listAllProducts(){
        return productService.listAllProducts();
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductDTO product) {
        return productService.updateProduct(id, product);
    }

    @GetMapping("/{id}")
    public ProductDTO findProduct(@PathVariable("id") Long id) {
        return productService.findProduct(id);
    }

    @GetMapping("/search")
    public List<ProductDTO> searchProducts(@RequestParam(value = "min_price", required = false) BigDecimal minimalPrice,
                                           @RequestParam(value = "max_price", required = false) BigDecimal maximumPrice,
                                           @RequestParam(value = "q", required = false) String searchText){

        return productService.searchProducts(minimalPrice, maximumPrice, searchText);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
    }

}
