create table SCAN_DEVICE (SCAN_DEVICE_ID number(2) not null, 
	NAME varchar2(50) not null,
	VERSION varchar2(50), 
	primary key(SCAN_DEVICE_ID)
);

create table SCAN_TERMINALE (SCAN_TERMINALE_ID number(22) not null, 
	SCAN_DEVICE_ID number(2) not null,
	NAME varchar2(50) not null,
	DATE_SCAN date,
	WIDTH number(3) not null,
	HEIGHT number(3) not null,
	primary key(SCAN_TERMINALE_ID)
);

ALTER TABLE SCAN_TERMINALE
  ADD CONSTRAINT FK_SCAN_TERMINALE_DEVICE_ID
      FOREIGN KEY (SCAN_DEVICE_ID)
      REFERENCES SCAN_DEVICE (SCAN_DEVICE_ID);

create table SCAN_TUBE (SCAN_TUBE_ID number(22) not null, 
	SCAN_TERMINALE_ID number(22) not null,
	CODE varchar2(50),
	CELL varchar2(10) not null,
	"ROW" varchar2(10) not null,
	"COL" varchar2(10) not null,
	primary key(SCAN_TUBE_ID)
);

ALTER TABLE SCAN_TUBE
  ADD CONSTRAINT FK_SCAN_TUBE_TERMINALE_ID
      FOREIGN KEY (SCAN_TERMINALE_ID)
      REFERENCES SCAN_TERMINALE (SCAN_TERMINALE_ID);