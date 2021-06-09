-- noinspection SqlDialectInspectionForFile

-- -- Room table
drop table Room if exists;
create table Room ( roomId varchar(255) not null, roomAdmin varchar(1023) not null, userPreferences varchar(1023) not null, movies varchar(16383) not null, roomClosed boolean not null, usersCanVote boolean not null, usersCanJoin boolean not null, numberOfMovies integer not null, primary key (roomId));
TRUNCATE TABLE Room;
INSERT INTO Room (roomId, roomAdmin, userPreferences, movies, roomClosed, usersCanVote, usersCanJoin, numberOfMovies) VALUES ( 'WN5sgnxYD8tC', 'Test administrator', '', '', FALSE, TRUE, TRUE, 0 );
INSERT INTO Room (roomId, roomAdmin, userPreferences, movies, roomClosed, usersCanVote, usersCanJoin, numberOfMovies) VALUES ( '99rxfyog0a87', 'Test administrator', '', '', FALSE, TRUE, TRUE, 0 );
INSERT INTO Room (roomId, roomAdmin, userPreferences, movies, roomClosed, usersCanVote, usersCanJoin, numberOfMovies) VALUES ( '7b07c2qj7lvc', 'Test administrator', '', '', FALSE, TRUE, TRUE, 0 );
INSERT INTO Room (roomId, roomAdmin, userPreferences, movies, roomClosed, usersCanVote, usersCanJoin, numberOfMovies) VALUES ( 'JC3Tzrx2c1nx', 'Test administrator', '', '', FALSE, TRUE, TRUE, 0 );
INSERT INTO Room (roomId, roomAdmin, userPreferences, movies, roomClosed, usersCanVote, usersCanJoin, numberOfMovies) VALUES ( 'Fgf2NLjhh9mx', 'Test administrator', '', '', TRUE, TRUE, TRUE, 0 );


-- -- RoomUser table
drop table RoomUser if exists;
create table RoomUser ( roomId varchar(255) not null, userNickname varchar(255) not null, creationDate timestamp not null, votes varchar(255) not null, primary key (roomId, userNickname));
TRUNCATE TABLE RoomUser;
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( '7b07c2qj7lvc', 'KnuckleDust', CURRENT_TIMESTAMP, '[2, 5, 1, -1, 0]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( '7b07c2qj7lvc', 'Frieda', CURRENT_TIMESTAMP, '[0, 2, 4, 5, 0]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( '7b07c2qj7lvc', 'Mayflower', CURRENT_TIMESTAMP, '[-4, 2, 10, 1, 1]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( '7b07c2qj7lvc', 'AtomicX', CURRENT_TIMESTAMP, '[3, 2, 3, 1, 1]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( '7b07c2qj7lvc', 'SandySun', CURRENT_TIMESTAMP, '[0, 0, 1, 1, 0]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( '7b07c2qj7lvc', 'OgreMan', CURRENT_TIMESTAMP, '[-1, 2, 6, 2, 1]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( '7b07c2qj7lvc', 'Test administrator', CURRENT_TIMESTAMP, '[7, 8, 1, 5, 4]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'JC3Tzrx2c1nx', 'MicroMash', CURRENT_TIMESTAMP, '[10, 2, 8, -2, 1]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'JC3Tzrx2c1nx', 'Test administrator', CURRENT_TIMESTAMP, '[3, 3, 3, 3, 3]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'JC3Tzrx2c1nx', 'RiseUp', CURRENT_TIMESTAMP, '[1, -1, 0, 1, 2]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( '99rxfyog0a87', 'Test administrator', CURRENT_TIMESTAMP, '[0, 0, 0, 0, 0]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'WN5sgnxYD8tC', 'Lemony', CURRENT_TIMESTAMP, '[3, 1, -1, 0, 0]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'WN5sgnxYD8tC', 'RustySilver', CURRENT_TIMESTAMP, '[0, -2, 1, 1, 0]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'WN5sgnxYD8tC', 'JulesCrown', CURRENT_TIMESTAMP, '[5, 2, 0, 10, 3]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'WN5sgnxYD8tC', 'Test administrator', CURRENT_TIMESTAMP, '[4, 3, 1, 7, 4]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'WN5sgnxYD8tC', 'RobbingHood', CURRENT_TIMESTAMP, '[1, 6, 3, -2, -4]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'WN5sgnxYD8tC', 'Everyday', CURRENT_TIMESTAMP, '[1, 1, 9, -5, 2]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'Fgf2NLjhh9mx', 'Test administrator', CURRENT_TIMESTAMP, '[8, 9, 3, 6, 2]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'Fgf2NLjhh9mx', 'Storm', CURRENT_TIMESTAMP, '[1, 1, 0, 1, 1]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'Fgf2NLjhh9mx', 'Jelly', CURRENT_TIMESTAMP, '[0, 1, 0, 1, 0]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'Fgf2NLjhh9mx', 'Bob', CURRENT_TIMESTAMP, '[6, 5, 4, 3, 2]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'Fgf2NLjhh9mx', 'Patel', CURRENT_TIMESTAMP, '[-2, -1, 0, 1, 2]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'Fgf2NLjhh9mx', 'Nickname', CURRENT_TIMESTAMP, '[10, 4, 5, 1, 3]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'Fgf2NLjhh9mx', 'Man', CURRENT_TIMESTAMP, '[0, 1, 1, 0, 0]' );
INSERT INTO RoomUser (roomId, userNickname, creationDate, votes) VALUES ( 'Fgf2NLjhh9mx', 'Woman', CURRENT_TIMESTAMP, '[0, 0, 5, 7, 4]' );
