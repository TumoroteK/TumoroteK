/*==============================================================*/
/* Table: CHAMP_ENTITE => prénom obligatoire lors d el'import   */
/*==============================================================*/
update CHAMP_ENTITE set IS_NULL = 0 
	where CHAMP_ENTITE_ID = 5 and NOM = 'Prenom';