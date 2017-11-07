/*============================================*/
/* Script de migration de la version 1.10     */
/* à la version 2       	      	          */
/* Tumorotek version : 2.0		              */
/* DBMS name: Oracle version 10	(xe)   	      */
/* Created on: 01/06/2011		              */    
/*============================================*/

set serveroutput on 1000000 format wrapped
whenever sqlerror exit SQL.SQLCODE rollback;
whenever oserror exit 2 rollback;
-- set termout off
-- spool upDDL.log
begin
	
/*==============================================================*/
/* Nettoyage tables CIM version 1.11                            */
/*==============================================================*/
declare
	v_count NUMBER:=0;
begin
	select count(*) into v_count from all_tables where table_name='LIBELLE';
	IF v_count = 1 THEN
		execute immediate 'drop table LIBELLE';
	END IF;
	v_count:=0;
	select count(*) into v_count from all_tables where table_name='MASTER';
	IF v_count = 1 THEN
		execute immediate 'drop table MASTER';
	END IF;
	v_count:=0;
	select count(*) into v_count from all_tables where table_name='CHAPTER';
	IF v_count = 1 THEN
		execute immediate 'drop table CHAPTER';
	END IF;
end;

/*==============================================================*/
/* Table: AFFICHAGE                                             */
/*==============================================================*/
execute immediate 'CREATE TABLE AFFICHAGE (
  	AFFICHAGE_ID NUMBER(10) NOT NULL,
 	CREATEUR_ID NUMBER(22) NOT NULL,
	BANQUE_ID NUMBER(22) NOT NULL,
  	INTITULE VARCHAR2(100) NOT NULL,
 	NB_LIGNES NUMBER(5) NOT NULL,
  	constraint PK_AFFICHAGE primary key (AFFICHAGE_ID)
)'; 
dbms_output.put_line('Table AFFICHAGE créée.');

/*==============================================================*/
/* Table: ANNOTATION_DEFAUT                                     */
/*==============================================================*/
execute immediate 'CREATE TABLE ANNOTATION_DEFAUT (
       ANNOTATION_DEFAUT_ID NUMBER(22) NOT NULL
     , CHAMP_ANNOTATION_ID NUMBER(22) NOT NULL
     , ALPHANUM VARCHAR2(100)
     , TEXTE VARCHAR2(4000)
     , ANNO_DATE DATE
     , BOOL NUMBER(1)   
     , ITEM_ID NUMBER(22)
     , OBLIGATOIRE NUMBER(1) DEFAULT 0 NOT NULL 
     , BANQUE_ID NUMBER(22)
     , constraint PK_ANNOTATION_DEFAUT primary key (ANNOTATION_DEFAUT_ID)
)';
dbms_output.put_line('Table ANNOTATION_DEFAUT créée.');

/*==============================================================*/
/* Table: ANNOTATION_VALEUR                                     */
/*==============================================================*/
execute immediate 'CREATE TABLE ANNOTATION_VALEUR (
       ANNOTATION_VALEUR_ID NUMBER(22) NOT NULL
     , CHAMP_ANNOTATION_ID NUMBER(22) NOT NULL
     , OBJET_ID NUMBER(22) NOT NULL
     , ALPHANUM VARCHAR2(100)
     , TEXTE VARCHAR2(4000)
     , ANNO_DATE DATE
     , BOOL NUMBER(1)
     , ITEM_ID NUMBER(22)
     , FICHIER_ID NUMBER(22) unique
     , BANQUE_ID NUMBER(22)
     , constraint PK_ANNOTATION_VALEUR primary key (ANNOTATION_VALEUR_ID)
)';
dbms_output.put_line('Table ANNOTATION_VALEUR créée.');
execute immediate 'CREATE INDEX objIdx on ANNOTATION_VALEUR (OBJET_ID, CHAMP_ANNOTATION_ID)';

execute immediate 'CREATE SEQUENCE annoValSeq START WITH (select max(annotation_valeur_id)+1 from ANNOTATION_VALEUR) INCREMENT BY 1 NOMAXVALUE'; 

/*==============================================================*/
/* Table: BANQUE                                                */
/*==============================================================*/
-- alter table BANQUE modify BANQUE_ID NUMBER(10);
execute immediate 'alter table BANQUE rename column COLLAB_ID to COLLABORATEUR_ID';
-- alter table BANQUE change COLLAB_ID COLLABORATEUR_ID NUMBER(10);
-- alter table BANQUE modify PROPRIETAIRE_ID NUMBER(10);
execute immediate 'alter table BANQUE modify BANQUE_NOM VARCHAR2(100)';
execute immediate 'alter table BANQUE rename column BANQUE_NOM to NOM';
execute immediate 'alter table BANQUE modify BANQUE_DESC VARCHAR2(4000) default null';
execute immediate 'alter table BANQUE rename column BANQUE_DESC to DESCRIPTION';
execute immediate 'alter table BANQUE add CONTACT_ID NUMBER(22)';
execute immediate 'alter table BANQUE add AUTORISE_CROSS_PATIENT NUMBER(1) default 0';
execute immediate 'alter table BANQUE add ARCHIVE NUMBER(1) default 0 not null';
execute immediate 'alter table BANQUE add DEFMALADIES NUMBER(1) default 1 not null';
execute immediate 'alter table BANQUE add DEFAUT_MALADIE VARCHAR2(250) DEFAULT NULL';
execute immediate 'alter table BANQUE add DEFAUT_MALADIE_CODE varchar2(50) DEFAULT NULL';
execute immediate 'alter table BANQUE add CONTEXTE_ID NUMBER(2) default 1 not null';
execute immediate 'alter table BANQUE add PLATEFORME_ID NUMBER(22)';
execute immediate 'update BANQUE set plateforme_id=1';
execute immediate 'alter table BANQUE modify PLATEFORME_ID not null';
execute immediate 'alter table BANQUE add ECHANTILLON_COULEUR_ID NUMBER(10) DEFAULT NULL';
execute immediate 'alter table BANQUE add PROD_DERIVE_COULEUR_ID NUMBER(10) DEFAULT NULL';

dbms_output.put_line('Table BANQUE modifiee.');

/*==============================================================*/
/* Table: BANQUE_TABLE_CODAGE                                   */
/*==============================================================*/
execute immediate 'CREATE TABLE BANQUE_TABLE_CODAGE (
       BANQUE_ID NUMBER(22) NOT NULL
     , TABLE_CODAGE_ID NUMBER(2) NOT NULL
     , LIBELLE_EXPORT NUMBER(1) DEFAULT 0 NOT NULL 
     , constraint PK_BANQUE_TABLE_CODAGE primary key (BANQUE_ID, TABLE_CODAGE_ID)
)';
dbms_output.put_line('Table BANQUE_TABLE_CODAGE créée.');

/*==============================================================*/
/* Table: BLOC_IMPRESSION                                       */
/*==============================================================*/
execute immediate 'CREATE TABLE BLOC_IMPRESSION (
       BLOC_IMPRESSION_ID NUMBER(22) NOT NULL
     , NOM VARCHAR2(50) NOT NULL
     , ENTITE_ID NUMBER(4) NOT NULL
	 , ORDRE NUMBER(3) NOT NULL
	 , IS_LISTE NUMBER(1) default 0 not null
     , constraint PK_BLOC_IMPRESSION primary key (BLOC_IMPRESSION_ID)
)';

/*==============================================================*/
/* Table: BLOC_IMPRESSION_TEMPLATE                              */
/*==============================================================*/
execute immediate 'CREATE TABLE BLOC_IMPRESSION_TEMPLATE (
       BLOC_IMPRESSION_ID NUMBER(22) NOT NULL
	 , TEMPLATE_ID NUMBER(22) NOT NULL
	 , ORDRE NUMBER(3) NOT NULL
     , constraint PK_BLOC_IMPRESSION_TEMPLATE primary key (BLOC_IMPRESSION_ID, TEMPLATE_ID)
)';

/*==============================================================*/
/* Table: BOITE -> TERMINALE                                    */
/*==============================================================*/
execute immediate 'alter table BOITE rename to TERMINALE';
execute immediate 'alter table TERMINALE rename column BOITE_ID to TERMINALE_ID';
--alter table TERMINALE modify ENCEINTE_ID int(10) not null;
execute immediate 'alter table TERMINALE add TERMINALE_TYPE_ID NUMBER(10) default 0 not null';
execute immediate 'alter table TERMINALE rename column BOITE_NOM to NOM';
execute immediate 'alter table TERMINALE modify NOM VARCHAR2(50) not null';
execute immediate 'alter table TERMINALE add POSITION NUMBER(10) default 1 not null';
execute immediate 'alter table TERMINALE rename column BOITE_ALIAS to ALIAS';
execute immediate 'alter table TERMINALE rename column BOITE_ADR_PHYSIQUE to ADRP';
/*pour permettre de restreindre une enceinte à une categorie d'objet*/
execute immediate 'alter table TERMINALE add BANQUE_ID NUMBER(22)';
execute immediate 'alter table TERMINALE add ENTITE_ID NUMBER(4)';
execute immediate 'alter table TERMINALE add TERMINALE_NUMEROTATION_ID NUMBER(10) default 5 not null';
execute immediate 'alter table TERMINALE add ARCHIVE NUMBER(1) default 0 not null';
execute immediate 'alter table TERMINALE add COULEUR_ID NUMBER(22) default null';

/*DROPS*/
-- alter table TERMINALE drop BOITE_ADR_LOGIQUE;
-- alter table TERMINALE drop column BOITE_NBR_VIDES;-- jamais renseigne 
-- alter table TERMINALE drop column BOITE_ZONE_TYPE;-- jamais renseigne 

dbms_output.put_line('Table BOITE modifiee en TERMINALE.');

/*==============================================================*/
/* Table: BOITE_TYPE -> TERMINALE_TYPE                          */
/*==============================================================*/
execute immediate 'CREATE TABLE TERMINALE_TYPE (
       TERMINALE_TYPE_ID NUMBER(10) NOT NULL
     , TYPE VARCHAR2(25) NOT NULL
     , NB_PLACES NUMBER(5) NOT NULL
     , HAUTEUR NUMBER(3) NOT NULL
     , LONGUEUR NUMBER(3) NOT NULL
     , SCHEME VARCHAR2(100)
	 , DEPART_NUM_HAUT NUMBER(1) default 1 NOT NULL
     , constraint PK_TERMINALE_TYPE primary key (TERMINALE_TYPE_ID)
)';

execute immediate 'CREATE SEQUENCE termTypeSeq START WITH 1 INCREMENT BY 1 NOMAXVALUE'; 

/*MIGRATION3: BOITE_TYPE=migration des infos nb emplacement, H et L de l'enceinte terminale vers le type*/

execute immediate 'alter table TERMINALE add TEMP_NEWTYPE VARCHAR2(50) default 0 not null'; 
execute immediate 'INSERT INTO TERMINALE_TYPE (terminale_type_id, type, nb_places, hauteur, longueur) 
	(SELECT termTypeSeq.nextVal, T, E, V, H from (SELECT DISTINCT(CONCAT(CONCAT(CONCAT(CONCAT(BOITE_TYPE.boite_type,''_''),TERMINALE.boite_empla_v),''x''),TERMINALE.boite_empla_h)) T, TERMINALE.boite_nbr_empla E, TERMINALE.boite_empla_v V, TERMINALE.boite_empla_h H FROM TERMINALE, BOITE_TYPE where TERMINALE.boite_type_id=BOITE_TYPE.boite_type_id)
	)';
execute immediate 'UPDATE TERMINALE t SET temp_newtype=(SELECT CONCAT(CONCAT(CONCAT(CONCAT(b.boite_type,''_''),t.boite_empla_v),''x''),t.boite_empla_h) FROM BOITE_TYPE b WHERE t.boite_type_id=b.boite_type_id)';
execute immediate 'UPDATE TERMINALE t SET t.terminale_type_id=(SELECT terminale_type_id FROM TERMINALE_TYPE p WHERE t.temp_newtype=p.type)';
execute immediate 'alter table TERMINALE drop column TEMP_NEWTYPE';
execute immediate 'DROP SEQUENCE termTypeSeq';

dbms_output.put_line('MIGRATION3-ORA: migration des infos de BOITE effectuee dans TERMINALE_TYPE');

-- correctif BUG cellylotheque 16/07/2011
execute immediate 'alter table TERMINALE modify BOITE_TYPE_ID null';

/*DROPS*/
-- alter table TERMINALE drop column boite_type_id;
-- alter table TERMINALE drop column boite_nbr_empla;
-- alter table TERMINALE drop column boite_empla_h;
-- alter table TERMINALE drop column boite_empla_v;
-- 

/*==============================================================*/
/* Table: CATALOGUE                                             */
/*==============================================================*/
execute immediate 'CREATE TABLE CATALOGUE (
       CATALOGUE_ID NUMERIC(3) NOT NULL
     , NOM VARCHAR2(25) NOT NULL
     , DESCRIPTION VARCHAR2(250)
     , ICONE CHAR(100)
     , constraint PK_CATALOGUE primary key (CATALOGUE_ID)
)';

/*==============================================================*/
/* Table: BANQUE_CATALOGUE                                      */
/*==============================================================*/
execute immediate 'CREATE TABLE BANQUE_CATALOGUE (
		BANQUE_ID NUMBER(22) NOT NULL
	, CATALOGUE_ID NUMERIC(3) NOT NULL
, constraint PK_BANQUE_CATALOGUE primary key (BANQUE_ID, CATALOGUE_ID)
)';

/*==============================================================*/
/* Table: CATALOGUE_CONTEXTE                                    */
/*==============================================================*/
execute immediate 'CREATE TABLE CATALOGUE_CONTEXTE (
       CATALOGUE_ID NUMERIC(3) NOT NULL
     , CONTEXTE_ID NUMERIC(2) NOT NULL
     , constraint PK_CATALOGUE_CONTEXTE primary key (CATALOGUE_ID, CONTEXTE_ID)
)';

/*==============================================================*/
/* Table: CATEGORIE                                             */
/*==============================================================*/
-- alter table CATEGORIE change ETAB_CAT_ID CATEGORIE_ID NUMERIC(2);
execute immediate 'alter table CATEGORIE rename column ETAB_CAT_ID to CATEGORIE_ID';
execute immediate 'alter table CATEGORIE modify ETAB_CAT_NOM VARCHAR2(50) not null';
execute immediate 'alter table CATEGORIE rename column ETAB_CAT_NOM to NOM';

dbms_output.put_line('Table CATEGORIE  modifiee');

/*==============================================================*/
/* Table: CESSION                                               */
/*==============================================================*/
execute immediate 'alter table CESSION rename column CESS_TYPE_ID to CESSION_TYPE_ID';
execute immediate 'alter table CESSION modify CESSION_TYPE_ID NUMBER not null'; 
execute immediate 'alter table CESSION add NUMERO varchar2(100)';
execute immediate 'update CESSION set NUMERO = CESSION_NUM';
execute immediate 'alter table CESSION modify NUMERO VARCHAR2(100) not null';
-- execute immediate 'alter table CESSION drop column CESSION_NUM';
execute immediate 'alter table CESSION add CESSION_EXAMEN_ID NUMBER(3)';
execute immediate 'alter table CESSION add CONTRAT_ID NUMBER(10)';
execute immediate 'alter table CESSION add DESTINATAIRE_ID NUMBER(22)';
execute immediate 'alter table CESSION add SERVICE_DEST_ID NUMBER(22)';
execute immediate 'alter table CESSION add DESCRIPTION VARCHAR2(4000)';
execute immediate 'alter table CESSION rename column CESS_STATUT_ID to CESSION_STATUT_ID';
execute immediate 'alter table CESSION modify CESSION_STATUT_ID NUMBER default 1 not null';
execute immediate 'alter table CESSION modify ETUDE_TITRE varchar2(250)';


execute immediate 'alter table CESSION rename column VALIDEUR_ID to EXECUTANT_ID';
execute immediate 'alter table CESSION rename column TRANS_TEMP to TEMPERATURE';
execute immediate 'alter table CESSION rename column TRANS_OBSERV to OBSERVATIONS';
execute immediate 'alter table CESSION modify OBSERVATIONS VARCHAR2(4000)';
/*Ajout des infos Destruction*/
execute immediate 'alter table CESSION add DESTRUCTION_MOTIF_ID NUMBER(3)';
execute immediate 'alter table CESSION add DESTRUCTION_DATE DATE';
execute immediate 'alter table CESSION add ETAT_INCOMPLET NUMBER(1)default 0';
execute immediate 'alter table CESSION add ARCHIVE NUMBER(1) default 0 not null';

/*DROPS*/
-- alter table CESSION drop column etude_desc;
-- alter table CESSION drop column DEPART_HEURE;
-- alter table CESSION drop column DEPART_MIN;
-- alter table CESSION drop column ARRIVEE_HEURE;
-- alter table CESSION drop column ARRIVEE_MIN;
-- alter table CESSION drop column ADRESSE_LIVR;-- jamais utilisee
-- alter table CESSION drop column RETOUR_DATE;-- jamais utilisee
-- alter table CESSION drop column RETOUR_COMMENT;-- jamais utilisee

dbms_output.put_line('Table CESSION modifiee');

/*==============================================================*/
/* Table: CESSION_EXAMEN                                        */
/*==============================================================*/
execute immediate 'CREATE TABLE CESSION_EXAMEN (
       CESSION_EXAMEN_ID NUMBER(3) NOT NULL
     , EXAMEN VARCHAR2(50) NOT NULL
     , EXAMEN_EN VARCHAR2(50)
     , PLATEFORME_ID NUMBER(22) NOT NULL
     , constraint PK_CESSION_EXAMEN primary key (CESSION_EXAMEN_ID)
)';

dbms_output.put_line('Table CESSION_EXAMEN creee');

/*==============================================================*/
/* Table: CESS_STATUT -> CESSION_STATUT                         */
/*==============================================================*/
execute immediate 'alter table CESS_STATUT rename to CESSION_STATUT';
execute immediate 'alter table CESSION_STATUT rename column CESS_STATUT_ID to CESSION_STATUT_ID';
execute immediate 'alter table CESSION_STATUT rename column CESS_STATUT to STATUT';
execute immediate 'alter table CESSION_STATUT modify STATUT VARCHAR2(20) not null';

dbms_output.put_line('Table CESS_STATUT modifiee en CESSION_STATUT');

/*==============================================================*/
/* Table: CESS_TYPE -> CESSION_TYPE                             */
/*==============================================================*/
execute immediate 'alter table CESS_TYPE rename to CESSION_TYPE';
execute immediate 'alter table CESSION_TYPE rename column CESS_TYPE_ID to CESSION_TYPE_ID';
execute immediate 'alter table CESSION_TYPE rename column CESS_TYPE to TYPE';
execute immediate 'alter table CESSION_TYPE modify TYPE VARCHAR2(30) not null';

dbms_output.put_line('Table CESS_TYPE modifiee en CESSION_TYPE');

