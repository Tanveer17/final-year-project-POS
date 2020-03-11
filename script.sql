
CREATE DATABASE  IF NOT EXISTS `attire_cloths` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `attire_cloths`;

DROP TABLE IF EXISTS `suppliers`;

CREATE TABLE  `suppliers` (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           address VARCHAR(200),
                           phone_number VARCHAR(50));


DROP TABLE IF EXISTS `items`;

CREATE TABLE `items`(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(100) NOT NULL,
                              supplier_id INT NOT NULL,
                              price_per_meter DECIMAL(10,2),
                              price_per_suit DECIMAL(10,2),
                              meters_in_a_suit DECIMAL(5,2),
                              FOREIGN KEY (supplier_id) REFERENCES suppliers(id));


DROP TABLE IF EXISTS `expenses`;

CREATE TABLE `expenses` (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         description VARCHAR(100) NOT NULL,
                         expense_date DATE NOT NULL,
                         amount DECIMAL(10,2) NOT NULL);


DROP TABLE IF EXISTS `purchases`;

CREATE TABLE `purchases` (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          item_id INT NOT NULL,
                          purchase_date DATE NOT NULL,
                          price_per_meter DECIMAL(10,2) NOT NULL,
                          no_of_meter_purchases DECIMAL(10,2) NOT NULL,
                          price_per_piece DECIMAL(10,2) NOT NULL,
                          no_of_pieces_purchases DECIMAL(10,2) NOT NULL,
                          total_price DECIMAL(10,2) NOT NULL,
                          price_paid DECIMAL(10,2),
                          price_rem DECIMAL(10,2),
                          FOREIGN KEY (item_id) REFERENCES items(id));

DROP TABLE IF EXISTS `sales`;

CREATE TABLE `sales` (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      item_id INT NOT NULL,
                      sale_date DATE NOT NULL,
                      sale_time TIME NOT NULL,
                      no_of_meters_sold DECIMAL(5,2) NOT NULL,
                      no_of_suits_sold DECIMAL(5,2),
                      amount DECIMAL(10,2) NOT NULL,
                      profit DECIMAL(10,2),
                      FOREIGN KEY (item_id) REFERENCES items(id));


DROP TABLE IF EXISTS `stocks`;

CREATE TABLE `stocks` (item_id INT NOT NULL PRIMARY KEY,
                       total_meter_purchases DECIMAL(20,3),
                       total_pieces_purchases DECIMAL(20,3),
                       currently_in_stock_meters DECIMAL(20,3),
                       currently_in_stock_pieces DECIMAL(20,3),
                       FOREIGN KEY(item_id) REFERENCES items(id));
