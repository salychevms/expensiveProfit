package ru.salychev.expensiveprofit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salychev.expensiveprofit.models.Profit;
import ru.salychev.expensiveprofit.models.User;
import ru.salychev.expensiveprofit.repo.*;
import ru.salychev.expensiveprofit.validation.Validator;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class ProfitController {
    final
    ProfitRepository profitRepository;
    final
    UserRepository userRepository;
    final
    UnitRepository unitRepository;
    final
    CurrencyRepository currencyRepository;
    final
    TypeRepository typeRepository;

    public ProfitController(ProfitRepository profitRepository, UserRepository userRepository,
                            UnitRepository unitRepository, CurrencyRepository currencyRepository,
                            TypeRepository typeRepository) {
        this.profitRepository = profitRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.currencyRepository = currencyRepository;
        this.typeRepository = typeRepository;
    }

    @GetMapping(value = "/profits")
    public ResponseEntity<List<Profit>> getAllProfits(){
        try{
            List<Profit> profits=profitRepository.findAll();
            return new ResponseEntity<>(profits, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users/{userId}/profits/currency={currencyId}/type={typeId}/unit={unitId}")
    public ResponseEntity<Profit> createProfit(@PathVariable(value = "userId")Long userId,
                                               @PathVariable(value="currencyId", required = false)Long currencyId,
                                               @PathVariable(value = "typeId", required = false)Long typeId,
                                               @PathVariable(value = "unitId", required = false) Long unitId,
                                               @RequestBody Profit profit){

        System.out.println("postMapping request consists of:\n" + profit + "\n");
        Optional<User> userData = userRepository.findById(userId);
        if (userData.isPresent()) { //finding and checking user from request
            User user = userData.get();
            profit.setUser(user);
            try {
                profit = Validator.profitPostValidating(profit, currencyId, typeId, unitId,
                        currencyRepository, typeRepository, unitRepository);
                assert profit != null;
                Profit profitData = profitRepository.save(new Profit(user,
                        profit.getType(),
                        profit.getUnit(),
                        profit.getCurrency(),
                        profit.getDate(),
                        profit.getQuantity(),
                        profit.getPrice(),
                        profit.getCost(),
                        profit.getComment()));
                System.out.println("Success! Profit data is saved!\n" + profitData);
                return new ResponseEntity<>(profitData, HttpStatus.CREATED);
            } catch (Exception e) {
                System.out.println("Error! Profit data is not saved!\n" + profit);
                e.printStackTrace();
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
