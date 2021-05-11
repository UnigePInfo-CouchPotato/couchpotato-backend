drop table if exists Users;
create table Users (
        id bigint not null,
        username varchar(31) not null,
        email varchar(31) not null,
        saltedPwd varchar(31) not null,
        registrationDate timestamp,
        primary key (id)
);
TRUNCATE TABLE Users;
INSERT INTO Users (id, username, email, saltedPwd, registrationDate) values ( 1, 'azeem', 'azeem@gmail.com', 'azeemarshad', CURRENT_TIMESTAMP );
INSERT INTO Users (id, username, email, saltedPwd, registrationDate) values ( 2, 'petter', 'petter@hotmail.com', 'petterstahle', CURRENT_TIMESTAMP );
INSERT INTO Users (id, username, email, saltedPwd, registrationDate) values ( 3, 'christina', 'christina@hotmail.com', 'christinamarron', CURRENT_TIMESTAMP );
INSERT INTO Users (id, username, email, saltedPwd, registrationDate) values ( 4, 'nicolas', 'nicolas@hotmail.com', 'nicolasboeckh', CURRENT_TIMESTAMP );
INSERT INTO Users (id, username, email, saltedPwd, registrationDate) values ( 5, 'antonin', 'antonin@hotmail.com', 'antoninsedoh', CURRENT_TIMESTAMP );
INSERT INTO Users (id, username, email, saltedPwd, registrationDate) values ( 6, 'faysal', 'faysal@hotmail.com', 'faysalsaber', CURRENT_TIMESTAMP );
