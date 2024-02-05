package com.example.demo.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
class SecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .csrf(CsrfConfigurer<HttpSecurity>::disable)
            .authorizeHttpRequests {
                it
                    .requestMatchers("/", "/ping", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated()
            }
            .cors {
                it.configurationSource {
                    val cors = org.springframework.web.cors.CorsConfiguration()
                    cors.allowedOrigins = listOf("*")
                    cors.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    cors.allowedHeaders = listOf("*")
                    cors
                }
            }
            .oauth2ResourceServer { resourceServerCustomizer ->
                resourceServerCustomizer.jwt { jwtCustomizer ->
                    jwtCustomizer.jwtAuthenticationConverter { jwt: Jwt ->

                        val customClaims = jwt.getClaimAsStringList("roles")
                        val authorities = if (customClaims.isNullOrEmpty()) {
                            listOf(SimpleGrantedAuthority("ROLE_USER"))
                        } else {
                            customClaims.mapNotNull { role: String? -> SimpleGrantedAuthority(role) }
                        }

                        JwtAuthenticationToken(jwt, authorities)
                    }
                }
            }
            .build()

    }

}