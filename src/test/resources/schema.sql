CREATE TABLE `users`
(
    `id`            INT NOT NULL AUTO_INCREMENT,
    `username`      VARCHAR(50) NULL,
    `password`      VARCHAR(50) NULL,
    `email`         VARCHAR(100) NULL,
    `refresh_token` VARCHAR(512) NULL,
    PRIMARY KEY (`id`),
    INDEX           `idx1_username` (`username` ASC),
    INDEX           `idx2_email` (`email` ASC),
    INDEX           `idx3_refresh_token` (`refresh_token` ASC)
);

CREATE TABLE `sns_info`
(
    `user_id`       INT          NOT NULL,
    `sns_id`        varchar(255) NOT NULL,
    `sns_type`      varchar(10) NULL,
    `access_token`  VARCHAR(512) NULL,
    `refresh_token` VARCHAR(512) NULL,
    KEY             `idx01_user_id` (`user_id`),
    KEY             `idx02_sns_id` (`sns_id`),
    CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);
