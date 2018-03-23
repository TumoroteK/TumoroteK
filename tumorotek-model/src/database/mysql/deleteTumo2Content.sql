delete from VERSION;
delete from FANTOME;
delete from OPERATION;
delete from MESSAGE;

delete from TRANSCODE_UTILISATEUR;
delete from CODE_UTILISATEUR;
delete from CODE_SELECT;
delete from CODE_DOSSIER where dossier_parent_id is not null;
delete from CODE_DOSSIER;
delete from CODE_ASSIGNE;

delete from OBJET_NON_CONFORME;
delete from RESERVATION;

delete from RETOUR;

delete from AFFECTATION_IMPRIMANTE;
delete from NUMEROTATION;
delete from IMPRIMANTE;

delete from RECHERCHE_BANQUE;
delete from RECHERCHE;
delete from GROUPEMENT;
delete from CRITERE;
delete from REQUETE;
delete from RESULTAT;
delete from AFFICHAGE;

delete from IMPORT_HISTORIQUE;
delete from IMPORT_TEMPLATE_ENTITE;
delete from IMPORT_COLONNE;
delete from IMPORT_TEMPLATE;
delete from IMPORTATION;

delete from CHAMP_IMPRIME;
delete from CHAMP_LIGNE_ETIQUETTE;
delete from CHAMP_ENTITE_BLOC;
delete from BLOC_IMPRESSION_TEMPLATE;
delete from BLOC_IMPRESSION;
delete from COMBINAISON;
delete from CHAMP;
delete from LIGNE_ETIQUETTE;
delete from MODELE;

delete from TABLE_ANNOTATION_TEMPLATE;
delete from TEMPLATE;

delete from ANNOTATION_VALEUR;
delete from ANNOTATION_DEFAUT;
delete from ITEM;
delete from CHAMP_ANNOTATION;
delete from TABLE_ANNOTATION_BANQUE;
delete from TABLE_ANNOTATION;

delete from INDICATEUR_BANQUE;
delete from INDICATEUR_PLATEFORME;
delete from INDICATEUR_REQUETE;
delete from INDICATEUR_SQL;
delete from INDICATEUR;

delete from CEDER_OBJET;
delete from CESSION;
delete from CONTRAT;

delete from PROD_DERIVE;
delete from TRANSFORMATION;


delete from FICHIER;
delete from ECHANTILLON;

delete from INCIDENT;
delete from CONTENEUR_BANQUE;
delete from CONTENEUR_PLATEFORME;
delete from EMPLACEMENT;
delete from TERMINALE;
delete from ENCEINTE_BANQUE;
update ENCEINTE set enceinte_pere_id=null;
delete from ENCEINTE;
delete from CONTENEUR;

delete from LABO_INTER;
delete from PRELEVEMENT_RISQUE;
delete from PRELEVEMENT;

delete from MALADIE_MEDECIN;
delete from MALADIE;

delete from PATIENT_MEDECIN;
delete from PATIENT_LIEN;
delete from PATIENT;

delete from PATIENT_SIP;

delete from PROFIL_UTILISATEUR;
delete from COULEUR_ENTITE_TYPE;
delete from BANQUE_CATALOGUE;
delete from BANQUE_TABLE_CODAGE;
delete from BANQUE;

delete from CONDIT_MILIEU where plateforme_id > 1;
delete from CONDIT_TYPE where plateforme_id > 1;
delete from CONSENT_TYPE where plateforme_id > 1;
delete from CONTENEUR_TYPE where plateforme_id > 1;
delete from CESSION_EXAMEN where plateforme_id > 1;
delete from DESTRUCTION_MOTIF where plateforme_id > 1;
delete from ECHANTILLON_TYPE where plateforme_id > 1;
delete from ECHAN_QUALITE where plateforme_id > 1;
delete from ENCEINTE_TYPE where plateforme_id > 1;
delete from ENCEINTE_TYPE where plateforme_id > 1;
delete from MODE_PREPA where plateforme_id > 1;
delete from MODE_PREPA_DERIVE where plateforme_id > 1;
delete from NATURE where plateforme_id > 1;
delete from NON_CONFORMITE where plateforme_id > 1;
delete from PRELEVEMENT_TYPE where plateforme_id > 1;
delete from PROD_QUALITE where plateforme_id > 1;
delete from PROD_TYPE where plateforme_id > 1;
delete from PROTOCOLE_TYPE where plateforme_id > 1;
delete from RISQUE where plateforme_id > 1;

delete from PLATEFORME_ADMINISTRATEUR where utilisateur_id > 1;
delete from PLATEFORME_ADMINISTRATEUR where plateforme_id > 1;
delete from PLATEFORME where plateforme_id > 1;
update PLATEFORME set collaborateur_id=null;

delete from UTILISATEUR where utilisateur_id > 1;
update UTILISATEUR set login='ADMIN_TUMO', password='tk4[teAm]' where utilisateur_id=1;
update UTILISATEUR set collaborateur_id=null;

delete from COLLABORATEUR_COORDONNEE;
delete from SERVICE_COLLABORATEUR;
delete from COLLABORATEUR;
delete from SERVICE;
delete from ETABLISSEMENT;
delete from TRANSPORTEUR;
delete from COORDONNEE;




