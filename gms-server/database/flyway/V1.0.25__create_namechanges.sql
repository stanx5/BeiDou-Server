CREATE TABLE IF NOT EXISTS `namechanges`
(
    `id`             INT(11)     NOT NULL AUTO_INCREMENT,
    `characterid`    INT(11)     NOT NULL,
    `old`            VARCHAR(13) NOT NULL,
    `new`            VARCHAR(13) NOT NULL,
    `requestTime`    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `completionTime` TIMESTAMP   NULL,
    PRIMARY KEY (`id`),
    INDEX (characterid)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;