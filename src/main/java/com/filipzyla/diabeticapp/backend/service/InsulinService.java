package com.filipzyla.diabeticapp.backend.service;

import com.filipzyla.diabeticapp.backend.models.Insulin;
import com.filipzyla.diabeticapp.backend.repositories.InsulinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsulinService {

    private final InsulinRepository insulinRepository;

    public Insulin save(Insulin insulin) {
        return insulinRepository.save(insulin);
    }

    public void delete(Insulin insulin) {
        insulinRepository.delete(insulin);
    }

    public Insulin findFirstByOrderByTimeAsc() {
        return insulinRepository.findFirstByOrderByTimeAsc();
    }

    public List<Insulin> findAllOrderByTimeBetweenDates(LocalDate from, LocalDate to) {
        return insulinRepository.findAllOrderByTimeBetweenDates(from, to);
    }
}
