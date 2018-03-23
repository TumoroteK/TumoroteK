package fr.aphp.tumorotek.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

public class UserDetailsContextMapperImpl implements UserDetailsContextMapper, Serializable
{

   private static final long serialVersionUID = 3962976258168853954L;

   @Override
   public UserDetails mapUserFromContext(final DirContextOperations ctx, final String username,
      final Collection<? extends GrantedAuthority> authority){

      final List<GrantedAuthority> mappedAuthorities = new ArrayList<>();

      mappedAuthorities.add(new GrantedAuthority()
      {
         private static final long serialVersionUID = 4356967414267942910L;

         @Override
         public String getAuthority(){
            return "ROLE_USER";
         }

      });

      return new User(username, "", true, true, true, true, mappedAuthorities);
   }

   @Override
   public void mapUserToContext(final UserDetails arg0, final DirContextAdapter arg1){}
}
