package ru.salychev.expensiveprofit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salychev.expensiveprofit.models.*;
import ru.salychev.expensiveprofit.repo.*;
import ru.salychev.expensiveprofit.validation.Validator;

import java.math.BigDecimal;
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

    public ExpenseController(ExpenseRepository expenseRepository, UserRepository userRepository, UnitRepository unitRepository, CurrencyRepository currencyRepository, TypeRepository typeRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.currencyRepository = currencyRepository;
        this.typeRepository = typeRepository;
    }

    @PostMapping("/users/{user}/expenses")
    public ResponseEntity<Expense> createExpense(@PathVariable("user") Long userId, @RequestBody Expense expense) {
        Optional<User> userData = userRepository.findById(userId);
        if (userData.isPresent()) { //finding and checking user from request
            User user=userData.get();
            expense.setUser(user);
        }
        return new ResponseEntity<>(expenseRepository.save(expense), HttpStatus.CREATED);
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

    @GetMapping("expenses/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable("id") Long id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        return expense.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{userId}/expenses")
    public ResponseEntity<List<Expense>> getAllExpensesByUserId(@PathVariable("userId") Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Expense> expenses = expenseRepository.findAllByUser(user.get());
            return new ResponseEntity<>(expenses, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
