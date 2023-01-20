package ru.salychev.expensiveprofit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salychev.expensiveprofit.models.Type;
import ru.salychev.expensiveprofit.repo.TypeRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class TypeController {
    final
    TypeRepository typeRepository;

    public TypeController(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @PostMapping("/types")
    public ResponseEntity<Type> createExpensiveProfitType(@RequestBody Type epType) {
        try {
            Type _epType = typeRepository.save(new Type(epType.getName()));
            return new ResponseEntity<>(_epType, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/types")
    public ResponseEntity<List<Type>> findAll() {
        List<Type> types = typeRepository.findAll();
        if (types.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @GetMapping("/types/{id}")
    public ResponseEntity<Type> findById(@PathVariable("id") Long id) {
        Optional<Type> type = typeRepository.findById(id);
        return type.map(expenseProfitType -> new ResponseEntity<>(expenseProfitType, HttpStatus.OK)).orElseGet(() ->
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PutMapping("/types/{id}")
    public ResponseEntity<Type> updateTypeById(@PathVariable("id") Long id, @RequestBody Type epType) {
        Optional<Type> type = typeRepository.findById(id);
        if (type.isPresent()) {
            Type newType = type.get();
            if (typeRepository.findAllByName(epType.getName()).isEmpty()) {
                newType.setName(epType.getName());
                return new ResponseEntity<>(typeRepository.save(newType), HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/types/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id")Long id){
        try{
            typeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/types")
    public ResponseEntity<HttpStatus> deleteAll(){
        try {
            typeRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
