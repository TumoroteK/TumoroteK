-- pour tous les contextes existants, ajout d'un contexte_champ_entite non visible pour les champs de la sérologie qui passent en standard pour éventuellement l'utiliser...
insert into GATSBI_CONTEXTE_CHAMP_ENTITE (CONTEXTE_ID, CHAMP_ENTITE_ID, VISIBLE, OBLIGATOIRE, IN_TABLEAU)
select contexte_id, 274, 0, 0, 0 from GATSBI_CONTEXTE where type = 'Prelevement';
insert into GATSBI_CONTEXTE_CHAMP_ENTITE (CONTEXTE_ID, CHAMP_ENTITE_ID, VISIBLE, OBLIGATOIRE, IN_TABLEAU)
select contexte_id, 275, 0, 0, 0 from GATSBI_CONTEXTE where type = 'Prelevement';
-- NB : Le champ "fiabilité du diagnostic" n'est pas géré dans Gatsbi car cette notion porte sur la Maladie qui est associée à une Visite dans ce type de collection

