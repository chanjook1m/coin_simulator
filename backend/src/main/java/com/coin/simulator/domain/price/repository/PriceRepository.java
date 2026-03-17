package com.coin.simulator.domain.price.repository;

import com.coin.simulator.domain.price.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Long>{

}
