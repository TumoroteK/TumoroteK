/* Ajout du nom du site à la version */
-- alter table VERSION add column NOM_SITE VARCHAR(100);

/* Gestion de la qualité */
alter table PRELEVEMENT add column CONFORME_ARRIVEE boolean after CONG_DEPART;
alter table ECHANTILLON add column CONFORME_TRAITEMENT boolean after STERILE;
alter table ECHANTILLON add column CONFORME_CESSION boolean after CONFORME_TRAITEMENT;

alter table CODE_ASSIGNE modify CODE varchar(100) not null;

alter table EMPLACEMENT modify VIDE boolean not null default 1;

ALTER TABLE TERMINALE
  DROP FOREIGN KEY FK_TERMINALE_BANQUE_ID;
ALTER TABLE TERMINALE
  ADD CONSTRAINT FK_TERMINALE_BANQUE_ID
      FOREIGN KEY (BANQUE_ID)
      REFERENCES BANQUE (BANQUE_ID)
      ON DELETE SET NULL;
