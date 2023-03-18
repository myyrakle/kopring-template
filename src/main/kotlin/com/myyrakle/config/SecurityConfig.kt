import com.myyrakle.filter.JwtAuthenticationFilter
import com.myyrakle.provider.JwtProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {
    var jwtProvider: JwtProvider;

    constructor(jwtProvider: JwtProvider)
    {
        this.jwtProvider = jwtProvider;
    }


    @Bean
    fun encoder(): BCryptPasswordEncoder
    {
        return  BCryptPasswordEncoder ();
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain
    {
        return http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 비활성화
            .and()
            .formLogin().disable() //자동 form login 비활성화
            .httpBasic().disable()
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .and()
            .addFilterBefore(JwtAuthenticationFilter (jwtProvider), UsernamePasswordAuthenticationFilter::class.java).build()
    }
}