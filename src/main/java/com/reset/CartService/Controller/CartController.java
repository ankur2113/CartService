package com.reset.CartService.Controller;

import com.reset.CartService.Entity.Cart;
import com.reset.CartService.Entity.ProductDTO;
import com.reset.CartService.Service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cartservice")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/products")
    public Flux<ProductDTO> listAllProducts() {
        return cartService.getAllProducts();
    }

    @PostMapping("/createcart")
    public Mono<ResponseEntity<Cart>> createOrUpdateCart(@RequestBody Cart cart) {
        return cartService.createOrUpdateCart(cart)
                .map(savedCart -> ResponseEntity.ok(savedCart))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}
