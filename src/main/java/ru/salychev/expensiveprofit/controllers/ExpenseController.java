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

    @PostMapping("/users/{user}/expenses/currency={currencyName}/type={typeName}/unit={unitName}")
    public ResponseEntity<Expense> createExpense(@PathVariable("user") Long userId,
                                                 @PathVariable(value = "currencyName", required = false) String currencyName,
                                                 @PathVariable(value = "typeName", required = false) String typeName,
                                                 @PathVariable(value = "unitName", required = false) String unitName,
                                                 @RequestBody Expense expense) {
        System.out.println("postMapping request consists of:\n" + expense+"\n");
        Optional<User> userData = userRepository.findById(userId);
        if (userData.isPresent()) { //finding and checking user from request
            User user = userData.get();
            expense.setUser(user);


            //currency
            try {
                if (currencyName == null || currencyName.isEmpty()) {
                    if (currencyRepository.findByName("EUR").getName().isEmpty()) {
                        currencyRepository.save(new Currency("EUR"));
                    }
                    Currency newCurrency = currencyRepository.findByName("EUR");
                    expense.setCurrency(newCurrency);
                } else {
                    Currency newCurrency = currencyRepository.findByName(currencyName);
                    expense.setCurrency(newCurrency);
                }
                System.out.println("currency name: " + expense.getCurrency().getName());
            } catch (Exception e) {
                System.out.println("\n1: " + expense + "\n2:" + currencyName);
                e.printStackTrace();
            }


            //type
            try {
                if (typeName == null || typeName.isEmpty()) {
                    if (typeRepository.findByName("UNDEFINED").getName().isEmpty()) {
                        typeRepository.save(new Type("UNDEFINED"));
                    }
                    Type newType= typeRepository.findByName("UNDEFINED");
                    expense.setType(newType);
                } else {
                    Type newType = typeRepository.findByName(typeName);
                    System.out.println("type: "+newType+"\n"+typeRepository.findByName(typeName));
                    expense.setType(newType);
                }
                System.out.println("type name: " + expense.getType().getName());
            } catch (Exception e) {
                System.out.println("\n3: " + expense + "\n4:" + typeName);
                e.printStackTrace();
            }


            //unit
            try {
                if (unitName == null || unitName.isEmpty()) {
                    if (unitRepository.findByName("UNITS").getName().isEmpty()) {
                        unitRepository.save(new Unit("UNITS"));
                    }
                    Unit newUnit=unitRepository.findByName("UNITS");
                    expense.setUnit(newUnit);
                } else {
                    Unit newUnit=unitRepository.findByName(unitName);
                    expense.setUnit(newUnit);
                }
                System.out.println("unit name: " + expense.getUnit().getName());
            } catch (Exception e) {
                System.out.println("\n5: " + expense + "\n6: " + unitName);
                e.printStackTrace();
            }

            try {
                Expense expenseData = expenseRepository.save(new Expense(user,
                        typeRepository.findByName(typeName),
                        unitRepository.findByName(unitName),
                        currencyRepository.findByName(currencyName),
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
