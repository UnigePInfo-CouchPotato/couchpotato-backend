drop table User if exists;
drop sequence if exists USER_SEQ;
create sequence USER_SEQ start with 1 increment by 50;
create table User (
        id bigint not null,
        username varchar(31) not null,
        email varchar(31) not null,
        saltedPwd varchar(31) not null,
        registrationDate timestamp,
        primary key (id)
);
INSERT INTO User (id, username, email, saltedPwd, registrationDate) values ( USER_SEQ.nextval, 'azeem', 'azeem@gmail.com', 'azeemarshad', PARSEDATETIME('17-09-2017','yyyy-dd-mm','en') );
INSERT INTO User (id, username, email, saltedPwd, registrationDate) values ( USER_SEQ.nextval, 'azeem2', 'azeem@hotmail.com', 'azeemarshad2', PARSEDATETIME('17-09-2020','yyyy-dd-mm','en') );


-- INSERT INTO Instrument (ID, instrumentType, brokerLei, counterpartyLei, dealDate, originalCurrency, valueDate, isin, tracker, quantity, maturityDate, amountInOriginalCurrency, strikeAmount, direction) values ( INSTRUMENT_SEQ.nextval, 'B', '335800DV2WKUAMUTYL21', '254900Z5WUXGPYH1WS92', PARSEDATETIME('10-07-2013','yyyy-dd-mm','en'), 'EUR', PARSEDATETIME('10-07-2013','yyyy-dd-mm','en'), 'BEC0000AIP48', NULL, 4969, PARSEDATETIME('09/22/2020','mm/dd/yyyy','en'), 539235.88, NULL, NULL  );
