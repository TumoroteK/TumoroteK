call create_tmp_patient_table();
call fill_tmp_table_patient(10029);
call create_tmp_maladie_table();
call fill_tmp_table_maladie(9997);
call create_tmp_prelevement_table();
call fill_tmp_table_prel(11327);
call create_tmp_echantillon_table();
call fill_tmp_table_echan(32285);

call create_tmp_tvgso_adds();
call fill_tmp_table_adds(32285);