DROP TABLE Room IF EXISTS;

CREATE TABLE Room (
    roomId bigint NOT NULL,
    roomAdminId bigint NOT NULL,
    roomClosed boolean DEFAULT FALSE
);

TRUNCATE TABLE Room;
INSERT INTO Room (roomId, roomAdminId, roomClosed) VALUES ( 1, 1, FALSE );
INSERT INTO Room (roomId, roomAdminId, roomClosed) VALUES ( 2, 2, FALSE );
INSERT INTO Room (roomId, roomAdminId, roomClosed) VALUES ( 3, 1, FALSE );
INSERT INTO Room (roomId, roomAdminId, roomClosed) VALUES ( 4, 1, TRUE );
