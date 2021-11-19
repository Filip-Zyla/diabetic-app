package com.filipzyla.diabeticapp.insulin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InsulinRepository extends JpaRepository<Insulin, Long> {

    @Query(value = "SELECT * FROM insulin ORDER BY time DESC LIMIT 1", nativeQuery = true)
    Insulin findFirstByOrderByTimeAsc();

}
