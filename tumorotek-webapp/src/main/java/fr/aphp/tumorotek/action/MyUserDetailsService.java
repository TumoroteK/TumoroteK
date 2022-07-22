/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.action;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Classe surchargeant l'authentification de Spring Security afin de
 * vérifier la validité de l'utilisateur dans notre BDD.
 *
 * @author pierre Ventadour
 * Créée le 07/06/2010.
 * @version 2.2.0
 */
public class MyUserDetailsService implements UserDetailsService
{

   private final Log log = LogFactory.getLog(MyUserDetailsService.class);

   private DataSource dataSource;

   @Override
   public UserDetails loadUserByUsername(final String username){
      final String sql = "select * from UTILISATEUR where login = ? and archive = 0";
      final JdbcTemplate jdbcTemplate = new JdbcTemplate();
      jdbcTemplate.setDataSource(getDataSource());
      User user;
      try{
         user = jdbcTemplate.queryForObject(sql, new String[] {username}, new UserMapper());
      }catch(final DataAccessException e){
         log.info("La tentative de connection " + username + " a échoué " + "car les paramètres de connection sont invalides");
         throw new UsernameNotFoundException("authentication error");
      }
      MDC.remove("client");
      MDC.put("client", username);
      return user;
   }

   private Collection<? extends GrantedAuthority> getAuthorities(final boolean isAdmin){
      final List<GrantedAuthority> authList = new ArrayList<>(2);
      authList.add(new SimpleGrantedAuthority("ROLE_USER"));
      if(isAdmin){
         authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
      }
      return authList;
   }

   public void setDataSource(final DataSource dSource){
      this.dataSource = dSource;
   }

   public DataSource getDataSource(){
      return dataSource;
   }

   private class UserMapper implements RowMapper<User>
   {
      @Override
      public User mapRow(final ResultSet rs, final int arg1) throws SQLException{
         return new User(rs.getString("login"), rs.getString("password"), true, true, true, true, getAuthorities(false));
      }
   }

   /*public static String getEncodedPassword(final String key){
      final PasswordEncoder encoder = new Md5PasswordEncoder();
      final String pwd = encoder.encodePassword(key, null);
      return pwd;
   }*/
}
