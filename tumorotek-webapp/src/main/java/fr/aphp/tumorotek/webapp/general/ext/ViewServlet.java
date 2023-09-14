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
package fr.aphp.tumorotek.webapp.general.ext;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.webapp.general.ConnexionUtils;

/**
 * Servlet permettant d'afficher une fiche détaillée d'un objet
 * TK à partir d'une requête HTTP.
 * Date: 18/05/2013
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.2.2-diamic
 *
 */
public class ViewServlet extends HttpServlet
{

   private static final long serialVersionUID = -8614022656257785986L;

   @Override
   protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException{

      // System.out.println(Servlets.getDetail(req));

      doPost(req, resp);

   }

   /**
    * @since 2.2.2-diamic permet visualisation externe banque_id, prelcode
    */
   @Override
   protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException{
      try{

         ResourceRequest<Prelevement> resReq = null;
         if(req.getParameter("id") != null){
            final Integer prelID = new Integer(req.getParameter("id"));

            final Prelevement prel = ManagerLocator.getPrelevementManager().findByIdManager(prelID);

            if(prel != null){

               resReq = new ResourceRequest<>(Prelevement.class, prel.getBanque());

               // verifie que l'utilisateur a accès au prélèvement
               if(ManagerLocator.getUtilisateurManager().getAvailableBanquesManager(ConnexionUtils.getLoggedUtilisateur(), false)
                  .contains(prel.getBanque())){
                  resReq.addToTkObjs(prel);
               }else{
                  resp.sendError(HttpServletResponse.SC_FORBIDDEN);
               }
            }
         }else if(req.getParameter("pCode") != null && req.getParameter("bId") != null){ // rech par prel code et banque id
            final Banque bank = ManagerLocator.getBanqueManager().findByIdManager(new Integer(req.getParameter("bId")));
            // verifie que l'utilisateur a accès à la banque
            if(bank != null && ManagerLocator.getUtilisateurManager()
               .getAvailableBanquesManager(ConnexionUtils.getLoggedUtilisateur(), false).contains(bank)){

               resReq = new ResourceRequest<>(Prelevement.class, bank, ManagerLocator.getPrelevementManager()
                  .findByCodeOrNumLaboLikeWithBanqueManager(req.getParameter("pCode"), bank, false));

            }else{
               resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
         }else{
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
         }

         if(resReq != null){
            if(resReq.isEmpty()){
               resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }else{
               req.getSession().setAttribute("resourceRequest", resReq);
               resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/login/SelectBanque.zul"));
            }
         }else{
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
         }

      }catch(final TKException tke){
         throw new RuntimeException("ext.request.illegal");
      }
   }
}
