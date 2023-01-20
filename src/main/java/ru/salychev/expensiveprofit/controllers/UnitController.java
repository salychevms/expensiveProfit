package ru.salychev.expensiveprofit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salychev.expensiveprofit.models.Unit;
import ru.salychev.expensiveprofit.repo.UnitRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class UnitController {
    final
    UnitRepository unitRepository;

    public UnitController(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @PostMapping("/units")
    public ResponseEntity<Unit> createUnit(@RequestBody Unit type) {
        try {
            Unit unit = unitRepository.save(new Unit(type.getName()));
            return new ResponseEntity<>(unit, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/units")
    public ResponseEntity<List<Unit>> getAllUnits() {
        List<Unit> types = unitRepository.findAll();
        if (types.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @GetMapping("/units/{id}")
    public ResponseEntity<Unit> findById(@PathVariable("id") Long id) {
        Optional<Unit> type = unitRepository.findById(id);
        return type.map(unit -> new ResponseEntity<>(unit, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PutMapping("/units/{id}")
    public ResponseEntity<Unit> updateById(@PathVariable("id") Long id, @RequestBody Unit unit) {
        Optional<Unit> type = unitRepository.findById(id);
        if (type.isPresent()) {
            Unit newType = type.get();
            newType.setName(unit.getName());
            return new ResponseEntity<>(unitRepository.save(newType), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/units/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Long id) {
        try {
            unitRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/units")
    public ResponseEntity<HttpStatus> deleteAllUnits() {
        try {
            unitRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
