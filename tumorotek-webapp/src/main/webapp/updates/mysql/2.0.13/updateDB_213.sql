-- ajout champ virtuel temperature stockage échantillon et dérivés 
insert into CHAMP_ENTITE values (265, 'TempStock', 5, 1, 1, null, 3, 0, null);
insert into CHAMP_ENTITE values (266, 'TempStock', 5, 1, 1, null, 8, 0, null);

update CHAMP_ENTITE set nom = 'CrAnapath' where nom = 'CRanapath';

insert into tumo2codes.ADICAP select max(adicap_id) + 1, 'AMA0', 'METASTASE d''UN ADENOCARCINOME', 5, null, 1 from tumo2codes.ADICAP;

-- Correctif tables annotations partagées PF
select distinct t.table_annotation_id, t.nom, t.plateforme_id from TABLE_ANNOTATION t 
	JOIN TABLE_ANNOTATION_BANQUE tb on t.table_annotation_id=tb.table_annotation_id 
	JOIN BANQUE b on b.banque_id = tb.banque_id where t.catalogue_id is null 
	group by t.table_annotation_id having count(distinct (b.plateforme_id)) > 1
	order by t.table_annotation_id;
	
create temporary table tableIds (table_annotation_id int(10) not null, plateforme_id int(10) not null, primary key (table_annotation_id)) ENGINE=MYISAM;
insert into tableIds select distinct t.table_annotation_id, t.plateforme_id from TABLE_ANNOTATION t 
	JOIN TABLE_ANNOTATION_BANQUE tb on t.table_annotation_id=tb.table_annotation_id 
	JOIN BANQUE b on b.banque_id = tb.banque_id where t.catalogue_id is null 
	group by t.table_annotation_id having count(distinct (b.plateforme_id)) > 1
	order by t.table_annotation_id;

--duplication tables
alter table TABLE_ANNOTATION modify TABLE_ANNOTATION_ID int(10) not null auto_increment;
alter table TABLE_ANNOTATION add ref_table int(10);
insert into TABLE_ANNOTATION (nom, description, entite_id, plateforme_id, ref_table) 
select t.nom, t.description, t.entite_id, b.plateforme_id, t.table_annotation_id 
	from TABLE_ANNOTATION t JOIN tableIds i on i.table_annotation_id = t.table_annotation_id 
	JOIN TABLE_ANNOTATION_BANQUE tb on t.table_annotation_id=tb.table_annotation_id 
	JOIN BANQUE b on b.banque_id = tb.banque_id 
	where b.plateforme_id != t.plateforme_id and t.table_annotation_id > 1 group by b.plateforme_id;
	
-- duplication champs
alter table CHAMP_ANNOTATION modify CHAMP_ANNOTATION_ID int(10) not null auto_increment;
alter table CHAMP_ANNOTATION add ref_champ int(10);
insert into CHAMP_ANNOTATION (nom, table_annotation_id, data_type_id, combine, ordre, edit, ref_champ) 
	select c.nom, t.table_annotation_id, c.data_type_id, c.combine, c.ordre, c.edit, c.champ_annotation_id
	from CHAMP_ANNOTATION c join TABLE_ANNOTATION t on t.ref_table = c.table_annotation_id;
	
-- duplication items
alter table ITEM modify ITEM_ID int(10) not null auto_increment;
alter table ITEM add ref_item int(10);
insert into ITEM (label, valeur, champ_annotation_id, ref_item)
	select i.label, i.valeur, c.champ_annotation_id, i.item_id
	from ITEM i join CHAMP_ANNOTATION c on c.ref_champ = i.champ_annotation_id;
	
-- duplication annotation defaut
alter table ANNOTATION_DEFAUT modify ANNOTATION_DEFAUT_ID int(10) not null auto_increment;
insert into ANNOTATION_DEFAUT (champ_annotation_id, alphanum, texte, anno_date, bool, item_id, obligatoire)
	select c.champ_annotation_id, a.alphanum, a.texte, a.anno_date, a.bool, i.item_id, a.obligatoire
	from ANNOTATION_DEFAUT a join CHAMP_ANNOTATION c on c.ref_champ = a.champ_annotation_id
	left join ITEM i on i.ref_item = a.item_id and i.champ_annotation_id = c.champ_annotation_id;
	
-- migration valeurs ref champ, ref item_id
select b.plateforme_id, count(*) from ANNOTATION_VALEUR a join CHAMP_ANNOTATION c on c.champ_annotation_id = a.champ_annotation_id join TABLE_ANNOTATION t on t.table_annotation_id = c.table_annotation_id join BANQUE b on a.banque_id = b.banque_id where b.plateforme_id != t.plateforme_id group by b.plateforme_id;

select champ_annotation_id, plateforme_id, count(*) from ANNOTATION_VALEUR a join BANQUE b on a.banque_id = b.banque_id where champ_annotation_id in (select ref_champ from CHAMP_ANNOTATION) group by champ_annotation_id, plateforme_id; 
	
-- update ref champ_annotation_id	
update ANNOTATION_VALEUR a join CHAMP_ANNOTATION c on a.champ_annotation_id = c.ref_champ
	join TABLE_ANNOTATION t on t.table_annotation_id = c.table_annotation_id
	join BANQUE b on a.banque_id = b.banque_id 
set a.champ_annotation_id = c.champ_annotation_id
where b.plateforme_id = t.plateforme_id;
-- 677
	
-- update ref_item pour thesaurus
update ANNOTATION_VALEUR a join ITEM i on a.item_id = i.ref_item
	join CHAMP_ANNOTATION c on a.champ_annotation_id = c.champ_annotation_id
set a.item_id = i.item_id
where c.ref_champ is not null;
-- 3846

-- update table_annotation_banques
update TABLE_ANNOTATION_BANQUE tb 
	join TABLE_ANNOTATION t on tb.table_annotation_id = t.ref_table
	join BANQUE b on b.plateforme_id = t.plateforme_id
set tb.table_annotation_id = t.table_annotation_id
where tb.banque_id = b.banque_id;
-- 27
 
alter table ITEM drop ref_item;
alter table CHAMP_ANNOTATION drop ref_champ;
alter table TABLE_ANNOTATION drop ref_table;
	
alter table ANNOTATION_DEFAUT modify ANNOTATION_DEFAUT_ID int(10) not null;
alter table ITEM modify ITEM_ID int(10) not null;
alter table CHAMP_ANNOTATION modify CHAMP_ANNOTATION_ID int(10) not null;
alter table TABLE_ANNOTATION modify TABLE_ANNOTATION_ID int(10) not null;

drop table tableIds;

 