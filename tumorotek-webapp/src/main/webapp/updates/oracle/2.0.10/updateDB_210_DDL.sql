alter table UTILISATEUR add plateforme_orig_id NUMBER(10);

ALTER TABLE UTILISATEUR
  ADD CONSTRAINT FK_UTIL_PF_ORIG_ID
      FOREIGN KEY (PLATEFORME_ORIG_ID)
      REFERENCES PLATEFORME (PLATEFORME_ID);

ALTER TABLE CHAMP
   ADD CONSTRAINT FK_CHAMP_CHAMP_PARENT_ID
        FOREIGN KEY (CHAMP_PARENT_ID)
        REFERENCES CHAMP (CHAMP_ID);

DECLARE
    rseq INTEGER;
BEGIN
   select max(retour_id) + 1
   into   rseq
   from   RETOUR;

    execute immediate 'CREATE SEQUENCE retourSeq START WITH ' || rseq ||
                       ' INCREMENT BY 1 NOMAXVALUE';
END;
/

create or replace 
TRIGGER  BI_RETOUR
  before insert on RETOUR              
  for each row  
begin   
  if :NEW.RETOUR_ID is null then 
    select RETOURSEQ.nextval into :NEW.RETOUR_ID from dual; 
  end if; 
end;
/

ALTER TABLE RETOUR DROP CONSTRAINT FK_RETOUR_OLD_EMPLACEMENT_ID;
       
alter table RETOUR rename column OLD_EMPLACEMENT_ID to CONTENEUR_ID;

ALTER TABLE RETOUR
  ADD CONSTRAINT FK_RETOUR_CONTENEUR_ID
      FOREIGN KEY (CONTENEUR_ID)
      REFERENCES CONTENEUR (CONTENEUR_ID);
      
alter table RETOUR add IMPACT NUMBER(1);

ALTER TABLE RETOUR add OBJET_STATUT_ID NUMBER(3);

ALTER TABLE RETOUR
  ADD CONSTRAINT FK_RETOUR_OBJET_STATUT_ID
      FOREIGN KEY (OBJET_STATUT_ID)
      REFERENCES OBJET_STATUT (OBJET_STATUT_ID);

DECLARE
    oseq INTEGER;
BEGIN
   select max(operation_id) + 1
   into   oseq
   from   OPERATION;

    execute immediate 'CREATE SEQUENCE opSeq START WITH ' || oseq ||
                       ' INCREMENT BY 1 NOMAXVALUE';
END;
/

create or replace 
TRIGGER  BI_OPERATION
  before insert on OPERATION              
  for each row  
begin   
  if :NEW.OPERATION_ID is null then 
    select OPSEQ.nextval into :NEW.OPERATION_ID from dual; 
  end if; 
end;
/

ALTER TABLE VALEUR_EXTERNE ADD CONTENU BLOB;

ALTER TABLE CONFORMITE_TYPE add ENTITE_ID NUMBER(22);
update CONFORMITE_TYPE set entite_id=2 where conformite_type_id=1;
update CONFORMITE_TYPE set entite_id=3 where conformite_type_id=2;
update CONFORMITE_TYPE set entite_id=3 where conformite_type_id=3;
ALTER TABLE CONFORMITE_TYPE modify ENTITE_ID NUMBER(22) not null;

ALTER TABLE CONFORMITE_TYPE
  ADD CONSTRAINT FK_CONFORMITE_TYPE_ENTITE_ID
      FOREIGN KEY (ENTITE_ID)
      REFERENCES ENTITE (ENTITE_ID);
      
-- FLOAT to DECIMAL
- FLOAT to DECIMAL
create table tempDec1 as select prelevement_id, quantite from PRELEVEMENT where quantite is not null;
alter table tempDec1 add constraint pk_dec1 primary key (prelevement_id);
alter table tempDec1 add constraint fk_prel_id foreign key (prelevement_id) references PRELEVEMENT (prelevement_id);
update PRELEVEMENT set quantite = null;
alter table PRELEVEMENT modify QUANTITE decimal(12,3);
update PRELEVEMENT p set p.quantite=(select t.quantite from tempDec1 t where t.prelevement_id=p.prelevement_id); 
drop table tempDec1;

