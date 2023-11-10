package com.reset.CartService.Repository;

import com.reset.CartService.Entity.Cart;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends ReactiveMongoRepository<Cart, Long> {
}
