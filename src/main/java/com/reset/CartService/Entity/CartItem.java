package com.reset.CartService.Entity;


import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartItem {

    private ProductDTO product;
    private int quantity;

    public double getTotalPrice(){
        return this.quantity*this.product.getPrice();
    }

}
