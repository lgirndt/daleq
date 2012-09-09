DROP TABLE IF EXISTS ALL_TYPES;
DROP TABLE IF EXISTS ASSERT_TABLE;

CREATE TABLE ALL_TYPES (
    A_VARCHAR         VARCHAR(250)   ,
    A_CHAR            CHAR           ,
--    A_LONGVARCHAR     LONGVARCHAR(250),
    A_NVARCHAR        NVARCHAR(250)  ,
    A_TEXT            TEXT           ,
    A_NUMERIC         NUMERIC        ,
    A_DECIMAL         DECIMAL        ,
    A_BOOLEAN         BOOLEAN        ,
    A_BIT             BIT            ,
    A_INTEGER         INTEGER        ,
    A_TINYINT         TINYINT        ,
    A_SMALLINT        SMALLINT       ,
    A_BIGINT          BIGINT         ,
    A_REAL            REAL           ,
    A_DOUBLE          DOUBLE         ,
    A_FLOAT           FLOAT          ,
    A_DATE            DATE           ,
--     A_TIME            TIME           ,
    A_TIMESTAMP       TIMESTAMP      ,
    A_VARBINARY       VARBINARY(250) ,
    A_BINARY          BINARY,
--    A_LONGVARBINARY   LONGVARBINARY  ,
   A_BLOB            BLOB

-- Types defined by DbUnit but not available in HSQLDB
--    A_LONGNVARCHAR    LONGNVARCHAR
--    A_NCHAR           NCHAR
--    A_BIGINT_AUX_LONG BIGINT_AUX_LONG
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE ASSERT_TABLE (
  ID INTEGER NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(250),
  AMOUNT DECIMAL(10,2),
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

