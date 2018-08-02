CREATE PROCEDURE `fill_tmp_table_prel_sero`(IN id INTEGER)
  BEGIN
    INSERT INTO TMP_PRELEVEMENT_EXPORT (PRELEVEMENT_ID,
                                        BANQUE,
                                        CODE,
                                        NUMERO_LABO,
                                        NATURE,
                                        PROTOCOLES,
                                        DATE_PRELEVEMENT,
                                        PRELEVEMENT_TYPE,
                                        STERILE,
                                        RISQUE,
                                        CONFORME_ARRIVEE,
                                        RAISON_NC_TRAITEMENT,
                                        ETABLISSEMENT,
                                        SERVICE_PRELEVEUR,
                                        PRELEVEUR,
                                        CONDIT_TYPE,
                                        CONDIT_NBR,
                                        CONDIT_MILIEU,
                                        CONSENT_TYPE,
                                        CONSENT_DATE,
                                        COMPLEMENT_DIAG,
                                        DATE_DEPART,
                                        TRANSPORTEUR,
                                        TRANSPORT_TEMP,
                                        DATE_ARRIVEE,
                                        OPERATEUR,
                                        CONG_DEPART,
                                        CONG_ARRIVEE,
                                        LABO_INTER,
                                        QUANTITE,
                                        PATIENT_NDA,
                                        DIAGNOSTIC,
                                        ECHAN_TOTAL,
                                        ECHAN_RESTANT,
                                        ECHAN_STOCKE,
                                        AGE_PREL,
                                        NOMBRE_DERIVES,
                                        DATE_HEURE_SAISIE,
                                        UTILISATEUR_SAISIE,
                                        MALADIE_ID,
                                        LIBELLE,
                                        CODE_MALADIE,
                                        DATE_DIAGNOSTIC,
                                        DATE_DEBUT,
                                        MEDECIN_MALADIE,
                                        PATIENT_ID)
    SELECT p.prelevement_id,
           b.nom                                                                                         as 'collection',
           p.code,
           p.numero_labo                                                                                 as laboratoire,
           n.nature,
           (SELECT GROUP_CONCAT(pt.nom)
            FROM PRELEVEMENT prlt
                   JOIN PRELEVEMENT_DELEGATE pd ON pd.PRELEVEMENT_ID = prlt.PRELEVEMENT_ID
                   JOIN PRELEVEMENT_SERO ps ON ps.PRELEVEMENT_DELEGATE_ID = pd.PRELEVEMENT_DELEGATE_ID
                   JOIN PRELEVEMENT_SERO_PROTOCOLE psp ON psp.PRELEVEMENT_DELEGATE_ID = pd.PRELEVEMENT_DELEGATE_ID
                   JOIN PROTOCOLE pt ON pt.PROTOCOLE_ID = psp.PROTOCOLE_ID
            WHERE prlt.PRELEVEMENT_ID
                    = id),
           p.date_prelevement,
           pt.type,
           p.sterile,
           (select GROUP_CONCAT(r.nom)
            from RISQUE r
                   JOIN PRELEVEMENT_RISQUE pr ON r.risque_id = pr.risque_id
            WHERE pr.prelevement_id = p.prelevement_id)                                                  as 'risque_infectieux',
           p.conforme_arrivee,
           (select GROUP_CONCAT(nc.nom)
            FROM OBJET_NON_CONFORME onc
                   LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
                   LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id
            WHERE ct.conformite_type_id = 1
              AND p.prelevement_id =
                  onc.objet_id)                                                                          as 'Raison_de_non_conformité',
           et.nom                                                                                        as 'établissement_préleveur',
           s.nom                                                                                         as 'Service_préleveur',
           co.nom                                                                                        as 'Préleveur',
           ct.type                                                                                       as 'Type_de_conditionnement',
           p.condit_nbr                                                                                  as 'Nombre_de_prélevements',
           cm.milieu,
           consent.type                                                                                  as 'Statut_juridique',
           p.consent_date                                                                                as 'date_du_statut',
           (SELECT ps.libelle
            FROM PRELEVEMENT prel
                   JOIN PRELEVEMENT_DELEGATE pd ON pd.prelevement_id = prel.prelevement_id
                   JOIN PRELEVEMENT_SERO ps ON ps.prelevement_delegate_id = pd.prelevement_delegate_id
            WHERE prel.prelevement_id = id),
           p.date_depart,
           tr.nom                                                                                        as 'Transporteur',
           p.transport_temp                                                                              as 'Temps_de_transport',
           p.date_arrivee,
           coco.nom                                                                                      as 'Opérateur',
           p.cong_depart,
           p.cong_arrivee,
           (select count(l.labo_inter_id) FROM LABO_INTER l where l.prelevement_id = id),
           p.quantite,
           p.patient_nda                                                                                 as 'Num_Dossier_Patient',
           (SELECT d.nom
            FROM PRELEVEMENT prel
                   JOIN MALADIE m ON prel.maladie_id = m.maladie_id
                   JOIN MALADIE_DELEGATE md ON md.maladie_id =
                                               m.maladie_id
                   JOIN MALADIE_SERO ms ON ms.maladie_delegate_id = md.maladie_delegate_id
                   JOIN DIAGNOSTIC d ON d.diagnostic_id =
                                        ms.diagnostic_id
            WHERE prel.prelevement_id = id),
           (SELECT count(e.prelevement_id) FROM ECHANTILLON e WHERE e.prelevement_id = p.prelevement_id) AS 'Total_Echantillons',
           (SELECT count(e1.prelevement_id)
            FROM ECHANTILLON e1
            WHERE e1.prelevement_id = p.prelevement_id
              AND e1.quantite > 0)                                                                       AS 'Echantillons_restants',
           (SELECT count(e2.prelevement_id)
            FROM ECHANTILLON e2
                   INNER JOIN OBJET_STATUT os ON e2.objet_statut_id = os.objet_statut_id AND (os.statut
                                                                                                = 'STOCKE' OR os.statut = 'RESERVE')
            WHERE e2.prelevement_id = p.prelevement_id)                                                  as 'Echantillons_stockés',
           (select FLOOR(datediff(p.date_prelevement, pat.DATE_NAISSANCE) / 365.25))                     as 'AGE_AU_PREL',
           (SELECT COUNT(tr.objet_id)
            FROM TRANSFORMATION tr
                   INNER JOIN PROD_DERIVE pd ON tr.TRANSFORMATION_ID = pd.TRANSFORMATION_ID
            WHERE tr.OBJET_ID = id
              and tr.entite_id = 2)                                                                      as 'Nb_Produits_dérivés',
           (SELECT op.date_
            FROM OPERATION op
            WHERE op.OPERATION_TYPE_ID = 3
              AND op.entite_id = 2
              AND op.objet_id = id)                                                                      as 'date_heure_saisie',
           (SELECT ut.login
            FROM UTILISATEUR ut
                   JOIN OPERATION op ON ut.utilisateur_id = op.utilisateur_id
            WHERE op.OPERATION_TYPE_ID = 3
              AND op.entite_id = 2
              AND op.objet_id = id)                                                                      as 'Utilisateur_saisie',
           p.maladie_id,
           m.libelle,
           m.code,
           m.date_diagnostic,
           m.date_debut,
           (select GROUP_CONCAT(c.nom)
            FROM MALADIE_MEDECIN mm
                   JOIN COLLABORATEUR c ON mm.collaborateur_id = c.collaborateur_id
            WHERE p.maladie_id =
                  mm.maladie_id),
           pat.patient_id
    FROM PRELEVEMENT p
           INNER JOIN BANQUE b
           INNER JOIN NATURE n
           INNER JOIN ENTITE ent
           LEFT JOIN PRELEVEMENT_TYPE pt ON p.prelevement_type_id = pt.prelevement_type_id
           LEFT JOIN SERVICE s ON p.service_preleveur_id = s.service_id
           LEFT JOIN ETABLISSEMENT et ON s.etablissement_id = et.etablissement_id
           LEFT JOIN COLLABORATEUR co ON p.preleveur_id = co.collaborateur_id
           LEFT JOIN CONDIT_TYPE ct ON p.condit_type_id = ct.condit_type_id
           LEFT JOIN CONDIT_MILIEU cm ON p.condit_milieu_id = cm.condit_milieu_id
           LEFT JOIN CONSENT_TYPE consent ON p.consent_type_id = consent.consent_type_id
           LEFT JOIN TRANSPORTEUR tr ON p.transporteur_id = tr.transporteur_id
           LEFT JOIN COLLABORATEUR coco ON p.operateur_id = coco.collaborateur_id
           LEFT JOIN MALADIE m on p.maladie_id = m.maladie_id
           LEFT JOIN PATIENT pat ON m.patient_id = pat.patient_id
    WHERE p.banque_id = b.banque_id
      AND p.nature_id = n.nature_id
      AND ent.ENTITE_ID = 2
      AND p.prelevement_id = id;
  END;