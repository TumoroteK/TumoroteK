-- PROFIL
alter table PROFIL add column ARCHIVE boolean;
update PROFIL set ARCHIVE = 0;
alter table PROFIL modify ARCHIVE boolean not null;

alter table PROFIL add column PLATEFORME_ID int(10);
update PROFIL set PLATEFORME_ID = 1;
alter table PROFIL modify PLATEFORME_ID int(10) not null;

ALTER TABLE PROFIL
  ADD CONSTRAINT FK_PROFIL_PLATEFORME_ID
      FOREIGN KEY (PLATEFORME_ID)
      REFERENCES PLATEFORME (PLATEFORME_ID);
      
-- Correctif profil partagées PF
-- ex lyon2 8 profils utilisés dans une seule pf!
update PROFIL p join (select distinct p.profil_id, b.plateforme_id
	FROM PROFIL p 
	JOIN PROFIL_UTILISATEUR pb on p.profil_id = pb.profil_id
	JOIN BANQUE b on pb.banque_id = b.banque_id  
	group by p.profil_id having count(distinct (b.plateforme_id)) = 1) zz 
on zz.profil_id = p.profil_id set p.plateforme_id = zz.plateforme_id;
      
-- duplication profils
-- 11 ex lyon2 profils
create temporary table profilIds (profil_id int(10) not null, plateforme_id int(10) not null, primary key (profil_id)) ENGINE=MYISAM;
insert into profilIds select distinct p.profil_id, b.plateforme_id
	FROM PROFIL p 
	JOIN PROFIL_UTILISATEUR pb on p.profil_id = pb.profil_id
	JOIN BANQUE b on pb.banque_id = b.banque_id  
	group by p.profil_id having count(distinct (b.plateforme_id)) > 1
	order by p.profil_id;
	
alter table PROFIL modify PROFIL_ID int(10) not null auto_increment;
alter table PROFIL add ref_profil int(10);
insert into PROFIL (nom, anonyme, profil_statistiques, profil_export, admin, acces_administration, archive, plateforme_id, ref_profil) 
select p.nom, p.anonyme, p.profil_statistiques, p.profil_export, p.admin, p.acces_administration, p.archive, b.plateforme_id, p.profil_id 
	FROM PROFIL p
	JOIN profilIds i on i.profil_id = p.profil_id 
	JOIN PROFIL_UTILISATEUR pb on i.profil_id = pb.profil_id
	JOIN BANQUE b on pb.banque_id = b.banque_id
where b.plateforme_id > 1 
	group by b.plateforme_id, p.profil_id
order by p.profil_id, b.plateforme_id;
-- lyon2 : 34 profils insérés

--  droits objets
insert into DROIT_OBJET (profil_id, entite_id, droit_niveau, droit_nom, operation_type_id) 
	select p.profil_id, d.entite_id, d.droit_niveau, d.droit_nom, d.operation_type_id 
from DROIT_OBJET d join PROFIL p on p.ref_profil = d.profil_id;
-- lyon2 : 657 droits objets insérés

-- update ref profil_utilisateur
update PROFIL_UTILISATEUR pb 
	join PROFIL p on pb.profil_id = p.ref_profil
	join BANQUE b on pb.banque_id = b.banque_id
set pb.profil_id = p.profil_id
where b.plateforme_id = p.plateforme_id;
-- 103 updates

-- verif
select distinct p.profil_id, p.nom, b.plateforme_id from PROFIL p 
	JOIN PROFIL_UTILISATEUR pb on p.profil_id = pb.profil_id
	JOIN BANQUE b on pb.banque_id = b.banque_id  
	group by p.profil_id having count(distinct (b.plateforme_id)) > 1
	order by p.profil_id;

-- clean up
alter table PROFIL modify PROFIL_ID int(10) not null;
alter table PROFIL drop ref_profil;
drop table profilIds;
 
-- cession full-rack scan date
alter table CESSION add LAST_SCAN_CHECK_DATE datetime;
