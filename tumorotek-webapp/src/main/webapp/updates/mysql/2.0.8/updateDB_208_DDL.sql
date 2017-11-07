-- objet_statut not null
alter table ECHANTILLON modify OBJET_STATUT_ID int(2) not null;
alter table PROD_DERIVE modify OBJET_STATUT_ID int(2) not null;

-- QRCode
alter table MODELE add IS_QRCODE boolean not null default 0;