package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private List<Product> productList = new ArrayList<>();

    @GetMapping
    public List<Product> getAllProducts() {
        return productList;
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        for (Product product : productList) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    @PostMapping()
    public Product addProduct(@RequestBody Product product) {
        productList.add(product);
        return product;
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product updatedProduct) {
        for (Product product : productList) {
            if (product.getId() == id) {
                product.setName(updatedProduct.getName());
                product.setPrice(updatedProduct.getPrice());
                return product;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) {
        productList.removeIf(product -> product.getId() == id);
    }
}