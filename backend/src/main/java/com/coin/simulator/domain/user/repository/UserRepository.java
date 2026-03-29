package com.coin.simulator.domain.user.repository;

import com.coin.simulator.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
