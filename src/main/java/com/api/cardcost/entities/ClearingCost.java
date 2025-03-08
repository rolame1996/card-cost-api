package com.api.cardcost.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "clearing_costs")
@NoArgsConstructor
@AllArgsConstructor
public class ClearingCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String countryCode;
    @Column(nullable = false)
    private Double cost;

    public ClearingCost(String countryCode, Double cost) {
        this.countryCode = countryCode;
        this.cost = cost;
    }
}
