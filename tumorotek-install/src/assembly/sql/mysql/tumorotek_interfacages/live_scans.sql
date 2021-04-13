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

create table SCAN_TUBE (SCAN_TUBE_ID int(10) not null, 
	SCAN_TERMINALE_ID int(10) not null,
	CODE varchar(50),
	CELL varchar(10) not null,
	ROW varchar(10) not null,
	COL varchar(10) not null,
	primary key(SCAN_TUBE_ID)
);

ALTER TABLE SCAN_TUBE
  ADD CONSTRAINT FK_SCAN_TUBE_TERMINALE_ID
      FOREIGN KEY (SCAN_TERMINALE_ID)
      REFERENCES SCAN_TERMINALE (SCAN_TERMINALE_ID);