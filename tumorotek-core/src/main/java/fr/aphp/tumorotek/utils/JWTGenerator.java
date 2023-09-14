/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de 
 * PARIS et SESAN
 * projet-tk@sesan.fr
 *
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français
 * et respectant les principes de diffusion des logiciels libres. Vous
 * pouvez utiliser, modifier et/ou redistribuer ce programme sous les
 * conditions de la licence CeCILL telle que diffusée par le CEA, le
 * CNRS et l'INRIA sur le site "http://www.cecill.info".
 * En contrepartie de l'accessibilité au code source et des droits de
 * copie, de modification et de redistribution accordés par cette
 * licence, il n'est offert aux utilisateurs qu'une garantie limitée.
 * Pour les mêmes raisons, seule une responsabilité restreinte pèse sur
 * l'auteur du programme, le titulaire des droits patrimoniaux et les
 * concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les
 * risques associés au chargement,  à l'utilisation,  à la modification
 * et/ou au  développement et à la reproduction du logiciel par
 * l'utilisateur étant donné sa spécificité de logiciel libre, qui peut
 * le rendre complexe à manipuler et qui le réserve donc à des
 * développeurs et des professionnels  avertis possédant  des
 * connaissances  informatiques approfondies.  Les utilisateurs sont
 * donc invités à charger  et  tester  l'adéquation  du logiciel à leurs
 * besoins dans des conditions permettant d'assurer la sécurité de leurs
 * systèmes et ou de leurs données et, plus généralement, à l'utiliser
 * et l'exploiter dans les mêmes conditions de sécurité.
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import fr.aphp.tumorotek.manager.administration.ConfigItemManager;
import fr.aphp.tumorotek.model.config.ConfigItem;

public class JWTGenerator
{
   public static final String ISSUER = "Tumorotek";

   public static final String KEY__TOKEN_CSRF = "tokenCSRF";
   
   public static final String CONFIG_KEY__JWT_SECURE_KEY = "jwtSecureKey";
   
   private ConfigItemManager configItemManager;
 
   public void setConfigItemManager(ConfigItemManager configItemManager){
      this.configItemManager = configItemManager;
   }

   public String generate(String audience, String login, int dureeVieEnMs, String tokenCSRF,
      Map<String, String> payloadClaims) throws JWTCreationException{

      Algorithm algorithm = Algorithm.HMAC256(retrieveSecureKey());
      if(payloadClaims == null){
         payloadClaims = new HashMap<String, String>();
      }
      if(tokenCSRF != null){
         payloadClaims.put(KEY__TOKEN_CSRF, tokenCSRF);
      }

      return JWT.create()
         .withIssuer(ISSUER)
         .withSubject(login)
         .withAudience(audience)
         .withExpiresAt(new Date(Calendar.getInstance().getTimeInMillis() + dureeVieEnMs))
         .withPayload(payloadClaims)
         .sign(algorithm);
   }

   private String retrieveSecureKey() {
      ConfigItem result = configItemManager.findByKey(CONFIG_KEY__JWT_SECURE_KEY);
      if(result == null) {
         ConfigItem configItem = new ConfigItem();
         configItem.setKey(CONFIG_KEY__JWT_SECURE_KEY);
         configItem.setValue(TokenGenerator.generateToken());
         configItemManager.createObjectManager(configItem);
         
         return configItem.getValue();
      }
      
      return result.getValue();
   }
}
