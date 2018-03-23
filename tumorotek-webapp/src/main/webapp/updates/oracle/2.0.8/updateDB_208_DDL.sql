-- objet_statut not null
alter table ECHANTILLON modify OBJET_STATUT_ID not null;
alter table PROD_DERIVE modify OBJET_STATUT_ID not null;

-- QRCode
alter table MODELE add IS_QRCODE number(1) default 0 not null;