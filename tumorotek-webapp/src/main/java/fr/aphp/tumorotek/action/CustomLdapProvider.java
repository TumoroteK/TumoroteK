/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
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
package fr.aphp.tumorotek.action;

import static fr.aphp.tumorotek.param.TkParam.LDAP_AUTHENTICATION;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;

/**
 * Provider d'authentification LDAP
 * @author GCH
 */
public class CustomLdapProvider extends LdapAuthenticationProvider
{

   private final Boolean ldapAuthActivated;

   /**
    * Exception indiquant que l'authentification est désactivée dans le paramétrage de l'application
    */
   private class LdapAuhtenticationDeactivatedException extends AuthenticationException
   {

      private static final long serialVersionUID = 5450668710171265295L;

      public LdapAuhtenticationDeactivatedException(){
         super("Authentification LDAP désactivée");
      }

   }

   /** Constructeur */
   public CustomLdapProvider(final LdapAuthenticator authenticator){
      super(authenticator);
      this.ldapAuthActivated = Boolean.valueOf(LDAP_AUTHENTICATION.getValue());
   }

   /*
    * (non-Javadoc)
    * @see org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
    */
   @Override
   public Authentication authenticate(final Authentication authentication) throws AuthenticationException{

      if(!ldapAuthActivated){
         throw new LdapAuhtenticationDeactivatedException();
      }

      return super.authenticate(authentication);

   }

}
