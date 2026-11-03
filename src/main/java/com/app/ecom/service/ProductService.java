package com.app.ecom.service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.entity.Product;
import com.app.ecom.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest){

        Product toBeSaved=mapToProductEntity(productRequest);
        productRepository.save(toBeSaved);
        return mapToProductResponse(toBeSaved);
    }

    public Optional<ProductResponse> fetchProductById(Long id){
        return productRepository.findById(id)
                .map(this::mapToProductResponse);
    }



    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest){

       return productRepository.findById(id)
               .map(existing -> {
                   Product updated=mapToProductEntity(productRequest);
                   updated.setId(existing.getId());
                   productRepository.save(updated);
                 return mapToProductResponse(updated);
               });

    }

    public List<ProductResponse> findByActiveTrue() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .description(product.getDescription())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .active(product.getActive())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private Product mapToProductEntity(ProductRequest productRequest) {
        return Product.builder()
                .category(productRequest.getCategory())
                .description(productRequest.getDescription())
                .name(productRequest.getName())
                .imageUrl(productRequest.getImageUrl())
                .price(productRequest.getPrice())
                .stockQuantity(productRequest.getStockQuantity())
                .build();
    }

    public boolean deleteProductById(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);

    }

    public List<ProductResponse> searchProducts(String keyword) {
       return productRepository.searchProducts(keyword)
               .stream()
               .map(this::mapToProductResponse)
               .collect(Collectors.toList());

    }


}