create table tempDec2 as select echantillon_id, quantite from ECHANTILLON where quantite is not null;
alter table tempDec2 add constraint pk_dec2 primary key (echantillon_id);
alter table tempDec2 add constraint fk_echan1_id foreign key (echantillon_id) references ECHANTILLON (echantillon_id);
update ECHANTILLON set quantite = null;
alter table ECHANTILLON modify QUANTITE decimal(12,3);
update ECHANTILLON e set e.quantite=(select t.quantite from tempDec2 t where t.echantillon_id=e.echantillon_id); 
drop table tempDec2;

create table tempDec3 as select echantillon_id, quantite_init from ECHANTILLON where quantite_init is not null;
alter table tempDec3 add constraint pk_dec3 primary key (echantillon_id);
alter table tempDec3 add constraint fk_echan2_id foreign key (echantillon_id) references ECHANTILLON (echantillon_id);
update ECHANTILLON set quantite_init = null;
alter table ECHANTILLON modify QUANTITE_INIT decimal(12,3);
update ECHANTILLON e set e.quantite_init=(select t.quantite_init from tempDec3 t where t.echantillon_id=e.echantillon_id); 
drop table tempDec3;

create table tempDec4 as select echantillon_id, delai_cgl from ECHANTILLON where delai_cgl is not null;
alter table tempDec4 add constraint pk_dec4 primary key (echantillon_id);
alter table tempDec4 add constraint fk_echan3_id foreign key (echantillon_id) references ECHANTILLON (echantillon_id);
update ECHANTILLON set delai_cgl = null;
alter table ECHANTILLON modify DELAI_CGL decimal(12,3);
update ECHANTILLON e set e.delai_cgl=(select t.delai_cgl from tempDec4 t where t.echantillon_id=e.echantillon_id); 
drop table tempDec4;

create table tempDec5 as select prod_derive_id, volume from PROD_DERIVE where volume is not null;
alter table tempDec5 add constraint pk_dec5 primary key (prod_derive_id);
alter table tempDec5 add constraint fk_derive1_id foreign key (prod_derive_id) references PROD_DERIVE (prod_derive_id);
update PROD_DERIVE set volume = null;
alter table PROD_DERIVE modify VOLUME decimal(12,3);
update PROD_DERIVE p set p.volume=(select t.volume from tempDec5 t where t.prod_derive_id=p.prod_derive_id); 
drop table tempDec5;

create table tempDec6 as select prod_derive_id, volume_init from PROD_DERIVE where volume_init is not null;
alter table tempDec6 add constraint pk_dec6 primary key (prod_derive_id);
alter table tempDec6 add constraint fk_derive2_id foreign key (prod_derive_id) references PROD_DERIVE (prod_derive_id);
update PROD_DERIVE set volume_init = null;
alter table PROD_DERIVE modify VOLUME_INIT decimal(12,3);
update PROD_DERIVE p set p.volume_init=(select t.volume_init from tempDec6 t where t.prod_derive_id=p.prod_derive_id); 
drop table tempDec6;

create table tempDec7 as select prod_derive_id, quantite from PROD_DERIVE where quantite is not null;
alter table tempDec7 add constraint pk_dec7 primary key (prod_derive_id);
alter table tempDec7 add constraint fk_derive3_id foreign key (prod_derive_id) references PROD_DERIVE (prod_derive_id);
update PROD_DERIVE set quantite = null;
alter table PROD_DERIVE modify QUANTITE decimal(12,3);
update PROD_DERIVE p set p.quantite=(select t.quantite from tempDec7 t where t.prod_derive_id=p.prod_derive_id); 
drop table tempDec7;

create table tempDec8 as select prod_derive_id, quantite_init from PROD_DERIVE where quantite_init is not null;
alter table tempDec8 add constraint pk_dec8 primary key (prod_derive_id);
alter table tempDec8 add constraint fk_derive4_id foreign key (prod_derive_id) references PROD_DERIVE (prod_derive_id);
update PROD_DERIVE set quantite_init = null;
alter table PROD_DERIVE modify QUANTITE_INIT decimal(12,3);
update PROD_DERIVE p set p.quantite_init=(select t.quantite_init from tempDec8 t where t.prod_derive_id=p.prod_derive_id); 
drop table tempDec8;

