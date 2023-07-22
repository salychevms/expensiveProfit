package ru.salychev.expensiveprofit.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.salychev.expensiveprofit.models.User;
import ru.salychev.expensiveprofit.repo.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetAllUsers() {
        List<User> userList = new ArrayList<>();
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
        User updatedUser=new User();
        User update = new User("testUserForCompare", new Date());
        update.setLastName("testLastName");
        update.setFirstName("testFirstName");
        update.setEmail("test@email.email");
        update.setTgId("testTGId");
        update.setPhoneNumber("+491234567890");

        when(userRepository.findById(update.getId())).thenReturn(Optional.of(updatedUser));
        when(userRepository.save(any(User.class))).thenReturn(update);

        ResponseEntity<User> result = userController.updateUserById(updatedUser.getId(), update);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(result.getBody(), update);
    }

    @Test
    public void testDeleteUserById() {
        User user = new User("testUser6", new Date());
        Long userId = user.getId();
        //when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userController.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
        assertThat(userRepository.findById(userId)).isEmpty();
    }

    @Test
    public void testDeleteAll() {
        ResponseEntity<HttpStatus> result = userController.deleteAllUsers();
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(userRepository, times(1)).deleteAll();
    }
}
