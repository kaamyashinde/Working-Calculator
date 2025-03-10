package ntnu.edu.stud.calculator.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    //Generate a random key
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    //set the expiration to five minutes
    private final long jwtExpirationInMs = 5 * 60 * 1000;

    //Generate token with subject (username)
    public String generateToken(String username){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key)
            .compact();
    }

    //validate token and return username
    public String validateTokenAndGetUsername(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e){
            //token is invalid or expired
            return null;
        }
    }
}
