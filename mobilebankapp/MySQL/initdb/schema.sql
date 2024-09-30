DROP SCHEMA IF EXISTS `mobilebankapp`;

CREATE SCHEMA `mobilebankapp`;

USE `mobilebankapp`;

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `user_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(60) DEFAULT NULL,
  `user_details_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_USER_DETAIL_idx` (`user_details_id`),
  CONSTRAINT `FK_USER_DETAIL` FOREIGN KEY (`user_details_id`)
  REFERENCES `user_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `account`(
  `id` int NOT NULL AUTO_INCREMENT,
  `branch_code` varchar(10) DEFAULT NULL,
  `account_code` varchar(15) DEFAULT NULL,
  `balance` double DEFAULT 0.0,
  `account_type` varchar(20) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ACCOUNT_UNIQUE` (`account_code`),
  KEY `FK_ACCOUNT_USER_idx` (`user_id`),
  CONSTRAINT `FK_ACCOUNT_USER` FOREIGN KEY (`user_id`)
  REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `transaction` (
  `id` int NOT NULL AUTO_INCREMENT,
  `transaction_type` varchar(45) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `transaction_date` varchar(40) DEFAULT NULL,
  `transaction_status` varchar(45) DEFAULT NULL,
  `account_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_TRANSACTION_ACCOUNT_idx` (`account_id`),
  CONSTRAINT `FK_TRANSACTION_ACCOUNT` FOREIGN KEY (`account_id`)
  REFERENCES `account`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;