-- correctif is_morpho is null
update CODE_ASSIGNE set is_morpho = 0 where is_morpho is null and is_organe = 1;

-- correctif nom_naissance
update PATIENT set nom_naissance = null where trim(nom_naissance) = '';

-- version 2.1.1
-- correctif cim_ref cimo_morpho
update CIMO_MORPHO set cim_ref = null where cim_ref = ''; -- 1

-- test cf PATIENT_SIP
create index dosExtIdx on DOSSIER_EXTERNE(IDENTIFICATION_DOSSIER);

