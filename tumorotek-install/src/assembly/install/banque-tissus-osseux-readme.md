#### Evolution banque d'organes de Cochin
##### Modification de la table CONTEXTE
    ALTER TABLE CONTEXTE ADD LIBELLE VARCHAR(50) NULL;
    
    UPDATE CONTEXTE SET NOM = 'DEFAUT', LIBELLE = 'Anatomopathologie' WHERE CONTEXTE_ID = 1;
    UPDATE CONTEXTE SET NOM = 'SEROLOGIE', LIBELLE = 'Sérologie' WHERE CONTEXTE_ID = 2;
    
##### Ajout colonne DATE_PEREMPTION pour les prélèvements
    ALTER TABLE PRELEVEMENT ADD DATE_PEREMPTION datetime DEFAULT NULL;
    
    ALTER TABLE CHAMP_ENTITE modify CHAMP_ENTITE_ID int(10) NOT NULL auto_increment;
    INSERT INTO CHAMP_ENTITE(NOM, DATA_TYPE_ID, IS_NULL, IS_UNIQUE, VALEUR_DEFAUT, ENTITE_ID, CAN_IMPORT, QUERY_CHAMP_ID) VALUES('DatePeremption', 3, 1, 0, NULL, 2, 1, NULL);
    ALTER TABLE CHAMP_ENTITE modify CHAMP_ENTITE_ID int(10) NOT NULL;-- enleve l'auto_increment
    
##### Ajout du type de consentement GREFFON dans la table CONSENT_TYPE
    ALTER TABLE CONSENT_TYPE modify CONSENT_TYPE_ID int(10) NOT NULL auto_increment;
    INSERT INTO CONSENT_TYPE (TYPE, PLATEFORME_ID) VALUES ('GREFFON', 1);
    ALTER TABLE CONSENT_TYPE modify CONSENT_TYPE_ID int(10) NOT NULL;-- enleve l'auto_increment
    
***
