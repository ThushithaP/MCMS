CREATE TABLE `__mcms`.`user` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(100) NULL,
  `last_name` VARCHAR(100) NULL,
  `email` VARCHAR(100) NULL,
  `role` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  PRIMARY KEY (`user_id`))
ENGINE = MyISAM;

INSERT INTO `__mcms`.`user` (`first_name`, `last_name`, `email`, `password`) VALUES ('Super', 'Admin', 'mcms@test.com', '1234');

CREATE TABLE `__mcms`.`customer` (
  `customer_id` INT NOT NULL AUTO_INCREMENT,
  `fullname` VARCHAR(400) NULL,
  `name_initial` VARCHAR(200) NULL,
  `short_name` VARCHAR(100) NULL,
  `gender` VARCHAR(10) NULL,
  `nic` VARCHAR(45) NULL,
  `address` VARCHAR(400) NULL,
  `mobile_number` VARCHAR(15) NULL,
  `phone_number` VARCHAR(15) NULL,
  `marital_status` VARCHAR(10) NULL,
  `customer_status` CHAR(1) NULL DEFAULT 'A',
  `dob` VARCHAR(10) NULL,
  `created_at` DATETIME NULL,
  `created_by` INT NULL,
  `lmd` DATETIME NULL,
  `updated_by` INT NULL,
  PRIMARY KEY (`customer_id`))
ENGINE = MyISAM;

CREATE TABLE `__mcms`.`loan` (
  `loan_id` INT NOT NULL AUTO_INCREMENT,
  `customer_id` INT NULL,
  `working_sector` VARCHAR(45) NULL,
  `profession` VARCHAR(45) NULL,
  `loan_amount` DECIMAL(11,2) NULL,
  `interest` DECIMAL(6,2) NULL,
  `duration` INT NULL,
  `installment` DECIMAL(11,2) NULL,
  `loan_status` CHAR(1) NULL DEFAULT 'P',
  `created_at` DATETIME NULL,
  `created_by` INT NULL,
  `lmd` DATETIME NULL,
  `updated_by` INT NULL,
  PRIMARY KEY (`loan_id`));

