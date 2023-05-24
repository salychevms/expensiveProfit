package ru.salychev.expensiveprofit.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.salychev.expensiveprofit.models.Currency;
import ru.salychev.expensiveprofit.repo.CurrencyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        ResponseEntity<Currency> result = currencyController.createCurrency(currency);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(currency, result.getBody());
    }

    @Test
    void testGetAllCurrency() {
        List<Currency> currencyList = new ArrayList<>();
        currencyList.add(new Currency("testCurrency1"));
        currencyList.add(new Currency("testCurrency2"));
        when(currencyRepository.findAll()).thenReturn(currencyList);

        ResponseEntity<List<Currency>> result = currencyController.getAllCurrency();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(currencyList, result.getBody());
    }

    @Test
    void testFindById() {
        Currency currency = new Currency("testCurrency");
        Long id = currency.getId();
        when(currencyRepository.findById(id)).thenReturn(Optional.of(currency));

        ResponseEntity<Currency> result = currencyController.findById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(currency, result.getBody());
    }

    @Test
    void testUpdateCurrencyById() {
        Currency updatedCurrency = new Currency("Currency");
        Currency update = new Currency("UPDATE");

        when(currencyRepository.findById(updatedCurrency.getId())).thenReturn(Optional.of(updatedCurrency));
        when(currencyRepository.save(any(Currency.class))).thenReturn(update);

        ResponseEntity<Currency> result = currencyController.updateCurrencyById(updatedCurrency.getId(), update);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(update, result.getBody());
    }

    @Test
    void testDeleteCurrencyById() {
        Currency currency = new Currency();

        ResponseEntity<HttpStatus> response = currencyController.deleteCurrencyById(currency.getId());

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(currencyRepository, times(1)).deleteById(currency.getId());
    }

    @Test
    void testDeleteAll() {
        ResponseEntity<HttpStatus> result = currencyController.deleteAll();

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(currencyRepository, times(1)).deleteAll();
    }
}