/*==============================================================*/
/* Table: CHAMP                                                 */
/*==============================================================*/
execute immediate 'CREATE TABLE CHAMP (
  	CHAMP_ID NUMBER(22) NOT NULL,
  	CHAMP_ANNOTATION_ID NUMBER default NULL,
  	CHAMP_ENTITE_ID NUMBER(22) default NULL,
	CHAMP_PARENT_ID NUMBER(22) default NULL,
  	constraint PK_CHAMP primary key (CHAMP_ID)
)';

/*==============================================================*/
/* Table: CHAMP_ANNOTATION                                      */
/*==============================================================*/
execute immediate 'alter table CHAMP_ANNOTATION rename column ID to CHAMP_ANNOTATION_ID';
execute immediate 'alter table CHAMP_ANNOTATION modify NOM VARCHAR2(100) not null';
execute immediate 'alter table CHAMP_ANNOTATION rename column TYPE to DATA_TYPE_ID';
execute immediate 'alter table CHAMP_ANNOTATION modify DATA_TYPE_ID NUMBER not null';
execute immediate 'alter table CHAMP_ANNOTATION add COMBINE NUMBER(1) default 0';
execute immediate 'alter table CHAMP_ANNOTATION add ORDRE NUMBER(3) default 1 not null';
execute immediate 'alter table CHAMP_ANNOTATION add EDIT NUMBER(1) default 1';

dbms_output.put_line('Table CHAMP_ANNOTATION modifiee');

/*==============================================================*/
/* Table: CHAMP_ENTITE                                          */
/*==============================================================*/
execute immediate 'CREATE TABLE CHAMP_ENTITE (
  	CHAMP_ENTITE_ID NUMBER(22) NOT NULL,
  	NOM VARCHAR2(50) NOT NULL,
  	DATA_TYPE_ID NUMBER(2) NOT NULL,
 	IS_NULL NUMBER(1) NOT NULL,
 	IS_UNIQUE NUMBER(1)NOT NULL,
  	VALEUR_DEFAUT VARCHAR2(50),
  	ENTITE_ID NUMBER(4) NOT NULL,
	CAN_IMPORT NUMBER(1) default 0 not null,
	QUERY_CHAMP_ID NUMBER(22) DEFAULT NULL,
  	constraint PK_CHAMP_ENTITE primary key (CHAMP_ENTITE_ID)
)';

dbms_output.put_line('Table CHAMP_ENTITE créée');

/*==============================================================*/
/* Table: CHAMP_ENTITE_BLOC                                     */
/*==============================================================*/
execute immediate 'CREATE TABLE CHAMP_ENTITE_BLOC (
  	CHAMP_ENTITE_ID NUMBER(22) NOT NULL,
	BLOC_IMPRESSION_ID NUMBER(22) NOT NULL,
	ORDRE NUMBER(3) NOT NULL,
  	constraint PK_CHAMP_ENTITE_BLOC primary key (CHAMP_ENTITE_ID, BLOC_IMPRESSION_ID)
)';

dbms_output.put_line('Table CHAMP_ENTITE_BLOC créée');

/*==============================================================*/
/* Table: CHAMP_IMPRIME                                         */
/*==============================================================*/
execute immediate 'CREATE TABLE CHAMP_IMPRIME (
  	CHAMP_ENTITE_ID NUMBER(22) NOT NULL,
	TEMPLATE_ID NUMBER(22) NOT NULL,
	BLOC_IMPRESSION_ID NUMBER(22) NOT NULL,
  	ORDRE NUMBER(3) NOT NULL,
  	constraint PK_CHAMP_IMPRIME primary key (CHAMP_ENTITE_ID, TEMPLATE_ID, BLOC_IMPRESSION_ID)
)';
dbms_output.put_line('Table CHAMP_IMPRIME créée');

/*==============================================================*/
/* Table: CODE_ASSIGNE                                          */
/*==============================================================*/
execute immediate 'CREATE TABLE CODE_ASSIGNE (
       CODE_ASSIGNE_ID NUMBER(22) NOT NULL
     , ECHANTILLON_ID NUMBER NOT NULL
     , CODE VARCHAR2(100) NOT NULL
     , LIBELLE VARCHAR2(300)
     , IS_ORGANE NUMBER(1) NOT NULL
     , IS_MORPHO NUMBER(1)
     , ORDRE NUMBER(3) default 1 NOT NULL
     , CODE_REF_ID NUMBER
     , TABLE_CODAGE_ID NUMBER(10)
     , EXPORT NUMBER(1) NOT NULL,
     constraint PK_CODE_ASSIGNE primary key (CODE_ASSIGNE_ID)
)';

dbms_output.put_line('Table CODE_ASSIGNE créée');

execute immediate 'CREATE INDEX tableCodageIdx ON CODE_ASSIGNE (TABLE_CODAGE_ID)';
execute immediate 'CREATE SEQUENCE codeAssigneSeq START WITH 1 INCREMENT BY 1 NOMAXVALUE'; 
/*MIGRATION7: CODE_ASSIGNE=migration des codes et ajout des colonnes temporaires pour associer plus tard des operations*/
execute immediate 'alter table CODE_ASSIGNE add TEMP_UT_SAISIE NUMBER';
execute immediate 'alter table CODE_ASSIGNE add TEMP_DATE_SAISIE DATE';

/* pour les nouveaux codes saisis */
execute immediate 'update ORGANE set organe_code = SUBSTR(organe_nom, 0, 2) where organe_code is null and organe_nom like ''%:%''';

/*MIGRATION 7.1: Code organe -> code assigne voir l 1523*/
execute immediate 'alter table ORGANE add ADICAP_id NUMBER';
execute immediate 'update ORGANE o set o.adicap_id = (SELECT a.adicap_id FROM ADICAP a where a.code = o.organe_code)';
-- migration code organe avec ordre = 1 laisse a defaut
execute immediate 'insert into CODE_ASSIGNE (code_assigne_id, echantillon_id, code, libelle, code_ref_id, table_codage_id, is_organe, temp_ut_saisie, temp_date_saisie, export) 
	(SELECT codeAssigneSeq.nextVal, I, C, N, A, 1, 1, US, DT, 1 from (
		SELECT ECHANTILLON.echan_id I, ORGANE.organe_code C, ORGANE.organe_nom N, ORGANE.adicap_id A, ANNO_ECHA.anno_ech_ut_saisie US, ANNO_ECHA.anno_ech_dt_saisie DT 
			FROM ECHANTILLON, ANNO_ECHA, ORGANE WHERE ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.organe_id=ORGANE.organe_id)
	)'; 
execute immediate 'update CODE_ASSIGNE set table_codage_id=null where code_ref_id is null';
execute immediate 'update CODE_ASSIGNE set code=libelle where code is null';
/*MIGRATION 72: code diagnostic*/
execute immediate 'insert into CODE_ASSIGNE (code_assigne_id, echantillon_id, code, libelle, is_organe, is_morpho, ordre, TEMP_UT_SAISIE, TEMP_DATE_SAISIE, export) 
	(SELECT codeAssigneSeq.nextVal, I, C, null, 0, 1, 1, US, DT, 1 from (
		SELECT ECHANTILLON.echan_id I, ANNO_ECHA.code_adicap_1 C, ANNO_ECHA.anno_ech_ut_saisie US, ANNO_ECHA.anno_ech_dt_saisie DT 
			FROM ECHANTILLON, ANNO_ECHA WHERE ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.code_adicap_1 is not null and TRIM(ANNO_ECHA.code_adicap_1) is not null)
	)';
execute immediate 'insert into CODE_ASSIGNE (code_assigne_id, echantillon_id, code, libelle, is_organe, is_morpho, ordre, TEMP_UT_SAISIE, TEMP_DATE_SAISIE, export) 
	(SELECT codeAssigneSeq.nextVal, I, C, null, 0, 1, 2, US, DT, 0 from (
		SELECT ECHANTILLON.echan_id I, ANNO_ECHA.code_adicap_2 C, ANNO_ECHA.anno_ech_ut_saisie US, ANNO_ECHA.anno_ech_dt_saisie DT 
			FROM ECHANTILLON, ANNO_ECHA WHERE ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.code_adicap_2 is not null and TRIM(ANNO_ECHA.code_adicap_2) is not null)
	)';
execute immediate 'insert into CODE_ASSIGNE (code_assigne_id, echantillon_id, code, libelle, is_organe, is_morpho, ordre, TEMP_UT_SAISIE, TEMP_DATE_SAISIE, export) 
	(SELECT codeAssigneSeq.nextVal, I, C, null, 0, 1, 3, US, DT, 0 from (
		SELECT ECHANTILLON.echan_id I, ANNO_ECHA.code_adicap_3 C, ANNO_ECHA.anno_ech_ut_saisie US, ANNO_ECHA.anno_ech_dt_saisie DT 
			FROM ECHANTILLON, ANNO_ECHA WHERE ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.code_adicap_3 is not null and TRIM(ANNO_ECHA.code_adicap_3) is not null)
	)';
execute immediate 'insert into CODE_ASSIGNE (code_assigne_id, echantillon_id, code, libelle, is_organe, is_morpho, ordre, TEMP_UT_SAISIE, TEMP_DATE_SAISIE, export) 
	(SELECT codeAssigneSeq.nextVal, I, C, null, 0, 1, 4, US, DT, 0 from (
		SELECT ECHANTILLON.echan_id I, ANNO_ECHA.code_adicap_4 C, ANNO_ECHA.anno_ech_ut_saisie US, ANNO_ECHA.anno_ech_dt_saisie DT 
			FROM ECHANTILLON, ANNO_ECHA WHERE ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.code_adicap_4 is not null and TRIM(ANNO_ECHA.code_adicap_4) is not null)
	)';
execute immediate 'insert into CODE_ASSIGNE (code_assigne_id, echantillon_id, code, libelle, is_organe, is_morpho, ordre, TEMP_UT_SAISIE, TEMP_DATE_SAISIE, export) 
	(SELECT codeAssigneSeq.nextVal, I, C, null, 0, 1, 5, US, DT, 0 from (
		SELECT ECHANTILLON.echan_id I, ANNO_ECHA.code_adicap_5 C, ANNO_ECHA.anno_ech_ut_saisie US, ANNO_ECHA.anno_ech_dt_saisie DT 
			FROM ECHANTILLON, ANNO_ECHA WHERE ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.code_adicap_5 is not null and TRIM(ANNO_ECHA.code_adicap_5) is not null)
	)';
	
-- execute immediate 'alter table CODE_ASSIGNE modify CODE not null';

dbms_output.put_line('MIGRATION7-ORA: migration des infos de ORGANE et ANNO_ECHA effectuee dans CODE_ASSIGNE');

/*==============================================================*/
/* Table: CODE_DOSSIER                                          */
/*==============================================================*/
execute immediate 'CREATE TABLE CODE_DOSSIER (
       CODE_DOSSIER_ID NUMBER(22) NOT NULL
     , NOM VARCHAR2(25) NOT NULL
     , DESCRIPTION VARCHAR2(100)
     , DOSSIER_PARENT_ID NUMBER(22)
     , UTILISATEUR_ID NUMBER(22)
     , BANQUE_ID NUMBER(22)
     , CODESELECT NUMBER(1) DEFAULT 0 NOT NULL 
     , constraint PK_CODE_DOSSIER primary key (CODE_DOSSIER_ID)
)';
dbms_output.put_line('Table CODE_DOSSIER créée');

/*==============================================================*/
/* Table: CODE_SELECT                                           */
/*==============================================================*/
execute immediate 'CREATE TABLE CODE_SELECT (
       CODE_SELECT_ID NUMBER(22) NOT NULL
     , UTILISATEUR_ID NUMBER(22) NOT NULL
     , BANQUE_ID NUMBER(22) NOT NULL
     , CODE_ID NUMBER(22) NOT NULL
     , TABLE_CODAGE_ID NUMBER(4) NOT NULL
     , CODE_DOSSIER_ID NUMBER(22)
     , constraint PK_CODE_SELECT primary key (CODE_SELECT_ID)
)';
dbms_output.put_line('Table CODE_DOSSIER créée');

/*==============================================================*/
/* Table: CODE_UTILISATEUR                                      */
/*==============================================================*/
execute immediate 'CREATE TABLE CODE_UTILISATEUR (
       CODE_UTILISATEUR_ID NUMBER(22) NOT NULL
     , CODE VARCHAR2(50) NOT NULL
     , LIBELLE VARCHAR2(300)
     , UTILISATEUR_ID NUMBER(22) NOT NULL
     , BANQUE_ID NUMBER(22) NOT NULL
     , CODE_DOSSIER_ID NUMBER(22)
     , CODE_PARENT_ID NUMBER(22)
     , constraint PK_CODE_UTILISATEUR primary key (CODE_UTILISATEUR_ID)
)';
dbms_output.put_line('Table CODE_UTILISATEUR créée');

/*==============================================================*/
/* Table: TRANSCODE_UTILISATEUR                                 */
/*==============================================================*/
execute immediate 'CREATE TABLE TRANSCODE_UTILISATEUR (
	TRANSCODE_UTILISATEUR_ID NUMBER(22) NOT NULL
     , CODE_UTILISATEUR_ID NUMBER(22) NOT NULL
     , TABLE_CODAGE_ID NUMBER(4) NOT NULL
     , CODE_ID NUMBER(22) NOT NULL
     , constraint PK_TRANSCODE_UTILISATEUR primary key (TRANSCODE_UTILISATEUR_ID)
)';
dbms_output.put_line('Table TRANSCODE_UTILISATEUR créée');

/*==============================================================*/
/* Table: COLLABORATEUR                                         */
/*==============================================================*/
execute immediate 'alter table COLLABORATEUR rename column COLLAB_ID to COLLABORATEUR_ID';
execute immediate 'alter table COLLABORATEUR rename column ETAB_ID to ETABLISSEMENT_ID';
execute immediate 'alter table COLLABORATEUR rename column SPECIAL_ID to SPECIALITE_ID';
execute immediate 'alter table COLLABORATEUR rename column COORD_ID to COORDONNEE_ID';
execute immediate 'alter table COLLABORATEUR rename column COLLAB_NOM to NOM';
execute immediate 'alter table COLLABORATEUR modify NOM not null';
execute immediate 'alter table COLLABORATEUR rename column COLLAB_PRENOM to PRENOM';
execute immediate 'alter table COLLABORATEUR rename column COLLAB_INITIALES to INITIALES';
execute immediate 'alter table COLLABORATEUR rename column COLLAB_TITRE to TITRE_ID';
execute immediate 'alter table COLLABORATEUR rename column COLLAB_STATUT to ARCHIVE';
execute immediate 'alter table COLLABORATEUR modify ARCHIVE NUMBER default 0 not null';

/*DROPS*/
-- alter table COLLABORATEUR drop column COLLAB_FONC;
dbms_output.put_line('Table COLLABORATEUR modifiée');

/*==============================================================*/
/* Table: COMBINAISON                                           */
/*==============================================================*/
execute immediate 'CREATE TABLE COMBINAISON (
  	COMBINAISON_ID NUMBER(22) NOT NULL,
  	OPERATEUR VARCHAR2(10) default NULL,
  	CHAMP1_ID NUMBER(22) NOT NULL,
  	CHAMP2_ID NUMBER(22) NOT NULL,
  	constraint PK_COMBINAISON primary key (COMBINAISON_ID)
)';
dbms_output.put_line('Table COMBINAISON créée');

/*==============================================================*/
/* Table: CONDIT_TYPE                                           */
/*==============================================================*/
-- alter table CONDIT_TYPE modify CONDIT_TYPE_ID int(3);
execute immediate 'alter table CONDIT_TYPE rename column CONDIT_TYPE to TYPE';
execute immediate 'alter table CONDIT_TYPE add PLATEFORME_ID NUMBER(22)';
execute immediate 'update CONDIT_TYPE set plateforme_id=1';
execute immediate 'alter table CONDIT_TYPE modify PLATEFORME_ID not null';
dbms_output.put_line('Table CONDIT_TYPE modifiée');

/*==============================================================*/
/* Table: CONDIT_MILIEU                                         */
/*==============================================================*/
-- alter table CONDIT_MILIEU modify CONDIT_MILIEU_ID int(3);
execute immediate 'alter table CONDIT_MILIEU rename column CONDIT_MILIEU to MILIEU';
execute immediate 'alter table CONDIT_MILIEU add PLATEFORME_ID NUMBER(22)';
execute immediate 'update CONDIT_MILIEU set plateforme_id=1';
execute immediate 'alter table CONDIT_MILIEU modify PLATEFORME_ID not null';
dbms_output.put_line('Table CONDIT_MILIEU modifiée');

/*==============================================================*/
/* Table: CONTRAT                                               */
/*==============================================================*/
execute immediate 'CREATE TABLE CONTRAT (
       CONTRAT_ID NUMBER(22) NOT NULL
	 , PLATEFORME_ID NUMBER(22) NOT NULL
     , NUMERO VARCHAR2(50) NOT NULL
     , DATE_DEMANDE_CESSION DATE
     , DATE_VALIDATION DATE
     , DATE_DEMANDE_REDACTION DATE
     , DATE_ENVOI_CONTRAT DATE
     , DATE_SIGNATURE DATE
     , TITRE_PROJET VARCHAR2(250)
     , COLLABORATEUR_ID NUMBER(22)
     , SERVICE_ID NUMBER(22)
	 , ETABLISSEMENT_ID NUMBER(22)
     , PROTOCOLE_TYPE_ID NUMBER(22)
     , DESCRIPTION VARCHAR2(4000)
	 , MONTANT FLOAT(12)
     , constraint PK_CONTRAT primary key (CONTRAT_ID)
)';
dbms_output.put_line('Table CONTRAT créée');

/*==============================================================*/
/* Table: CONSENTIR->MALADIE                                    */
/*==============================================================*/
/*MIGRATION8: CONSENTIR=attribution des consentements à la table PRELEVELENT*/
execute immediate 'alter table PRELEVEMENT add CONSENT_TYPE_ID NUMBER(22)';
execute immediate 'alter table PRELEVEMENT add CONSENT_DATE date';
execute immediate 'update CONSENTIR set consent_type_id=(select consent_type_id from CONSENT_TYPE where consent_type like ''%ATTENTE%'') where consent_type_id is null';
execute immediate 'update PRELEVEMENT p set (p.consent_type_id, p.consent_date) = 
	(SELECT c.consent_type_id, c.consent_date from CONSENTIR c where p.prelevement_id=c.prelevement_id)';
execute immediate 'update PRELEVEMENT set consent_type_id=(select consent_type_id from CONSENT_TYPE where consent_type like ''%ATTENTE%'') where consent_type_id is null';
execute immediate 'alter table PRELEVEMENT modify CONSENT_TYPE_ID not null';

-- 
execute immediate 'CREATE TABLE MALADIE (
       MALADIE_ID NUMBER(22) NOT NULL
     , PATIENT_ID NUMBER(22) NOT NULL
     , LIBELLE VARCHAR2(300) DEFAULT ''Inconnue'' NOT NULL 
     , CODE VARCHAR2(50)
     , DATE_DIAGNOSTIC DATE
     , DATE_DEBUT DATE
     , SYSTEME_DEFAUT NUMBER(1) DEFAULT 0 NOT NULL 
     , constraint PK_MALADIE primary key (MALADIE_ID)
)';

