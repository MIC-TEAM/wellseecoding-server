CREATE TABLE `users`
(
    `id`            INT          NOT NULL AUTO_INCREMENT,
    `username`      VARCHAR(50)  NOT NULL,
    `password`      VARCHAR(512) NULL,
    `email`         VARCHAR(100) NOT NULL,
    `refresh_token` VARCHAR(512) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE `idx1_email` (`email`),
    INDEX           `idx2_refresh_token` (`refresh_token`)
);

CREATE TABLE `sns_info`
(
    `user_id`       INT          NOT NULL,
    `sns_id`        varchar(255) NOT NULL,
    `sns_type`      varchar(10)  NOT NULL,
    PRIMARY KEY (`sns_id`, `sns_type`),
    INDEX           `idx1_user_id` (`user_id`),
    CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);
