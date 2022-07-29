package fr.aphp.tumorotek.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

public class JWTGenerator
{
   public static final String ISSUER_VALUE = "Tumorotek";

   public static final String TOKEN_CSRF_KEY = "tokenCSRF";

   public static String generate(String audience, String login, int dureeVieEnMs, String tokenCSRF,
      Map<String, String> payloadClaims) throws JWTCreationException{

      Algorithm algorithm = Algorithm.HMAC256("secret");//TEMP !
      if(payloadClaims == null){
         payloadClaims = new HashMap<String, String>();
      }
      if(tokenCSRF != null){
         payloadClaims.put(TOKEN_CSRF_KEY, tokenCSRF);
      }

      return JWT.create().withIssuer(ISSUER_VALUE).withSubject(login).withAudience(audience)
         .withExpiresAt(new Date(Calendar.getInstance().getTimeInMillis() + dureeVieEnMs)).withPayload(payloadClaims)
         .sign(algorithm);
   }
}