/*MIGRATION9: MALADIE=recuperation des informations de consentir dans MALADIE et references vers PRELEVEMENT*/
execute immediate 'CREATE SEQUENCE maladieSeq START WITH 1 INCREMENT BY 1 NOMAXVALUE'; 
execute immediate 'insert into MALADIE (maladie_id, patient_id) (SELECT maladieSeq.nextVal, P FROM (select distinct(patient_id) P from CONSENTIR))'; 
execute immediate 'alter table PRELEVEMENT add MALADIE_ID NUMBER(22)';
execute immediate 'update PRELEVEMENT p set p.maladie_id = (SELECT m.maladie_id FROM MALADIE m, CONSENTIR c WHERE p.prelevement_id=c.prelevement_id and m.patient_id=c.patient_id)';
execute immediate 'DROP SEQUENCE maladieSeq'; 

dbms_output.put_line('MIGRATION8 et 9-ORA: migration des infos de CONSENTIR dans MALADIE');

-- TODO: 
/*MIGRATION10-SITESPECIFIQUE: Diagnostic attribué au prélèvement ou annotation 007*/

/*==============================================================*/
/* Table: CONSENT_TYPE                                          */
/*==============================================================*/
-- alter table CONSENT_TYPE modify CONSENT_TYPE_ID int(2);
execute immediate 'alter table CONSENT_TYPE rename column CONSENT_TYPE to TYPE';
execute immediate 'alter table CONSENT_TYPE modify TYPE not null';
execute immediate 'alter table CONSENT_TYPE add PLATEFORME_ID NUMBER(22)';
execute immediate 'update CONSENT_TYPE set plateforme_id=1';
execute immediate 'alter table CONSENT_TYPE modify PLATEFORME_ID not null';
dbms_output.put_line('Table CONSENT_TYPE modifiée');

/*==============================================================*/
/* Table: CONTENEUR                                             */
/*==============================================================*/
-- alter table CONTENEUR modify CONTENEUR_TYPE_ID int(2);
execute immediate 'alter table CONTENEUR modify SERVICE_ID not null';
execute immediate 'alter table CONTENEUR rename column CONTENEUR_CODE to CODE'; 
execute immediate 'alter table CONTENEUR modify CODE VARCHAR2(5) not null';
execute immediate 'alter table CONTENEUR rename column CONTENEUR_NOM to NOM';
execute immediate 'alter table CONTENEUR rename column CONTENEUR_TEMP to TEMP';
execute immediate 'alter table CONTENEUR rename column CONTENEUR_NBR_NIV to NBR_NIV';
execute immediate 'alter table CONTENEUR modify NBR_NIV NUMBER(2) not null';
execute immediate 'alter table CONTENEUR rename column CONTENEUR_NBR_ENC to NBR_ENC';
execute immediate 'alter table CONTENEUR modify NBR_ENC NUMBER(2) not null';
execute immediate 'alter table CONTENEUR add PIECE VARCHAR2(20)';
execute immediate 'alter table CONTENEUR rename column CONTENEUR_DESC to DESCRIPTION';
execute immediate 'alter table CONTENEUR add PLATEFORME_ORIG_ID NUMBER(2) default 1 not null';
execute immediate 'alter table CONTENEUR add ARCHIVE NUMBER(1) default 0 not null';
dbms_output.put_line('Table CONTENEUR modifiée');

/*DROPS*/
-- alter table CONTENEUR drop column CONTENEUR_MILIEU;-- champs jamais renseigne

/*==============================================================*/
/* Table: CONTENEUR_TYPE                                        */
/*==============================================================*/
-- alter table CONTENEUR_TYPE modify CONTENEUR_TYPE_ID int(2);
execute immediate 'alter table CONTENEUR_TYPE rename column CONTENEUR_TYPE to TYPE';
execute immediate 'alter table CONTENEUR_TYPE modify TYPE not null';
execute immediate 'alter table CONTENEUR_TYPE add PLATEFORME_ID NUMBER(22)';
execute immediate 'update CONTENEUR_TYPE set plateforme_id=1';
execute immediate 'alter table CONTENEUR_TYPE modify PLATEFORME_ID not null';
dbms_output.put_line('Table CONTENEUR_TYPE modifiée');

/*==============================================================*/
/* Table: CONTENEUR_PLATEFORME                                 */
/*==============================================================*/
execute immediate 'CREATE TABLE CONTENEUR_PLATEFORME (
       CONTENEUR_ID NUMBER(22) NOT NULL
     , PLATEFORME_ID NUMBER(22) NOT NULL
	 , PARTAGE NUMBER(1) default 0 NOT NULL 
     , constraint PK_CONTENEUR_PLATEFORME primary key (CONTENEUR_ID, PLATEFORME_ID)
)';
dbms_output.put_line('Table CONTENEUR_PLATEFORME créée');

/*==============================================================*/
/* Table: CONTENEUR_BANQUE                                      */
/*==============================================================*/
execute immediate 'CREATE TABLE CONTENEUR_BANQUE (
       CONTENEUR_ID NUMBER(22) NOT NULL
     , BANQUE_ID NUMBER(22) NOT NULL
     , constraint PK_CONTENEUR_BANQUE primary key (CONTENEUR_ID, BANQUE_ID)
)';
dbms_output.put_line('Table CONTENEUR_BANQUE créée');

-- 'MIGRATION2: migration de la relation BANQUE_STOCKAGE  N-N effectuee dans la table BANQUE_STOCKAGE';
execute immediate 'insert into CONTENEUR_BANQUE (conteneur_id, banque_id) 
	(SELECT c.conteneur_id, b.banque_id FROM BANQUE b, CONTENEUR c where b.service_id = c.service_id)';
dbms_output.put_line('MIGRATION2-ORA: migration de la relation BANQUE_STOCKAGE  N-N effectuee dans la table BANQUE_STOCKAGE');

/*==============================================================*/
/* Table: CONTEXTE                                              */
/*==============================================================*/
execute immediate 'CREATE TABLE CONTEXTE (
       CONTEXTE_ID NUMBER(2) NOT NULL
     , NOM VARCHAR2(25) NOT NULL
     , constraint PK_CONTEXTE primary key (CONTEXTE_ID)
)';
dbms_output.put_line('Table CONTEXTE créée');

/*==============================================================*/
/* Table: COORDONNEE                                            */
/*==============================================================*/
execute immediate 'alter table COORDONNEE rename column COORD_ID to COORDONNEE_ID';
execute immediate 'alter table COORDONNEE add ADRESSE VARCHAR2(250)'; 
execute immediate 'alter table COORDONNEE rename column COORD_CP to CP';
execute immediate 'alter table COORDONNEE modify CP VARCHAR2(20)';
execute immediate 'alter table COORDONNEE rename column COORD_VILLE to VILLE';
execute immediate 'alter table COORDONNEE modify VILLE VARCHAR2(100)';
execute immediate 'alter table COORDONNEE rename column COORD_PAYS to PAYS';
execute immediate 'alter table COORDONNEE modify PAYS VARCHAR2(100)';
execute immediate 'alter table COORDONNEE rename column COORD_TEL to TEL';
execute immediate 'alter table COORDONNEE rename column COORD_FAX to FAX';
execute immediate 'alter table COORDONNEE rename column COORD_MAIL to MAIL';
dbms_output.put_line('Table COORDONNEE modifiée');

/*MIGRATION11: COORDONNEE.adresse=concatene les champs ADR_1 ADR_2*/
execute immediate 'update COORDONNEE set ADRESSE = CONCAT(CONCAT(COORD_ADR1, '' ''), COORD_ADR2)';
dbms_output.put_line('MIGRATION11-ORA: COORDONNEE.adresse=concatene les champs ADR_1 ADR_2');

/*DROPS*/
-- alter table COORDONNEE drop column COORD_ADR1;
-- alter table COORDONNEE drop column COORD_ADR2;

/*==============================================================*/
/* Table: COLLABORATEUR_COORDONNEE                              */
/*==============================================================*/
execute immediate 'CREATE TABLE COLLABORATEUR_COORDONNEE (
       COLLABORATEUR_ID NUMBER(22) NOT NULL
     , COORDONNEE_ID NUMBER(22) NOT NULL
     , constraint PK_COLLABORATEUR_COORDONNEE primary key (COLLABORATEUR_ID, COORDONNEE_ID)
)';
dbms_output.put_line('Table COLLABORATEUR_COORDONNEE créée');

/*MIGRATION65: transformation relation 1-N COORDONNEE-COLLABORATEUR en N-N*/
execute immediate 'insert into COLLABORATEUR_COORDONNEE (collaborateur_id, coordonnee_id) 
	(SELECT collaborateur_id, coordonnee_id from COLLABORATEUR where coordonnee_id is not null)';
dbms_output.put_line('MIGRATION65-ORA:  transformation relation 1-N COORDONNEE-COLLABORATEUR en N-N');

/*DROPS*/
-- alter table COLLABORATEUR drop column COORDONNEE_ID;

/*==============================================================*/
/* Table: COULEUR                                               */
/*==============================================================*/
execute immediate 'CREATE TABLE COULEUR (
	   COULEUR_ID NUMBER(3) NOT NULL
	 , COULEUR VARCHAR2(25) NOT NULL
	 , HEXA VARCHAR2(10) NOT NULL
	 , ORDRE_VISOTUBE NUMBER(3) DEFAULT NULL
	 , constraint PK_COULEUR primary key (COULEUR_ID)
)';
dbms_output.put_line('Table COULEUR créée');

/*==============================================================*/
/* Table: COULEUR_ENTITE_TYPE                                   */
/*==============================================================*/
execute immediate 'CREATE TABLE COULEUR_ENTITE_TYPE (
	   COULEUR_ENTITE_TYPE_ID NUMBER(22) NOT NULL
	 , COULEUR_ID NUMBER(3) NOT NULL
	 , BANQUE_ID NUMBER(22) NOT NULL
	 , ECHANTILLON_TYPE_ID NUMBER(22)
	 , PROD_TYPE_ID NUMBER(22)
	 , constraint PK_COULEUR_ENTITE_TYPE primary key (COULEUR_ENTITE_TYPE_ID)
)';
dbms_output.put_line('Table COULEUR_ENTITE_TYPE créée');

/*==============================================================*/
/* Table: FICHIER                                               */
/*==============================================================*/
execute immediate 'CREATE TABLE FICHIER (
       FICHIER_ID NUMBER(22) NOT NULL
     , NOM VARCHAR2(50) NOT NULL
     , PATH VARCHAR2(250) NOT NULL
     , MIMETYPE VARCHAR2(100) NOT NULL
     , constraint PK_FICHIER primary key (FICHIER_ID)
)';
dbms_output.put_line('Table FICHIER créée');

/*==============================================================*/
/* Table: CRITERE                                               */
/*==============================================================*/
execute immediate 'CREATE TABLE CRITERE (
  	CRITERE_ID NUMBER(22) NOT NULL,
  	OPERATEUR VARCHAR2(10) NOT NULL,
  	VALEUR VARCHAR2(40) NOT NULL,
  	CHAMP_ID NUMBER(22),
  	COMBINAISON_ID NUMBER(22),
  	constraint PK_CRITERE primary key (CRITERE_ID)
)';
dbms_output.put_line('Table CRITERE créée');

/*==============================================================*/
/* Table: DATA_TYPE                                              */
/*==============================================================*/
execute immediate 'CREATE TABLE DATA_TYPE (
       DATA_TYPE_ID NUMBER(2) NOT NULL
     , TYPE VARCHAR2(10) NOT NULL
     , constraint PK_DATA_TYPE primary key (DATA_TYPE_ID)
)';
dbms_output.put_line('Table DATA_TYPE créée');

/*==============================================================*/
/* Table: DESTRUCTION_MOTIF                                     */
/*==============================================================*/
execute immediate 'CREATE TABLE DESTRUCTION_MOTIF (
       DESTRUCTION_MOTIF_ID NUMBER(22) NOT NULL
     , MOTIF VARCHAR2(100) NOT NULL
     , PLATEFORME_ID NUMBER(22) NOT NULL
     , constraint PK_DESTRUCTION_MOTIF primary key (DESTRUCTION_MOTIF_ID)
)';
dbms_output.put_line('Table DESTRUCTION_MOTIF créée');

/*==============================================================*/
/* Table: DEM_ECHANTILLON                                       */
/*==============================================================*/
-- execute immediate 'alter table DEM_ECHANTILLON drop constraint PK_DEM_ECHANTILLON drop index';
execute immediate 'alter table DEM_ECHANTILLON rename to CEDER_OBJET';
execute immediate 'alter table CEDER_OBJET rename column ECHAN_ID to OBJET_ID';
execute immediate 'alter table CEDER_OBJET add ENTITE_ID NUMBER(4) default 3 not null';
execute immediate 'alter table CEDER_OBJET rename column QUANTITE_CEDEE to QUANTITE';
execute immediate 'alter table CEDER_OBJET add QUANTITE_UNITE_ID NUMBER(22)';

/*MIGRATION12: CEDER_OBJET.echantillon_volume/quantite (doit preceder migration unite ECHANTILLON/DERIVE)=recuperation de l'unite definie dans l'echantillon pour separer volume/quantite*/
execute immediate 'update CEDER_OBJET c set c.quantite_unite_id = (
	SELECT e.unite_id FROM ECHANTILLON e where c.objet_id=e.echan_id)';
dbms_output.put_line('MIGRATION12-ORA:  CEDER_OBJET.echantillon_volume/quantite recuperation de l''unite definie dans l''echantillon');
-- 
/*MIGRATION13: CEDER_OBJET.derive=recuperation des donnees de la table DEM_DERIVE et de l'unite*/
-- execute immediate 'alter table CEDER_OBJET drop constraint FK_DEM_ECHA_DEM_ECHAN_ECHANTIL';
execute immediate 'insert into CEDER_OBJET (cession_id, objet_id, entite_id, quantite, quantite_unite_id, adresse_logique_old) 
	(SELECT C, I, 8, Q, U, O FROM (SELECT d.cession_id C, d.prod_derive_id I, d.quantite_cedee Q, p.id_pd_quantite_unite U, d.adresse_logique_old O 
		FROM DEM_DERIVE d, PROD_DERIVE p where d.prod_derive_id=p.prod_derive_id)
	)';
dbms_output.put_line('MIGRATION13-ORA:  CEDER_OBJET.derive=recuperation des donnees de la table DEM_DERIVE et de l''unite');

-- execute immediate 'alter table CEDER_OBJET DROP CONSTRAINT PK_DEM_ECHANTILLON';
execute immediate 'alter table CEDER_OBJET add constraint PK_CEDER_OBJET primary key (CESSION_ID, OBJET_ID, ENTITE_ID)';
execute immediate 'CREATE INDEX objIdIdx ON CEDER_OBJET (objet_id)';

/*==============================================================*/
/* Table: DISPOSER->PROFIL_UTILISATEUR                          */
/*==============================================================*/
execute immediate 'alter table DISPOSER rename to PROFIL_UTILISATEUR';
execute immediate 'alter table PROFIL_UTILISATEUR rename column USER_ID to UTILISATEUR_ID';
-- alter table PROFIL_UTILISATEUR modify BANQUE_ID int(10);
-- alter table PROFIL_UTILISATEUR modify PROFIL_ID int(10);
dbms_output.put_line('table DISPOSER modifiée en PROFIL_UTILISATEUR');

/*==============================================================*/
/* Table: ECHAN_STATUT -> OBJET_STATUT                          */
/*==============================================================*/
execute immediate 'alter table ECHAN_STATUT rename to OBJET_STATUT';
execute immediate 'alter table OBJET_STATUT rename column ECHAN_STATUT_ID to OBJET_STATUT_ID';
execute immediate 'alter table OBJET_STATUT rename column ECHAN_STATUT to STATUT';
execute immediate 'alter table OBJET_STATUT modify STATUT not null';
execute immediate 'update OBJET_STATUT set statut=''EPUISE'' where statut=''CEDE''';
dbms_output.put_line('table ECHAN_STATUT modifiée en OBJET_STATUT');

/*==============================================================*/
/* Table: ECHAN_TYPE->ECHANTILLON_TYPE                          */
/*==============================================================*/
execute immediate 'alter table ECHAN_TYPE rename to ECHANTILLON_TYPE';
execute immediate 'alter table ECHANTILLON_TYPE rename column ECHAN_TYPE_ID to ECHANTILLON_TYPE_ID';
execute immediate 'alter table ECHANTILLON_TYPE rename column ECHAN_TYPE to TYPE';
execute immediate 'alter table ECHANTILLON_TYPE modify TYPE not null';
execute immediate 'alter table ECHANTILLON_TYPE add INCA_CAT VARCHAR2(10)';
execute immediate 'alter table ECHANTILLON_TYPE add PLATEFORME_ID NUMBER(22)';
execute immediate 'update ECHANTILLON_TYPE set plateforme_id=1';
execute immediate 'alter table ECHANTILLON_TYPE modify PLATEFORME_ID not null';
dbms_output.put_line('table ECHAN_TYPE modifiée en ECHANTILLON_TYPE');

/*MIGRATION14-SITESPECIFIQUE: ECHANTILLON_TYPE.inca-categories=specifique a chaque site, ex: TVGSO TT: tissu tumoral*/
execute immediate 'update ECHANTILLON_TYPE set INCA_CAT=''TISSU'' where type like ''%TISSU%''';
execute immediate 'update ECHANTILLON_TYPE set INCA_CAT=''CELLULES'' where type like ''%CELLULE%''';
execute immediate 'update ECHANTILLON_TYPE set INCA_CAT=''AUTRE'' where INCA_CAT is null';
dbms_output.put_line('MIGRATION14-ORA: SITESPECIFIQUE: ECHANTILLON_TYPE.inca-categories=specifique a chaque site, ex: TVGSO TT: tissu tumoral');

/*==============================================================*/
/* Table: ECHANTILLON                                           */
/*==============================================================*/
execute immediate 'alter table ECHANTILLON rename column ECHAN_ID to ECHANTILLON_ID';
execute immediate 'alter table ECHANTILLON add BANQUE_ID NUMBER(22)';
/*MIGRATION66: Ajout de la reference vers la banque*/
execute immediate 'update ECHANTILLON e set e.banque_id = (SELECT p.banque_id FROM PRELEVEMENT p WHERE e.prelevement_id=p.prelevement_id)'; 
dbms_output.put_line('MIGRATION66-ORA: Ajout de la reference vers la banque dans ECHANTILLON');
execute immediate 'alter table ECHANTILLON modify BANQUE_ID not null';
--
execute immediate 'alter table ECHANTILLON rename column ECHAN_STATUT_ID to OBJET_STATUT_ID';
execute immediate 'alter table ECHANTILLON modify OBJET_STATUT_ID not null';
-- alter table ECHANTILLON modify PRELEVEMENT_ID int(10);
execute immediate 'alter table ECHANTILLON rename column COLLAB_STOCK to COLLABORATEUR_ID';
execute immediate 'alter table ECHANTILLON rename column ECHAN_CODE to CODE';
execute immediate 'alter table ECHANTILLON modify CODE not null';
execute immediate 'alter table ECHANTILLON rename column ECHAN_DATE_STOCK to DATE_STOCK';
/*MIGRATION15: ECHANTILLON.date_stock=concatene la date heure min*/
execute immediate 'update ECHANTILLON set echan_min_stock=0 where echan_min_stock=-1';
-- update ECHANTILLON set echan_heure_stock=12 where echan_heure_stock=0;
execute immediate 'update ECHANTILLON set echan_heure_stock=0 where echan_heure_stock=-1';
execute immediate 'update ECHANTILLON 
	set date_stock=TO_DATE(CONCAT(
								CONCAT(
									CONCAT(
										CONCAT(TO_CHAR(date_stock, ''yyyy/mm/dd''),'' - ''),
										TO_CHAR(echan_heure_stock)),
									'':''),
								TO_CHAR(echan_min_stock)), ''yyyy/mm/dd  - HH24:MI'')
	where date_stock is not null';
