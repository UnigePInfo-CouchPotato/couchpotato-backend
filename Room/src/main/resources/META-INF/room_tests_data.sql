-- Room table
TRUNCATE TABLE Room;
INSERT INTO Room (roomId, roomAdminId, roomClosed) VALUES ( 1, 1, FALSE );
INSERT INTO Room (roomId, roomAdminId, roomClosed) VALUES ( 2, 6, FALSE );
INSERT INTO Room (roomId, roomAdminId, roomClosed) VALUES ( 3, 3, FALSE );
INSERT INTO Room (roomId, roomAdminId, roomClosed) VALUES ( 4, 2, TRUE );

-- Room_User table
ALTER TABLE Room_User DROP PRIMARY KEY;
ALTER TABLE Room_User ADD PRIMARY KEY (roomId, userId);
TRUNCATE TABLE Room_User;
INSERT INTO Room_User (roomId, userId, creationDate) VALUES ( 1, 1, CURRENT_TIMESTAMP );
INSERT INTO Room_User (roomId, userId, creationDate) VALUES ( 1, 2, CURRENT_TIMESTAMP );
INSERT INTO Room_User (roomId, userId, creationDate) VALUES ( 1, 3, CURRENT_TIMESTAMP );
INSERT INTO Room_User (roomId, userId, creationDate) VALUES ( 2, 4, CURRENT_TIMESTAMP );
INSERT INTO Room_User (roomId, userId, creationDate) VALUES ( 2, 5, CURRENT_TIMESTAMP );
INSERT INTO Room_User (roomId, userId, creationDate) VALUES ( 2, 6, CURRENT_TIMESTAMP );
INSERT INTO Room_User (roomId, userId, creationDate) VALUES ( 3, 3, CURRENT_TIMESTAMP );
INSERT INTO Room_User (roomId, userId, creationDate) VALUES ( 4, 1, CURRENT_TIMESTAMP );
INSERT INTO Room_User (roomId, userId, creationDate) VALUES ( 4, 2, CURRENT_TIMESTAMP );
INSERT INTO Room_User (roomId, userId, creationDate) VALUES ( 4, 3, CURRENT_TIMESTAMP );
INSERT INTO Room_User (roomId, userId, creationDate) VALUES ( 4, 4, CURRENT_TIMESTAMP );
INSERT INTO Room_User (roomId, userId, creationDate) VALUES ( 4, 5, CURRENT_TIMESTAMP );
INSERT INTO Room_User (roomId, userId, creationDate) VALUES ( 4, 6, CURRENT_TIMESTAMP );