package ru.salychev.expensiveprofit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salychev.expensiveprofit.models.*;
import ru.salychev.expensiveprofit.repo.*;

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

    @PostMapping("/users/{user}/expenses/currency={currencyId}/type={typeId}/unit={unitId}")
    public ResponseEntity<Expense> createExpense(@PathVariable("user") Long userId,
                                                 @PathVariable(value = "currencyId", required = false) Long currencyId,
                                                 @PathVariable(value = "typeId", required = false) Long typeId,
                                                 @PathVariable(value = "unitId", required = false) Long unitId,
                                                 @RequestBody Expense expense) {
        System.out.println("postMapping request consists of:\n" + expense + "\n");
        Optional<User> userData = userRepository.findById(userId);
        if (userData.isPresent()) { //finding and checking user from request
            User user = userData.get();
            expense.setUser(user);


            //currency
            try {
                if (currencyId == null) { //set default currency
                    Currency newCurrency = currencyRepository.findByName("EUR");
                    expense.setCurrency(newCurrency);
                } else {
                    Optional<Currency> newCurrency = currencyRepository.findById(currencyId);
                    newCurrency.ifPresent(expense::setCurrency);
                }
                System.out.println("currency name: " + expense.getCurrency().getName());
            } catch (Exception e) {
                System.out.println("\n1: " + expense + "\n2:" + currencyId);
                e.printStackTrace();
            }


            //type
            try {
                if (typeId == null) {
                    Type newType = typeRepository.findByName("UNDEFINED");
                    expense.setType(newType);
                } else {
                    Optional<Type> newType = typeRepository.findById(typeId);
                    newType.ifPresent(expense::setType);
                }
                System.out.println("type name: " + expense.getType().getName());
            } catch (Exception e) {
                System.out.println("\n3: " + expense + "\n4:" + typeId);
                e.printStackTrace();
            }


            //unit
            try {
                if (unitId == null) {
                    Unit newUnit = unitRepository.findByName("UNITS");
                    expense.setUnit(newUnit);
                } else {
                    Optional<Unit> newUnit = unitRepository.findById(unitId);
                    newUnit.ifPresent(expense::setUnit);
                }
                System.out.println("unit name: " + expense.getUnit().getName());
            } catch (Exception e) {
                System.out.println("\n5: " + expense + "\n6: " + unitId);
                e.printStackTrace();
            }

            try {
                assert currencyId != null;
                assert unitId != null;
                assert typeId != null;
                Expense expenseData = expenseRepository.save(new Expense(user,
                        expense.getType(),
                        expense.getUnit(),
                        expense.getCurrency(),
                        new java.util.Date(),
                        expense.getQuantity(),
                        expense.getPrice(),
                        expense.getCost(),
                        expense.getComment()));
                System.out.println("expense data is saved\n" + expenseData);
                return new ResponseEntity<>(expenseData, HttpStatus.CREATED);
            } catch (Exception e) {
                System.out.println("expense is not saved\n" + expense);
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