-- 
execute immediate 'alter table ECHANTILLON add EMPLACEMENT_ID NUMBER(22) unique';
execute immediate 'alter table ECHANTILLON rename column ECHAN_ADRP_STOCK to ADRP_STOCK';
execute immediate 'alter table ECHANTILLON rename column ECHAN_TYPE_ID to ECHANTILLON_TYPE_ID';
execute immediate 'alter table ECHANTILLON modify ECHANTILLON_TYPE_ID not null';
execute immediate 'alter table ECHANTILLON rename column ECHAN_QUANTITE to QUANTITE';
execute immediate 'alter table ECHANTILLON rename column ECHAN_QUANTITE_INIT to QUANTITE_INIT';
execute immediate 'alter table ECHANTILLON rename column UNITE_ID to QUANTITE_UNITE_ID';
execute immediate 'alter table ECHANTILLON rename column ECHAN_DELAI_CGL to DELAI_CGL';
-- alter table ECHANTILLON modify ECHAN_QUALITE_ID int(3);
execute immediate 'alter table ECHANTILLON add TUMORAL NUMBER(1)';
execute immediate 'alter table ECHANTILLON add MODE_PREPA_ID NUMBER(22)';
execute immediate 'alter table ECHANTILLON add CR_ANAPATH_ID NUMBER(22) unique';
execute immediate 'alter table ECHANTILLON add STERILE NUMBER(1)';
execute immediate 'alter table ECHANTILLON add CONFORME_TRAITEMENT NUMBER(1)';
execute immediate 'alter table ECHANTILLON add CONFORME_CESSION NUMBER(1)';
execute immediate 'alter table ECHANTILLON add LATERALITE VARCHAR2(1)';
execute immediate 'alter table ECHANTILLON add RESERVATION_ID NUMBER(22)';
execute immediate 'alter table ECHANTILLON add ETAT_INCOMPLET NUMBER(1) default 0';
execute immediate 'alter table ECHANTILLON add ARCHIVE NUMBER(1) default 0 not null';
execute immediate 'update ECHANTILLON set ECHAN_QUALITE_ID = null where ECHAN_QUALITE_ID=0';
execute immediate 'update ECHANTILLON set COLLABORATEUR_ID = null where COLLABORATEUR_ID=0';
dbms_output.put_line('Table ECHANTILLON modifiée');

/*MIGRATION 73 lateralite*/
execute immediate 'update LATERALITE set lateralite=''D'' where lateralite_id=1';
execute immediate 'update LATERALITE set lateralite=''G'' where lateralite_id=2';
execute immediate 'update LATERALITE set lateralite=''I'' where lateralite_id=3';
execute immediate 'update LATERALITE set lateralite=''B'' where lateralite_id=4';
execute immediate 'update ECHANTILLON e set e.lateralite = (SELECT l.lateralite 
	FROM LATERALITE l, ANNO_ECHA a where e.anno_echa_id=a.anno_echa_id and a.lateralite_id=l.lateralite_id)';
dbms_output.put_line('MIGRATION73-ORA: LATERALITE dans CHAR pour table ECHANTILLON');

execute immediate 'CREATE SEQUENCE echantillonSeq START WITH (select max(echantillon_id)+1 from ECHANTILLON) INCREMENT BY 1 NOMAXVALUE'; 

/*DROPS*/
-- alter table ECHANTILLON drop column ECHAN_HEURE_STOCK;
-- alter table ECHANTILLON drop column ECHAN_MIN_STOCK;
-- alter table ECHANTILLON drop column ECHAN_NBR_OUT;-- interet?
-- alter table ECHANTILLON drop column CONTENEUR_ID;-- toujours null, info contenue dans ADRP_STOCK
-- alter table ECHANTILLON drop column ANNO_ECHA_ID;-- toutes infos migrées dans table 
-- alter table drop column ECHAN_QUALITE;-- n'apparait pas dans l'interface?

/*==============================================================*/
/* Table: ECHAN_QUALITE                                         */
/*==============================================================*/
-- alter table ECHAN_QUALITE modify echan_qualite_id int(3) not null;
execute immediate 'alter table ECHAN_QUALITE modify ECHAN_QUALITE not null';
execute immediate 'alter table ECHAN_QUALITE add PLATEFORME_ID NUMBER(22)';
execute immediate 'update ECHAN_QUALITE set plateforme_id=1';
execute immediate 'alter table ECHAN_QUALITE modify PLATEFORME_ID not null';
dbms_output.put_line('Table ECHAN_QUALITE modifiée');

/*==============================================================*/
/* Table: ENCEINTE                                              */
/*==============================================================*/
-- alter table ENCEINTE modify ENCEINTE_ID int(10);
execute immediate 'alter table ENCEINTE modify ENCEINTE_TYPE_ID not null';
-- alter table ENCEINTE modify CONTENEUR_ID int(10); 
-- alter table ENCEINTE modify ENCEINTE_PERE_ID int(10);
execute immediate 'alter table ENCEINTE rename column ENCEINTE_NOM to NOM';
execute immediate 'alter table ENCEINTE modify NOM VARCHAR2(50) not null';
execute immediate 'alter table ENCEINTE rename column ENCEINTE_ALIAS to ALIAS';
execute immediate 'alter table ENCEINTE add POSITION NUMBER(10)';
execute immediate 'alter table ENCEINTE add NB_PLACES NUMBER(22)';
/*MIGRATION17: ENCEINTE.nb_places=merge les deux nombres nbr_enc et nrb_boite dans nb_places*/
execute immediate 'update ENCEINTE set nb_places=enceinte_nbr_enc where enceinte_nbr_enc is not null';
execute immediate 'update ENCEINTE set nb_places=enceinte_nbr_boite where enceinte_nbr_boite is not null';
execute immediate 'alter table ENCEINTE modify NB_PLACES not null';
dbms_output.put_line('MIGRATION17-ORA: ENCEINTE.nb_places=merge les deux nombres nbr_enc et nrb_boite dans nb_places');
-- 
execute immediate 'alter table ENCEINTE add ENTITE_ID NUMBER(4)';
execute immediate 'alter table ENCEINTE add ARCHIVE NUMBER(1) default 0 not null';
execute immediate 'alter table ENCEINTE add COULEUR_ID NUMBER(22) default null';

dbms_output.put_line('Table ENCEINTE modifiée');
/*DROPS*/
-- alter table ENCEINTE drop column enceinte_nbr_boite;
-- alter table ENCEINTE drop column enceinte_nbr_enc;
-- alter table ENCEINTE drop column enceinte_zone_type;-- jamais renseigne

/*==================================================================*/
/* Table: ENCEINTE_BANQUE                                           */
/*pour permettre de restreindre une enceinte à une categorie d'objet*/
/*==================================================================*/
execute immediate 'CREATE TABLE ENCEINTE_BANQUE (
       ENCEINTE_ID NUMBER(22) NOT NULL
     , BANQUE_ID NUMBER(22) NOT NULL
     , constraint PK_ENCEINTE_BANQUE primary key (ENCEINTE_ID, BANQUE_ID)
)';
dbms_output.put_line('Table ENCEINTE_BANQUE créée');

/*==============================================================*/
/* Table: ENCEINTE_TYPE                                         */
/*==============================================================*/
-- alter table ENCEINTE_TYPE modify ENCEINTE_TYPE_ID int(2);
execute immediate 'alter table ENCEINTE_TYPE rename column ENCEINTE_TYPE to TYPE';
execute immediate 'alter table ENCEINTE_TYPE modify TYPE not null';
execute immediate 'alter table ENCEINTE_TYPE add PREFIXE VARCHAR2(5)';
execute immediate 'update ENCEINTE_TYPE set prefixe=SUBSTR(type, 0, 1)';
execute immediate 'alter table ENCEINTE_TYPE modify PREFIXE not null';
execute immediate 'alter table ENCEINTE_TYPE add PLATEFORME_ID NUMBER(22)';
execute immediate 'update ENCEINTE_TYPE set plateforme_id=1';
execute immediate 'alter table ENCEINTE_TYPE modify PLATEFORME_ID not null';
dbms_output.put_line('Table ENCEINTE_TYPE modifiée');

/*==============================================================*/
/* Table: GROUPEMENT                                            */
/*==============================================================*/
execute immediate 'CREATE TABLE GROUPEMENT (
  	GROUPEMENT_ID NUMBER(22) NOT NULL,
  	CRITERE1_ID NUMBER(22),
  	CRITERE2_ID NUMBER(22),
 	PARENT_ID NUMBER(22),
  	OPERATEUR VARCHAR2(10),
  	constraint PK_GROUPEMENT primary key (GROUPEMENT_ID)
)';
dbms_output.put_line('Table GROUPEMENT créée');

/*==============================================================*/
/* Table: NUMEROTATION                                          */
/*==============================================================*/
execute immediate 'alter table NUMEROTATION rename to NUMEROTATION_V1';
execute immediate 'alter table NUMEROTATION_V1 drop constraint PK_NUMEROTATION drop index';
execute immediate 'create table NUMEROTATION (
	NUMEROTATION_ID NUMBER(22) NOT NULL,
	BANQUE_ID NUMBER(22) NOT NULL,
	ENTITE_ID NUMBER(4) default 2 NOT NULL,
	CODE_FORMULA VARCHAR2(25) NOT NULL,
	CURRENT_INCREMENT NUMBER(10) NOT NULL,
	START_INCREMENT NUMBER(10) default 0,
	NB_CHIFFRES NUMBER(2) default 5,
	ZERO_FILL NUMBER(1) default 1,
	constraint PK_NUMEROTATION primary key (NUMEROTATION_ID)
)';

/*MIGRATION18: NUMEROTATION=migre la liaison vers la banque*/
execute immediate 'CREATE SEQUENCE numSeq START WITH 1 INCREMENT BY 1 NOMAXVALUE'; 
execute immediate 'insert into NUMEROTATION (numerotation_id, banque_id, code_formula, current_increment) 
	(SELECT numSeq.nextVal, B, F, I FROM (select id_collection B, code_collection F, numero I from NUMEROTATION_V1))'; 
execute immediate 'DROP SEQUENCE numSeq'; 

dbms_output.put_line('MIGRATION18_ORA: Table NUMEROTATION re-créée à partir de NUMEROTATION_V1');

/*==============================================================*/
/* Table: ETABLISSEMENT                                         */
/*==============================================================*/
execute immediate 'alter table ETABLISSEMENT rename column ETAB_ID to ETABLISSEMENT_ID';
execute immediate 'alter table ETABLISSEMENT rename column COORD_ID to COORDONNEE_ID';
execute immediate 'alter table ETABLISSEMENT modify COORDONNEE_ID unique';
execute immediate 'alter table ETABLISSEMENT rename column ETAB_CAT_ID to CATEGORIE_ID';
execute immediate 'alter table ETABLISSEMENT rename column ETAB_NOM to NOM';
execute immediate 'alter table ETABLISSEMENT modify NOM not null';
execute immediate 'alter table ETABLISSEMENT rename column ETAB_CODE to FINESS';
execute immediate 'alter table ETABLISSEMENT modify FINESS VARCHAR2(20)';
execute immediate 'alter table ETABLISSEMENT rename column ETAB_LOCAL to LOCAL';
execute immediate 'alter table ETABLISSEMENT modify LOCAL default 0';
execute immediate 'alter table ETABLISSEMENT add ARCHIVE NUMBER(1) default 0 not null';
dbms_output.put_line('Table ETABLISSEMENT modifiée');

/*==============================================================*/
/* Table: FANTOME                                               */
/*==============================================================*/
execute immediate 'CREATE TABLE FANTOME (
       FANTOME_ID NUMBER(22) NOT NULL
     , NOM VARCHAR2(100) NOT NULL
     , COMMENTAIRES VARCHAR2(4000)
     , ENTITE_ID NUMBER(4) NOT NULL
     , constraint PK_FANTOME primary key (FANTOME_ID)
)';
dbms_output.put_line('Table FANTOME créée');

/*==============================================================*/
/* Table: IMPRIMANTE_API                                        */
/*==============================================================*/
execute immediate 'CREATE TABLE IMPRIMANTE_API (
       IMPRIMANTE_API_ID NUMBER(22) NOT NULL
     , NOM VARCHAR2(50) NOT NULL
     , constraint PK_IMPRIMANTE_API primary key (IMPRIMANTE_API_ID)
)';
dbms_output.put_line('Table IMPRIMANTE_API créée');

/*==============================================================*/
/* Table: IMPRIMANTE                                            */
/*==============================================================*/
execute immediate 'alter table IMPRIMANTE rename column ID to IMPRIMANTE_ID';
execute immediate 'alter table IMPRIMANTE add IMPRIMANTE_API_ID NUMBER(22) DEFAULT 1';
execute immediate 'alter table IMPRIMANTE add MBIO_PRINTER NUMBER(22) DEFAULT null';
execute immediate 'alter table IMPRIMANTE add PLATEFORME_ID NUMBER(22)';
execute immediate 'update IMPRIMANTE set plateforme_id=1';
execute immediate 'alter table IMPRIMANTE modify PLATEFORME_ID not null';
execute immediate 'alter table IMPRIMANTE add ADRESSEIP varchar2(20)';
execute immediate 'alter table IMPRIMANTE add RESOLUTION int(5)';
execute immediate 'alter table IMPRIMANTE add PORT int(5)'
dbms_output.put_line('Table IMPRIMANTE_API modifiée');

/*==============================================================*/
/* Table: INCIDENT                                              */
/*==============================================================*/
execute immediate 'CREATE TABLE INCIDENT (
       INCIDENT_ID NUMBER(22) NOT NULL
     , NOM VARCHAR2(50) NOT NULL
     , DATE_ DATE NOT NULL
     , DESCRIPTION VARCHAR2(4000)
     , CONTENEUR_ID NUMBER(22)
     , ENCEINTE_ID NUMBER(22)
	 , TERMINALE_ID NUMBER(22)
     , constraint PK_INCIDENT primary key (INCIDENT_ID)
)';
dbms_output.put_line('Table INCIDENT créée');

/*==============================================================*/
/* Table: INDICATEUR                                            */
/*==============================================================*/
execute immediate 'create table INDICATEUR (
       INDICATEUR_ID NUMBER(22) NOT NULL
     , NOM VARCHAR2(50) NOT NULL
     , GROUPE_COLLECTION NUMBER(1) DEFAULT 0
     , TIMER_ID NUMBER(3)
     , constraint PK_INDICATEUR primary key (INDICATEUR_ID)
)';
dbms_output.put_line('Table INDICATEUR créée');

/*==============================================================*/
/* Table: INDICATEUR_BANQUE                                     */
/*==============================================================*/
execute immediate 'CREATE TABLE INDICATEUR_BANQUE (
       INDICATEUR_ID NUMBER(22) NOT NULL
     , BANQUE_ID NUMBER(22) NOT NULL
     , constraint PK_INDICATEUR_BANQUE primary key (INDICATEUR_ID, BANQUE_ID)
)';
dbms_output.put_line('Table INDICATEUR_BANQUE créée');

/*==============================================================*/
/* Table: INDICATEUR_PLATEFORME                                 */
/*==============================================================*/
execute immediate 'CREATE TABLE INDICATEUR_PLATEFORME (
       INDICATEUR_ID NUMBER(22) NOT NULL
     , PLATEFORME_ID NUMBER(22) NOT NULL
     , constraint PK_INDICATEUR_PLATEFORME primary key (INDICATEUR_ID, PLATEFORME_ID)
)';
dbms_output.put_line('Table INDICATEUR_PLATEFORME créée');

/*==============================================================*/
/* Table: INDICATEUR_REQUETE                                    */
/*==============================================================*/
execute immediate 'CREATE TABLE INDICATEUR_REQUETE (
       INDICATEUR_ID NUMBER(22) NOT NULL
     , REQUETE_ID NUMBER(22) NOT NULL
     , constraint PK_INDICATEUR_REQUETE primary key (INDICATEUR_ID, REQUETE_ID)
)';
dbms_output.put_line('Table INDICATEUR_REQUETE créée');

/*==============================================================*/
/* Table: INDICATEUR_SQL                                        */
/*==============================================================*/
execute immediate 'CREATE TABLE INDICATEUR_SQL (
       INDICATEUR_ID NUMBER(22) NOT NULL
     , SQL_ID NUMBER(22) NOT NULL
     , constraint PK_INDICATEUR_SQL primary key (INDICATEUR_ID, SQL_ID)
)';
dbms_output.put_line('Table INDICATEUR_SQL créée');

/*==============================================================*/
/* Table: ITEM_ANNOTATION_THESAURUS->ITEM                       */
/*==============================================================*/
execute immediate 'alter table ITEM_ANNOTATION_THESAURUS rename to ITEM';
execute immediate 'alter table ITEM rename column ID to ITEM_ID';
execute immediate 'alter table ITEM rename column NOM to LABEL';
execute immediate 'alter table ITEM add VALEUR VARCHAR2(100)';
execute immediate 'alter table ITEM rename column ANNOTATION_THESAURUS_ID to CHAMP_ANNOTATION_ID';
execute immediate 'alter table ITEM add PLATEFORME_ID NUMBER(22)';
dbms_output.put_line('Table ITEM_ANNOTATION_THESAURUS modifiée en ITEM');

/*==============================================================*/
/* Table: LABO_INTER                                            */
/*==============================================================*/
-- alter table LABO_INTER modify LABO_INTER_ID int(10);
execute immediate 'alter table LABO_INTER modify PRELEVEMENT_ID not null';
execute immediate 'alter table LABO_INTER add ORDRE NUMBER(2) default 1 not null';
execute immediate 'alter table LABO_INTER rename column SITE_ANALYSE to SERVICE_ID';
execute immediate 'alter table LABO_INTER rename column ARRIVEE_LABO to DATE_ARRIVEE';
execute immediate 'alter table LABO_INTER rename column CONSERV_STERILE to STERILE';
execute immediate 'alter table LABO_INTER add CONGELATION NUMBER(1)';
execute immediate 'alter table LABO_INTER rename column LABO_TRANSP_TEMP to TRANSPORT_TEMP';
execute immediate 'alter table LABO_INTER rename column DEPART_LABO to DATE_DEPART';
execute immediate 'alter table LABO_INTER rename column COLLAB_ID to COLLABORATEUR_ID';
-- alter table LABO_INTER modify TRANSPORTEUR_ID int(10);
dbms_output.put_line('Table LABO_INTER modifiée');

