package learn.tdd.infra.config;

import learn.tdd.infra.util.JwtKeyHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    JwtKeyHolder jwtKeyHolder(@Value("${security.jwt.secret}") String secret) {
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        return new JwtKeyHolder(secretKey);
    }

    @Bean
    JwtDecoder jwtDecoder(JwtKeyHolder jwtKeyHolder){
        return NimbusJwtDecoder.withSecretKey(jwtKeyHolder.key()).build();
    }

    @Bean
    DefaultSecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtDecoder jwtDecoder
    ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(c -> c.jwt(jwt -> jwt.decoder(jwtDecoder)))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/seller/signup").permitAll()
                        .requestMatchers("/seller/issueToken").permitAll()
                        .requestMatchers("/shopper/signup").permitAll()
                        .requestMatchers("/shopper/me").authenticated()
                )
                .build();
    }
}
