package y.project.core.configuration.sercurityCofig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import y.project.core.application.user.UserStorage;
import y.project.core.domain.User;
import y.project.core.domain.UserInfo;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserStorage userStorage;

    @Bean
    public AuthenticationProvider authenticationProvider(
            final UserDetailsService userDetailsService,
            final PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> userOptional = userStorage.findByUserName(username);
            if (userOptional.isEmpty()) {
                throw new UsernameNotFoundException(username);
            }
            return new UserInfo(userOptional.get());
        };
    }
}