/*DROPS*/
-- alter table LABO_INTER drop column ARRIVEE_HEURE_LABO;
-- alter table LABO_INTER drop column ARRIVEE_MIN_LABO;
-- alter table LABO_INTER drop column DEPART_HEURE_LABO;
-- alter table LABO_INTER drop column DEPART_MIN_LABO;
-- alter table LABO_INTER drop column labo_technique;-- jamais utilisee

/*==============================================================*/
/* Table: LIEN_FAMILIAL                                         */
/*==============================================================*/
execute immediate 'CREATE TABLE LIEN_FAMILIAL (
       LIEN_FAMILIAL_ID NUMBER(2) NOT NULL
     , NOM VARCHAR2(50) NOT NULL
     , RECIPROQUE_ID NUMBER(2)
     , ASCENDANT NUMBER(1)
     , constraint PK_LIEN_FAMILIAL primary key (LIEN_FAMILIAL_ID)
)';
dbms_output.put_line('Table LIEN_FAMILIAL créée');

/*==============================================================*/
/* Table: MALADIE_MEDECIN                                       */
/*==============================================================*/
execute immediate 'CREATE TABLE MALADIE_MEDECIN (
       MALADIE_ID NUMBER(22) NOT NULL
     , COLLABORATEUR_ID NUMBER(22) NOT NULL
     , constraint PK_MALADIE_MEDECIN primary key (MALADIE_ID, COLLABORATEUR_ID)
)';
dbms_output.put_line('Table MALADIE_MEDECIN créée');

/*==============================================================*/
/* Table: MESSAGE                                               */
/*==============================================================*/
execute immediate 'CREATE TABLE MESSAGE (
       MESSAGE_ID NUMBER(22) NOT NULL
     , OBJET VARCHAR2(100) NOT NULL
     , TEXTE CLOB
     , DESTINATAIRE_ID NUMBER(22) NOT NULL
     , EXPEDITEUR_ID NUMBER(22) NOT NULL
     , IMPORTANCE NUMBER(1)
     , constraint PK_MESSAGE primary key (MESSAGE_ID)
)';
dbms_output.put_line('Table MESSAGE créée');

/*==============================================================*/
/* Table: MODE_PREPA                                            */
/*==============================================================*/
execute immediate 'CREATE TABLE MODE_PREPA (
       MODE_PREPA_ID NUMBER(22) NOT NULL
     , NOM VARCHAR2(25) NOT NULL
     , NOM_EN VARCHAR2(25)
     , PLATEFORME_ID NUMBER(22) NOT NULL
     , constraint PK_MODE_PREPA primary key (MODE_PREPA_ID)
)';
dbms_output.put_line('Table MODE_PREPA créée');

/*==============================================================*/
/* Table: MODE_PREPA_DERIVE                                     */
/*==============================================================*/
execute immediate 'CREATE TABLE MODE_PREPA_DERIVE (
       MODE_PREPA_DERIVE_ID NUMBER(22) NOT NULL
     , NOM VARCHAR2(25) NOT NULL
     , NOM_EN VARCHAR2(25)
     , PLATEFORME_ID NUMBER(22) NOT NULL
     , constraint PK_MODE_PREPA_DERIVE primary key (MODE_PREPA_DERIVE_ID)
)';
dbms_output.put_line('Table MODE_PREPA_DERIVE créée');

/*==============================================================*/
/* Table: MODELE_ETIQUETTE->MODELE                              */
/*==============================================================*/
execute immediate 'alter table MODELE_ETIQUETTE rename to MODELE';
execute immediate 'alter table MODELE rename column ID to MODELE_ID';
execute immediate 'alter table MODELE modify NOM VARCHAR2(25)';
execute immediate 'alter table MODELE add MODELE_TYPE_ID NUMBER(2) default 1 not null';
execute immediate 'alter table MODELE add PLATEFORME_ID NUMBER(22) default 1 not null';
execute immediate 'alter table MODELE add IS_DEFAULT NUMBER(22) default 1 not null';
execute immediate 'alter table MODELE add IS_QRCODE NUMBER(1) default 0 not null';
dbms_output.put_line('Table MODELE_ETIQUETTE modifiée en MODELE');

/*DROPS*/
-- alter table MODELE drop column LABEL1;-- jamais utilisee
-- alter table MODELE drop column LABEL2;-- idem
-- alter table MODELE drop column LABEL3;-- idem
-- alter table MODELE drop column LABEL4;-- idem
-- alter table MODELE drop column LABEL5;-- idem
-- alter table MODELE drop column TEXTE_LIBRE;
-- alter table PROFIL_UTILISATEUR drop column id_imprimante;

/*==============================================================*/
/* Table: MODELE_TYPE                                           */
/*==============================================================*/
execute immediate 'CREATE TABLE MODELE_TYPE (
       MODELE_TYPE_ID NUMBER(2) NOT NULL
     , TYPE VARCHAR2(15) NOT NULL
     , constraint PK_MODELE_TYPE primary key (MODELE_TYPE_ID)
)';
dbms_output.put_line('Table MODELE_TYPE créée');

/*==============================================================*/
/* Table: LIGNE_ETIQUETTE                                       */
/*==============================================================*/
execute immediate 'CREATE TABLE LIGNE_ETIQUETTE (
       LIGNE_ETIQUETTE_ID NUMBER(22) NOT NULL
	 , MODELE_ID NUMBER(22) NOT NULL
	 , ORDRE NUMBER(2) NOT NULL
     , IS_BARCODE NUMBER(1)
	 , ENTETE VARCHAR2(25)
	 , CONTENU VARCHAR2(50)
	 , FONT VARCHAR2(25)
	 , STYLE VARCHAR2(25)
	 , FONT_SIZE NUMBER(2)
     , constraint PK_LIGNE_ETIQUETTE primary key (LIGNE_ETIQUETTE_ID)
)';
dbms_output.put_line('Table LIGNE_ETIQUETTE modifiée en MODELE');

/*==============================================================*/
/* Table: CHAMP_LIGNE_ETIQUETTE                                 */
/*==============================================================*/
execute immediate 'CREATE TABLE CHAMP_LIGNE_ETIQUETTE (
       CHAMP_LIGNE_ETIQUETTE_ID NUMBER(22) NOT NULL
	 , LIGNE_ETIQUETTE_ID NUMBER(22) NOT NULL
	 , CHAMP_ID NUMBER(22) NOT NULL
	 , ENTITE_ID NUMBER(4) NOT NULL
     , ORDRE NUMBER(2) NOT NULL
	 , EXP_REG VARCHAR2(25)
     , constraint PK_CHAMP_LIGNE_ETIQUETTE primary key (CHAMP_LIGNE_ETIQUETTE_ID)
)';
dbms_output.put_line('Table CHAMP_LIGNE_ETIQUETTE modifiée en MODELE');


/*==============================================================*/
/* Table: AFFECTATION_IMPRIMANTE                                */
/*==============================================================*/
execute immediate 'CREATE TABLE AFFECTATION_IMPRIMANTE (
       UTILISATEUR_ID NUMBER(22) NOT NULL
     , BANQUE_ID NUMBER(22) NOT NULL
     , IMPRIMANTE_ID NUMBER(22) NOT NULL
     , MODELE_ID NUMBER(22) 
     , constraint PK_AFFECTATION_IMPRIMANTE primary key (BANQUE_ID, IMPRIMANTE_ID, UTILISATEUR_ID)
)';

execute immediate 'insert into AFFECTATION_IMPRIMANTE (utilisateur_id, banque_id, imprimante_id, modele_id) 
	(SELECT p.utilisateur_id, p.banque_id,p.id_imprimante, p.id_modele_etiquette FROM PROFIL_UTILISATEUR p 
		WHERE p.id_imprimante is not null and p.id_imprimante > 0)';
		
dbms_output.put_line('Table AFFECTATION_IMPRIMANTE créée à partir de PROFIL_UTILISATEUR');

/*DROPS*/
-- alter table PROFIL_UTILISATEUR drop column id_modele_etiquette;

/*==============================================================*/
/* Table: OBJET -> ENTITE                                       */
/*==============================================================*/
execute immediate 'alter table OBJET rename to ENTITE';
execute immediate 'alter table ENTITE rename column OBJET_ID to ENTITE_ID';
execute immediate 'alter table ENTITE rename column OBJET_NOM to NOM';
execute immediate 'alter table ENTITE modify NOM VARCHAR2(25) not null';
execute immediate 'alter table ENTITE add MASC NUMBER(1)  default 1 not null';
execute immediate 'alter table ENTITE add ANNOTABLE NUMBER(1)';
execute immediate 'update ENTITE set annotable=1';
execute immediate 'alter table ENTITE modify ANNOTABLE not null';
dbms_output.put_line('Table OBJET modifiée en ENTITE');

/*==============================================================*/
/* Table: OPERATION                                             */
/*==============================================================*/
execute immediate 'CREATE TABLE OPERATION (
       OPERATION_ID NUMBER(22) NOT NULL
     , UTILISATEUR_ID NUMBER(22)
     , DATE_ date NOT NULL
     , OBJET_ID NUMBER(22) NOT NULL
     , OPERATION_TYPE_ID NUMBER(2) NOT NULL
     , ENTITE_ID NUMBER(4) NOT NULL
     , V1 NUMBER(1) DEFAULT 0 NOT NULL 
     , constraint PK_OPERATION primary key (OPERATION_ID)
)';

execute immediate 'CREATE INDEX opTypeIdx ON OPERATION (operation_type_id)';
execute immediate 'CREATE INDEX opEntIdx ON OPERATION (entite_id)';
execute immediate 'CREATE INDEX opObjIdx ON OPERATION (objet_id)';

execute immediate 'CREATE SEQUENCE opSeq START WITH (select max(operation_id)+1 from OPERATION) INCREMENT BY 1 NOMAXVALUE'; 

/*MIGRATION26: OPERATION.anno_echa=Recuperation des infos organe/lesionnel de la table ANNO_ECHA enregistrees dans la table CODE_ASSIGNE*/
execute immediate 'insert into OPERATION (operation_id, utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) 
	(SELECT opSeq.nextVal, U, D, O, 3, 41, 1 FROM (SELECT temp_ut_saisie U, temp_date_saisie D, code_assigne_id O 
	FROM CODE_ASSIGNE WHERE is_organe=1 and temp_ut_saisie is not null and temp_date_saisie is not null)
)';
execute immediate 'insert into OPERATION (operation_id, utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) 
	(SELECT opSeq.nextVal, U, D, O, 3, 54, 1 FROM (SELECT temp_ut_saisie U, temp_date_saisie D, code_assigne_id O 
	FROM CODE_ASSIGNE WHERE is_organe=0 and temp_ut_saisie is not null and temp_date_saisie is not null)
)';
dbms_output.put_line('Migration26-ORA: operations codes organe/lesionel');
/*MIGRATION27: OPERATION.echantillon=Recuperation des infos enregistrees dans la table ECHANTILLON, oblige de recuperer l'utilisateur ayant saisi le prelevement*/
execute immediate 'insert into OPERATION (operation_id, utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) 
	(SELECT opSeq.nextVal, U, D, O, 3, 3, 1 FROM (SELECT p.pre_ut_saisie U, e.ech_dt_saisie D, e.echantillon_id O 
	FROM ECHANTILLON e, PRELEVEMENT p WHERE e.prelevement_id=p.prelevement_id and e.ech_dt_saisie is not null and p.pre_ut_saisie is not null)
)';
dbms_output.put_line('Migration26-ORA: operations echantillons');
/*pas d'infos récupérables pour les dérivé*/
/*MIGRATION28: OPERATION.cession=Recuperation des infos operations des cessions*/
execute immediate 'insert into OPERATION (operation_id, utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) 
	(SELECT opSeq.nextVal, U, D, O, 3, 5, 1 FROM (SELECT ces_ut_saisie U, ces_dt_saisie D, cession_id O 
	FROM CESSION WHERE ces_dt_saisie is not null and ces_ut_saisie is not null)
)';
dbms_output.put_line('Migration26-ORA: operations cessions');
/*MIGRATION29: OPERATION.prelevement=Recuperation des infos operations des prelevements*/
execute immediate 'insert into OPERATION (operation_id, utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) 
	(SELECT opSeq.nextVal, U, D, O, 3, 2, 1 FROM (SELECT pre_ut_saisie U, pre_dt_saisie D, prelevement_id O 
	FROM PRELEVEMENT WHERE pre_dt_saisie is not null and pre_ut_saisie is not null)
)';
dbms_output.put_line('Migration26-ORA: operations prelevements');
/*MIGRATION30: OPERATION.patient=Recuperation des infos operations des patients*/
execute immediate 'insert into OPERATION (operation_id, utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) 
	(SELECT opSeq.nextVal, U, D, O, 3, 1, 1 FROM (SELECT pat_ut_saisie U, pat_dt_saisie D, patient_id O 
	FROM PATIENT WHERE pat_dt_saisie is not null and pat_ut_saisie is not null)
)';
dbms_output.put_line('Migration26-ORA: operations patients');
/*MIGRATION: HISTORIQUE connection login logout*/
execute immediate 'insert into OPERATION (operation_id, utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) 
	(SELECT opSeq.nextVal, U, D, O, 16, 13, 1 FROM (SELECT user_id U, date_action D, user_id O 
	FROM HISTORIQUE WHERE action_id=21)
)';
execute immediate 'insert into OPERATION (operation_id, utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) 
	(SELECT opSeq.nextVal, U, D, O, 17, 13, 1 FROM (SELECT user_id U, date_action D, user_id O 
	FROM HISTORIQUE WHERE action_id=22)
)';
dbms_output.put_line('Migration26-ORA: operations login logout');

-- execute immediate 'DROP SEQUENCE opSeq'; 

dbms_output.put_line('Table OPERATION créée et peuplée');

/*DROPS*/
-- alter table CODE_ASSIGNE drop column TEMP_UT_SAISIE;
-- alter table CODE_ASSIGNE drop column TEMP_DATE_SAISIE;
-- alter table ECHANTILLON drop column ECH_DT_SAISIE;
-- alter table CESSION drop column CES_UT_SAISIE;
-- alter table CESSION drop column CES_DT_SAISIE;
-- alter table PRELEVEMENT drop column PRE_UT_SAISIE;
-- alter table PRELEVEMENT drop column PRE_DT_SAISIE;
-- alter table PATIENT drop column PAT_UT_SAISIE;
-- alter table PATIENT drop column PAT_DT_SAISIE;

/*==============================================================*/
/* Table: OPERATION_TYPE                                        */
/*==============================================================*/
execute immediate 'CREATE TABLE OPERATION_TYPE (
       OPERATION_TYPE_ID NUMBER(2) NOT NULL
     , NOM VARCHAR2(25) NOT NULL
     , PROFILABLE NUMBER(1) NOT NULL
     , constraint PK_OPERATION_TYPE primary key (OPERATION_TYPE_ID)
)';
dbms_output.put_line('Table OPERATION_TYPE créée');

/*==============================================================*/
/* Table: PATIENT                                               */
/*==============================================================*/
-- alter table PATIENT modify PATIENT_ID int(10);
execute immediate 'alter table PATIENT rename column PATIENT_NIP to NIP';
execute immediate 'alter table PATIENT rename column PATIENT_NOM_NAISS to NOM_NAISSANCE';
execute immediate 'alter table PATIENT rename column PATIENT_NOM to NOM';
execute immediate 'alter table PATIENT modify NOM not null';
execute immediate 'alter table PATIENT rename column PATIENT_PRENOM to PRENOM';
execute immediate 'alter table PATIENT rename column PATIENT_SEXE to SEXE';
execute immediate 'alter table PATIENT rename column PATIENT_DATE_NAISS to DATE_NAISSANCE';
execute immediate 'alter table PATIENT rename column PATIENT_LIEU_VILLE to VILLE_NAISSANCE';
execute immediate 'alter table PATIENT rename column PATIENT_LIEU_PAYS to PAYS_NAISSANCE';
execute immediate 'alter table PATIENT add PATIENT_ETAT VARCHAR2(10) default ''Inconnu'' not null';
execute immediate 'alter table PATIENT add DATE_ETAT date';
execute immediate 'alter table PATIENT rename column patient_date_deces to date_deces';
execute immediate 'alter table PATIENT add ETAT_INCOMPLET NUMBER(1) default 0';
execute immediate 'alter table PATIENT add ARCHIVE NUMBER(1) default 0 not null'; 
dbms_output.put_line('Table PATIENT modifiée');

/*DROPS*/
-- alter table PATIENT drop column PATIENT_LIEU_CP;-- jamais utilise
-- alter table PATIENT drop column NOCONA;-- jamais utilise
-- alter table PATIENT drop column CDPONA;-- jamais utilise
-- alter table PATIENT drop column LBLONA;-- jamais utilise
-- alter table PATIENT drop column CDPNAT;-- jamais utilise
-- alter table PATIENT drop column CDNAT;-- jamais utilise
-- alter table PATIENT drop column NOTLDO;-- jamais utilise
-- alter table PATIENT drop column NOTLPR;-- jamais utilise
-- alter table PATIENT drop column CDCGSP;-- jamais utilise
-- alter table PATIENT drop column CDSIFA;-- jamais utilise

/*==============================================================*/
/* Table: PATIENT_LIEN                                          */
/*==============================================================*/
execute immediate 'CREATE TABLE PATIENT_LIEN (
       PATIENT1_ID NUMBER(22) NOT NULL
     , LIEN_FAMILIAL_ID NUMBER(2) NOT NULL
     , PATIENT2_ID NUMBER(22) NOT NULL
     , constraint PK_PATIENT_LIEN primary key (PATIENT1_ID, LIEN_FAMILIAL_ID, PATIENT2_ID)
)';
dbms_output.put_line('Table PATIENT_LIEN créée');

/*==============================================================*/
/* Table: PATIENT_MEDECIN                                       */
/*==============================================================*/
execute immediate 'CREATE TABLE PATIENT_MEDECIN (
       PATIENT_ID NUMBER(22) NOT NULL
     , COLLABORATEUR_ID NUMBER(22) NOT NULL
     , ORDRE NUMBER(3) DEFAULT 1 NOT NULL 
     , constraint PK_PATIENT_MEDECIN primary key (PATIENT_ID, COLLABORATEUR_ID)
)';
dbms_output.put_line('Table PATIENT_MEDECIN créée');

/*MIGRATION34: PATIENT_MEDECIN=migration des medecins referent de PATIENT vers PATIENT_MEDECIN pour relation N-N*/
execute immediate 'insert into PATIENT_MEDECIN (patient_id, collaborateur_id, ordre) (SELECT patient_id, medecin_ref1, 1 from PATIENT where medecin_ref1 > 0)';
execute immediate 'insert into PATIENT_MEDECIN (patient_id, collaborateur_id, ordre) (SELECT patient_id, medecin_ref2, 2 from PATIENT where medecin_ref2 > 0 and medecin_ref1 != medecin_ref2)';
execute immediate 'insert into PATIENT_MEDECIN (patient_id, collaborateur_id, ordre) 
	(SELECT patient_id, medecin_ref3, 3 from PATIENT where medecin_ref3 > 0 and medecin_ref1 != medecin_ref3 and medecin_ref2 != medecin_ref3)';
