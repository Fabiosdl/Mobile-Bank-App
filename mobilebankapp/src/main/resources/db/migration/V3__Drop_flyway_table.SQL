DROP IF EXISTS `testing-flyway`;

CREATE TABLE `testing_flyway` (
  `id` int NOT NULL AUTO_INCREMENT,
  `column_1` varchar(45) DEFAULT NULL,
  `column_2` varchar(45) DEFAULT NULL,
  `column_3` varchar(45) DEFAULT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

