-- exemple insertion plateforme hématologie Toulouse
-- les champs alias et collaborateur_id (qui correspond au responsable administratif de la PF sont optionnels et pourront être renseignés par l'interface par l'admin PF)
SET FOREIGN_KEY_CHECKS=0;
alter table PLATEFORME modify PLATEFORME_ID INT(10) NOT NULL auto_increment;
INSERT INTO PLATEFORME (NOM, ALIAS, COLLABORATEUR_ID) VALUES ('Plateforme', null, null);
alter table PLATEFORME modify PLATEFORME_ID INT(10) NOT NULL;
SET FOREIGN_KEY_CHECKS=1;

-- ensuite le plus simple est de se connecter dans l'interface en tant qu'ADMIN_TUMO pour
-- au besoin, créer un utilisateur dans cette PF Administration > Compte
-- assigner cet utilisateur (ou un existant) comme admin dans Administration > Plateforme
-- pour attribuer un ADMIN PF en base
insert into PLATEFORME_ADMINISTRATEUR select max(plateforme_id), (select utilisateur_id from UTILISATEUR where login = '<user_dans_tk>') from PLATEFORME;

-- cet admin pourra alors:
-- renseigner les établissements/services/collaborateurs partenaires dans Administration > Collaborations (si ils n'existent pas déja car toutes les données collaborations sont partagées par toutes les PFs

-- créer ses profils dans Administration>Profils
-- !!il y a un problème à ce niveau car dans l'interface il n'est pas possible de créer le profil 'Admin de collection' PROFIL.ADMIN=1
-- qui est un profil avec des droits pré-définis et elevés au niveau de la collection
-- il peux être utile alors d'insérer en base ce profil
insert into PROFIL (profil_id, nom, admin, archive, plateforme_id) select max(profil_id) + 1, 'Administrateur de collection', 1, 0, (select max(pateforme_id) from PLATEFORME) from PROFIL;

-- créer ses annotations dans Administration>Annotations

-- créer ses collections dans Administration>Collections

-- les utilisateurs et leurs accès aux collections dans Administration > Comptes

-- ensuite les thésaurii qui sont obligatoires pour commencer l'activité (voir ex de scripts plus bas)

-- les conteneurs de stockage dans l'onglet Stockage

-- exs de scripts pour enregistrer en base par défaut qq elements de thesaurus
-- PRELEVEMENT - NATURE
alter table NATURE modify NATURE_ID int(3) NOT NULL auto_increment;
insert into NATURE (nature, plateforme_id) values ('TISSU', maxPfId), ('SANG', maxPfId), ('LCR', maxPfId);
alter table NATURE modify NATURE_ID int(3) NOT NULL;

-- PRELEVEMENT - CONSENT_TYPE (= statut juridique)
alter table CONSENT_TYPE modify consent_type_id int(2) NOT NULL auto_increment;
insert into CONSENT_TYPE (type, plateforme_id) values ('EN ATTENTE', maxPfId), ('RECHERCHE', maxPfId), ('GENETIQUE', maxPfId);
alter table CONSENT_TYPE modify consent_type_id int(2) NOT NULL;

-- ECHANTILLON_TYPE
alter table ECHANTILLON_TYPE modify ECHANTILLON_TYPE_ID int(2) NOT NULL auto_increment;
insert into ECHANTILLON_TYPE (type, inca_cat, plateforme_id) values ('CELLULES', null, maxPfId), ('SERUM', null, maxPfId), ('TISSU', null, maxPfId);
alter table ECHANTILLON_TYPE modify ECHANTILLON_TYPE_ID int(2) NOT NULL;

-- PROD_TYPE
alter table PROD_TYPE modify PROD_TYPE_ID int(2) NOT NULL auto_increment;
insert into PROD_TYPE (type, plateforme_id) values ('ADN', maxPfId), ('ARN', maxPfId), ('PROTEINE', maxPfId);
alter table PROD_TYPE modify PROD_TYPE_ID int(2) NOT NULL;

-- CONTENEUR_TYPE
alter table CONTENEUR_TYPE modify CONTENEUR_TYPE_ID int(2) NOT NULL auto_increment;
insert into CONTENEUR_TYPE (type, plateforme_id) values ('CONGELATEUR -80', maxPfId), ('AZOTE', maxPfId);
alter table CONTENEUR_TYPE modify CONTENEUR_TYPE_ID int(2) NOT NULL;

-- ENCEINTE_TYPE
alter table ENCEINTE_TYPE modify ENCEINTE_TYPE_ID int(2) NOT NULL auto_increment;
insert into ENCEINTE_TYPE (type, prefixe, plateforme_id) values ('TIROIR', 'T', maxPfId), ('RACK', 'R', maxPfId), ('CASIER', 'C', maxPfId);
alter table ENCEINTE_TYPE modify ENCEINTE_TYPE_ID int(2) NOT NULL;