package ru.salychev.expensiveprofit.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.salychev.expensiveprofit.models.*;
import ru.salychev.expensiveprofit.repo.CurrencyRepository;
import ru.salychev.expensiveprofit.repo.TypeRepository;
import ru.salychev.expensiveprofit.repo.UnitRepository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatorTest {
    @Mock
    UnitRepository unitRepository;
    @Mock
    TypeRepository typeRepository;
    @Mock
    CurrencyRepository currencyRepository;

    @Test
    void validationOnCreation() {
        Currency currency = new Currency("testCurrency");
        currency.setId(123L);
        Type type = new Type("testType");
        type.setId(234L);
        Unit unit = new Unit("testUnit");
        unit.setId(345L);
        Operation request = new Operation("test",
                new User("testUser", new Date()),
                type,
                unit,
                currency,
                new Date(),
                0.0,
                new BigDecimal(0),
                new BigDecimal(0),
                "comment");

        when(unitRepository.findById(unit.getId())).thenReturn(Optional.of(unit));
        when(currencyRepository.findById(currency.getId())).thenReturn(Optional.of(currency));
        when(typeRepository.findById(type.getId())).thenReturn(Optional.of(type));

        Operation result = Validator.validationOnCreation(request,
                currency.getId(),
                type.getId(),
                unit.getId(),
                currencyRepository,
                typeRepository,
                unitRepository);

        assertNotNull(result);
        assertEquals(result, request);
    }

    @Test
    void validationOnUpdate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        Currency requestCurrency = new Currency();
        requestCurrency.setId(987L);
        Type requestType = new Type();
        requestType.setId(876L);
        Unit requestUnit = new Unit();
        requestUnit.setId(765L);
        Date requestDate = dateFormat.parse("2020");
        BigDecimal requestPrice = BigDecimal.valueOf(10);
        BigDecimal requestCost = BigDecimal.valueOf(20);
        Double requestQuantity = 2.0;
        String requestComment = "qwertyqwe";

        Operation updatableOperation = new Operation(
                "testOperation",
                new User(),
                requestType,
                requestUnit,
                requestCurrency,
                requestDate,
                requestQuantity,
                requestPrice,
                requestCost,
                requestComment);

        Currency currency = new Currency("testCurrency");
        currency.setId(123L);
        Type type = new Type("testType");
        type.setId(234L);
        Unit unit = new Unit("testUnit");
        unit.setId(345L);
        Date date = dateFormat.parse("2023");
        BigDecimal price = BigDecimal.valueOf(5);
        BigDecimal cost = BigDecimal.valueOf(15);
        Double quantity = 3.0;
        String comment = "testComment";

        Operation request = new Operation();
        request.setCurrency(currency);
        request.setUnit(unit);
        request.setType(type);
        request.setCost(cost);
        request.setPrice(price);
        request.setQuantity(quantity);
        request.setDate(date);
        request.setComment(comment);

        when(unitRepository.findById(unit.getId())).thenReturn(Optional.of(unit));
        when(currencyRepository.findById(currency.getId())).thenReturn(Optional.of(currency));
        when(typeRepository.findById(type.getId())).thenReturn(Optional.of(type));

        Operation result = Validator.validationOnUpdate(
                request,
                updatableOperation,
                currency.getId(),
                type.getId(),
                unit.getId(),
                currencyRepository,
                typeRepository,
                unitRepository);

        assertNotNull(result);
        assertEquals(result.getCurrency(), request.getCurrency());
        assertEquals(result.getType(), request.getType());
        assertEquals(result.getUnit(), request.getUnit());
        assertEquals(result.getDate(), request.getDate());
        assertEquals(result.getQuantity(), request.getQuantity());
        assertEquals(result.getCost(), request.getCost());
        assertEquals(result.getPrice(), request.getPrice());
        assertEquals(result.getComment(), request.getComment());
    }
}