dbms_output.put_line('Migration34-ORA: migration des medecins referent de PATIENT vers PATIENT_MEDECIN pour relation N-N');

/*DROPS*/
-- alter table PATIENT drop column medein_ref1;
-- alter table PATIENT drop column medein_ref2;
-- alter table PATIENT drop column medein_ref3;

/*==============================================================*/
/* Table: PLATEFORME                                            */
/*==============================================================*/
execute immediate 'CREATE TABLE PLATEFORME (
       PLATEFORME_ID NUMBER(22) NOT NULL
     , NOM VARCHAR2(50) NOT NULL
     , ALIAS VARCHAR2(5)
     , COLLABORATEUR_ID NUMBER(22)
     , constraint PK_PLATEFORME primary key (PLATEFORME_ID)
)';
dbms_output.put_line('Table PLATEFORME créée');

/*==============================================================*/
/* Table: PLATEFORME_ADMINISTRATEUR                             */
/*==============================================================*/
execute immediate 'CREATE TABLE PLATEFORME_ADMINISTRATEUR (
       PLATEFORME_ID NUMBER(22) NOT NULL
     , UTILISATEUR_ID NUMBER(22) NOT NULL
     , constraint PK_PLATEFORME_ADMINISTRATEUR primary key (PLATEFORME_ID, UTILISATEUR_ID)
)';
dbms_output.put_line('Table PLATEFORME_ADMINISTRATEUR créée');

/*==============================================================*/
/* Table: POINTCUT                                              */
/*==============================================================*/
execute immediate 'CREATE TABLE POINTCUT (
       POINTCUT_ID NUMBER(2) NOT NULL
     , NOM VARCHAR2(25) NOT NULL
     , constraint PK_POINTCUT primary key (POINTCUT_ID)
)';
dbms_output.put_line('Table POINTCUT créée');

/*==============================================================*/
/* Table: PRELEVEMENT                                           */
/*==============================================================*/
-- alter table PRELEVEMENT modify PRELEVEMENT_ID int(10);
execute immediate 'alter table PRELEVEMENT modify BANQUE_ID not null';
execute immediate 'alter table PRELEVEMENT rename column PRELEVEMENT_CODE to CODE';
execute immediate 'alter table PRELEVEMENT modify CODE not null';
execute immediate 'alter table PRELEVEMENT rename column PRELE_TYPE_ID to NATURE_ID';
execute immediate 'alter table PRELEVEMENT modify NATURE_ID not null';
execute immediate 'alter table PRELEVEMENT rename column PRELEVEUR to PRELEVEUR_ID';
execute immediate 'alter table PRELEVEMENT rename column PRELEV_DATE to DATE_PRELEVEMENT';
execute immediate 'alter table PRELEVEMENT rename column SITE_PRELEVEUR to SERVICE_PRELEVEUR_ID';
execute immediate 'alter table PRELEVEMENT rename column PRELE_MODE_ID to PRELEVEMENT_TYPE_ID';
-- alter table PRELEVEMENT modify CONDIT_TYPE_ID int(3);
-- alter table PRELEVEMENT modify CONDIT_MILIEU_ID int(3) after CONDIT_TYPE_ID;
execute immediate 'alter table PRELEVEMENT rename column DEPART_PRELEVEUR to DATE_DEPART';
execute immediate 'alter table PRELEVEMENT rename column TRANSPORTEUR_PRELE to TRANSPORTEUR_ID';
execute immediate 'alter table PRELEVEMENT rename column ARRIVEE_BANQUE to DATE_ARRIVEE';
execute immediate 'alter table PRELEVEMENT rename column COL_COLLAB_ID to OPERATEUR_ID';
execute immediate 'alter table PRELEVEMENT rename column PRELEVEMENT_QUANTITE to QUANTITE';
execute immediate 'alter table PRELEVEMENT rename column PRELE_UNITE_ID to QUANTITE_UNITE_ID';
-- alter table PRELEVEMENT add column DATE_CONGELATION datetime;
execute immediate 'alter table PRELEVEMENT add STERILE NUMBER(1)';
execute immediate 'alter table PRELEVEMENT add CONG_DEPART NUMBER(1)';
execute immediate 'alter table PRELEVEMENT add CONG_ARRIVEE NUMBER(1) default 1';
execute immediate 'alter table PRELEVEMENT add CONFORME_ARRIVEE NUMBER(1)';
execute immediate 'alter table PRELEVEMENT add ETAT_INCOMPLET NUMBER(1) default 0';
execute immediate 'alter table PRELEVEMENT add ARCHIVE NUMBER(1) default 0 not null';
dbms_output.put_line('Table PRELEVEMENT modifiée');

/*DROPS*/
-- alter table PRELEVEMENT drop column PRELEV_HEURE;
-- alter table PRELEVEMENT drop column PRELEV_MIN;
-- alter table PRELEVEMENT drop column DEPART_PRELE_HEURE;
-- alter table PRELEVEMENT drop column DEPART_PRELE_MIN;
-- alter table PRELEVEMENT drop column ARRIVEE_BANQUE_HEURE;
-- alter table PRELEVEMENT drop column ARRIVEE_BANQUE_MIN;
-- alter table PRELEVEMENT drop column PRESCRIPTEUR;-- jamais utilisee
-- alter table PRELEVEMENT drop column PRESCR_DATE;-- jamais utilisee
-- alter table PRELEVEMENT drop column PRELEVEUR2;-- jamais utilisee
-- alter table PRELEVEMENT drop column CONDIT_MILIEU;-- remplacee par CONDIT_MILIEU_ID
-- alter table PRELEVEMENT drop column PREPARATEUR;-- jamais utilisee
-- alter table PRELEVEMENT drop column PREPA_DATE;-- jamais utilisee
-- alter table PRELEVEMENT drop column PREPA_TYPE_ID;-- jamais utilisee
-- alter table PRELEVEMENT drop column PRELEVEMENT_NBR_ECHAN;-- toujours à 0
-- alter table PRELEVEMENT drop column SERVICE_ID;-- jamais utilisee
-- alter table PRELEVEMENT drop column DIAGNOSTIC;-- info provenant de maladie ou code lesionnel du premier echantillon?

/*==============================================================*/
/* Table: PRELE_MODE->PRELEVEMENT_TYPE                          */
/*==============================================================*/
execute immediate 'alter table PRELE_MODE rename to PRELEVEMENT_TYPE';
execute immediate 'alter table PRELEVEMENT_TYPE rename column PRELE_MODE_ID to PRELEVEMENT_TYPE_ID';
execute immediate 'alter table PRELEVEMENT_TYPE rename column PRELE_MODE_CODE to INCA_CAT';
execute immediate 'alter table PRELEVEMENT_TYPE rename column PRELE_MODE to TYPE';
execute immediate 'alter table PRELEVEMENT_TYPE modify TYPE VARCHAR2(25) not null';
execute immediate 'alter table PRELEVEMENT_TYPE add PLATEFORME_ID NUMBER(22)';
execute immediate 'update PRELEVEMENT_TYPE set plateforme_id = 1';
execute immediate 'alter table PRELEVEMENT_TYPE modify PLATEFORME_ID not null';
dbms_output.put_line('Table PRELE_MODE modifiée en PRELEVEMENT_TYPE');

/*==============================================================*/
/* Table: PRELE_TYPE->NATURE                                    */
/*==============================================================*/
execute immediate 'alter table PRELE_TYPE rename to NATURE';
execute immediate 'alter table NATURE rename column PRELE_TYPE_ID to NATURE_ID';
execute immediate 'alter table NATURE rename column PRELE_TYPE to NATURE';
execute immediate 'alter table NATURE modify NATURE not null';
execute immediate 'alter table NATURE add PLATEFORME_ID NUMBER(22)';
execute immediate 'update NATURE set plateforme_id = 1';
execute immediate 'alter table NATURE modify PLATEFORME_ID not null';
dbms_output.put_line('Table PRELE_TYPE modifiée en NATURE');

/*==============================================================*/
/* Table: PRELEVEMENT_RISQUE                                    */
/*==============================================================*/
execute immediate 'CREATE TABLE PRELEVEMENT_RISQUE (
       PRELEVEMENT_ID NUMBER(22) NOT NULL
     , RISQUE_ID NUMBER(22) NOT NULL
     , constraint PK_PRELEVEMENT_RISQUE primary key  (PRELEVEMENT_ID, RISQUE_ID)
)';
dbms_output.put_line('Table PRELEVEMENT_RISQUE créée');

/*==============================================================*/
/* Table: PROD_DERIVE                                           */
/*==============================================================*/
-- alter table PROD_DERIVE modify PROD_TYPE_ID int(2) not null;
execute immediate 'alter table PROD_DERIVE add BANQUE_ID NUMBER(22)';
execute immediate 'alter table PROD_DERIVE modify CODE_LABO varchar2(50)';

/*MIGRATION67: Ajout de la reference vers la banque*/
execute immediate 'update PROD_DERIVE p set p.banque_id = (SELECT e.banque_id FROM ECHANTILLON e WHERE p.echan_id=e.echantillon_id)'; 
execute immediate 'alter table PROD_DERIVE modify BANQUE_ID not null';
dbms_output.put_line('Migration67-ORA: PROD_DERIVE Ajout de la reference vers la banque');
--
execute immediate 'alter table PROD_DERIVE rename column COLLAB_STOCK to COLLABORATEUR_ID';
execute immediate 'alter table PROD_DERIVE rename column PROD_DERIVE_CODE to CODE';
execute immediate 'alter table PROD_DERIVE modify CODE not null';
execute immediate 'alter table PROD_DERIVE add TRANSFORMATION_ID NUMBER(22)';
execute immediate 'alter table PROD_DERIVE add MODE_PREPA_DERIVE_ID NUMBER(22)';
execute immediate 'alter table PROD_DERIVE rename column PROD_DERIVE_VOLUME to VOLUME';
execute immediate 'alter table PROD_DERIVE add VOLUME_INIT float(12)';
-- update PROD_DERIVE set volume_init=volume;
execute immediate 'alter table PROD_DERIVE rename column ID_PD_VOL_UNITE to VOLUME_UNITE_ID';
execute immediate 'alter table PROD_DERIVE rename column PROD_DERIVE_CONC to CONC';
execute immediate 'alter table PROD_DERIVE rename column ID_PD_CON_UNITE to CONC_UNITE_ID';
execute immediate 'alter table PROD_DERIVE rename column PROD_DERIVE_QUANTITE to QUANTITE';
execute immediate 'alter table PROD_DERIVE add QUANTITE_INIT float(12)';
execute immediate 'alter table PROD_DERIVE rename column ID_PD_QUANTITE_UNITE to QUANTITE_UNITE_ID';
-- alter table PROD_DERIVE add column POURCENT_UTILISATION float(12);-- calulé à la volée???
execute immediate 'alter table PROD_DERIVE rename column PROD_DATE_STOCK to DATE_STOCK';
execute immediate 'alter table PROD_DERIVE add EMPLACEMENT_ID NUMBER(22) unique';
execute immediate 'alter table PROD_DERIVE rename column PROD_ADRP_STOCK to ADRP_STOCK';
execute immediate 'alter table PROD_DERIVE rename column PROD_STATUT_ID to OBJET_STATUT_ID';
execute immediate 'alter table PROD_DERIVE modify OBJET_STATUT_ID not null';
-- alter table PROD_DERIVE modify PROD_QUALITE_ID int(3);
execute immediate 'alter table PROD_DERIVE rename column PROD_DATE_TRANSFORMATION to DATE_TRANSFORMATION';
execute immediate 'alter table PROD_DERIVE add RESERVATION_ID NUMBER(22)';
execute immediate 'alter table PROD_DERIVE add ETAT_INCOMPLET NUMBER(1) default 0';
execute immediate 'alter table PROD_DERIVE add ARCHIVE NUMBER(1) default 0 not null';
execute immediate 'alter table PROD_DERIVE add CONFORME_TRAITEMENT NUMBER(1)';
execute immediate 'alter table PROD_DERIVE add CONFORME_CESSION NUMBER(1)';

dbms_output.put_line('Table PROD_DERIVE modifiée');

/*DROPS*/
-- alter table PROD_DERIVE drop column PROD_HEURE_STOCK;
-- alter table PROD_DERIVE drop column PROD_MIN_STOCK;
-- alter table PROD_DERIVE drop column PROD_HEURE_TRANSFORMATION;
-- alter table PROD_DERIVE drop column PROD_MIN_TRANSFORMATION;
-- alter table PROD_DERIVE drop column CONTENEUR_ID;-- toujours null, info contenue dans ADRP_STOCK
-- alter table PROD_DERIVE drop column COLLAB_EXTRAC;-- toujours null, info jamais renseignee
-- alter table PROD_DERIVE drop column DATE_EXTRACTION;-- toujours null, info jamais renseignee
-- alter table PROD_DERIVE drop column PROD_DELAI_CGL;-- toujours null, info jamais renseignee, la conserver qd même?

/*==============================================================*/
/* Table: PROD_QUALITE                                         */
/*==============================================================*/
-- alter table PROD_QUALITE modify PROD_QUALITE_ID int(3) not null;
execute immediate 'alter table PROD_QUALITE modify PROD_QUALITE VARCHAR2(100) not null';
execute immediate 'alter table PROD_QUALITE add PLATEFORME_ID NUMBER(22)';
execute immediate 'update PROD_QUALITE set plateforme_id = 1';
execute immediate 'alter table PROD_QUALITE modify PLATEFORME_ID not null';
dbms_output.put_line('Table PROD_QUALITE modifiée');

/*==============================================================*/
/* Table: PROD_TYPE                                             */
/*==============================================================*/
-- alter table PROD_TYPE modify PROD_TYPE_ID int(2);
execute immediate 'alter table PROD_TYPE rename column PROD_TYPE to TYPE';
execute immediate 'alter table PROD_TYPE modify TYPE not null';
execute immediate 'alter table PROD_TYPE add PLATEFORME_ID NUMBER(22)';
execute immediate 'update PROD_TYPE set plateforme_id = 1';
execute immediate 'alter table PROD_TYPE modify PLATEFORME_ID not null';
dbms_output.put_line('Table PROD_TYPE modifiée');

/*==============================================================*/
/* Table: PROFIL                                                */
/*==============================================================*/
-- alter table PROFIL modify PROFIL_ID int(10);
execute immediate 'alter table PROFIL rename column PROFIL_NOM to NOM';
execute immediate 'alter table PROFIL modify NOM not null';
execute immediate 'alter table PROFIL rename column PATIENT_ANONYME to ANONYME';
execute immediate 'alter table PROFIL add ADMIN NUMBER(1) default 0 not null';
execute immediate 'alter table PROFIL add ACCES_ADMINISTRATION NUMBER(1) default 0 not null';
execute immediate 'alter table PROFIL add column ARCHIVE number(1)';
execute immediate 'update PROFIL set ARCHIVE = 0';
execute immediate 'alter table PROFIL modify ARCHIVE number(1) not null';
execute immediate 'alter table PROFIL add column PLATEFORME_ID number(22)';
execute immediate 'update PROFIL set PLATEFORME_ID = 1';
execute immediate 'alter table PROFIL modify PLATEFORME_ID number(10) not null';
dbms_output.put_line('Table PROFIL modifiée');

/*DROPS*/
-- alter table PROFIL drop column PROFIL_NIVEAU;-- jamais utilisee
-- alter table PROFIL drop column PROFIL_STATISTIQUES;-- car suppression module stat

/*==============================================================*/
/* Table: PROFIL_CORRESP->DROIT_OBJET                           */
/*==============================================================*/
execute immediate 'alter table PROFIL_CORRESP rename to DROIT_OBJET';
execute immediate 'alter table DROIT_OBJET modify PROFIL_ID not null';
execute immediate 'alter table DROIT_OBJET rename column OBJET_ID to ENTITE_ID';
execute immediate 'alter table DROIT_OBJET modify ENTITE_ID not null';
execute immediate 'alter table DROIT_OBJET add OPERATION_TYPE_ID NUMBER(2)'; 
/*MIGRATION43: DROIT_OBJET.operation=migration droits 1, 2, 4, 8 vers la table OPERATION_TYPE*/
execute immediate 'update DROIT_OBJET set operation_type_id=1 where droit_niveau=1';
execute immediate 'update DROIT_OBJET set operation_type_id=3 where droit_niveau=2';
execute immediate 'update DROIT_OBJET set operation_type_id=5 where droit_niveau=4';
execute immediate 'update DROIT_OBJET set operation_type_id=7 where droit_niveau=8';
dbms_output.put_line('MIGRATION43-ORA: migration droits 1, 2, 4, 8 vers la table OPERATION_TYPE');
execute immediate 'alter table DROIT_OBJET modify OPERATION_TYPE_ID not null'; 
/*Ajout clef primaire*/
execute immediate 'alter table DROIT_OBJET add constraint PK_DROIT_OBJET primary key (PROFIL_ID,ENTITE_ID,OPERATION_TYPE_ID)';
dbms_output.put_line('Table PROFIL_CORRESP modifiée en DROIT_OBJET');

/*DROPS*/
-- alter table PROFIL drop column DROIT_NIVEAU;
-- alter table DROIT_OBJET drop column DROIT_NOM;-- jamais utilisee

/*==============================================================*/
/* Table: PROTOCOLE_TYPE                                        */
/*==============================================================*/
execute immediate 'CREATE TABLE PROTOCOLE_TYPE (
       PROTOCOLE_TYPE_ID NUMBER(22) NOT NULL
     , TYPE VARCHAR2(25) NOT NULL
     , PLATEFORME_ID NUMBER(22) NOT NULL
     , constraint PK_PROTOCOLE_TYPE primary key (PROTOCOLE_TYPE_ID)
)';
dbms_output.put_line('Table PROTOCOLE_TYPE créée');

/*==============================================================*/
/* Table: QUANTITE_UNITE -> UNITE                               */
/*==============================================================*/
execute immediate 'alter table QUANTITE_UNITE rename to UNITE';
-- alter table UNITE modify UNITE_ID int(2);
execute immediate 'alter table UNITE modify UNITE not null';
execute immediate 'alter table UNITE add TYPE VARCHAR2(15) default ''masse'' not null';
-- alter table UNITE add column CONTINU boolean not null default 0;
dbms_output.put_line('Table UNITE modifiée');

/*==============================================================*/
/* Table: RESULTAT                                              */
/*==============================================================*/
execute immediate 'CREATE TABLE RESULTAT (
  	RESULTAT_ID NUMBER(22) NOT NULL,
  	NOM_COLONNE VARCHAR2(40) NOT NULL,
  	TRI NUMBER(1) NOT NULL,
  	ORDRE_TRI NUMBER(5) NOT NULL,
  	POSITION NUMBER(5) NOT NULL,
  	FORMAT VARCHAR2(40),
  	CHAMP_ID NUMBER(22) NOT NULL,
  	AFFICHAGE_ID NUMBER(22) NOT NULL,
  	constraint PK_RESULTAT primary key (RESULTAT_ID)
)';
dbms_output.put_line('Table RESULTAT créée');

