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
package fr.aphp.tumorotek.manager.stats;

import java.util.Date;

import fr.aphp.tumorotek.model.stats.GraphesModele;

/**
 * date: 19/12/2013
 * 
 * @author Marc DESCHAMPS
 * @version 2.0.10
 *
 */
public interface GraphesModeleManager {


	/**
	 * Calcule la répartition des patients par plateforme en fonction de dates passées en param, 
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */	
	GraphesModele platformeViewByPatientManager(Date date_debut, Date date_fin, boolean isOracle);
	
	/**
	 * Calcule la répartition des prélèvements par plateforme en fonction de dates passées en param, 
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele platformeViewByPrelevementManager(Date date_debut, Date date_fin, boolean isOracle);

	/**
	 * Calcule la répartition des échantillons par plateforme en fonction de dates passées en param, 
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele platformeViewByEchantillonManager(Date date_debut, Date date_fin, boolean isOracle);
	
	/**
	 * Calcule la répartition des Derive par plateforme en fonction de dates passées en param, 
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele platformeViewByDeriveManager(Date date_debut, Date date_fin, boolean isOracle);

	/**
	 * Calcule la répartition des Cession par plateforme en fonction de dates passées en param, 
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele platformeViewByCessionManager(Date date_debut, Date date_fin, boolean isOracle);

	/**
	 * Calcule la répartition des Patients par collection d'une plateforme passée en paramêtre, 
	 * en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param String nom de la plateforme
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele collectionViewByPatientManager(Date date_debut, Date date_fin, String pfNom, boolean isOracle);

	/**
	 * Calcule la répartition des Prelevements par collection d'une plateforme passée en paramêtre,
	 * en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param String nom de la plateforme
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele collectionViewByPrelevementManager(Date date_debut, Date date_fin, String pfNom, boolean isOracle);

	/**
	 * Calcule la répartition des Echantillons par collection d'une plateforme passée en paramêtre,
	 * en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param String nom de la plateforme
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele collectionViewByEchantillonManager(Date date_debut, Date date_fin, String pfNom, boolean isOracle);

	/**
	 * Calcule la répartition des Derives par collection d'une plateforme passée en paramêtre,
	 * en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param String nom de la plateforme
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele collectionViewByDeriveManager(Date date_debut, Date date_fin, String pfNom, boolean isOracle);

	/**
	 * Calcule la répartition des Cessions par collection d'une plateforme passée en paramêtre,
	 * en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param String nom de la plateforme
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele collectionViewByCessionManager(Date date_debut, Date date_fin, String pfNom, boolean isOracle);

	/**
	 * Calcule la répartition des Types de Prelevements en fonction d'une collection et d'une plateforme
	 * passées en paramêtre, en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param String nom de la banque
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele prelevementTypeByCollectionManager(Date date_debut, Date date_fin, String banqueNom, String pfNom, boolean isOracle);
	
	/**
	 * Calcule la répartition des Etablisements effetuant les Prelevements en fonction d'une collection et d'une plateforme
	 * passées en paramêtre, en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param String nom de la banque
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele prelevementByEtablissementByCollectionManager(Date date_debut, Date date_fin, 
			String banqueNom, String pfNom, boolean isOracle);

	/**
	 * Calcule la répartition des consentement liés aux Prelevements en fonction d'une collection et d'une plateforme
	 * passées en paramêtre, en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param String nom de la banque
	 * @param String nom de la pf
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele prelevementByConsentementByCollectionManager(Date date_debut, Date date_fin, 
			String banqueNom, String pfNom, boolean isOracle);

	/**
	 * Calcule la répartition des Types d'Echantillons en fonction d'une collection et d'une plateforme
	 * passées en paramêtre, en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param String nom de la banque
	 * @param String nom de la pf
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele echantillonTypeByCollectionManager(Date date_debut, Date date_fin, String banqueNom, 
				String pfNom, boolean isOracle);
	
	/**
	 * Calcule la répartition des Codes Diagnostic(CIM10) liés aux Echantillons en fonction d'une collection et d'une plateforme
	 * passées en paramêtre, en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param String nom de la banque
	 * @param String nom de la pf
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele echantillonsCIM10ByCollectionManager(Date date_debut, Date date_fin, String banqueNom, String pfNom, 
			boolean isOracle);
	
	/**
	 * Calcule la répartition des Codes ADICAP liés aux Echantillons en fonction d'une collection et d'une plateforme
	 * passées en paramêtre, en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param String nom de la banque
	 * @param String nom de la pf
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele echantillonsADICAPByCollectionManager(Date date_debut, Date date_fin, String banqueNom, String pfNom, 
			boolean isOracle);


	/**
	 * Calcule la répartition des Types de Cessions en fonction d'une collection et d'une plateforme
	 * passées en paramêtre, en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param String nom de la banque
	 * @param String nom de la pf
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele cessionTypeByCollectionManager(Date date_debut, Date date_fin, String banqueNom, String pfNom, boolean isOracle);
	
	/**
	 * Calcule la répartition des Types de Derives en fonction d'une collection et d'une plateforme
	 * passées en paramêtre, en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
 	 * @param Date de debut de recherche
	 * @param Date de fin de recherche
	 * @param String nom de la banque
	 * @param String nom de la pf
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele deriveTypeByCollectionManager(Date date_debut, Date date_fin, String banqueNo, String pfNom, boolean isOracle);

	/**
	 * Méthode d'appel à la base de données, par le biais d'une procedure stockée,
	 * avec parametre (dates et banque)
	 * @param procedure
	 * @param date_debut (date large si non selectionée)
	 * @param date_fin (date large si non selectionée)
	 * @param parma 1 (pf or banque or null)
	 * @param param 2 (pf or null)
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele callOnDBManager(String procName, Date date_debut, Date date_fin, String param1, String param2, boolean isOracle);

	/**
	 * Calcule la répartition des échantillons cédés par collections pour la plateforme
	 * passées en paramêtre, en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
	 * @param date_debut
	 * @param date_fin
	 * @param pfNom
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele collectionViewByEchansCedesManager(Date date_debut,
			Date date_fin, String pfNom, boolean isOracle);
	
	/**
	 * Calcule la répartition des échantillons cédés par collections pour la plateforme
	 * passées en paramêtre, en fonction de dates passées en param
	 * pour dessiner le graphe correspondant du tableau de bord.
	 * @param date_debut
	 * @param date_fin
	 * @param pfNom
	 * @param boolean isOracle true si DBMS ORACLE
	 * @return grapheModele
	 */
	GraphesModele collectionViewByDerivesCedesManager(Date date_debut,
			Date date_fin, String pfNom, boolean isOracle);
	
}
