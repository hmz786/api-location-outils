package dti.crosemont.quebec.location_outils

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.Customizer.withDefaults

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class ConfigurationAcces {

    @Bean
    @Throws(Exception::class)
    fun configurerChaineAcces(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests {
                it.requestMatchers("/").permitAll()
                    .anyRequest().authenticated()
            }
            .cors(withDefaults())
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt(withDefaults())
            }
            .build()
    }
}
