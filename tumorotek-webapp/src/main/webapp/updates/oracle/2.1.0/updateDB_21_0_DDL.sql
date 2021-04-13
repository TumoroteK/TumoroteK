-- PROFIL
alter table PROFIL add PLATEFORME_ID number(22);
update PROFIL set PLATEFORME_ID = 1;
alter table PROFIL modify PLATEFORME_ID number(22) not null;

ALTER TABLE PROFIL
  ADD CONSTRAINT FK_PROFIL_PLATEFORME_ID
      FOREIGN KEY (PLATEFORME_ID)
      REFERENCES PLATEFORME (PLATEFORME_ID);
      
-- Correctif profil partagées PF
-- verif?
select distinct p.profil_id from PROFIL p 
	JOIN PROFIL_UTILISATEUR pb on p.profil_id = pb.profil_id
	JOIN BANQUE b on pb.banque_id = b.banque_id  
	group by p.profil_id having count(distinct (b.plateforme_id)) > 1
	order by p.profil_id;
	
	
alter table PROFIL add ref_profil number(22);

-- 4 et 5 BDX

insert into PROFIL (profil_id, nom, anonyme, profil_statistiques, profil_export, admin, acces_administration, archive, plateforme_id, ref_profil) 
select (select max(profil_id) + 1 from PROFIL), p.nom, p.anonyme, p.profil_statistiques, p.profil_export, p.admin, p.acces_administration, p.archive, 2, 4 from PROFIL p where p.profil_id=4;
insert into PROFIL (profil_id, nom, anonyme, profil_statistiques, profil_export, admin, acces_administration, archive, plateforme_id, ref_profil) 
select (select max(profil_id) + 1 from PROFIL), p.nom, p.anonyme, p.profil_statistiques, p.profil_export, p.admin, p.acces_administration, p.archive, 2, 5 from PROFIL p where p.profil_id=5;

	
	-- duplication profil
-- lyon2 : 34 profils insérés

--  droits objets
insert into DROIT_OBJET (profil_id, entite_id, droit_niveau, droit_nom, operation_type_id) 
	select p.profil_id, d.entite_id, d.droit_niveau, d.droit_nom, d.operation_type_id 
from DROIT_OBJET d join PROFIL p on p.ref_profil = d.profil_id;
-- lyon2 : 657 droits objets insérés

-- update ref profil_utilisateur
-- update PROFIL_UTILISATEUR pb 
--	join PROFIL p on pb.profil_id = p.ref_profil
--	join BANQUE b on pb.banque_id = b.banque_id
-- set pb.profil_id = p.profil_id where b.plateforme_id = p.plateforme_id;
-- 103 updates
-- BDX uniquement
update PROFIL_UTILISATEUR set profil_id = 9 where profil_id = 4 and banque_id in (select banque_id from BANQUE where plateforme_id = 2);

-- verif
select distinct p.profil_id from PROFIL p 
	JOIN PROFIL_UTILISATEUR pb on p.profil_id = pb.profil_id
	JOIN BANQUE b on pb.banque_id = b.banque_id  
	group by p.profil_id having count(distinct (b.plateforme_id)) > 1
	order by p.profil_id;

-- clean up
alter table PROFIL modify PROFIL_ID int(10) not null;
alter table PROFIL drop column ref_profil;
drop table profilIds;
	
-- cession full-rack scan date
alter table CESSION add LAST_SCAN_CHECK_DATE date;
