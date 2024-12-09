package BE.Duch_Ez.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long validityInMilliseconds = 3600000; // 1시간 유효

    // 생성자에서 시크릿 키 초기화
    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // JWT 생성
    public String createToken(String username, Long userId) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId); // 사용자 ID 추가
        System.out.println("id : " + userId);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }




    // JWT에서 사용자 이름 추출
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // JWT 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 토큰이 유효하지 않음
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Object idObject = claims.get("id"); // "id" 값을 가져옵니다.
        System.out.println("ID 타입: " + idObject.getClass().getName());

        if (idObject instanceof Number) {
            // "id"가 숫자라면 그대로 반환
            return ((Number) idObject).longValue();
        } else if (idObject instanceof String) {
            try {
                // "id"가 문자열이라면 숫자로 변환 시도
                return Long.valueOf((String) idObject);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("토큰의 'id'를 숫자로 변환할 수 없습니다: " + idObject, e);
            }
        } else {
            throw new IllegalArgumentException("토큰에서 'id' 필드를 찾을 수 없습니다. 데이터: " + idObject);
        }
    }





}
