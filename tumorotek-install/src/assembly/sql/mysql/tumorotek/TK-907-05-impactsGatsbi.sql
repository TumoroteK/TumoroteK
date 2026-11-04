-- pour tous les contextes existants, ajout d'un contexte_champ_entite non visible pour le nouveau champ requalifiable
-- pour éventuellement pouvoir l'ajouter
insert into GATSBI_CONTEXTE_CHAMP_ENTITE (CONTEXTE_ID, CHAMP_ENTITE_ID, VISIBLE, OBLIGATOIRE, IN_TABLEAU)
select contexte_id, 277, 0, 0, 0 from GATSBI_CONTEXTE where type = 'Echantillon';