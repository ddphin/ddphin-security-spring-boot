package com.ddphin.security.configuration;

import com.ddphin.security.configurer.AJwtLoginConfigurer;
import com.ddphin.security.configurer.ARestLoginConfigurer;
import com.ddphin.security.configurer.AWebSecurityProperties;
import com.ddphin.security.decide.APermissionBasedVoter;
import com.ddphin.security.decide.APermissionFilterInvocationSecurityMetadataSource;
import com.ddphin.security.endpoint.service.AuthenticationService;
import com.ddphin.security.filter.AOptionsRequestFilter;
import com.ddphin.security.handler.AJwtClearLogoutHandler;
import com.ddphin.security.jwt.AJWTService;
import com.ddphin.security.provider.AIdentityAuthenticationProvider;
import com.ddphin.security.provider.AJwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

/**
 * AWebSecurityConfig
 *
 * @Date 2019/7/21 下午2:57
 * @Author ddphin
 */
@EnableWebSecurity
@EnableConfigurationProperties({AWebSecurityProperties.class})
public class AWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private AuthenticationService authenticationService;
    private AJWTService aJWTService;
    private AWebSecurityProperties properties;

    @Autowired
    public AWebSecurityConfigurer(
            AuthenticationService authenticationService,
            AJWTService aJWTService,
            AWebSecurityProperties properties) {
        this.authenticationService = authenticationService;
        this.aJWTService = aJWTService;
        this.properties = properties;
    }

    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .withObjectPostProcessor(this.permissionPostProcessor())
                .and()
                .csrf().disable()
                .formLogin().disable()
                .cors()
                .and()
                .apply(new ARestLoginConfigurer<>(aJWTService, properties.getLogin()))
                .and()
                .apply(new AJwtLoginConfigurer<>(aJWTService, properties.getPermissive()))
                .and()
                .addFilterBefore(new AOptionsRequestFilter(), CorsFilter.class)
                .logout().logoutUrl(properties.getLogout())
                .addLogoutHandler(new AJwtClearLogoutHandler(aJWTService))
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    private ObjectPostProcessor permissionPostProcessor() {
        return new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                AccessDecisionManager accessDecisionManager = new UnanimousBased(
                        Collections.singletonList(new APermissionBasedVoter()));

                APermissionFilterInvocationSecurityMetadataSource securityMetadataSource =
                        new APermissionFilterInvocationSecurityMetadataSource(
                                fsi.getSecurityMetadataSource(),
                                authenticationService);

                fsi.setAccessDecisionManager(accessDecisionManager);
                fsi.setSecurityMetadataSource(securityMetadataSource);
                return fsi;
            }
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(new AIdentityAuthenticationProvider(authenticationService))
                .authenticationProvider(new AJwtAuthenticationProvider(aJWTService));
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    protected CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTION"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }
}
