package ru.salychev.expensiveprofit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salychev.expensiveprofit.models.*;
import ru.salychev.expensiveprofit.repo.*;
import ru.salychev.expensiveprofit.validation.Validator;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class ExpenseController {
    final
    ExpenseRepository expenseRepository;
    final
    UserRepository userRepository;
    final
    UnitRepository unitRepository;
    final
    CurrencyRepository currencyRepository;
    final
    TypeRepository typeRepository;

    public ExpenseController(ExpenseRepository expenseRepository, UserRepository userRepository,
                             UnitRepository unitRepository, CurrencyRepository currencyRepository,
                             TypeRepository typeRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.currencyRepository = currencyRepository;
        this.typeRepository = typeRepository;
    }

    @PostMapping("/users/{userId}/expenses/currency={currencyId}/type={typeId}/unit={unitId}")
    public ResponseEntity<Expense> createExpense(@PathVariable("userId") Long userId,
                                                 @PathVariable(value = "currencyId", required = false) Long currencyId,
                                                 @PathVariable(value = "typeId", required = false) Long typeId,
                                                 @PathVariable(value = "unitId", required = false) Long unitId,
                                                 @RequestBody Expense expense) {
        System.out.println("postMapping request consists of:\n" + expense + "\n");
        Optional<User> userData = userRepository.findById(userId);
        if (userData.isPresent()) { //finding and checking user from request
            User user = userData.get();
            expense.setUser(user);
            try {
                expense = Validator.expensePostValidating(expense, currencyId, typeId, unitId,
                        currencyRepository, typeRepository, unitRepository);
                assert expense != null;
                Expense expenseData = expenseRepository.save(new Expense(user,
                        expense.getType(),
                        expense.getUnit(),
                        expense.getCurrency(),
                        expense.getDate(),
                        expense.getQuantity(),
                        expense.getPrice(),
                        expense.getCost(),
                        expense.getComment()));
                System.out.println("Success! Expense data is saved!\n" + expenseData);
                return new ResponseEntity<>(expenseData, HttpStatus.CREATED);
            } catch (Exception e) {
                System.out.println("Error! Expense data is not saved!\n" + expense);
                e.printStackTrace();
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<Expense>> getAllExpenses() {
        try {
            List<Expense> expenses = expenseRepository.findAll();
            return new ResponseEntity<>(expenses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/expenses/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable("id") Long id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        return expense.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{userId}/expenses")
    public ResponseEntity<List<Expense>> getAllExpensesByUserId(@PathVariable("userId") Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Expense> expenses = expenseRepository.findAllByUserId(user.get());
            return new ResponseEntity<>(expenses, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/expenses/{id}/currency={currencyId}/type={typeId}/unit={unitId}")
    public ResponseEntity<Expense> updateExpenseById(@PathVariable("id") Long id,
                                                     @PathVariable(value = "currencyId", required = false) Long currencyId,
                                                     @PathVariable(value = "typeId", required = false) Long typeId,
                                                     @PathVariable(value = "unitId", required = false) Long unitId,
                                                     @RequestBody Expense request) {
        Optional<Expense> expenseData = expenseRepository.findById(id);
        if (expenseData.isPresent()) {
            Expense updatableExpense = expenseData.get();
            updatableExpense = Validator.expensePutValidating(request, updatableExpense,
                    currencyId, typeId, unitId,
                    currencyRepository, typeRepository, unitRepository);
            if (updatableExpense != null)
                return new ResponseEntity<>(expenseRepository.save(updatableExpense), HttpStatus.OK);
            else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<HttpStatus> deleteExpenseById(@PathVariable("id") Long id) {
        try {
            expenseRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{userId}/expenses")
    public ResponseEntity<HttpStatus> deleteExpensesByUserId(@PathVariable("userId") Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            expenseRepository.deleteByUserId(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/expenses")
    public ResponseEntity<HttpStatus> deleteAllExpenses() {
        try {
            expenseRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
