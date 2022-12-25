package projectManagement.utils;

import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class Token {
    private static Logger logger = LogManager.getLogger(Token.class.getName());

    private static String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";

    /**
     * createJWT is a function that create a jwt token, that use hashing and the secret key of the class.
     *
     * @param id        - String.valueOf(userid)
     * @param issuer    - "Project Management"
     * @param subject   - "login"
     * @param ttlMillis - Instant.now().toEpochMilli()
     * @return - string that will be the token for the client.
     */
    public static String createJWT(String id, String issuer, String subject, long ttlMillis) {
        logger.info("in Token -> createJWT to id" + id);
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);
        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    /**
     * decodeJWT getting an jwt token that should look like that: (but longer)
     * eyJhbGciOiJIUzI1NiJ9.eyJNzg0NX0.3L0VVZ213bH9jc
     *
     * @param jwt - the token we get from the header request.
     * @return -  Claims, that contain the id we need to extract.
     */
    public static Claims decodeJWT(String jwt) {
        logger.info("in Token -> decodeJWT: " + jwt);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                    .parseClaimsJws(jwt).getBody();
            return claims;
        } catch (MalformedJwtException e) {
            logger.error("in Token -> decodeJWT Wrong jwt token: " + jwt);
            return null;
        }
    }
}