package ru.salychev.expensiveprofit.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.salychev.expensiveprofit.repo.CurrencyRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {
    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyController currencyController;

    public void setup(){
        initMocks(this);
    }

    @Test
    void createCurrency() {
    }

    @Test
    void getAllCurrency() {
    }

    @Test
    void findById() {
    }

    @Test
    void updateCurrencyById() {
    }

    @Test
    void deleteCurrencyById() {
    }

    @Test
    void deleteAll() {
    }
}