create table tempDec9 as select prod_derive_id, conc from PROD_DERIVE where conc is not null;
alter table tempDec9 add constraint pk_dec9 primary key (prod_derive_id);
alter table tempDec9 add constraint fk_derive5_id foreign key (prod_derive_id) references PROD_DERIVE (prod_derive_id);
update PROD_DERIVE set conc = null;
alter table PROD_DERIVE modify CONC decimal(12,3);
update PROD_DERIVE p set p.conc=(select t.conc from tempDec9 t where t.prod_derive_id=p.prod_derive_id); 
drop table tempDec9;

create table tempDec10 as select cession_id, objet_id, entite_id, quantite from CEDER_OBJET where quantite is not null;
create index pk_dec10 on tempDec10 (cession_id);
update CEDER_OBJET set quantite = null;
alter table CEDER_OBJET modify QUANTITE decimal(12,3);
update CEDER_OBJET c set c.quantite=(select t.quantite from tempDec10 t where t.cession_id=c.cession_id and t.objet_id=c.objet_id and t.entite_id=c.entite_id); 
drop table tempDec10;

create table tempDec11 as select transformation_id, quantite from TRANSFORMATION where quantite is not null;
create index pk_dec11 on tempDec11 (transformation_id);
update TRANSFORMATION set quantite = null;
alter table TRANSFORMATION modify QUANTITE decimal(12,3);
update TRANSFORMATION f set f.quantite=(select t.quantite from tempDec11 t where t.transformation_id=f.transformation_id); 
drop table tempDec11;


-- verifs
select count(*) from PRELEVEMENT where quantite is not null;
select count(*) from ECHANTILLON where quantite is not null;
select count(*) from ECHANTILLON where quantite_init is not null;
select count(*) from ECHANTILLON where delai_cgl is not null;
select count(*) from PROD_DERIVE where quantite is not null;
select count(*) from PROD_DERIVE where quantite_init is not null;
select count(*) from PROD_DERIVE where volume is not null;
select count(*) from PROD_DERIVE where volume_init is not null;
select count(*) from PROD_DERIVE where conc is not null;
select count(*) from CEDER_OBJET where quantite is not null;
select count(*) from TRANSFORMATION where quantite is not null;

alter table BANQUE add DEFAUT_MALADIE_CODE varchar2(50);

-- incident
alter table INCIDENT modify CONTENEUR_ID null;
alter table INCIDENT add ENCEINTE_ID number(22);
ALTER TABLE INCIDENT ADD CONSTRAINT FK_INCIDENT_ENCEINTE_ID 
	FOREIGN KEY (ENCEINTE_ID) REFERENCES ENCEINTE (ENCEINTE_ID);
alter table INCIDENT add TERMINALE_ID number(22);
ALTER TABLE INCIDENT ADD CONSTRAINT FK_INCIDENT_TERMINALE_ID 
	FOREIGN KEY (TERMINALE_ID) REFERENCES TERMINALE (TERMINALE_ID);
	
-- conteneur
alter table CONTENEUR add PLATEFORME_ORIG_ID NUMBER(2) default 1 not null;
ALTER TABLE CONTENEUR
  ADD CONSTRAINT FK_CONTENEUR_PF_ORIG_ID
      FOREIGN KEY (PLATEFORME_ORIG_ID)
      REFERENCES PLATEFORME (PLATEFORME_ID);
            
? pensez à MAJ conteneurs Lyon Bordeaux HematoSLS CelluloSls Nîmes
select c.conteneur_id, c.nom, b.plateforme_id from CONTENEUR c 
	JOIN CONTENEUR_BANQUE cb on cb.conteneur_id=c.conteneur_id 
	JOIN BANQUE b on b.banque_id = cb.banque_id order by c.conteneur_id;
	
select c.conteneur_id from CONTENEUR c 
	JOIN CONTENEUR_BANQUE cb on cb.conteneur_id=c.conteneur_id 
	JOIN BANQUE b on b.banque_id = cb.banque_id group by c.conteneur_id having count(distinct (b.plateforme_id)) > 1;
      
alter table CONTENEUR_PLATEFORME add PARTAGE number(1) default 0 not null;

-- delete pfs origine
-- delete p.* from CONTENEUR c join CONTENEUR_PLATEFORME p on c.conteneur_id = p.conteneur_id where c.plateforme_orig_id = p.plateforme_id;

