insert into OPERATION_TYPE values (21,'Export TVGSO',0);
insert into OPERATION_TYPE values (22,'Export INCa',0);
insert into OPERATION_TYPE values (23,'Export BIOCAP',0);

insert into BANQUE_CATALOGUE select distinct(b.banque_id), t.catalogue_id from TABLE_ANNOTATION_BANQUE b, TABLE_ANNOTATION t where t.table_annotation_id=b.table_annotation_id and t.catalogue_id is not null;