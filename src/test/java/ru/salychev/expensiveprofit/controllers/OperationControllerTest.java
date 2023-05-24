package ru.salychev.expensiveprofit.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.salychev.expensiveprofit.models.*;
import ru.salychev.expensiveprofit.repo.OperationRepository;
import ru.salychev.expensiveprofit.repo.UserRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OperationControllerTest {
    @Mock
    OperationRepository operationRepository;

    @InjectMocks
    OperationController operationController;

    @Test
    void testCreateOperation() {
        User user=new User("user", new Date());
        Currency currency=new Currency("currency");
        currency.setId(123L);
        Type type=new Type("type");
        type.setId(234L);
        Unit unit=new Unit("unit");
        unit.setId(345L);
        Operation operation = new Operation();
        operation.setOperation("operation");
        operation.setComment("comment");
        operation.setCost(new BigDecimal(10));
        operation.setPrice(new BigDecimal(10));
        operation.setUser(user);
        operation.setDate(new Date());
        operation.setQuantity(1.0);
        operation.setUnit(unit);
        operation.setType(type);
        operation.setCurrency(currency);

        when(operationRepository.save(any(Operation.class))).thenReturn(operation);

        ResponseEntity<Operation> result = operationController.createOperation(operation.getCurrency().getId(),
                user.getId(), operation.getType().getId(), operation.getUnit().getId(), operation);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(operation, result.getBody());
    }

    @Test
    void getAllOperations() {
    }

    @Test
    void getOperationById() {
    }

    @Test
    void getAllOperationsByUserId() {
    }

    @Test
    void updateExpenseById() {
    }

    @Test
    void deleteOperationById() {
    }

    @Test
    void deleteOperationByUserId() {
    }

    @Test
    void deleteAllOperations() {
    }
}