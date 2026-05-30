insert into persons (id, first_name, last_name, email, password) values
    (1, 'Hannah', 'Peel', 'hannah.peel@lifeisbananas.be', '$2b$12$jdGmqybtq.LYDQkFldRlz.OvWiBPE.zK6tgBQHH8ZmeYotxfsnpMe'),
    (2, 'Marcus', 'Store', 'marcus.store@lifeisbananas.be', '$2b$12$H2ExKSCc5fNpE1A6nwRItewpOPnA668f7BTNxQqktXB7Aw5FBBF9C'),
    (3, 'Nina', 'Cross', 'nina.cross@lifeisbananas.be', '$2b$12$jU.JaeE/FNA6wGl8PRoNze91XGpgUg3qDeANyuuJGhgdu6ckWJNpy');

insert into roles (id, role_name, person_id) values
    (1, 'BIO_ENGINEER', 1),
    (2, 'WAREHOUSE_MANAGER', 2),
    (3, 'BIO_ENGINEER', 3),
    (4, 'WAREHOUSE_MANAGER', 3);

insert into raw_materials (id, code, name, organic_certified, price, amount, unit) values
    (1, 'RM-001', 'Freeze-Dried Banana Flakes', true, 8.40, 5.50, 'KG'),
    (2, 'RM-002', 'Organic Banana Puree', true, 6.95, 12.00, 'LITER'),
    (3, 'RM-003', 'Crispy Banana Chips', false, 4.20, 100.00, 'PIECE');
