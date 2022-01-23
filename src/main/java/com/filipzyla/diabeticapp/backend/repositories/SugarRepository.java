package com.filipzyla.diabeticapp.backend.repositories;

import com.filipzyla.diabeticapp.backend.models.Sugar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SugarRepository extends JpaRepository<Sugar, Long> {

    @Query(value = "SELECT * FROM sugar WHERE USER_ID = :userId ORDER BY time DESC LIMIT 1", nativeQuery = true)
    Sugar findFirstByOrderByTimeAsc(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM sugar WHERE USER_ID = :userId AND time BETWEEN :start AND :end ORDER BY time DESC ", nativeQuery = true)
    List<Sugar> findAllOrderByTimeBetweenDates(Long userId, LocalDate start, LocalDate end);
}