package com.budget.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "subcategory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "category_id")
    private UUID category_id;

    @Column(name = "subcategory")
    private String subcategory;
}
