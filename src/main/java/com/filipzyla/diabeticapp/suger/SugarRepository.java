package com.filipzyla.diabeticapp.suger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SugarRepository extends JpaRepository<Sugar, Long> {

    @Query(value = "SELECT * FROM sugar ORDER BY time DESC LIMIT 1", nativeQuery = true)
    Sugar findFirstByOrderByTimeAsc();
}
