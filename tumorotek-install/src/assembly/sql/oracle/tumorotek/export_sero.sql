-- -----------------------------------------------------------------------------
-- Contexte Sérothèque
-- since 2.2.1
-- ------------------------------------------------------------------------------

-- ------------------------------------------------------
--  procedures MALADIE
-- MALADIE ajout DIAGNOSTIC
-- ------------------------------------------------------

-- dans un premier temps, fait exactement la même chose que le contexte anapth pour débloquer l'export en serologie
create or replace
PROCEDURE create_tmp_maladie_table_sero AS 
BEGIN
    create_tmp_maladie_table();
END create_tmp_maladie_table_sero;
/

create or replace
PROCEDURE fill_tmp_table_maladie_sero(id IN patient.patient_id%Type) AS
BEGIN
	fill_tmp_table_maladie(id);
END fill_tmp_table_maladie_sero;
/


-- -----------------------------------------------------
-- PRELEVEMENT 
-- ajout PROTOCOLES et COMPLEMENT_DIAGNOSTIC
-- suppr CODE_ORGANE
-- -----------------------------------------------------

-- dans un premier temps, fait exactement la même chose que le contexte anapth pour débloquer l'export en serologie
create or replace
PROCEDURE create_tmp_prel_sero_table AS
  BEGIN
    create_tmp_prelevement_table();
  END create_tmp_prel_sero_table;
/

create or replace
PROCEDURE fill_tmp_table_prel_sero(prel_id IN prelevement.prelevement_id%Type) AS
  BEGIN 
	  fill_tmp_table_prel(prel_id);
  END fill_tmp_table_prel_sero;
/

-- ------------------------------------------------------
--  procedures ECHANTILLON
-- suppr champs anapath (TUMORAL, CODE_ORGANES, CODE_MORPHOS, LATERALITE
-- ------------------------------------------------------

-- dans un premier temps, fait exactement la même chose que le contexte anapth pour débloquer l'export en serologie
create or replace
PROCEDURE create_tmp_echan_table_sero AS
  BEGIN
    create_tmp_echantillon_table();
  END create_tmp_echan_table_sero;
/

create or replace
PROCEDURE fill_tmp_table_echan_sero(id IN echantillon.echantillon_id%Type) AS
  BEGIN
	fill_tmp_table_echan(id);
  END fill_tmp_table_echan_sero;
/
  
  
-- ------------------------------------------------------
--  procedures CESSION
-- suppr champs anapath (TUMORAL, CODE_ORGANES, CODE_MORPHOS, LATERALITE
-- ------------------------------------------------------
  
-- dans un premier temps, fait exactement la même chose que le contexte anapth pour débloquer l'export en serologie
create or replace
PROCEDURE select_cession_data_sero(entite_id IN NUMBER, count_annotation IN NUMBER, prc OUT sys_refcursor) AS
  BEGIN
	select_cession_data(entite_id, count_annotation, prc);
  END select_cession_data_sero;
/
