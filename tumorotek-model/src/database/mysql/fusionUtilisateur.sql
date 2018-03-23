delimiter &&
DROP PROCEDURE IF EXISTS fusionUtilisateur&&
CREATE PROCEDURE fusionUtilisateur(IN userId_1 INT, IN userId_2 INT)
BEGIN
	IF userId_1 is not null and userId_2 is not null THEN
		update AFFECTATION_IMPRIMANTE set UTILISATEUR_ID=userId_1 WHERE UTILISATEUR_ID = userId_2;
		update AFFICHAGE set CREATEUR_ID = userId_1 WHERE CREATEUR_ID = userId_2;
		update CODE_DOSSIER set UTILISATEUR_ID = userId_1 WHERE UTILISATEUR_ID = userId_2;
		update CODE_SELECT set UTILISATEUR_ID = userId_1 WHERE UTILISATEUR_ID = userId_2;
		update CODE_UTILISATEUR set UTILISATEUR_ID = userId_1 WHERE UTILISATEUR_ID = userId_2;
		update IMPORT_HISTORIQUE set UTILISATEUR_ID = userId_1 WHERE UTILISATEUR_ID = userId_2;
		update MESSAGE set DESTINATAIRE_ID = userId_1 WHERE DESTINATAIRE_ID = userId_2;
		update MESSAGE set EXPEDITEUR_ID = userId_1 WHERE EXPEDITEUR_ID = userId_2;
		update OPERATION set UTILISATEUR_ID = userId_1 WHERE UTILISATEUR_ID = userId_2;
		update PLATEFORME_ADMINISTRATEUR set UTILISATEUR_ID = userId_1 WHERE UTILISATEUR_ID = userId_2;
		update PROFIL_UTILISATEUR set UTILISATEUR_ID = userId_1 WHERE UTILISATEUR_ID = userId_2;
		update RECHERCHE set CREATEUR_ID = userId_1 WHERE CREATEUR_ID = userId_2;
		update REQUETE set CREATEUR_ID = userId_1 WHERE CREATEUR_ID = userId_2;
		update RESERVATION set UTILISATEUR_ID = userId_1 WHERE UTILISATEUR_ID = userId_2;	


		delete from OPERATION where UTILISATEUR_ID = userId_2;
		delete from UTILISATEUR where UTILISATEUR_ID = userId_2;
		select concat('utilisateur id=', userId_2, ', a été fusionné avec utilisateur id=' , userId_1);
	END IF;

	

END&&
