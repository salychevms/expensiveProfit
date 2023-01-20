package ru.salychev.expensiveprofit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salychev.expensiveprofit.models.User;
import ru.salychev.expensiveprofit.repo.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class UserController {
    final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*create a new user*/
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User _user = userRepository.save(new User(user.getUsername(), new java.util.Date()));
            return new ResponseEntity<>(_user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*get all users*/
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = new ArrayList<>(userRepository.findAll());
        if (users.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /*get specific user by user id*/
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    /*update a user by id if new data is existed on request body and checked*/
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUserTgId(@PathVariable("id") Long id, @RequestBody User user) {
        Optional<User> userData = userRepository.findById(id);
        if (userData.isPresent()) {
            User updateUser = userData.get();
            if (userRepository.findByTgId(user.getTgId()).isEmpty()
                    && !user.getTgId().isEmpty()) {
                updateUser.setTgId(user.getTgId());
            }
            if (userRepository.findByEmail(user.getEmail()).isEmpty()
                    && !user.getEmail().isEmpty()) {
                updateUser.setEmail(user.getEmail());
            }
            if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isEmpty()
                    && !user.getPhoneNumber().isEmpty()) {
                updateUser.setPhoneNumber(user.getPhoneNumber());
            }
            updateUser.setLastName(user.getLastName());
            updateUser.setFirstName(user.getFirstName());
            return new ResponseEntity<>(userRepository.save(updateUser), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /*delete all users*/
    @DeleteMapping("/users")
    public ResponseEntity<HttpStatus> deleteAllUsers() {
        try {
            userRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*delete a user by user id*/
    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
