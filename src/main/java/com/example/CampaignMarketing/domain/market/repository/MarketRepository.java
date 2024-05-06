package com.example.CampaignMarketing.domain.market.repository;

import com.example.CampaignMarketing.domain.market.entity.Market;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarketRepository extends JpaRepository<Market, Long>, QuerydslPredicateExecutor<Market> {

    Optional<Market> findByOwnerId(Long ownerId);

    Optional<Market> findByName(String name);

    Optional<Market> findByNameAndOwnerId(String name, Long ownerId);

    Page<Market> findAll(Pageable pageable);

}
