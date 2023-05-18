package ru.salychev.expensiveprofit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salychev.expensiveprofit.models.*;
import ru.salychev.expensiveprofit.repo.*;
import ru.salychev.expensiveprofit.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class OperationController {
    final
    OperationRepository operationRepository;
    final
    UserRepository userRepository;
    final
    UnitRepository unitRepository;
    final
    CurrencyRepository currencyRepository;
    final
    TypeRepository typeRepository;

    public OperationController(OperationRepository operationRepository, UserRepository userRepository,
                               UnitRepository unitRepository, CurrencyRepository currencyRepository,
                               TypeRepository typeRepository) {
        this.operationRepository = operationRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.currencyRepository = currencyRepository;
        this.typeRepository = typeRepository;
    }


    @PostMapping("/users/{userId}/operations/currency={currencyId}/type={typeId}/unit={unitId}")
    public ResponseEntity<Operation> createOperation(@PathVariable("userId") Long userId,
                                                     @PathVariable(value = "currencyId", required = false) Long currencyId,
                                                     @PathVariable(value = "typeId", required = false) Long typeId,
                                                     @PathVariable(value = "unitId", required = false) Long unitId,
                                                     @RequestBody Operation operation) {
        System.out.println("postMapping request consists of:\n" + operation + "\n");
        Optional<User> userData = userRepository.findById(userId);
        if (userData.isPresent()) { //finding and checking user from request
            User user = userData.get();
            operation.setUser(user);
            try {
                operation = Validator.validationOnCreation(operation, currencyId, typeId, unitId,
                        currencyRepository, typeRepository, unitRepository);
                assert operation != null;
                Operation operationData = operationRepository.save(
                        new Operation(
                                operation.getOperation(),
                                user,
                                operation.getType(),
                                operation.getUnit(),
                                operation.getCurrency(),
                                operation.getDate(),
                                operation.getQuantity(),
                                operation.getPrice(),
                                operation.getCost(),
                                operation.getComment()));
                System.out.println("Success! Operation <<" + operation.getOperation() + ">> data is saved!\n" + operationData);
                return new ResponseEntity<>(operationData, HttpStatus.CREATED);
            } catch (Exception e) {
                System.out.println("Error! Operation <<" + operation.getOperation() + ">> data is not saved!\n" + operation);
                e.printStackTrace();
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
    @GetMapping("/operations")
    public ResponseEntity<List<Operation>> getAllOperations(@RequestParam(required = false) String operation) {
        try {
            List<Operation> operations = new ArrayList<>();
            if (operation == null) {
                operations.addAll(operationRepository.findAll());
            } else {
                operations.addAll(operationRepository.findAllByOperationContaining(operation));
            }
            return new ResponseEntity<>(operations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/operations/{id}")
    public ResponseEntity<Operation> getOperationById(@PathVariable("id") Long id) {
        Optional<Operation> operation = operationRepository.findById(id);
        return operation.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{userId}/operations")
    public ResponseEntity<List<Operation>> getAllOperationsByUserId(@PathVariable("userId") Long userId,
                                                                    @RequestParam(required = false) String operation) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            try {
                List<Operation> operations;
                if (operation != null) {
                    operations=operationRepository.findAllByUserIdAndOperationContaining(userId, operation);
                }else{
                    operations =  operationRepository.findAllByUserId(userId);
                }
                return new ResponseEntity<>(operations, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/operations/{id}/currency={currencyId}/type={typeId}/unit={unitId}")
    public ResponseEntity<Operation> updateExpenseById(@PathVariable("id") Long id,
                                                       @PathVariable(value = "currencyId", required = false) Long currencyId,
                                                       @PathVariable(value = "typeId", required = false) Long typeId,
                                                       @PathVariable(value = "unitId", required = false) Long unitId,
                                                       @RequestBody Operation request) {
        Optional<Operation> operationData = operationRepository.findById(id);
        if (operationData.isPresent()) {
            Operation updatableOperation = operationData.get();
            updatableOperation = Validator.validationOnUpdate(request, updatableOperation,
                    currencyId, typeId, unitId,
                    currencyRepository, typeRepository, unitRepository);
            if (updatableOperation != null)
                return new ResponseEntity<>(operationRepository.save(updatableOperation), HttpStatus.OK);
            else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/operations/{id}")
    public ResponseEntity<HttpStatus> deleteOperationById(@PathVariable("id") Long id) {
        try {
            operationRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{userId}/operations")
    public ResponseEntity<HttpStatus> deleteOperationByUserId(@PathVariable("userId") Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            operationRepository.deleteByUserId(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/operations")
    public ResponseEntity<HttpStatus> deleteAllOperations() {
        try {
            operationRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
