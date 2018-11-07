-- DELIMITER &&
DROP FUNCTION IF EXISTS `get_terminale_adrl_as_ids`&&

CREATE FUNCTION `get_terminale_adrl_as_ids`(id_terminale INT, cont_id INT)
  RETURNS VARCHAR(100)
DETERMINISTIC
READS SQL DATA
  BEGIN
    DECLARE adrl VARCHAR(50);
    DECLARE id_conteneur int(10);
    DECLARE id_enceinte int(10);

    IF id_terminale is not null
    THEN
      SET id_enceinte = (select t.enceinte_id from TERMINALE t where t.terminale_id = id_terminale);

      SET adrl = (select t.terminale_id from TERMINALE t where t.terminale_id = id_terminale);

      while_enceinte: WHILE id_enceinte is not null DO
        SET adrl = concat(id_enceinte, '.', adrl);
        SET id_conteneur = (select conteneur_id from ENCEINTE where enceinte_id = id_enceinte);
        IF id_conteneur is not null
        THEN
          IF cont_id is null OR id_conteneur = cont_id
          THEN
            SET adrl = concat(id_conteneur, '.', adrl);
            LEAVE while_enceinte;
          ELSE
            return null;
          END IF;

        END IF;
        SET id_enceinte = (select enceinte_pere_id from ENCEINTE where enceinte_id = id_enceinte);
      END WHILE while_enceinte;
    END IF;

    RETURN adrl;

  END&&

DROP PROCEDURE IF EXISTS `get_terminale_by_nom_and_parent`&&
CREATE PROCEDURE `get_terminale_by_nom_and_parent`(IN tnom CHAR(50), IN id_enceinte INT, IN id_conteneur INT)
  BEGIN
    IF id_enceinte is not null
    THEN
      SELECT t.terminale_id
      FROM TERMINALE t
      WHERE (t.nom like tnom OR t.alias like tnom)
        AND get_terminale_adrl_as_ids(t.terminale_id, id_conteneur) LIKE concat('%.', id_enceinte, '.%');

    ELSE
      SELECT t.terminale_id
      FROM TERMINALE t
      WHERE (t.nom like tnom OR t.alias like tnom)
        AND get_terminale_adrl_as_ids(t.terminale_id, id_conteneur) is not null;
    END IF;
  END&&

