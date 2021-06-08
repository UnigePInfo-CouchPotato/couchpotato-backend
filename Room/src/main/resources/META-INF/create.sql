
    create table Room (
       roomId varchar(255) not null,
        movies TEXT not null,
        numberOfMovies int4 not null,
        roomAdmin TEXT not null,
        roomClosed boolean not null,
        userPreferences TEXT not null,
        usersCanJoin boolean not null,
        usersCanVote boolean not null,
        primary key (roomId)
    )

    create table RoomUser (
       userNickname varchar(255) not null,
        roomId varchar(255) not null,
        creationDate timestamp not null,
        votes varchar(255) not null,
        primary key (userNickname, roomId)
    )
