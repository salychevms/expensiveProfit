package ru.salychev.expensiveprofit.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.salychev.expensiveprofit.models.User;
import ru.salychev.expensiveprofit.repo.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class userControllerTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        List<User> userList = new ArrayList<User>();
        userList.add(new User("testUser1", new Date()));
        userList.add(new User("testUser2", new Date()));

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userController.getAllUsers().getBody();

        assertThat(result).isEqualTo(userList);
    }

    @Test
    public void testGetUserById() {
        User user = new User("testUser3", new Date());
        Long userId = user.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userController.getUserById(userId).getBody();

        assertThat(result).isEqualTo(user);
    }

    @Test
    public void testCreateUser() {
        User user = new User("testUser4", new Date());
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userController.createUser(user).getBody();

        assert result != null;
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getLastName()).isEqualTo(user.getLastName());
    }

    @Test
    public void testUpdateUser() {
        User existingUser = new User("testUser5", new Date());
        User updatedUser = new User("testUserForCompare", new Date());
        updatedUser.setLastName("testLastName");
        updatedUser.setFirstName("testFirstName");
        updatedUser.setEmail("test@email.email");
        updatedUser.setTgId("testTGId");
        updatedUser.setPhoneNumber("+491234567890");

        when(userRepository.findById(updatedUser.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userController.updateUserById(updatedUser.getId(), updatedUser).getBody();

        assert result != null;
        assertThat(result.getFirstName()).isEqualTo(updatedUser.getFirstName());
        assertThat(result.getLastName()).isEqualTo(updatedUser.getLastName());
        assertThat(result.getEmail()).isEqualTo(updatedUser.getEmail());
        assertThat(result.getTgId()).isEqualTo(updatedUser.getTgId());
        assertThat(result.getPhoneNumber()).isEqualTo(updatedUser.getPhoneNumber());
    }

    @Test
    public void testDeleteUser() {
        User user = new User("testUser6", new Date());
        Long userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userController.deleteUserById(userId);
        System.out.println("User deleted: " + user);
        verify(userRepository, times(1)).deleteById(userId);
        assertThat(userRepository.findById(userId)).isEmpty();
    }
}
