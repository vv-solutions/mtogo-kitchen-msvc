INSERT INTO ticket (order_id, supplier_id, comment, pickup_time, status, create_stamp)
VALUES
    (1, 1, 'Extra cheese', '2023-11-21 11:00:00', 1, '2023-11-21 10:30:00'),
    (2, 2, 'Gluten-free pasta', '2023-11-21 12:15:00', 2, '2023-11-21 11:45:00'),
    (3, 4, 'Extra spicy', '2023-11-21 13:30:00', 1, '2023-11-21 13:00:00'),
    (4, 4, 'Without onions', '2023-11-21 13:30:00', 2, '2023-11-21 13:00:00'),
    (5, 4, 'Without tomatoes', '2023-11-21 13:30:00', 2, '2023-11-21 13:00:00'),
    (6, 4, '', null, 4, '2023-11-21 13:00:00');

INSERT INTO ticket_line (product_id, quantity, product_name, ticket_id)
VALUES
    (1, 2, 'Pizza Margherita', 1),
    (2, 1, 'Spaghetti Bolognese', 1),
    (3, 3, 'Chicken Alfredo', 2),
    (4, 2, 'Vegetarian Sushi Roll', 3),
    (5, 1, 'Burger', 4),
    (5, 1, 'Burger', 5),
    (5, 2, 'Burger', 6);

