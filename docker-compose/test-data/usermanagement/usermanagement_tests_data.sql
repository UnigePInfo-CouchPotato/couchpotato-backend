drop table Users if exists;
-- drop sequence if exists USERS_SEQ;
-- create sequence USERS_SEQ start with 1 increment by 50;
-- CREATE SEQUENCE IF NOT EXISTS USERS_SEQ
--     INCREMENT 1
--     MINVALUE 1
--     MAXVALUE 20
--     START 1;
create table Users (
        id bigint not null,
        username varchar(31) not null,
        email varchar(31) not null,
        saltedPwd varchar(31) not null,
        registrationDate timestamp,
        primary key (id)
);
-- INSERT INTO Users (id, username, email, saltedPwd, registrationDate) values ( USERS_SEQ.nextval, 'azejem1', 'azeem@gmail.com', 'azeemarshad', PARSEDATETIME('17-09-2017','yyyy-dd-mm','en') );

INSERT INTO Users(id, username, email, saltedPwd, registrationDate) values( 1, 'azeem', 'azeem@gmail.com', 'azeemarshad', CURRENT_TIMESTAMP );
INSERT INTO Users(id, username, email, saltedPwd, registrationDate) values( 2, 'alice', 'alice@hotmail.com', 'alicesalty', CURRENT_TIMESTAMP );
INSERT INTO Users(id, username, email, saltedPwd, registrationDate) values( 3, 'bob', 'bob@gmail.com', 'bobsalty', CURRENT_TIMESTAMP );
INSERT INTO Users(id, username, email, saltedPwd, registrationDate) values( 4, 'petpet', 'petpet@hotmail.com', 'petpetsalty', CURRENT_TIMESTAMP );
INSERT INTO Users(id, username, email, saltedPwd, registrationDate) values( 5, 'ben', 'ben@gmail.com', 'bensalty', CURRENT_TIMESTAMP );
INSERT INTO Users(id, username, email, saltedPwd, registrationDate) values( 6, 'saddam', 'saddam@hotmail.com', 'saddamsalty', CURRENT_TIMESTAMP );
INSERT INTO Users(id, username, email, saltedPwd, registrationDate) values( 7, 'trump', 'trump@gmail.com', 'trumpsalty', CURRENT_TIMESTAMP );
INSERT INTO Users(id, username, email, saltedPwd, registrationDate) values( 8, 'castro', 'castro@hotmail.com', 'castrosalty', CURRENT_TIMESTAMP );
INSERT INTO Users(id, username, email, saltedPwd, registrationDate) values( 9, 'soap', 'soap@gmail.com', 'soapsalty', CURRENT_TIMESTAMP );



-- INSERT INTO Users(id, username, email, saltedPwd, registrationDate) values( nextval('USERS_SEQ'), 'makarov', 'makarov@hotmail.com', 'makarovsalty', CURRENT_TIMESTAMP );
-- INSERT INTO Instrument (ID, instrumentType, brokerLei, counterpartyLei, dealDate, originalCurrency, valueDate, isin, tracker, quantity, maturityDate, amountInOriginalCurrency, strikeAmount, direction) values ( INSTRUMENT_SEQ.nextval, 'B', '335800DV2WKUAMUTYL21', '254900Z5WUXGPYH1WS92', PARSEDATETIME('10-07-2013','yyyy-dd-mm','en'), 'EUR', PARSEDATETIME('10-07-2013','yyyy-dd-mm','en'), 'BEC0000AIP48', NULL, 4969, PARSEDATETIME('09/22/2020','mm/dd/yyyy','en'), 539235.88, NULL, NULL  );
