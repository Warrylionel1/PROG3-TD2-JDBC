insert into dish (id, name, dish_type, selling_price)
    values (1, 'Salade fraîche', 'STARTER', 3500.00),
           (2, 'Poulet grillé', 'MAIN', 12000.00),
           (3, 'Riz aux légumes', 'MAIN', NULL),
           (4, 'Gâteau au chocolat', 'DESSERT', 8000.00),
           (5, 'Salade de fruits', 'DESSERT', NULL);

insert into ingredient (id, name, price, category)
    values (1, 'Laitue', 800.00, 'VEGETABLE'),
           (2, 'Tomate', 600.00, 'VEGETABLE'),
           (3, 'Poulet', 8000.00, 'ANIMAL'),
           (4, 'Huile', 1500.00, 'OTHER'),
           (5, 'Chocolat', 7000.00, 'OTHER'),
           (6, 'Fruits assortis', 3000.00, 'VEGETABLE');

insert into dish_ingredient (id, id_dish, id_ingredient, quantity_required, unit)
    values (1, 1, 1, 0.20, 'KG'),
           (2, 1, 2, 0.15, 'KG'),
           (3, 2, 3, 1.00, 'KG'),
           (4, 2, 4, 0.30, 'L'),
           (5, 4, 5, 0.20, 'KG');