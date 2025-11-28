import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testLoginSuccess() {


        UserModel user = new UserModel();

        user.setUsername("Alice");
        user.setPassword("password1");

        Mockito.when(userRepository.findByUsername("Alice")).thenReturn(user);

        assertTrue(userService.checkLogin("Alice", "password1"));
    }

    @Test
    void testLoginFailure() {

        UserModel user = new UserModel();

        user.setUsername("bob");
        user.setPassword("password2");

        Mockito.when(userRepository.findByUsername("bob")).thenReturn(user);

        assertFalse(userService.checkLogin("bob", "wrong password"));
    }

    @Test
    void testUserNotFound() {

        Mockito.when(userRepository.findByUsername("Bob")).thenReturn(null);

        boolean result = userService.checkLogin("Bob", "password1");

        assertFalse(result, "login failed, no such user");

    }

}
