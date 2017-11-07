-- maladie collaborateurs non importable
update CHAMP_ENTITE set can_import=0, is_null=1 where champ_entite_id=250;