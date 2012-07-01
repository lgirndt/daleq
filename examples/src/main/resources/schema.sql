DROP SEQUENCE PRODUCT_SEQ IF EXISTS;
DROP TABLE PRODUCT IF EXISTS;

CREATE SEQUENCE PRODUCT_SEQ;

CREATE TABLE PRODUCT (
  ID    INT  GENERATED BY DEFAULT AS SEQUENCE PRODUCT_SEQ,
  NAME  VARCHAR(250) NOT NULL,
  SIZE  VARCHAR(250) NOT NULL,
  PRICE DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (ID)
);
