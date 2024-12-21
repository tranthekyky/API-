package com.springbootapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Name cannot be empty and must contain at least one non-whitespace character")
    private String name;
    @NotNull(message = "Price cannot be empty and must contain at least one non-whitespace character")
    private Double price;
    @NotNull(message = "Quantity cannot be empty and must contain at least one non-whitespace character")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}
