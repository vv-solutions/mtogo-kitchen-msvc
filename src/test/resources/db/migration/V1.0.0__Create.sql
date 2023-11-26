CREATE TABLE ticket (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    supplier_id INT,
    comment VARCHAR(255),
    pickup_time DATETIME,
    status INT,
    create_stamp DATETIME
);

CREATE TABLE ticket_line (
     id INT PRIMARY KEY AUTO_INCREMENT,
     product_id INT,
     quantity INT,
     product_name VARCHAR(255),
     ticket_id INT,
     FOREIGN KEY (ticket_id) REFERENCES ticket(id)
);