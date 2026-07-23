package com.nguyenhien.api_gateway.security;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.nguyenhien.api_gateway.common.exceptions.ExpiredTokenException;
import com.nguyenhien.api_gateway.common.exceptions.InvalidTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidatorImpl implements JwtValidator {
    private final JwtProperties jwtProperties;

    @Override
    public Claims validate(String token) {

        try {

            return Jwts.parser()
                    .verifyWith(getSigningKey())

                    .requireIssuer(jwtProperties.getIssuer())

                    .build()

                    .parseSignedClaims(token)

                    .getPayload();

        }

        catch (ExpiredJwtException ex) {

            throw new ExpiredTokenException();

        }

        catch (MalformedJwtException ex) {

            throw new InvalidTokenException();

        }

        catch (JwtException ex) {

            throw new InvalidTokenException();

        }

    }

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(

                Decoders.BASE64.decode(

                        jwtProperties.getSecretKey()

                )

        );

    }
}
