
    create table Room (
       roomId varchar(255) not null,
        roomAdminId int4 not null,
        roomClosed boolean not null,
        primary key (roomId)
    );

    create table Room_User (
       userId int4 not null,
        roomId varchar(255) not null,
        creationDate timestamp not null,
        genres TEXT CHECK (char_length(genres) <= 500) not null,
        votes varchar(255) not null,
        primary key (userId, roomId)
    );
