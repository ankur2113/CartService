package com.reset.CartService.Service;

import com.reset.CartService.Entity.Cart;
import com.reset.CartService.Entity.ProductDTO;
import com.reset.CartService.Exceptions.ProductNotFoundException;
import com.reset.CartService.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final WebClient webClient;

    public CartService(WebClient.Builder webClientBuilder, CartRepository cartRepository, @Value("${productservice.baseurl}") String productServiceBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(productServiceBaseUrl).build();
        this.cartRepository = cartRepository;
    }

    public Flux<ProductDTO> getAllProducts() {
        return webClient.get().uri("/getallproducts")
                .retrieve().bodyToFlux(ProductDTO.class);
    }

    public Mono<Cart> createOrUpdateCart(Cart cart) {
        // Verify each product in the cart and calculate total price
        return Flux.fromIterable(cart.getItems())
                .flatMap(cartItem -> verifyProduct(cartItem.getProduct().getId())
                        .switchIfEmpty(Mono.error(new ProductNotFoundException("Product with ID " + cartItem.getProduct().getId() + " not found")))
                        .map(productDTO -> {
                            cartItem.setProduct(productDTO); // Update the item with verified product info
                            cartItem.getProduct().setPrice(productDTO.getPrice());
                            return cartItem;
                        }))
                .collectList()
                .doOnNext(items -> cart.setItems(items)) // Ensure the cart has all the verified items
                .doOnNext(items -> cart.calculateTotalPrice())
                .flatMap(items -> cartRepository.save(cart))
                .onErrorResume(e -> {
                    // Log error or take additional actions if necessary
                    return Mono.error(e);
                });
    }

    private Mono<ProductDTO> verifyProduct(Long productId) {
        return webClient
                .get()
                .uri("/getproductbyid/{id}", productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new ProductNotFoundException("Product with ID " + productId + " not found")))
                .bodyToMono(ProductDTO.class);
    }
}
