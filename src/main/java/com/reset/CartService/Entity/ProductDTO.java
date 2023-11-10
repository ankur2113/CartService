package com.reset.CartService.Entity;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDTO {

    private Long id;
    private String name;
    private double price;

}
