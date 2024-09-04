-- Avant de poser la contrainte, il faut alimenter le champ pour les enregistrements existants :

update IMPORT_HISTORIQUE histo set IMPORT_BANQUE_ID = (select BANQUE_ID from IMPORT_TEMPLATE template where histo.IMPORT_TEMPLATE_ID = template.IMPORT_TEMPLATE_ID) 
WHERE EXISTS (
    SELECT 1
      FROM IMPORT_TEMPLATE template
     WHERE histo.IMPORT_TEMPLATE_ID = template.IMPORT_TEMPLATE_ID);


ALTER TABLE IMPORT_HISTORIQUE
ADD CONSTRAINT FK_IMPORT_BANQUE_ID FOREIGN KEY (IMPORT_BANQUE_ID) REFERENCES BANQUE(BANQUE_ID);