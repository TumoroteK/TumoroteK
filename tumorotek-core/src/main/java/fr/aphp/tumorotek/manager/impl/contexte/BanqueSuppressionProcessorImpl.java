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
package fr.aphp.tumorotek.manager.impl.contexte;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.context.BanqueSuppressionProcessor;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 * La suppression d'une banque comprend des actions au niveau de la base de données et d'autres
 * au niveau du file system.
 * Le file sytem ne doit pas être supprimé si les actions au niveau de la base ont rencontré un problème
 * Or avec la mise en oeuvre de la gestion des transactions dans TK, il n'est pas possible de faire un commit
 * avant de faire la suppression du file system (le developpeur n'a pas la main
 * sur l'appel du commit qui est toujours fait en sortie des méthodes du manager et toutes les méthodes
 * des managerImpl doivent avoir une transaction - cf dans applicationContextAOP.xml)
 * Pour simplifier, passage par cette classe qui n'est pas directement rattachée à une transaction JDBC
 * @author chuet
 *
 */
public class BanqueSuppressionProcessorImpl implements BanqueSuppressionProcessor
{
   private final Log log = LogFactory.getLog(BanqueSuppressionProcessorImpl.class);
      
   BanqueManager banqueManager;

   public void setBanqueManager(BanqueManager banqueManager){
      this.banqueManager = banqueManager;
   }
  
   @Override
   public void removeObjectAndFileSystem(Banque banque, final String comments, final Utilisateur user, final String basedir,
      final boolean force){
      banqueManager.removeObjectInBddOnly(banque, comments, user, basedir, force);
      //si la suppression en base s'est bien passée, on supprime l'arborescence. Sinon, elle reste en place
      //si la suppression de l'arborescence échoue, on trace juste dans les logs. 
      //l'id collection ne sera pas réattribué donc pas de risque de conflit ... 
      deleteBanqueArborescence(basedir, banque);
   }

   private void deleteBanqueArborescence(String basedir, Banque banque) {
      final String path = Utils.writeAnnoFilePath(basedir, banque, null, null);
      if(Utils.deleteDirectory(new File(path))){
         log.info("Filesystem complet supprimé pour la banque " + banque.toString());
      }else{
         log.error("Erreur dans la suppression du systeme de fichier anapath pour la banque " + banque.toString());
      }
   }
 
}
