TRUNCATE users CASCADE;
ALTER SEQUENCE users_id_seq RESTART WITH 1;

TRUNCATE tokens CASCADE;

TRUNCATE boards CASCADE;
ALTER SEQUENCE boards_id_seq RESTART WITH 1;

TRUNCATE user_board CASCADE;

TRUNCATE lists CASCADE;
ALTER SEQUENCE lists_id_seq RESTART WITH 1;

TRUNCATE cards CASCADE;
ALTER SEQUENCE cards_id_seq RESTART WITH 1;

