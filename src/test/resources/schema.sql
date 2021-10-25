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

CREATE TABLE `tags`
(
    `id`    INT         NOT NULL AUTO_INCREMENT,
    `value` varchar(32) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE `idx1_value` (`value`)
);

CREATE TABLE `tag_post_map`
(
    `id`      INT NOT NULL AUTO_INCREMENT,
    `tag_id`  INT NOT NULL,
    `post_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX     `idx1_tag_id` (`tag_id`),
    INDEX     `idx1_post_id` (`post_id`),
    CONSTRAINT `tag_post_map_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`),
    CONSTRAINT `tag_post_map_post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
);

CREATE TABLE `tag_user_map`
(
    `id`      INT NOT NULL AUTO_INCREMENT,
    `tag_id`  INT NOT NULL,
    `user_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX     `idx1_tag_id` (`tag_id`),
    INDEX     `idx1_user_id` (`user_id`),
    CONSTRAINT `tag_user_map_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`),
    CONSTRAINT `tag_user_map_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE `keyword_post_map`
(
    `id`      INT         NOT NULL AUTO_INCREMENT,
    `keyword` varchar(64) NOT NULL,
    `post_id` INT         NOT NULL,
    PRIMARY KEY (`id`),
    INDEX     `idx1_keyword_id` (`id`),
    INDEX     `idx1_post_id` (`post_id`),
    CONSTRAINT `keyword_post_map_post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
);

CREATE TABLE `comments`
(
    `id`        INT          NOT NULL AUTO_INCREMENT,
    `parent_id` INT NULL,
    `post_id`   INT          NOT NULL,
    `user_id`   INT          NOT NULL,
    `date`      INT          NOT NULL,
    `deleted`   BIT          NOT NULL,
    `text`      varchar(512) NOT NULL,
    PRIMARY KEY (`id`),
    KEY         `parent_id_idex` (`parent_id`),
    KEY         `post_id_index` (`post_id`),
    KEY         `user_id_index` (`user_id`),
    CONSTRAINT `comment_user_map` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE `likes`
(
    `user_id` INT NOT NULL,
    `post_id` INT NOT NULL,
    CONSTRAINT `likes_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `likes_post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
);

CREATE TABLE `members`
(
    `id`         INT NOT NULL AUTO_INCREMENT,
    `user_id`    INT NOT NULL,
    `post_id`    INT NOT NULL,
    `authorized` BIT NOT NULL,
    PRIMARY KEY (`id`),
    KEY          `members_key_user_id_post_id` (`user_id`, `post_id`),
    KEY          `members_key_post_id` (`post_id`),
    CONSTRAINT `members_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `members_post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
);

CREATE TABLE `notifications`
(
    `id`               INT NOT NULL AUTO_INCREMENT,
    `sender_user_id`   INT NOT NULL,
    `receiver_user_id` INT NOT NULL,
    `post_id`          INT NOT NULL,
    `event_category`   INT NOT NULL,
    `timestamp`        INT NOT NULL,
    `stale`            BIT NOT NULL,
    PRIMARY KEY (`id`),
    KEY                `notifications_key_receiver_user_id_timestamp` (`receiver_user_id`, `timestamp`),
    CONSTRAINT `notifications_sender_user_id` FOREIGN KEY (`sender_user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `notifications_receiver_user_id` FOREIGN KEY (`receiver_user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `notifications_post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
);
