-- imports en modification
alter table IMPORT_TEMPLATE add IS_UPDATE boolean not null default 0;
alter table IMPORTATION add IS_UPDATE boolean not null default 0;


