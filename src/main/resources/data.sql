INSERT INTO ADDRESS (CITY, STREET, BUILDING_NUMBER)
VALUES ('CITY', 'AVENUE', 100),
       ('TOWN', 'STREET', 22);

INSERT INTO RESTAURANT (NAME, ADDRESS_ID)
VALUES ('ASTORIA', 1),
       ('CONTINENTAL', 1),
       ('PRAGUE', 2),
       ('SUSHI BAR', 1),
       ('NIAM-NIAM', 2);

INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('User1', 'user1@yandex.ru', '{noop}password1'),
       ('User2', 'user2@yandex.ru', '{noop}password2'),
       ('User3', 'user3@yandex.ru', '{noop}password3'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest'),
       ('User4', 'user4@yandex.ru', '{noop}password4'),
       ('User5', 'user5@yandex.ru', '{noop}password5'),
       ('User6', 'user6@yandex.ru', '{noop}password6');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('USER', 2),
       ('USER', 3),
       ('ADMIN', 4),
       ('USER', 4),
       ('USER', 6),
       ('USER', 7);

INSERT INTO VOTE (VOTE_DATE, VOTE_TIME, RESTAURANT_ID, USER_ID)
VALUES (CURRENT_DATE - 2, CURRENT_TIME, 2 , 4),
       (CURRENT_DATE - 1, CURRENT_TIME, 2 , 1),
       (CURRENT_DATE - 1, CURRENT_TIME, 3 , 6),
       (CURRENT_DATE - 1, CURRENT_TIME, 5 , 4),
       (CURRENT_DATE - 1, CURRENT_TIME, 3 , 3);

INSERT INTO VOTE (RESTAURANT_ID, USER_ID)
VALUES (1, 1),
       (1, 4),
       (3, 2),
       (3, 3),
       (5, 6),
       (5, 7);

INSERT INTO MENU_ITEM (NAME, PRICE, RESTAURANT_ID)
VALUES ('Escalope', 250, 1),
       ('Grilled chicken', 110, 1),
       ('Marinated squid', 50, 1),
       ('Scrambled eggs', 90, 2),
       ('Vegetable stew', 105, 2),
       ('Italian pasta', 60, 2),
       ('Sponge cake', 310, 3),
       ('Coconut ice cream', 210, 3),
       ('Coffee with milk', 25, 3),
       ('Coffee', 10, 5),
       ('Tea', 5, 5);

INSERT INTO MENU_ITEM (NAME, REGISTERED, PRICE, RESTAURANT_ID)
VALUES ('Coffee2', CURRENT_DATE - 1, 10, 5),
       ('Tea2', CURRENT_DATE - 1, 5, 5);