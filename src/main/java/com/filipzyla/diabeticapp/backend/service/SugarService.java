package com.filipzyla.diabeticapp.backend.service;

import com.filipzyla.diabeticapp.backend.models.Sugar;
import com.filipzyla.diabeticapp.backend.repositories.SugarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SugarService {

    private final SugarRepository sugarRepository;

    public Sugar save(Sugar sugar) {
        return sugarRepository.save(sugar);
    }

    public void delete(Sugar sugar) {
        sugarRepository.delete(sugar);
    }

    public Sugar findFirstByOrderByTimeAsc(Long userId) {
        return sugarRepository.findFirstByOrderByTimeAsc(userId);
    }

    public List<Sugar> findAllOrderByTimeBetweenDates(Long userId, LocalDate from, LocalDate to) {
        return sugarRepository.findAllOrderByTimeBetweenDates(userId, from, to);
    }
}