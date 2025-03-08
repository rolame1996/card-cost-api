package com.api.cardcost.repositories;

import com.api.cardcost.entities.ClearingCost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClearingCostRepository extends JpaRepository<ClearingCost, Long> {

    Optional<ClearingCost> findByCountryCode(String country);
}
