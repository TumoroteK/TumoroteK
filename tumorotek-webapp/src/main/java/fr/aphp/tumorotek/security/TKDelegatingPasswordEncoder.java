/**
 * Copyright ou © ou Copr. SESAN
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
package fr.aphp.tumorotek.security;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Initialement, les mots de passe stockés en base de données étaient encodés en MD5 sans sel. Pour améliorer la sécurité, il était nécessaire de passer 
 * par un encodage plus sûr (BCrypt). 
 * Pour simplifier la bascule entre les 2 modes et éviter de demander à tous les utilisateurs de modifier leurs mots 
 * de passe, la bascule va se faire progressivement lors du changement de mot de passe programmé (tous les 5 mois). 
 * Pour cela, utilisation du même principe que celui introduit par Spring 5 avec DelegatingpasswordEncoder. 
 * Cela facilitera l'éventuellement montée de version de Spring.
 * 
 * @since 2.3.1.0 (TK-489)
 * @author chuet
 *
 */
public class TKDelegatingPasswordEncoder implements PasswordEncoder
{
   private static final String ENCODER_ID_PREFIX = "{";
   private static final String ENCODER_ID_SUFFIX = "}";
   
   private static final String CURRENT_ENCODER_ID = "bcrypt";
   private static final String CURRENT_ENCODER_PREFIX = new StringBuilder(ENCODER_ID_PREFIX).append(CURRENT_ENCODER_ID).append(ENCODER_ID_SUFFIX).toString();
   
   //définition des 2 encoders utilisés par TK sachant que bCryptPasswordEncoder est celui qui est désormais utilisé pour l'encodage
   //md5PasswordEncoder ne sert plus qu'à gérer les mots de passe qui n'ont pas encore basculé en bcrypt. Cette bascule se fait lors
   //du changement de mot de passe imposé normalement tous les 5 mois.
   private Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
   private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
   
   @Override 
   public String encode(CharSequence rawPassword){
      return addEncoderPrefix(bCryptPasswordEncoder.encode(rawPassword));
   }

   @Override 
   public boolean matches(CharSequence rawPassword, String passwordFromDb){
      if(passwordFromDb.startsWith(CURRENT_ENCODER_PREFIX)) {
         return bCryptPasswordEncoder.matches(rawPassword, removeEncoderPrefix(passwordFromDb));
      }
      //sinon on est sur des mots de passe en MD5 (ancienne gestion) : ils ne sont pas préfixés.
      //et pour le MD5, la méthode "matches" s'appelle isPasswordValid.
      return md5PasswordEncoder.isPasswordValid(passwordFromDb, rawPassword.toString(), null);
   }

   //encoderPrefix vaut {xxx} avec xxx l'identifiant indiquant l'encodage utilisé
   private String addEncoderPrefix(String encodedPassord) {
      return new StringBuilder(CURRENT_ENCODER_PREFIX).append(encodedPassord).toString();
   }
   
   //encoderPrefix vaut {xxx} avec xxx l'identifiant indiquant l'encodage utilisé
   private String removeEncoderPrefix(String passwordFromDb) {
      return passwordFromDb.substring(CURRENT_ENCODER_PREFIX.length());
   }
}
