CREATE TABLE `users`
(
    `id`            INT          NOT NULL AUTO_INCREMENT,
    `username`      VARCHAR(50)  NOT NULL,
    `password`      VARCHAR(512) NULL,
    `email`         VARCHAR(100) NOT NULL,
    `refresh_token` VARCHAR(512) NOT NULL,
    `status`        VARCHAR(20)  NULL DEFAULT '',
    `about_me`      VARCHAR(512) NULL DEFAULT '',
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

CREATE TABLE `education`
(
    `id`        INT          NOT NULL AUTO_INCREMENT,
    `user_id`   INT          NOT NULL,
    `degree`    varchar(255) NOT NULL,
    `major`     varchar(50)  NOT NULL,
    `graduated` BIT          NOT NULL,
    PRIMARY KEY (`id`),
    INDEX       `idx1_user_id` (`user_id`),
    CONSTRAINT `education_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE `link`
(
    `id`          INT          NOT NULL AUTO_INCREMENT,
    `user_id`     INT          NOT NULL,
    `name`        varchar(50)  NOT NULL,
    `link`        varchar(256) NOT NULL,
    `description` varchar(512) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX         `idx1_user_id` (`user_id`),
    CONSTRAINT `link_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE `work`
(
    `id`         INT          NOT NULL AUTO_INCREMENT,
    `user_id`    INT          NOT NULL,
    `role`       varchar(50)  NOT NULL,
    `technology` varchar(256) NOT NULL,
    `years`      INT          NOT NULL,
    PRIMARY KEY (`id`),
    INDEX        `idx1_user_id` (`user_id`),
    CONSTRAINT `work_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE `posts`
(
    `id`            INT           NOT NULL AUTO_INCREMENT,
    `user_id`       INT           NOT NULL,
    `name`          varchar(50)   NOT NULL,
    `deadline`      varchar(50)   NOT NULL,
    `schedule`      varchar(50)   NOT NULL,
    `summary`       varchar(1024) NOT NULL,
    `qualification` varchar(512)  NOT NULL,
    `size`          varchar(128)  NOT NULL,
    PRIMARY KEY (`id`),
    INDEX           `idx1_user_id` (`user_id`),
    CONSTRAINT `posts_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);
