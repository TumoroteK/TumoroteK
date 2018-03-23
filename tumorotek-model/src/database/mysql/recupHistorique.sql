/*cree les procedures de recuperations des historiques*/
/*Patient*/
select 'Creation et appel de la procedure qui va recuperer historique patient...';
source recupHistoPatV1.sql;
call RecupPat();
drop procedure RecupPat;

select 'Creation et appel de la procedure qui va recuperer historique prelevement...';
source recupHistoPrelV1.sql;
call RecupPrel();
drop procedure RecupPrel;

select 'Creation et appel de la procedure qui va recuperer historique echantillon...';
source recupHistoEchanV1.sql;
call RecupEchan();
drop procedure RecupEchan;

select 'Creation et appel de la procedure qui va recuperer historique derive...';
source recupHistoDeriveV1.sql;
call RecupDerive();
drop procedure RecupDerive;

select 'Creation et appel de la procedure qui va recuperer historique cession...';
source recupHistoCessionV1.sql;
call RecupCession();
drop procedure RecupCession;