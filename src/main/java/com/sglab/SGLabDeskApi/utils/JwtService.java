package com.sglab.SGLabDeskApi.utils;

import com.sglab.SGLabDeskApi.confirmation_token.ConfirmationToken;
import com.sglab.SGLabDeskApi.confirmation_token.IConfirmationTokenRepository;
import com.sglab.SGLabDeskApi.users.IUsersRepository;
import com.sglab.SGLabDeskApi.users.UsersEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class JwtService {

    private static final String SECRET_KEY = "86479b2803deb27aa498b3ed30c023dc8d26c467c3b83f1d9decedba773a136f";
    private final IConfirmationTokenRepository confirmationTokenRepo;
    private final IUsersRepository usersRepository;

    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = exractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .subject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 24)))
                .signWith(getSingInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        boolean isTokenExpired = extractExpiration(token).before(new Date());

        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepo.findByToken(token);

        if(confirmationToken.isPresent() && !isTokenExpired){
            Optional<UsersEntity> user = usersRepository.findByUsername(extractUsername(token));

            if(user.isPresent() && !Objects.equals(user.get().getId(), confirmationToken.get().getUserEntity().getId()))
                isTokenExpired = true;

            if(confirmationToken.get().getExpiresAt() != null && !isTokenExpired)
                isTokenExpired = true;
        }

        return isTokenExpired;
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims exractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSingInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSingInKey() {
        byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }
}
