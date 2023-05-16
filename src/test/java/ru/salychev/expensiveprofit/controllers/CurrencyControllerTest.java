package ru.salychev.expensiveprofit.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.salychev.expensiveprofit.models.Currency;
import ru.salychev.expensiveprofit.models.Operation;
import ru.salychev.expensiveprofit.repo.CurrencyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {
    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyController currencyController;

    @Test
    void testCreateCurrency() {
        Currency currency = new Currency("testCurrency");
        when(currencyRepository.save(any(Currency.class))).thenReturn(currency);

        Currency result = currencyController.createCurrency(currency).getBody();

        assert result != null;
        assertThat(result.getName()).isEqualTo(currency.getName());
        assertThat(result.getId()).isEqualTo(currency.getId());
    }

    @Test
    void testGetAllCurrency() {
        List<Currency> currencyList = new ArrayList<>();
        currencyList.add(new Currency("testCurrency1"));
        currencyList.add(new Currency("testCurrency2"));
        when(currencyRepository.findAll()).thenReturn(currencyList);

        List<Currency> result = currencyController.getAllCurrency().getBody();

        assertThat(result).isEqualTo(currencyList);
    }

    @Test
    void findById() {
        Currency currency = new Currency("testCurrency");
        Long id = currency.getId();
        when(currencyRepository.findById(id)).thenReturn(Optional.of(currency));

        Currency result = currencyController.findById(id).getBody();

        assertThat(result).isEqualTo(currency);
    }

    @Test
    void testUpdateCurrencyById() {
        Currency existingCurrency = new Currency("existingCurrency");
        Currency updatedCurrency = new Currency("updatedCurrency");

        when(currencyRepository.findById(updatedCurrency.getId())).thenReturn(Optional.of(existingCurrency));
        when(currencyRepository.save(any(Currency.class))).thenReturn(updatedCurrency);

        Currency result = currencyController.updateCurrencyById(updatedCurrency.getId(), updatedCurrency).getBody();

        assert result != null;
        assertThat(result.getName()).isEqualTo(updatedCurrency.getName());
    }

    @Test
    void deleteCurrencyById() {
    }

    @Test
    void deleteAll() {
    }
}