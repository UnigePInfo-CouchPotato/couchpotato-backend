-- noinspection SqlDialectInspectionForFile

-- -- Room table
drop table Room if exists;
create table Room ( roomId varchar(255) not null, roomAdminId integer not null, roomClosed boolean not null, primary key (roomId));
TRUNCATE TABLE Room;
INSERT INTO Room (roomId, roomAdminId, roomClosed) VALUES ( 'WN5sgnxYD8tC', 2, FALSE );
INSERT INTO Room (roomId, roomAdminId, roomClosed) VALUES ( '99rxfyog0a87', 3, FALSE );
INSERT INTO Room (roomId, roomAdminId, roomClosed) VALUES ( '7b07c2qj7lvc', 7, FALSE );
INSERT INTO Room (roomId, roomAdminId, roomClosed) VALUES ( 'JC3Tzrx2c1nx', 5, FALSE );
INSERT INTO Room (roomId, roomAdminId, roomClosed) VALUES ( 'Fgf2NLjhh9mx', 6, TRUE );


-- -- Room_User table
drop table Room_User if exists;
create table Room_User ( roomId varchar(255) not null, userId integer not null, creationDate timestamp not null, genres varchar(500) not null, votes varchar(255) not null, primary key (roomId, userId));
TRUNCATE TABLE Room_User;
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( '7b07c2qj7lvc', 1, CURRENT_TIMESTAMP, '28,35', '[2, 5, 1, -1, 0]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( '7b07c2qj7lvc', 2, CURRENT_TIMESTAMP, '28', '[0, 2, 4, 5, 0]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( '7b07c2qj7lvc', 3, CURRENT_TIMESTAMP, '35,16', '[-4, 2, 10, 1, 1]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( '7b07c2qj7lvc', 4, CURRENT_TIMESTAMP, '35', '[3, 2, 3, 1, 1]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( '7b07c2qj7lvc', 5, CURRENT_TIMESTAMP, '16', '[0, 0, 1, 1, 0]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( '7b07c2qj7lvc', 6, CURRENT_TIMESTAMP, '10751', '[-1, 2, 6, 2, 1]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( '7b07c2qj7lvc', 7, CURRENT_TIMESTAMP, '99,10751', '[7, 8, 1, 5, 4]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'JC3Tzrx2c1nx', 4, CURRENT_TIMESTAMP, '35,99', '[10, 2, 8, -2, 1]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'JC3Tzrx2c1nx', 5, CURRENT_TIMESTAMP, '10402', '[3, 3, 3, 3, 3]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'JC3Tzrx2c1nx', 6, CURRENT_TIMESTAMP, '36,99', '[1, -1, 0, 1, 2]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( '99rxfyog0a87', 3, CURRENT_TIMESTAMP, '35,10402', '[0, 0, 0, 0, 0]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'WN5sgnxYD8tC', 1, CURRENT_TIMESTAMP, '14', '[3, 1, -1, 0, 0]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'WN5sgnxYD8tC', 2, CURRENT_TIMESTAMP, '12,28', '[0, -2, 1, 1, 0]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'WN5sgnxYD8tC', 3, CURRENT_TIMESTAMP, '28,10402,14,99,36', '[5, 2, 0, 10, 3]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'WN5sgnxYD8tC', 4, CURRENT_TIMESTAMP, '18,10751', '[4, 3, 1, 7, 4]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'WN5sgnxYD8tC', 5, CURRENT_TIMESTAMP, '10402', '[1, 6, 3, -2, -4]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'WN5sgnxYD8tC', 6, CURRENT_TIMESTAMP, '36', '[1, 1, 9, -5, 2]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'Fgf2NLjhh9mx', 1, CURRENT_TIMESTAMP, '878,35', '[8, 9, 3, 6, 2]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'Fgf2NLjhh9mx', 2, CURRENT_TIMESTAMP, '10770,37', '[1, 1, 0, 1, 1]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'Fgf2NLjhh9mx', 3, CURRENT_TIMESTAMP, '9648,10402', '[0, 1, 0, 1, 0]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'Fgf2NLjhh9mx', 4, CURRENT_TIMESTAMP, '18', '[6, 5, 4, 3, 2]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'Fgf2NLjhh9mx', 5, CURRENT_TIMESTAMP, '10751', '[-2, -1, 0, 1, 2]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'Fgf2NLjhh9mx', 6, CURRENT_TIMESTAMP, '16,35,10402', '[10, 4, 5, 1, 3]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'Fgf2NLjhh9mx', 7, CURRENT_TIMESTAMP, '36, 878', '[0, 1, 1, 0, 0]' );
INSERT INTO Room_User (roomId, userId, creationDate, genres, votes) VALUES ( 'Fgf2NLjhh9mx', 8, CURRENT_TIMESTAMP, '12', '[0, 0, 5, 7, 4]' );
