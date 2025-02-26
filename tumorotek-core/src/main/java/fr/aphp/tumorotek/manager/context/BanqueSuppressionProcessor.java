package fr.aphp.tumorotek.manager.context;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public interface BanqueSuppressionProcessor
{

   /**
    * supprime la banque : tous les éléments associés en base ET le répertoire créé sur le file system
    * @param banque
    * @param comments
    * @param user
    * @param basedir
    * @param force
    */
   void removeObjectAndFileSystem(Banque banque, final String comments, final Utilisateur user, final String basedir,
      final boolean force);
}
