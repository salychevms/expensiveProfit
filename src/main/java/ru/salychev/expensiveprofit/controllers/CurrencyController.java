package ru.salychev.expensiveprofit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salychev.expensiveprofit.models.Currency;
import ru.salychev.expensiveprofit.repo.CurrencyRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class CurrencyController {
    final
    CurrencyRepository currencyRepository;

    public CurrencyController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @PostMapping("/currency")
    public ResponseEntity<Currency> createCurrency(@RequestBody Currency type) {
        try {
            Currency currency = currencyRepository.save(new Currency(type.getName()));
            return new ResponseEntity<>(currency, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/currency")
    public ResponseEntity<List<Currency>> getAllCurrency() {
        List<Currency> currencies = currencyRepository.findAll();
        if (currencies.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(currencies, HttpStatus.OK);
    }

    @GetMapping("/currency/{id}")
    public ResponseEntity<Currency> findById(@PathVariable("id") Long id) {
        Optional<Currency> currency = currencyRepository.findById(id);
        return currency.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PutMapping("/currency/{id}")
    public ResponseEntity<Currency> updateCurrencyById(@PathVariable("id") Long id, @RequestBody Currency currency) {
        Optional<Currency> currencyData = currencyRepository.findById(id);
        if (currencyData.isPresent()) {
            if (currencyRepository.findAllByName(currency.getName()).isEmpty()) {
                Currency newCurrency = currencyData.get();
                newCurrency.setName(currency.getName());
                return new ResponseEntity<>(currencyRepository.save(newCurrency), HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/currency/{id}")
    public ResponseEntity<HttpStatus> deleteCurrencyById(@PathVariable("id") Long id) {
        try {
            currencyRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/currency")
    public ResponseEntity<HttpStatus> deleteAll() {
        try {
            currencyRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
