package com.reset.CartService.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "cart_table")
public class Cart {

    @Id
    private Long cartId;
    private List<CartItem> items;
    private double totalPrice;

    public void calculateTotalPrice(){
        this.totalPrice = items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }


}
