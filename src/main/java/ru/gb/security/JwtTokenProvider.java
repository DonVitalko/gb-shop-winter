//package ru.gb.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import java.util.Date;
//
//@Component
//@RequiredArgsConstructor
//public class JwtTokenProvider {
//
//
//    @Value("${jwt.token.expired}")
//    private long expired;
//
//    public String createToken(String username,  String secret) {
//        return generateToken(username, expired, secret);
//    }
//
//
//    private String generateToken(String username,  Long expire, String secret) {
//        Claims claims = Jwts.claims().setSubject(username);
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(new Date().getTime() + expire))
//                .signWith(SignatureAlgorithm.HS256, secret)
//                .compact();
//    }
//
//
//
//    public boolean validateToken(String token, String secret) {
//        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
//
//        if (claimsJws.getBody().getExpiration().before(new Date())) {
//            return false;
//        }
//        return true;
//    }
//
//
//}