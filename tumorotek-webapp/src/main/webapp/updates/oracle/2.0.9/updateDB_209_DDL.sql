-- augmentation taille thesaurus
alter table CESSION_EXAMEN modify EXAMEN varchar2(200);
alter table CONDIT_MILIEU modify MILIEU varchar2(200);
alter table MODE_PREPA modify NOM varchar2(200);
alter table MODE_PREPA_DERIVE modify NOM varchar2(200);
alter table DESTRUCTION_MOTIF modify MOTIF varchar2(200);
alter table NATURE modify NATURE varchar2(200);
alter table NON_CONFORMITE modify NOM varchar2(200);
alter table PROTOCOLE modify NOM varchar2(200);
alter table ECHAN_QUALITE modify ECHAN_QUALITE varchar2(200);
alter table PROD_QUALITE modify PROD_QUALITE varchar2(200);
alter table RISQUE modify NOM varchar2(200);
alter table SPECIALITE modify NOM varchar2(200);
alter table CONSENT_TYPE modify TYPE varchar2(200);
alter table ECHANTILLON_TYPE modify TYPE varchar2(200);
alter table ENCEINTE_TYPE modify TYPE varchar2(200);
alter table CONDIT_TYPE modify TYPE varchar2(200);
alter table CONTENEUR_TYPE modify TYPE varchar2(200);
alter table PRELEVEMENT_TYPE modify TYPE varchar2(200);
alter table PROD_TYPE modify TYPE varchar2(200);
alter table PROTOCOLE_TYPE modify TYPE varchar2(200);

-- correctif Import Oracle
alter table IMPORTATION drop column NOM_ENTITE;
