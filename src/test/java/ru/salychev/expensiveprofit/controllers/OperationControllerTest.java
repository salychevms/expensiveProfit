package ru.salychev.expensiveprofit.controllers;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.salychev.expensiveprofit.models.*;
import ru.salychev.expensiveprofit.repo.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OperationControllerTest {
    @Mock
    OperationRepository operationRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    CurrencyRepository currencyRepository;
    @Mock
    TypeRepository typeRepository;
    @Mock
    UnitRepository unitRepository;

    @InjectMocks
    OperationController operationController;

    @Test
    void testCreateOperationAndDataValidation() {
        User user = new User("user", new Date());
        Currency currency = new Currency("currency");
        Type type = new Type("type");
        Unit unit = new Unit("unit");
        user.setId(123L);
        currency.setId(234L);
        type.setId(345L);
        unit.setId(456L);

        Operation operation = new Operation();
        operation.setOperation("testOperation");
        operation.setComment("testComment");

        int randomDigits = (int) (Math.random() * 3);
        if (randomDigits == 0) {
            operation.setQuantity(1.0);
            operation.setPrice(new BigDecimal(20));
        } else if (randomDigits == 1) {
            operation.setQuantity(2.0);
            operation.setCost(new BigDecimal(50));
        } else {
            operation.setCost(new BigDecimal(35));
            operation.setPrice(new BigDecimal(10));
        }

        when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(currencyRepository.findById(currency.getId())).thenReturn(Optional.of(currency));
        when(typeRepository.findById(type.getId())).thenReturn(Optional.of(type));
        when(unitRepository.findById(unit.getId())).thenReturn(Optional.of(unit));

        ResponseEntity<Operation> result = operationController.createOperation(
                user.getId(), currency.getId(), type.getId(), unit.getId(), operation);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(operation, result.getBody());
    }

    @Test
    void testGetAllOperations() {
        List<Operation> operationList = new ArrayList<>();
        List<Operation> expenseList = new ArrayList<>();
        List<Operation> profitList = new ArrayList<>();
        Operation expenseOperation = new Operation();
        Operation profitOperation = new Operation();
        expenseOperation.setOperation("expense");
        profitOperation.setOperation("profit");
        operationList.add(expenseOperation);
        operationList.add(profitOperation);
        expenseList.add(expenseOperation);
        profitList.add(profitOperation);

        when(operationRepository.findAll()).thenReturn(operationList);
        when(operationRepository.findAllByOperationContaining("expense")).thenReturn(expenseList);
        when(operationRepository.findAllByOperationContaining("profit")).thenReturn(profitList);

        ResponseEntity<List<Operation>> resultExpense = operationController.getAllOperations("expense");
        ResponseEntity<List<Operation>> resultProfit = operationController.getAllOperations("profit");
        ResponseEntity<List<Operation>> result = operationController.getAllOperations(null);

        assertNotNull(resultExpense);
        assertEquals(HttpStatus.OK, resultExpense.getStatusCode());
        assertEquals(expenseList, resultExpense.getBody());

        assertNotNull(resultProfit);
        assertEquals(HttpStatus.OK, resultProfit.getStatusCode());
        assertEquals(profitList, resultProfit.getBody());

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(operationList, result.getBody());
    }

    @Test
    void testGetOperationById() {
        Operation operation = new Operation();

        when(operationRepository.findById(operation.getId())).thenReturn(Optional.of(operation));

        ResponseEntity<Operation> result = operationController.getOperationById(operation.getId());

        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(operation, result.getBody());
    }

    @Test
    void testGetAllOperationsByUserId() {
        User user = new User("user", new Date());
        Long id = 123L;
        user.setId(id);

        Operation operationExpense = new Operation();
        Operation operationProfit = new Operation();
        operationProfit.setUser(user);
        operationExpense.setUser(user);

        List<Operation> operationList1 = new ArrayList<>();
        operationList1.add(operationExpense);
        operationList1.add(operationProfit);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(operationRepository.findAllByUserId(id)).thenReturn(operationList1);
        ResponseEntity<List<Operation>> allResults = operationController.getAllOperationsByUserId(id, null);

        List<Operation> operationList2 = new ArrayList<>();
        operationList2.add(operationExpense);

        when(operationRepository.findAllByUserIdAndOperationContaining(id, "expense")).thenReturn(operationList2);
        ResponseEntity<List<Operation>> result = operationController.getAllOperationsByUserId(id, "expense");

        assertNotNull(allResults);
        assertEquals(HttpStatus.OK, allResults.getStatusCode());
        assertEquals(operationList1, allResults.getBody());

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(operationList2, result.getBody());
    }

    @Test
    void testUpdateOperationById() {
        User user = new User("user", new Date());
        Currency currency = new Currency("currency");
        Type type = new Type("type");
        Unit unit = new Unit("unit");
        user.setId(123L);
        currency.setId(234L);
        type.setId(345L);
        unit.setId(456L);

        User newUser = new User("newUser", new Date());
        Currency newCurrency = new Currency("newCurrency");
        Type newType = new Type("newType");
        Unit newUnit = new Unit("newUnit");
        newUser.setId(321L);
        newCurrency.setId(432L);
        newType.setId(543L);
        newUnit.setId(654L);

        Operation updatableOperation = new Operation("testOperation", user, type, unit, currency, new Date(), 20.0,
                new BigDecimal(5), new BigDecimal(100), "testComment");
        Operation request = new Operation("request", newUser, newType, newUnit, newCurrency, new Date(), 15.0,
                new BigDecimal(3), new BigDecimal(75), "newComment");

        when(operationRepository.findById(updatableOperation.getId())).thenReturn(Optional.of(updatableOperation));
        when(operationRepository.save(any(Operation.class))).thenReturn(request);

        ResponseEntity<Operation> result = operationController.updateOperationById(updatableOperation.getId(), newCurrency.getId(),
                newType.getId(), newUnit.getId(), request);

        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(request, result.getBody());
    }

    @Test
    void testDeleteOperationById() {
        ResponseEntity<HttpStatus> result = operationController.deleteOperationById(123L);

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(operationRepository, times(1)).deleteById(123L);
    }

    @Test
    void testDeleteOperationByUserId() {
        User user=new User();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        ResponseEntity<HttpStatus> result=operationController.deleteOperationByUserId(user.getId());

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(userRepository, times(1)).findById(user.getId());
        verify(operationRepository, times(1)).deleteByUserId(user.getId());
    }

    @Test
    void deleteAllOperations() {
        ResponseEntity<HttpStatus> result=operationController.deleteAllOperations();

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(operationRepository, times(1)).deleteAll();
    }
}