create table SCAN_DEVICE (SCAN_DEVICE_ID int(10) not null, 
	NAME varchar(50) not null,
	VERSION varchar(50), 
	primary key(SCAN_DEVICE_ID)
);

create table SCAN_TERMINALE (SCAN_TERMINALE_ID int(10) not null, 
	SCAN_DEVICE_ID int(10) not null,
	NAME varchar(50) not null,
	DATE_SCAN datetime,
	WIDTH int(3) not null,
	HEIGHT int(3) not null,
	primary key(SCAN_TERMINALE_ID)
);

ALTER TABLE SCAN_TERMINALE
  ADD CONSTRAINT FK_SCAN_TERMINALE_DEVICE_ID
      FOREIGN KEY (SCAN_DEVICE_ID)
      REFERENCES SCAN_DEVICE (SCAN_DEVICE_ID);

-- TK-568 : changement du nom des colonnes pour éviter les problèmes avec ROW qui est un mot réservé de mysql
-- => ROW devient LIGNE. Et pour éviter d'éventuels problèmes futurs, CELL devient CELLULE et COL devient COLONNE
-- avec des termes en français, il n'y a plus de risque d'être sur un futur mot réservé.      
create table SCAN_TUBE (SCAN_TUBE_ID int(10) not null, 
	SCAN_TERMINALE_ID int(10) not null,
	CODE varchar(50),
	CELLULE varchar(10) not null,
	LIGNE varchar(10) not null,
	COLONNE varchar(10) not null,
	primary key(SCAN_TUBE_ID)
);

ALTER TABLE SCAN_TUBE
  ADD CONSTRAINT FK_SCAN_TUBE_TERMINALE_ID
      FOREIGN KEY (SCAN_TERMINALE_ID)
      REFERENCES SCAN_TERMINALE (SCAN_TERMINALE_ID);