create index objdIdIdxRetour on RETOUR (objet_id);

-- correction nouvelle base
create unique index echanEmpUIdx on ECHANTILLON(EMPLACEMENT_ID);
create unique index prodEmpUIdx on PROD_DERIVE(EMPLACEMENT_ID);

-- 2.0.10.3
CREATE TABLE RECEPTEUR (
  RECEPTEUR_ID NUMBER(22) NOT NULL,
  LOGICIEL_ID NUMBER(22) NOT NULL,
  IDENTIFICATION VARCHAR2(50) NOT NULL,
  OBSERVATIONS VARCHAR2(4000),
  ENVOI_NUM NUMBER(22),
  constraint PK_RECEPTEUR PRIMARY KEY (RECEPTEUR_ID)
);

ALTER TABLE RECEPTEUR
	ADD CONSTRAINT FK_RECEPTEUR_LOGICIEL_ID 
	FOREIGN KEY (LOGICIEL_ID) 
	REFERENCES LOGICIEL (LOGICIEL_ID);
	
-- 2.0.10.4
alter table CONTRAT modify TITRE_PROJET varchar2(250);
alter table CESSION modify ETUDE_TITRE varchar2(250);
alter table CONTENEUR modify NBR_ENC number(38) not null;
alter table CONTENEUR modify NBR_NIV number(38) not null;

-- 2.0.10.6
DECLARE
    echantillonSeq INTEGER;
BEGIN
   select max(echantillon_id) + 1
   into   echantillonSeq
   from   ECHANTILLON;

    execute immediate 'CREATE SEQUENCE echantillonSeq START WITH ' || echantillonSeq ||
                       ' INCREMENT BY 1 NOMAXVALUE';
END;
/

create or replace 
TRIGGER  BI_ECHANTILLON
  before insert on ECHANTILLON             
  for each row  
begin   
  if :NEW.ECHANTILLON_ID is null then 
    select ECHANTILLONSEQ.nextval into :NEW.ECHANTILLON_ID from dual; 
  end if; 
end;
/

DECLARE
    annoValSeq INTEGER;
BEGIN
   select max(annotation_valeur_id) + 1
   into   annoValSeq
   from   ANNOTATION_VALEUR;

    execute immediate 'CREATE SEQUENCE annoValSeq START WITH ' || annoValSeq ||
                       ' INCREMENT BY 1 NOMAXVALUE';
END;
/

create or replace 
TRIGGER  BI_ANNOTATION_VALEUR
  before insert on ANNOTATION_VALEUR            
  for each row  
begin   
  if :NEW.ANNOTATION_VALEUR_ID is null then 
    select annoValSeq.nextval into :NEW.ANNOTATION_VALEUR_ID from dual; 
  end if; 
end;
/

DECLARE
    codeAssigneSeq INTEGER;
BEGIN
   select max(code_assigne_id) + 1
   into   codeAssigneSeq
   from   CODE_ASSIGNE;

    execute immediate 'CREATE SEQUENCE codeAssigneSeq START WITH ' || codeAssigneSeq ||
                       ' INCREMENT BY 1 NOMAXVALUE';
END;
/

create or replace 
TRIGGER  BI_CODE_ASSIGNE
  before insert on CODE_ASSIGNE            
  for each row  
begin   
  if :NEW.CODE_ASSIGNE_ID is null then 
    select codeAssigneSeq.nextval into :NEW.CODE_ASSIGNE_ID from dual; 
  end if; 
end;
/

DECLARE
    objetNonConformeSeq INTEGER;
BEGIN
   select max(objet_non_conforme_id) + 1
   into   objetNonConformeSeq
   from   OBJET_NON_CONFORME;

    execute immediate 'CREATE SEQUENCE objetNonConformeSeq START WITH ' || objetNonConformeSeq ||
                       ' INCREMENT BY 1 NOMAXVALUE';
END;
/

create or replace 
TRIGGER  BI_OBJET_NON_CONFORME
  before insert on OBJET_NON_CONFORME           
  for each row  
begin   
  if :NEW.OBJET_NON_CONFORME_ID is null then 
    select objetNonConformeSeq.nextval into :NEW.OBJET_NON_CONFORME_ID from dual; 
  end if; 
end;
/

alter table PATIENT_SIP modify DATE_CREATION date not null;