/*==============================================================*/
/* Table: RECHERCHE                                             */
/*==============================================================*/
execute immediate 'CREATE TABLE RECHERCHE (
  	RECHERCHE_ID NUMBER(22) NOT NULL,
  	CREATEUR_ID NUMBER(22) NOT NULL,
  	INTITULE VARCHAR2(100) NOT NULL,
  	AFFICHAGE_ID NUMBER(22) NOT NULL,
  	REQUETE_ID NUMBER(22) NOT NULL,
  	constraint PK_RECHERCHE primary key (RECHERCHE_ID)
)';
dbms_output.put_line('Table RECHERCHE créée');

/*==============================================================*/
/* Table: RECHERCHE _BANQUE                                     */
/*==============================================================*/
execute immediate 'CREATE TABLE RECHERCHE_BANQUE (
  	RECHERCHE_ID NUMBER(22) NOT NULL,
 	BANQUE_ID NUMBER(22) NOT NULL,
  	constraint PK_RECHERCHE_BANQUE primary key (RECHERCHE_ID, BANQUE_ID)
)';
dbms_output.put_line('Table RECHERCHE_BANQUE créée');

/*==============================================================*/
/* Table: REQUETE                                               */
/*==============================================================*/
execute immediate 'CREATE TABLE REQUETE (
  	REQUETE_ID NUMBER(22) NOT NULL,
  	CREATEUR_ID NUMBER(22) NOT NULL,
	BANQUE_ID NUMBER(22) NOT NULL,
 	INTITULE VARCHAR2(100) NOT NULL,
 	GROUPEMENT_RACINE_ID NUMBER(22),
  	constraint PK_REQUETE primary key (REQUETE_ID)
)';
dbms_output.put_line('Table REQUETE créée');

/*==============================================================*/
/* Table: RESERVATION                                           */
/*==============================================================*/
execute immediate 'CREATE TABLE RESERVATION (
       RESERVATION_ID NUMBER(22) NOT NULL
     , FIN DATE
     , DEBUT DATE
     , UTILISATEUR_ID NUMBER(22) NOT NULL
     , constraint PK_RESERVATION primary key (RESERVATION_ID)
)';
dbms_output.put_line('Table RESERVATION créée');

execute immediate 'CREATE SEQUENCE resSeq START WITH 1 INCREMENT BY 1 NOMAXVALUE'; 
/*MIGRATION56:  RESERVATION.echantillon=migration des informations de reservation depuis echantillon*/
execute immediate 'alter table RESERVATION add ECHANTILLON_ID NUMBER(22)';
execute immediate 'insert into RESERVATION (reservation_id, echantillon_id, utilisateur_id) 
	(SELECT resSeq.nextVal, E , U FROM 
		(SELECT e.echantillon_id E, o.utilisateur_id U FROM ECHANTILLON e, OPERATION o 
			WHERE e.objet_statut_id=3 AND o.objet_id=e.echantillon_id AND o.entite_id=3)
	)'; 
execute immediate 'update ECHANTILLON e set e.reservation_id=(SELECT r.reservation_id FROM RESERVATION r WHERE e.echantillon_id=r.echantillon_id)';
execute immediate 'alter table RESERVATION drop column ECHANTILLON_ID';
dbms_output.put_line('MIGRATION56-ORA: RESERVATION.echantillon=migration des informations de reservation depuis echantillon');

/*MIGRATION57:  RESERVATION.derive=migration des informations de reservation depuis derive*/
execute immediate 'alter table RESERVATION add PROD_DERIVE_ID NUMBER(22)';
execute immediate 'insert into RESERVATION (reservation_id, prod_derive_id, utilisateur_id) 
	(SELECT resSeq.nextVal, P , U FROM 
		(SELECT p.prod_derive_id P, o.utilisateur_id U FROM PROD_DERIVE p, OPERATION o 
			WHERE p.objet_statut_id=3 AND o.objet_id=p.prod_derive_id AND o.entite_id=8)
	)'; 
execute immediate 'update PROD_DERIVE p set p.reservation_id=(SELECT r.reservation_id FROM RESERVATION r WHERE p.prod_derive_id=r.prod_derive_id)';
execute immediate 'alter table RESERVATION drop column PROD_DERIVE_ID';
dbms_output.put_line('MIGRATION57-ORA: RESERVATION.derive=migration des informations de reservation depuis derive');

execute immediate 'DROP SEQUENCE resSeq'; 
-- 
-- update ECHANTILLON set objet_statut_id=null where objet_statut_id=3;
-- update PROD_DERIVE set objet_statut_id=null where objet_statut_id=3;
-- delete from OBJET_STATUT where statut='RESERVE';

/*==============================================================*/
/* Table: RISQUE                                               */
/*==============================================================*/
execute immediate 'CREATE TABLE RISQUE (
       RISQUE_ID NUMBER(22) NOT NULL
     , NOM VARCHAR2(50) NOT NULL
     , INFECTIEUX NUMBER(1) DEFAULT 0 NOT NULL
     , PLATEFORME_ID NUMBER(22) NOT NULL
     , constraint PK_RISQUE primary key (RISQUE_ID)
)';
dbms_output.put_line('Table RISQUE créée');

/*==============================================================*/
/* Table: RETOUR                                               */
/*==============================================================*/
execute immediate 'CREATE TABLE RETOUR (
       RETOUR_ID NUMBER(22) NOT NULL
     , OBJET_ID NUMBER(22) NOT NULL
     , ENTITE_ID NUMBER(4) NOT NULL
     , DATE_SORTIE DATE NOT NULL
     , DATE_RETOUR DATE
     , TEMP_MOYENNE FLOAT(12) NOT NULL
     , STERILE NUMBER(1)
	 , IMPACT NUMBER(1)
     , COLLABORATEUR_ID NUMBER(22)
     , OBSERVATIONS VARCHAR2(4000)
	 , OLD_EMPLACEMENT_ADRL VARCHAR2(100) DEFAULT NULL
     , CESSION_ID NUMBER(22)
     , TRANSFORMATION_ID NUMBER(22)
     , CONTENEUR_ID NUMBER
     , INCIDENT_ID NUMBER(22)
	 , OBJET_STATUT_ID NUMBER(22)
     , constraint PK_RETOUR primary key (RETOUR_ID)
)';
dbms_output.put_line('Table RETOUR créée');

execute immediate 'CREATE SEQUENCE retourSeq START WITH 1 INCREMENT BY 1 NOMAXVALUE'; 
execute immediate 'CREATE INDEX objdIdIdxRetour on RETOUR (objet_id)';

execute immediate 'create trigger retour_trigger
before insert on RETOUR
for each row
   begin
     select retourSeq.nextval into :new.retour_id from dual;
   end;/'

/*==============================================================*/
/* Table: SERVICE                                               */
/*==============================================================*/
-- alter table SERVICE modify SERVICE_ID int(10);
execute immediate 'alter table SERVICE rename column COORD_ID to COORDONNEE_ID';
execute immediate 'alter table SERVICE modify COORDONNEE_ID unique';
execute immediate 'alter table SERVICE rename column ETAB_ID to ETABLISSEMENT_ID';
execute immediate 'alter table SERVICE modify ETABLISSEMENT_ID not null';
execute immediate 'alter table SERVICE rename column SERVICE_NOM to NOM';
execute immediate 'alter table SERVICE modify NOM not null';
-- alter table SERVICE change SERVICE_DESCR DESCRIPTION varchar(100);
execute immediate 'alter table SERVICE add ARCHIVE NUMBER(1) default 0 not null';
dbms_output.put_line('Table SERVICE modifiée');

/*==============================================================*/
/* Table: SERVICE_COLLABORATEUR                                 */
/*==============================================================*/
execute immediate 'CREATE TABLE SERVICE_COLLABORATEUR (
       SERVICE_ID NUMBER(22) NOT NULL
     , COLLABORATEUR_ID NUMBER(22) NOT NULL
     , constraint PK_SERVICE_COLLABORATEUR primary key (SERVICE_ID, COLLABORATEUR_ID)
)';

/*MIGRATION58: SERVICE_COLLABORATEUR=modifie l'association en N-N*/
execute immediate 'insert into SERVICE_COLLABORATEUR (service_id, collaborateur_id) 
	(SELECT service_id, collaborateur_id FROM COLLABORATEUR)';
dbms_output.put_line('MIGRATION58-ORA: SERVICE_COLLABORATEUR=modifie l''association en N-N');

/*DROPS*/
-- alter table COLLABORATEUR drop SERVICE_ID;

/*==============================================================*/
/* Table: SPECIALITE                                            */
/*==============================================================*/
execute immediate 'alter table SPECIALITE rename column SPECIAL_ID to SPECIALITE_ID';
execute immediate 'alter table SPECIALITE rename column SPECIAL_NOM to NOM';
execute immediate 'alter table SPECIALITE modify NOM not null';
dbms_output.put_line('Table SPECIALITE modifiée');

/*==============================================================*/
/* Table: TABLE_ANNOTATION                                      */
/*==============================================================*/
execute immediate 'alter table TABLE_ANNOTATION rename column ID to TABLE_ANNOTATION_ID';
/*alter table TABLE_ANNOTATION drop key ID;*/
execute immediate 'alter table TABLE_ANNOTATION modify NOM VARCHAR2(50) not null';
execute immediate 'alter table TABLE_ANNOTATION add DESCRIPTION VARCHAR2(250)';
execute immediate 'alter table TABLE_ANNOTATION add ENTITE_ID NUMBER(4)';
execute immediate 'alter table TABLE_ANNOTATION add CATALOGUE_ID NUMBER(3)';
execute immediate 'alter table TABLE_ANNOTATION add PLATEFORME_ID NUMBER(22)';

/*MIGRATION59: TABLE_ANNOTATION.entite=migration de l'info contenue de la table BANQUE vers ENTITE_ID*/
execute immediate 'update TABLE_ANNOTATION set entite_id=1';
execute immediate 'update TABLE_ANNOTATION set plateforme_id=1';
execute immediate 'update TABLE_ANNOTATION t set t.entite_id=2 where t.table_annotation_id in (SELECT anno_biol from BANQUE)';
execute immediate 'update TABLE_ANNOTATION t set t.entite_id=3 where t.table_annotation_id in (SELECT anno_ech from BANQUE)';
execute immediate 'update TABLE_ANNOTATION t set t.entite_id=8 where t.table_annotation_id in (SELECT anno_derive from BANQUE)';
dbms_output.put_line('MIGRATION59-ORA: TABLE_ANNOTATION.entite=migration de l''info contenue de la table BANQUE vers ENTITE_ID');

execute immediate 'alter table TABLE_ANNOTATION modify ENTITE_ID not null';
dbms_output.put_line('Table TABLE_ANNOTATION modifiée');

/*==============================================================*/
/* Table: TABLE_ANNOTATION_BANQUE                               */
/*==============================================================*/
execute immediate 'CREATE TABLE TABLE_ANNOTATION_BANQUE (
       TABLE_ANNOTATION_ID NUMBER(22) NOT NULL
     , BANQUE_ID NUMBER(22) NOT NULL
     , ORDRE NUMBER(3) DEFAULT 1 NOT NULL
     , constraint PK_TABLE_ANNOTATION_BANQUE primary key (TABLE_ANNOTATION_ID, BANQUE_ID)
)';

/*MIGRATION60: TABLE_ANNOTATION_BANQUE=migration de l'info contenue vers la table BANQUE vers une association N-N*/
execute immediate 'insert into TABLE_ANNOTATION_BANQUE (table_annotation_id, banque_id, ordre) (select anno_clin, banque_id, 1 from BANQUE where anno_clin is not null)';
execute immediate 'insert into TABLE_ANNOTATION_BANQUE (table_annotation_id, banque_id, ordre) (select anno_biol, banque_id, 1 from BANQUE where anno_biol is not null)';
execute immediate 'insert into TABLE_ANNOTATION_BANQUE (table_annotation_id, banque_id, ordre) (select anno_ech, banque_id, 1 from BANQUE where anno_ech is not null)';
execute immediate 'insert into TABLE_ANNOTATION_BANQUE (table_annotation_id, banque_id, ordre) (select anno_derive, banque_id, 1 from BANQUE where anno_derive is not null)';
dbms_output.put_line('MIGRATION60-ORA: TABLE_ANNOTATION_BANQUE=migration de l''info contenue vers la table BANQUE vers une association N-N');

/*MIGRATION1: ANNOTATION_VALEUR=Rassemblement de toutes les valeurs des annotations en une seule table*/
execute immediate 'CREATE SEQUENCE valSeq START WITH 1 INCREMENT BY 1 NOMAXVALUE'; 
execute immediate 'insert into ANNOTATION_VALEUR (annotation_valeur_id, alphanum, objet_id, champ_annotation_id, banque_id) 
	(select valSeq.nextVal, V, O, C, B from (select a.valeur V, a.objet_id O, a.champ_annotation_id C, t.banque_id B
		from ANNOTATION_ALPHANUM a, CHAMP_ANNOTATION c, TABLE_ANNOTATION_BANQUE t 
			where a.champ_annotation_id=c.champ_annotation_id and c.table_annotation_id=t.table_annotation_id)
	)';
execute immediate 'insert into ANNOTATION_VALEUR (annotation_valeur_id, bool, objet_id, champ_annotation_id, banque_id) 
	(select valSeq.nextVal, 1, O, C, B from (select a.objet_id O, a.champ_annotation_id C, t.banque_id B
		from ANNOTATION_BOOL a, CHAMP_ANNOTATION c, TABLE_ANNOTATION_BANQUE t 
			where a.champ_annotation_id=c.champ_annotation_id and c.table_annotation_id=t.table_annotation_id and a.valeur = 1)
	)';
execute immediate 'insert into ANNOTATION_VALEUR (annotation_valeur_id, bool, objet_id, champ_annotation_id, banque_id) 
	(select valSeq.nextVal, 0, O, C, B from (select a.objet_id O, a.champ_annotation_id C, t.banque_id B
		from ANNOTATION_BOOL a, CHAMP_ANNOTATION c, TABLE_ANNOTATION_BANQUE t 
			where a.champ_annotation_id=c.champ_annotation_id and c.table_annotation_id=t.table_annotation_id and a.valeur = 2)
	)';
execute immediate 'insert into ANNOTATION_VALEUR (annotation_valeur_id, anno_date, objet_id, champ_annotation_id, banque_id) 
	(select valSeq.nextVal, V, O, C, B from (select a.valeur V, a.objet_id O, a.champ_annotation_id C, t.banque_id B
		from ANNOTATION_DATE a, CHAMP_ANNOTATION c, TABLE_ANNOTATION_BANQUE t 
			where a.champ_annotation_id=c.champ_annotation_id and c.table_annotation_id=t.table_annotation_id)
	)';
execute immediate 'insert into ANNOTATION_VALEUR (annotation_valeur_id, alphanum, objet_id, champ_annotation_id, banque_id) 
	(select valSeq.nextVal, V, O, C, B from (select REPLACE(a.valeur, '','',''.'') V, a.objet_id O, a.champ_annotation_id C, t.banque_id B
		from ANNOTATION_NUM a, CHAMP_ANNOTATION c, TABLE_ANNOTATION_BANQUE t 
			where a.champ_annotation_id=c.champ_annotation_id and c.table_annotation_id=t.table_annotation_id)
	)';
execute immediate 'insert into ANNOTATION_VALEUR (annotation_valeur_id, texte, objet_id, champ_annotation_id, banque_id) 
	(select valSeq.nextVal, V, O, C, B from (select a.valeur V, a.objet_id O, a.champ_annotation_id C, t.banque_id B
		from ANNOTATION_TEXTE a, CHAMP_ANNOTATION c, TABLE_ANNOTATION_BANQUE t 
			where a.champ_annotation_id=c.champ_annotation_id and c.table_annotation_id=t.table_annotation_id)
	)';
execute immediate 'insert into ANNOTATION_VALEUR (annotation_valeur_id, item_id, objet_id, champ_annotation_id, banque_id) 
	(select valSeq.nextVal, V, O, C, B from (select a.valeur V, a.objet_id O, a.champ_annotation_id C, t.banque_id B
		from ANNOTATION_THES a, CHAMP_ANNOTATION c, TABLE_ANNOTATION_BANQUE t 
			where a.champ_annotation_id=c.champ_annotation_id and c.table_annotation_id=t.table_annotation_id)
	)';
execute immediate 'DROP SEQUENCE valSeq'; 
dbms_output.put_line('MIGRATION1-ORA: ANNOTATION_VALEUR=Rassemblement de toutes les valeurs des annotations en une seule table');

/* null Date */
-- update ANNOTATION_VALEUR set anno_date=null where anno_date='0000-00-00 00:00:00';

execute immediate 'alter table ANNOTATION_VALEUR modify BANQUE_ID not null';

/*DROPS*/
-- alter table BANQUE drop column anno_clin;
-- alter table BANQUE drop column anno_biol;
-- alter table BANQUE drop column anno_ech;

/*==============================================================*/
/* Table: TABLE_ANNOTATION_TEMPLATE                             */
/*==============================================================*/
execute immediate 'CREATE TABLE TABLE_ANNOTATION_TEMPLATE (
  	TABLE_ANNOTATION_ID NUMBER(22) NOT NULL,
	TEMPLATE_ID NUMBER(22) NOT NULL,
	ORDRE NUMBER(2) NOT NULL,
  	constraint PK_TABLE_ANNOTATION_TEMPLATE primary key (TABLE_ANNOTATION_ID, TEMPLATE_ID)
)';
dbms_output.put_line('Table TABLE_ANNOTATION_TEMPLATE créée');


/*==============================================================*/
/* Table: TABLE_CODAGE                                          */
/*==============================================================*/
execute immediate 'CREATE TABLE  TABLE_CODAGE (
       TABLE_CODAGE_ID NUMBER(2) NOT NULL
     , NOM VARCHAR2(25) NOT NULL
     , VERSION VARCHAR2(10)
     , constraint PK_TABLE_CODAGE primary key (TABLE_CODAGE_ID)
)';
dbms_output.put_line('Table TABLE_CODAGE créée');

/*==============================================================*/
/* Table: TEMPERATURE                                           */
/*==============================================================*/
execute immediate 'CREATE TABLE TEMPERATURE (
  	TEMPERATURE_ID NUMBER(5) NOT NULL,
 	TEMPERATURE FLOAT(12) NOT NULL,
  	constraint PK_TEMPERATURE primary key (TEMPERATURE_ID)
)';
dbms_output.put_line('Table TEMPERATURE créée');

/*==============================================================*/
/* Table: TEMPLATE                                              */
/*==============================================================*/
execute immediate 'CREATE TABLE TEMPLATE (
       TEMPLATE_ID NUMBER(22) NOT NULL
	 , BANQUE_ID NUMBER(22) NOT NULL
     , NOM VARCHAR2(50) NOT NULL
     , ENTITE_ID NUMBER(4) NOT NULL
     , DESCRIPTION VARCHAR2(250) default null
	 , EN_TETE VARCHAR2(50) default null
	 , PIED_PAGE VARCHAR2(50) default null
     , constraint PK_TEMPLATE primary key (TEMPLATE_ID)
)';
dbms_output.put_line('Table TEMPLATE créée');

