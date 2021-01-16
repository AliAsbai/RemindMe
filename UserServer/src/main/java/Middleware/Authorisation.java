package Middleware;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;


public class Authorisation {

    private final static String SECRET_KEY = "M4MltB5JYcenxR2UL5OYGwQtNLrUcESoR/i+dpGTFsk=";

    public static boolean validateToken(String token){
        byte[] secret = Base64.getDecoder().decode(SECRET_KEY);
        try {
            Jws<Claims> result = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret))
                    .build()
                    .parseClaimsJws(token);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String generateJWT(String id) {
        Instant now = Instant.now();
        byte[] secret = Base64.getDecoder().decode(SECRET_KEY);

        return Jwts.builder()
                .claim("UserID", id)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();
    }
}
