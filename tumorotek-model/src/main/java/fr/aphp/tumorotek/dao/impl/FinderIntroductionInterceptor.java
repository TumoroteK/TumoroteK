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
package fr.aphp.tumorotek.dao.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;

import fr.aphp.tumorotek.dao.FinderExecutor;

/**
 *
 * Implémente le FinderIntroductionInterceptor.
 *
 * @author Pierre Ventadour
 * @version 09/09/2009
 *
 */
public class FinderIntroductionInterceptor implements IntroductionInterceptor
{

   //private static Logger logger = Logger.getRootLogger();

   /**
    * @param methodInvocation est la méthode à invoquer.
    * @return retourne la valeur methodInvocation.proceed().
    * @throws Throwable retourne une exception.
    */
   @Override
   public Object invoke(final MethodInvocation methodInvocation) throws Throwable{

      final FinderExecutor genericDao = (FinderExecutor) methodInvocation.getThis();

      final String methodName = methodInvocation.getMethod().getName();
      if(methodName.startsWith("find") && !methodName.equals("findById") && !methodName.equals("findAll")){
         final Object[] arguments = methodInvocation.getArguments();
         return genericDao.executeFinder(methodInvocation.getMethod(), arguments);
      }
      return methodInvocation.proceed();
   }

   /**
    * @param intf est l'interface à implémenter.
    * @return true ou false.
    */
   @Override

   public boolean implementsInterface(final Class<?> intf){
      //logger.debug("Intercepted class type: " + intf.getCanonicalName());
      //logger.debug("intf.isInterface() = " + intf.isInterface());
      //logger.debug("FinderExecutor.class.isAssignableFrom(intf) = 
      //" + FinderExecutor.class.isAssignableFrom(intf));
      return intf.isInterface() && FinderExecutor.class.isAssignableFrom(intf);

   }
}
