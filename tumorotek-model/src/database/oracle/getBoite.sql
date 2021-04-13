create or replace 
FUNCTION get_terminale_adrl_as_ids(id_terminale IN NUMBER, cont_id IN NUMBER) 
	RETURN VARCHAR2
IS
	adrl VARCHAR2(50);
	id_conteneur NUMBER(10);
	id_enceinte NUMBER(10);
BEGIN
	IF id_terminale is not null THEN
		select t.enceinte_id into id_enceinte from TERMINALE t 
			where t.terminale_id=id_terminale;
		
		select t.terminale_id into adrl from TERMINALE t 
			where t.terminale_id=id_terminale;
	
		WHILE id_enceinte is not null 
		LOOP
			adrl := concat(concat(id_enceinte, '.'), adrl);
			select conteneur_id into id_conteneur from ENCEINTE where enceinte_id = id_enceinte;
			IF id_conteneur is not null THEN
				IF cont_id is null OR id_conteneur = cont_id THEN
					adrl := concat(concat(id_conteneur, '.'), adrl);
					RETURN adrl;	
				ELSE 
					return null;
				END IF;	
			END IF;
			select enceinte_pere_id into id_enceinte from ENCEINTE where enceinte_id=id_enceinte;
		END LOOP;
	END IF;
	RETURN adrl;
END get_terminale_adrl_as_ids;
/

create or replace 
PROCEDURE get_term_by_nom_and_parent (tnom IN VARCHAR2, id_enceinte IN NUMBER, id_cont IN NUMBER, prc OUT sys_refcursor) AS
BEGIN
	IF id_enceinte is not null THEN 
		OPEN prc FOR 'SELECT t.terminale_id FROM TERMINALE t 
			WHERE (t.nom like ''' || tnom || ''' OR t.alias like ''' || tnom || ''') 
		AND get_terminale_adrl_as_ids(t.terminale_id,' || id_cont ||') LIKE concat(concat(''%.'', ' || id_enceinte || '), ''.%'')';
	ELSE 
		OPEN prc FOR 'SELECT t.terminale_id FROM TERMINALE t 
			WHERE (t.nom like ''' || tnom || ''' OR t.alias like ''' || tnom || ''') AND get_terminale_adrl_as_ids(t.terminale_id, ' || id_cont ||') is not null';		
	END IF;
END get_term_by_nom_and_parent;
/

