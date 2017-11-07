-- imports en modification
alter table IMPORT_TEMPLATE add IS_UPDATE NUMBER(1) default 0 not null;
alter table IMPORTATION add IS_UPDATE NUMBER(1) default 0 not null;


