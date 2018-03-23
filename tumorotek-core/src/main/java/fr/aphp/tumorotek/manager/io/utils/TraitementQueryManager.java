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
package fr.aphp.tumorotek.manager.io.utils;

import java.util.Calendar;
import java.util.List;

import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.qualite.ConformiteType;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * @version 2.0.13
 * @author Mathieu BARTHELEMY
 *
 */
public interface TraitementQueryManager
{

   /**
    * Retourne les objets correspondant à un critère.
    * @param critere Critere à exécuter.
    * @param banks Banques dans lesquelles le critère doit être
    * respecter.
    * @param value Valeur du critere.
    * @return Liste d'objets correspondant au critère.
    */
   List<Object> findObjetByCritereManager(Critere critere, List<Banque> banks, Object value, String jdbcDialect);

   /**
    * Retourne les objets correspondant à un critère et
    * qui appartiennent aux banques spécifiées.
    * Methode non utilisée au profit de findObjetByCriteresWithBanquesManager
    * @param critere Critere à exécuter.
    * @param banques Liste de Banques.
    * @param value Valeur du critère.
    * @param idSearch detourne la methode pour rechercher des ids de parents à partir des ids
    * @since 2.0.10
    * @return Liste d'objets correspondant au critère
    */
   List<Object> findObjetByCritereWithBanquesManager(Critere critere, List<Banque> banques, Object value, boolean idSearch);

   List<Integer> findObjetByCriteresWithBanquesManager(List<Critere> critere, List<Banque> banques, List<Object> values);

   /**
    * Retourne les objets correspondant à un critère et
    * qui appartiennent aux banques spécifiées.
    * @param critere Critere à exécuter.
    * @param banques Liste de Banques.
    * @param value Valeur du critère.
    * @param searchDerive recherche de derives
    * @param idSearch detourne la methode pour rechercher des ids de parents à partir des ids
    * @since 2.0.10
    * @return Liste d'objets correspondant au critère.
    */
   List<Object> findObjetByCritereWithBanquesDeriveVersionManager(Critere critere, List<Banque> banques, Object value,
      boolean searchForDerives, boolean idSearch);

   List<Integer> findObjetByCriteresWithBanquesDeriveVersionManager(List<Critere> criteres, List<Banque> banques,
      List<Object> values, boolean searchForDerives);

   /**
    * Retourne les objets correspondant à un critère et
    * qui appartiennent aux banques spécifiées.
    * @param critere Critere à exécuter.
    * @param banques Liste de Banques.
    * @param value Valeur du critère.
    * @param cumulative si la recherche doit retourner les élements 
    * correspondant à la totalité des critères 
    * @return Liste d'objets correspondant au critère.
    * @version 2.0.13
    */
   List<Integer> findObjetByCritereInListWithBanquesManager(Critere critere, List<Banque> banques, List<Object> values,
      boolean cumulative);

   /**
    * Retourne les pérlèvements qui ont un certain nombre
    * d'échantillons.
    * @param operateur Opérateur de la requête.
    * @param nb Nombre d'échantillons
    * @param banques Liste de Banques.
    * @return Liste d'objets correspondant au critère.
    */
   List<Integer> findPrelevementsByNbEchantillonsWithBanquesManager(String operateur, Integer nb, List<Banque> banques);

   /**
    * Retourne les prélèvements fait à un certain age d'un patient.
    * @param operateur Opérateur de la requête.
    * @param age age du patient lors du prélèvement.
    * @param banques Liste de Banques.
    * @param dbms Mysql ou oracle car la requête implique une native query
    * @return Liste prelevementId.
    */
   List<Integer> findPrelevementsByAgePatientWithBanquesManager(String operateur, Integer age, List<Banque> banques, String dbms);

   /**
    * Retourne les ids des TKStockableObjects (échantillons ou dérivés) dont la 
    * température de stockage correspond la recherche.
    * @param Entite tkStockableObject
    * @param temp Float Température de stockage recherchée
    * @param op String opérateur décimal
    * @param banques List de banques
    * @param fetchIds récupère les ids si true.
    * @return Liste TKStockableObjects ou Ids
    * @since 2.0.13
    */
   List<? extends Object> findTKStockableObjectsByTempStockWithBanquesManager(Entite ent, Object temp, String op,
      List<Banque> banques, boolean fetchIds);

   List<Integer> findObjetByCritereOnCodesWithBanquesManager(Critere critere, List<Banque> banques, List<String> codes,
      List<String> libelles, String value, boolean isMorpho);

   List<Integer> findObjetByCritereOnCodesWithBanquesDerivesVersionManager(List<Banque> banques, List<String> codes,
      List<String> libelles, String value, boolean isMorpho, Entite echanEntite);

   /**
    * Recherche tous les échantillons pour la recherche BioCap : 
    * échantillons dont le prlvt est fait sur des mineurs, entre
    * 2 dates et pour une liste de services.
    * @param dbms 
    * @param banques Liste des banques des échantillons.
    * @param services Liste des services préleveurs.
    * @param dateInf Date inf de prélèvement.
    * @param dateSup Date sup de prélèvement.
    * @param age Age max des patients.
    * @param statut de stockage des prélèvements
    * @return Liste d'ids échantillons.
    * @version 2.0.13
    */
   List<Integer> findEchantillonsByRequeteBiocapManager(String dbms, List<Banque> banques, List<Service> services,
      Calendar dateInf, Calendar dateSup, Integer age, ObjetStatut statut);

   /**
    * Retourne les prélèvements pour un médecin. Recherche dans les medecins 
    * referents de la maladie et du patient.
    * @param medecin Collaborateur
    * @param banques Liste de Banques.
    * @return Liste d'objets correspondant au critère.
    */
   List<Integer> findPrelevementsByMedecinsManager(Collaborateur collab, List<Banque> banques);

   /**
    * Retourne les ids des objets qui référence au moins une non conformite 
    * caractérisé par un type, une plateforme et contenant dans son nom les 
    * paramètres respectifs.
    * Filtre les résultats obtenus par une liste de banque d'appartenance
    * @param nom
    * @param cType
    * @param pf
    * @param banques 
    * @return liste objet ids
    */
   List<Integer> findObjetIdsFromNonConformiteNomManager(String nom, ConformiteType cType, Plateforme pf, List<Banque> banks);

   /**
    * Retourne les ids des objets pour lesquels un fichier a été chargé pour 
    * le champ passé en paramètre, qui peux être un ChampEntite Echantillon (CrAnapath) 
    * ou un champ annotation pour toutes entites.
    * Le paramètre target permet d'obtenir la correspondance si l'entité recherchée 
    * n'est pas celle sur laquelle la requête s'applique.
    * @param fileChp Champ
    * @param target Entite
    * @param banques liste de banques
    * @param true recherche file reference vides
    * @return liste objets ids
    */
   List<Integer> findFileUploadedManager(Champ fileChp, Entite target, List<Banque> banques, boolean empty);

}
