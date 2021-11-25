package com.filipzyla.diabeticapp.backend.repositories;

import com.filipzyla.diabeticapp.backend.models.Insulin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InsulinRepository extends JpaRepository<Insulin, Long> {

    @Query(value = "SELECT * FROM insulin ORDER BY time DESC LIMIT 1", nativeQuery = true)
    Insulin findFirstByOrderByTimeAsc();

    @Query(value = "SELECT * FROM insulin WHERE time BETWEEN :start AND :end ORDER BY time DESC ", nativeQuery = true)
    List<Insulin> findAllOrderByTimeBetweenDates(LocalDate start, LocalDate end);
}