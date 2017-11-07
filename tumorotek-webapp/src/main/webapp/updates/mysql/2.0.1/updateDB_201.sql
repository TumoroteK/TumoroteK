update IMPORTATION set date_import=null where date_import = '0000-00-00';

insert into OPERATION_TYPE values (21,'Export INCa',0);
 -- sites GSO
insert into CATALOGUE values (3, 'TVGSO', 'Catalogue régional', '/images/icones/catalogues/tvgso.gif');
insert into OPERATION_TYPE values (22,'Export TVGSO',0);

 -- sites BIOCAP
insert into CATALOGUE values (4, 'BIOCAP', 'Projet BIOCAP', '/images/icones/catalogues/biocap.gif');
insert into OPERATION_TYPE values (23,'Export BIOCAP',0);

INSERT INTO CATALOGUE_CONTEXTE VALUES (3, 1); 
INSERT INTO CATALOGUE_CONTEXTE VALUES (4, 1);
INSERT INTO CATALOGUE_CONTEXTE VALUES (3, 2);
INSERT INTO CATALOGUE_CONTEXTE VALUES (4, 2);