/*==============================================================*/
/* Table: TERMINALE_NUMEROTATION                                */
/*==============================================================*/
execute immediate 'CREATE TABLE TERMINALE_NUMEROTATION (
       TERMINALE_NUMEROTATION_ID NUMBER(10) NOT NULL
     , LIGNE VARCHAR2(3) NOT NULL
     , COLONNE VARCHAR2(3) NOT NULL
     , constraint PK_TERMINALE_NUMEROTATION primary key (TERMINALE_NUMEROTATION_ID)
)';
dbms_output.put_line('Table TERMINALE_NUMEROTATION créée');

/*==============================================================*/
/* Table: TIMER                                                 */
/*==============================================================*/
execute immediate 'CREATE TABLE TIMER (
       TIMER_ID NUMBER(22) NOT NULL
     , MIN NUMBER(3)
     , HEURE NUMBER(3)
     , NUM_JOUR_MOIS NUMBER(2)
     , NUM_MOIS NUMBER(2)
     , NUM_JOUR_SEM NUMBER(2)
     , constraint PK_TIMER primary key (TIMER_ID)
)';
dbms_output.put_line('Table TIMER créée');

/*==============================================================*/
/* Table: TRANSFORMATION                                        */
/*==============================================================*/
execute immediate 'CREATE TABLE TRANSFORMATION (
       TRANSFORMATION_ID NUMBER(22) NOT NULL
     , OBJET_ID NUMBER(22) NOT NULL
     , ENTITE_ID NUMBER(4) NOT NULL
     , QUANTITE FLOAT(12)
     , QUANTITE_UNITE_ID NUMBER(22)
     , constraint PK_TRANSFORMATION primary key (TRANSFORMATION_ID)
)';
dbms_output.put_line('Table TRANSFORMATION créée');

/*MIGRATION61: TRANSFORMER=migration du lien PROD_DERIVE.ECHAN_ID vers TRANSFORMATION*/
execute immediate 'CREATE SEQUENCE transSeq START WITH 1 INCREMENT BY 1 NOMAXVALUE';
execute immediate 'insert into TRANSFORMATION (transformation_id, objet_id, entite_id) 
	(SELECT transSeq.nextVal, E, 3 FROM (SELECT distinct(echan_id) E from PROD_DERIVE))';
execute immediate 'update PROD_DERIVE p set p.transformation_id=(SELECT t.transformation_id FROM TRANSFORMATION t WHERE p.echan_id=t.objet_id)';
-- alter table PROD_DERIVE modify TRANSFORMATION_ID int(10);
execute immediate 'DROP SEQUENCE transSeq';
dbms_output.put_line('MIGRATION61-ORA: TRANSFORMER=migration du lien PROD_DERIVE.ECHAN_ID vers TRANSFORMATION');

/*DROPS*/
-- alter table PROD_DERIVE drop column echan_id;

/*==============================================================*/
/* Table: TRANSPORTEUR                                          */
/*==============================================================*/
-- alter table TRANSPORTEUR modify TRANSPORTEUR_ID int(10);
execute immediate 'alter table TRANSPORTEUR rename column COORD_ID to COORDONNEE_ID';
execute immediate 'alter table TRANSPORTEUR modify COORDONNEE_ID unique';
execute immediate 'alter table TRANSPORTEUR rename column TRANSPORTEUR_NOM to NOM';
execute immediate 'alter table TRANSPORTEUR modify NOM not null';
execute immediate 'alter table TRANSPORTEUR add ARCHIVE NUMBER(1) default 0 not null';
dbms_output.put_line('Table TRANSPORTEUR modifiée');

/*==============================================================*/
/* Table: UTILISATEUR                                           */
/*==============================================================*/
execute immediate 'alter table UTILISATEUR rename column USER_ID to UTILISATEUR_ID';
execute immediate 'alter table UTILISATEUR modify LOGIN not null';
execute immediate 'alter table UTILISATEUR modify PASSWORD not null';
execute immediate 'alter table UTILISATEUR add ARCHIVE NUMBER(1) default 0 not null';
execute immediate 'alter table UTILISATEUR add EMAIL VARCHAR2(50)';
execute immediate 'alter table UTILISATEUR add TIMEOUT date';
execute immediate 'alter table UTILISATEUR add COLLABORATEUR_ID NUMBER(22)';
execute immediate 'alter table UTILISATEUR add SUPER NUMBER(1) default 0 not null';
execute immediate 'alter table UTILISATEUR add PLATEFORME_ORIG_ID NUMBER(22)';
dbms_output.put_line('Table UTILISATEUR modifiée');

-- alter table UTILISATEUR drop column COMPTE_ACTIF;

/*==============================================================*/
/* Table: IMPORT_TEMPLATE                                       */
/*==============================================================*/
execute immediate 'CREATE TABLE IMPORT_TEMPLATE (
       IMPORT_TEMPLATE_ID NUMBER(22) NOT NULL
     , BANQUE_ID NUMBER(22) NOT NULL
	 , NOM VARCHAR2(50) NOT NULL
     , DESCRIPTION VARCHAR2(250)
     , IS_EDITABLE NUMBER(1) DEFAULT 1
     , DERIVE_PARENT_ENTITE_ID NUMBER(22)
     , constraint PK_IMPORT_TEMPLATE primary key (IMPORT_TEMPLATE_ID)
)';
dbms_output.put_line('Table IMPORT_TEMPLATE créée');

/*==============================================================*/
/* Table: IMPORT_TEMPLATE_ENTITE                                */
/*==============================================================*/
execute immediate 'CREATE TABLE IMPORT_TEMPLATE_ENTITE (
  	IMPORT_TEMPLATE_ID NUMBER(22) NOT NULL,
	ENTITE_ID NUMBER(22) NOT NULL,
  	constraint PK_IMPORT_TEMPLATE_ENTITE primary key (IMPORT_TEMPLATE_ID, ENTITE_ID)
)';
dbms_output.put_line('Table IMPORT_TEMPLATE_ENTITE créée');

/*==============================================================*/
/* Table: IMPORT_COLONNE                                        */
/*==============================================================*/
execute immediate 'CREATE TABLE IMPORT_COLONNE (
	 IMPORT_COLONNE_ID NUMBER(22) NOT NULL
     , IMPORT_TEMPLATE_ID NUMBER(22) NOT NULL
	 , CHAMP_ID NUMBER(22) NOT NULL
	 , NOM VARCHAR2(50) NOT NULL
	 , ORDRE NUMBER(2) DEFAULT 0
     , constraint PK_IMPORT_COLONNE primary key (IMPORT_COLONNE_ID)
)';
dbms_output.put_line('Table IMPORT_COLONNE créée');

/*==============================================================*/
/* Table: IMPORT_HISTORIQUE                                     */
/*==============================================================*/
execute immediate 'CREATE TABLE IMPORT_HISTORIQUE (
	 IMPORT_HISTORIQUE_ID NUMBER(22) NOT NULL
     , IMPORT_TEMPLATE_ID NUMBER(22) NOT NULL
	 , UTILISATEUR_ID NUMBER(22) NOT NULL
	 , DATE_ DATE NOT NULL
     , constraint PK_IMPORT_HISTORIQUE primary key (IMPORT_HISTORIQUE_ID)
)';
dbms_output.put_line('Table IMPORT_HISTORIQUE créée');

/*==============================================================*/
/* Table: IMPORTATION                                           */
/*==============================================================*/
execute immediate 'alter table IMPORTATION rename column ID to IMPORTATION_ID';
execute immediate 'alter table IMPORTATION rename column ID_ENTITE to OBJET_ID';
execute immediate 'update IMPORTATION set NOM_ENTITE = ''1'' where NOM_ENTITE=''Patient''';
execute immediate 'update IMPORTATION set NOM_ENTITE = ''2'' where NOM_ENTITE=''Prelevement''';
execute immediate 'alter table IMPORTATION add ENTITE_ID NUMBER(22)';
execute immediate 'update IMPORTATION set entite_id=nom_entite';
execute immediate 'alter table IMPORTATION modify ENTITE_ID not null';
execute immediate 'alter table IMPORTATION add IMPORT_HISTORIQUE_ID NUMBER(22)';
execute immediate 'alter table IMPORTATION modify DATE_IMPORT date NULL';
execute immediate 'alter table IMPORTATION drop column NOM_ENTITE';

dbms_output.put_line('Table IMPORTATION modifiée');

/*==============================================================*/
/* Table: VERSION                                               */
/*==============================================================*/
execute immediate 'CREATE TABLE VERSION (
	VERSION_ID NUMBER(10) NOT NULL
    , VERSION VARCHAR2(20) NOT NULL
    , DATE_ DATE NOT NULL
	, NOM_SITE VARCHAR2(100)
	, constraint PK_VERSION primary key (VERSION_ID)
)';
dbms_output.put_line('Table VERSION créée');

/*==============================================================*/
/* Table: EMPLACEMENT                                           */
/*==============================================================*/
execute immediate 'CREATE TABLE EMPLACEMENT (
       EMPLACEMENT_ID NUMBER(22) NOT NULL
     , TERMINALE_ID NUMBER(22) NOT NULL
     , POSITION NUMBER(10) NOT NULL
     , OBJET_ID NUMBER(22)
     , ENTITE_ID NUMBER(4)
     , VIDE NUMBER(1) DEFAULT 1
     , ADRP VARCHAR2(25)
     , ADRL VARCHAR2(50)
     , constraint PK_EMPLACEMENT primary key (EMPLACEMENT_ID)
)';
dbms_output.put_line('Table EMPLACEMENT créée');

/*==============================================================*/
/* Table: LOGICIEL                                              */
/*==============================================================*/
execute immediate 'CREATE TABLE LOGICIEL (
  	LOGICIEL_ID NUMBER(22) NOT NULL,
  	NOM VARCHAR2(50) NOT NULL,
	EDITEUR VARCHAR2(50),
	VERSION VARCHAR2(50),
  	constraint PK_LOGICIEL primary key (LOGICIEL_ID)
)'; 
dbms_output.put_line('Table LOGICIEL créée.');

/*==============================================================*/
/* Table: EMETTEUR                                              */
/*==============================================================*/
execute immediate 'CREATE TABLE EMETTEUR (
  	EMETTEUR_ID NUMBER(22) NOT NULL,
	LOGICIEL_ID NUMBER(22) NOT NULL,
  	IDENTIFICATION VARCHAR2(50) NOT NULL,
	SERVICE VARCHAR2(50),
	OBSERVATIONS VARCHAR2(250),
  	constraint PK_EMETTEUR primary key (EMETTEUR_ID)
)'; 
dbms_output.put_line('Table EMETTEUR créée.');

/*==============================================================*/
/* Table: DOSSIER_EXTERNE                                       */
/*==============================================================*/
execute immediate 'CREATE TABLE DOSSIER_EXTERNE (
	DOSSIER_EXTERNE_ID NUMBER(22) NOT NULL,
  	EMETTEUR_ID NUMBER(22) NOT NULL,
	IDENTIFICATION_DOSSIER VARCHAR2(100) NOT NULL,
  	DATE_OPERATION DATE,
	OPERATION VARCHAR2(50),
  	constraint PK_DOSSIER_EXTERNE primary key (DOSSIER_EXTERNE_ID)
)'; 
dbms_output.put_line('Table DOSSIER_EXTERNE créée.');

/*==============================================================*/
/* Table: BLOC_EXTERNE                                          */
/*==============================================================*/
execute immediate 'CREATE TABLE BLOC_EXTERNE (
	BLOC_EXTERNE_ID NUMBER(22) NOT NULL,
	DOSSIER_EXTERNE_ID NUMBER(22) NOT NULL,
  	ENTITE_ID NUMBER(22) NOT NULL,
	ORDRE NUMBER(10) NOT NULL,
  	constraint PK_BLOC_EXTERNE primary key (BLOC_EXTERNE_ID)
)'; 
dbms_output.put_line('Table BLOC_EXTERNE créée.');

/*==============================================================*/
/* Table: VALEUR_EXTERNE                                        */
/*==============================================================*/
execute immediate 'CREATE TABLE VALEUR_EXTERNE (
	VALEUR_EXTERNE_ID NUMBER(22) NOT NULL,
	BLOC_EXTERNE_ID NUMBER(22) NOT NULL,
	VALEUR VARCHAR2(250),
  	CHAMP_ENTITE_ID NUMBER(22),
	CHAMP_ANNOTATION_ID NUMBER(22),
  	constraint PK_VALEUR_EXTERNE primary key (VALEUR_EXTERNE_ID)
)'; 
dbms_output.put_line('Table VALEUR_EXTERNE créée.');

/*==============================================================*/
/* Table: CONFORMITE_TYPE                                       */
/*==============================================================*/
execute immediate 'CREATE TABLE CONFORMITE_TYPE (
  	CONFORMITE_TYPE_ID NUMBER(22) NOT NULL,
  	CONFORMITE_TYPE VARCHAR2(50) NOT NULL,
	ENTITE_ID NUMBER(22) NOT NULL,
  	constraint PK_CONFORMITE_TYPE primary key (CONFORMITE_TYPE_ID)
)'; 
dbms_output.put_line('Table CONFORMITE_TYPE créée.');

/*==============================================================*/
/* Table: NON_CONFORMITE                                        */
/*==============================================================*/
execute immediate 'CREATE TABLE NON_CONFORMITE (
  	NON_CONFORMITE_ID NUMBER(22) NOT NULL,
 	CONFORMITE_TYPE_ID NUMBER(22) NOT NULL,
	PLATEFORME_ID NUMBER(22) NOT NULL,
  	NOM VARCHAR2(50) NOT NULL,
  	constraint PK_NON_CONFORMITE primary key (NON_CONFORMITE_ID)
)'; 
dbms_output.put_line('Table NON_CONFORMITE créée.');

/*==============================================================*/
/* Table: OBJET_NON_CONFORME                                    */
/*==============================================================*/
execute immediate 'CREATE TABLE OBJET_NON_CONFORME (
  	OBJET_NON_CONFORME_ID NUMBER(22) NOT NULL,
 	NON_CONFORMITE_ID NUMBER(22) NOT NULL,
	OBJET_ID NUMBER(22) NOT NULL,
	ENTITE_ID NUMBER(22) NOT NULL,
  	constraint PK_OBJET_NON_CONFORME primary key (OBJET_NON_CONFORME_ID)
)'; 
dbms_output.put_line('Table OBJET_NON_CONFORME créée.');

execute immediate 'CREATE SEQUENCE objetNonConformeSeq START WITH (select max(objet_non_conforme_id)+1 from OBJET_NON_CONFORME) INCREMENT BY 1 NOMAXVALUE'; 

-- 2.0.13.1
/*==============================================================*/
/* Table: CONSULTATION                                          */
/*==============================================================*/
execute immediate 'CREATE TABLE CONSULTATION_INTF (
  	CONSULTATION_INTF_ID NUMBER(22) NOT NULL,
  	IDENTIFICATION VARCHAR2(100) NOT NULL,
	DATE_ DATE NOT NULL,
 	UTILISATEUR_ID NUMBER(22) NOT NULL,
 	EMETTEUR_IDENT VARCHAR2(100) NOT NULL,
  	constraint PK_CONSULTATION_ID primary key (CONSULTATION_INTF_ID)
)';

/*DROPS*/
-- alter table ECHANTILLON drop column ADRP_STOCK;
-- alter table PROD_DERIVE drop column ADRP_STOCK;
-- alter table TERMINALE drop column ADRP;
-- alter table ECHANTILLON drop column ECHAN_ADRL_STOCK;-- dangereux car pas mis à jour
-- alter table PROD_DERIVE drop column PROD_ADRL_STOCK;-- dangereux car pas mis à jour
-- alter table TERMINALE drop BOITE_ADR_LOGIQUE;-- dangereux car pas mis à jour

/*==============================================================*/
/* TABLE DROPS                                                  */
/*==============================================================*/
-- drop table ACTION;
-- drop table ANNO_ECHA;
-- drop table CODIFICATION;
-- drop table CONSENTIR;
-- drop table DICTIONNAIRE;
-- drop table IDENTITE_TUBE_RFID;
-- drop table HISTORIQUE;
-- drop table FILTRE;
-- drop table LATERALITE;
-- drop table ORGANE;
-- drop table PRELE_UNITE;
-- drop table PROD_STATUT;
-- drop table PD_QUANTITE_UNITE;
-- drop table PD_VOL_UNITE;
-- drop table PD_CON_UNITE;
-- drop table PREPA_TYPE;
-- drop table SERVEUR_IDENTITE;
-- drop table VALEUR;
-- drop table ANNOTATION_ALPHANUM;
-- drop table ANNOTATION_BOOL;
-- drop table ANNOTATION_DATE;
-- drop table ANNOTATION_NUM;
-- drop table ANNOTATION_TEXTE;
-- drop table ANNOTATION_THES;
-- drop table BOITE_TYPE;

dbms_output.put_line('******************************');
dbms_output.put_line('Migration version 2.0 terminee');
dbms_output.put_line('******************************');

execute immediate 'create or replace 
TRIGGER  BI_OPERATION
  before insert on OPERATION              
  for each row  
begin   
  if :NEW.OPERATION_ID is null then 
    select OPSEQ.nextval into :NEW.OPERATION_ID from dual; 
  end if; 
end;';

execute immediate 'create or replace 
TRIGGER  BI_RETOUR
  before insert on RETOUR              
  for each row  
begin   
  if :NEW.RETOUR_ID is null then 
    select RETOURSEQ.nextval into :NEW.RETOUR_ID from dual; 
  end if; 
end;';

execute immediate 'create or replace 
TRIGGER  BI_ECHANTILLON
  before insert on ECHANTILLON             
  for each row  
begin   
  if :NEW.ECHANTILLON_ID is null then 
    select ECHANTILLONSEQ.nextval into :NEW.ECHANTILLON_ID from dual; 
  end if; 
end;';

execute immediate 'create or replace 
TRIGGER  BI_ANNOTATION_VALEUR
  before insert on ANNOTATION_VALEUR            
  for each row  
begin   
  if :NEW.ANNOTATION_VALEUR_ID is null then 
    select annoValSeq.nextval into :NEW.ANNOTATION_VALEUR_ID from dual; 
  end if; 
end;';

 execute immediate 'create or replace 
TRIGGER  BI_CODE_ASSIGNE
  before insert on CODE_ASSIGNE            
  for each row  
begin   
  if :NEW.CODE_ASSIGNE_ID is null then 
    select codeAssigneSeq.nextval into :NEW.CODE_ASSIGNE_ID from dual; 
  end if; 
end;';

execute immediate 'create or replace 
TRIGGER  BI_OBJET_NON_CONFORME
  before insert on OBJET_NON_CONFORME           
  for each row  
begin   
  if :NEW.OBJET_NON_CONFORME_ID is null then 
    select objetNonConformeSeq.nextval into :NEW.OBJET_NON_CONFORME_ID from dual; 
  end if; 
end;';

end;
/
exit 0;
