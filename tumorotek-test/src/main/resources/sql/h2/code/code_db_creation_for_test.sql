DROP TABLE IF EXISTS C3P0TEST;
DROP TABLE IF EXISTS CIM_MASTER;
DROP TABLE IF EXISTS CIM_LIBELLE;
DROP TABLE IF EXISTS CIM_CHAPTER;
DROP TABLE IF EXISTS CIMO_MORPHO;
DROP TABLE IF EXISTS ADICAPCIM_TOPO;
DROP TABLE IF EXISTS ADICAPCIMO_MORPHO;
DROP TABLE IF EXISTS ADICAP;
DROP TABLE IF EXISTS ADICAP_GROUPE;

-- Create ADICAP_GROUPE table first since others reference it
CREATE TABLE ADICAP_GROUPE (
                               ADICAP_GROUPE_ID INT NOT NULL AUTO_INCREMENT,
                               NOM VARCHAR(200) NOT NULL,
                               GROUPE_PARENT_ID INT DEFAULT NULL,
                               PRIMARY KEY (ADICAP_GROUPE_ID),
                               CONSTRAINT FK_ADICAP_GROUPE_GROUPE_PARENT_ID FOREIGN KEY (GROUPE_PARENT_ID) REFERENCES ADICAP_GROUPE (ADICAP_GROUPE_ID)
);

CREATE TABLE ADICAP (
                        ADICAP_ID INT NOT NULL,
                        CODE VARCHAR(50) NOT NULL,
                        LIBELLE VARCHAR(255) NOT NULL,
                        ADICAP_GROUPE_ID INT NOT NULL,
                        ADICAP_PARENT_ID INT DEFAULT NULL,
                        MORPHO BOOLEAN DEFAULT FALSE,
                        PRIMARY KEY (ADICAP_ID),
                        CONSTRAINT FK_ADICAP_ADICAP_GROUPE_ID FOREIGN KEY (ADICAP_GROUPE_ID) REFERENCES ADICAP_GROUPE (ADICAP_GROUPE_ID),
                        CONSTRAINT FK_ADICAP_ADICAP_PARENT_ID FOREIGN KEY (ADICAP_PARENT_ID) REFERENCES ADICAP (ADICAP_ID)
);

CREATE TABLE ADICAPCIMO_MORPHO (
                                   CIMO_MORPHO_ID INT NOT NULL,
                                   ADICAP_ID INT NOT NULL,
                                   PRIMARY KEY (ADICAP_ID, CIMO_MORPHO_ID)
);

CREATE TABLE ADICAPCIM_TOPO (
                                SID INT NOT NULL,
                                ADICAP_ID INT NOT NULL,
                                PRIMARY KEY (SID, ADICAP_ID)
);

CREATE TABLE CIMO_MORPHO (
                             CIMO_MORPHO_ID INT NOT NULL,
                             CODE VARCHAR(10) NOT NULL,
                             LIBELLE VARCHAR(250) NOT NULL,
                             CIM_REF VARCHAR(50) DEFAULT NULL,
                             PRIMARY KEY (CIMO_MORPHO_ID)
);

CREATE TABLE CIM_CHAPTER (
                             CHAP INT DEFAULT NULL,
                             SID INT DEFAULT NULL,
                             ROM VARCHAR(10) DEFAULT NULL
);

CREATE TABLE CIM_LIBELLE (
                             LID INT NOT NULL DEFAULT 0,
                             SID INT DEFAULT NULL,
                             SOURCE VARCHAR(2) DEFAULT NULL,
                             VALID CHAR(1) DEFAULT NULL,
                             LIBELLE VARCHAR(510) DEFAULT NULL,
                             FR_OMS VARCHAR(510) DEFAULT NULL,
                             EN_OMS VARCHAR(510) DEFAULT NULL,
                             GE_DIMDI VARCHAR(510) DEFAULT NULL,
                             GE_AUTO VARCHAR(510) DEFAULT NULL,
                             FR_CHRONOS VARCHAR(510) DEFAULT NULL,
                             DATE DATE DEFAULT NULL,
                             AUTHOR VARCHAR(20) DEFAULT NULL,
                             COMMENT VARCHAR(200) DEFAULT NULL,
                             PRIMARY KEY (LID)
);

CREATE TABLE CIM_MASTER (
                            SID INT NOT NULL DEFAULT 0,
                            CODE VARCHAR(20) DEFAULT NULL,
                            SORT VARCHAR(20) DEFAULT NULL,
                            ABBREV VARCHAR(20) DEFAULT NULL,
                            LEVEL VARCHAR(2) DEFAULT NULL,
                            TYPE VARCHAR(2) DEFAULT NULL,
                            ID1 INT DEFAULT NULL,
                            ID2 INT DEFAULT NULL,
                            ID3 INT DEFAULT NULL,
                            ID4 INT DEFAULT NULL,
                            ID5 INT DEFAULT NULL,
                            ID6 INT DEFAULT NULL,
                            ID7 INT DEFAULT NULL,
                            VALID CHAR(1) DEFAULT NULL,
                            DATE DATE DEFAULT NULL,
                            AUTHOR VARCHAR(20) DEFAULT NULL,
                            COMMENT VARCHAR(200) DEFAULT NULL,
                            CIMO3 BOOLEAN DEFAULT FALSE,
                            LIBELLE VARCHAR(300) DEFAULT NULL,
                            PRIMARY KEY (SID)
);

CREATE TABLE C3P0TEST (
    A CHAR(1) DEFAULT NULL
);