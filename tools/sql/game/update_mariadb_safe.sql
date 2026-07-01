/*
 * MariaDB safe migration for update.sql
 * Corrige bookmark de char_id/id para player_id/(player_id,name)
 * e evita erro quando partes da migration ja foram aplicadas.
 */

SET FOREIGN_KEY_CHECKS=0;

DELIMITER $$

DROP PROCEDURE IF EXISTS `migrate_aion_bookmark_mariadb`$$

CREATE PROCEDURE `migrate_aion_bookmark_mariadb`()
BEGIN
    /* Remove itens antigos somente se a tabela inventory existir. */
    IF EXISTS (
        SELECT 1
        FROM information_schema.TABLES
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = 'inventory'
    ) THEN
        DELETE FROM `inventory`
        WHERE `item_id` IN (182007170, 188100252, 188100253, 188100254, 188100255, 188100256);
    END IF;

    /* Migra a tabela bookmark somente se ela existir. */
    IF EXISTS (
        SELECT 1
        FROM information_schema.TABLES
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = 'bookmark'
    ) THEN
        /* Remove FK antiga se existir. */
        IF EXISTS (
            SELECT 1
            FROM information_schema.TABLE_CONSTRAINTS
            WHERE CONSTRAINT_SCHEMA = DATABASE()
              AND TABLE_NAME = 'bookmark'
              AND CONSTRAINT_NAME = 'bookmark_ibfk_1'
              AND CONSTRAINT_TYPE = 'FOREIGN KEY'
        ) THEN
            ALTER TABLE `bookmark` DROP FOREIGN KEY `bookmark_ibfk_1`;
        END IF;

        /* char_id -> player_id, somente se ainda nao foi migrado. */
        IF EXISTS (
            SELECT 1
            FROM information_schema.COLUMNS
            WHERE TABLE_SCHEMA = DATABASE()
              AND TABLE_NAME = 'bookmark'
              AND COLUMN_NAME = 'char_id'
        ) AND NOT EXISTS (
            SELECT 1
            FROM information_schema.COLUMNS
            WHERE TABLE_SCHEMA = DATABASE()
              AND TABLE_NAME = 'bookmark'
              AND COLUMN_NAME = 'player_id'
        ) THEN
            ALTER TABLE `bookmark` CHANGE COLUMN `char_id` `player_id` INT NOT NULL;
        END IF;

        /* Remove coluna id se existir. */
        IF EXISTS (
            SELECT 1
            FROM information_schema.COLUMNS
            WHERE TABLE_SCHEMA = DATABASE()
              AND TABLE_NAME = 'bookmark'
              AND COLUMN_NAME = 'id'
        ) THEN
            ALTER TABLE `bookmark` DROP COLUMN `id`;
        END IF;

        /* Garante os tipos finais das colunas principais. */
        IF EXISTS (
            SELECT 1
            FROM information_schema.COLUMNS
            WHERE TABLE_SCHEMA = DATABASE()
              AND TABLE_NAME = 'bookmark'
              AND COLUMN_NAME = 'player_id'
        ) THEN
            ALTER TABLE `bookmark`
                MODIFY COLUMN `player_id` INT NOT NULL,
                MODIFY COLUMN `name` VARCHAR(27) NOT NULL,
                MODIFY COLUMN `world_id` INT NOT NULL;
        END IF;

        /* Recria a primary key corretamente. */
        IF EXISTS (
            SELECT 1
            FROM information_schema.TABLE_CONSTRAINTS
            WHERE CONSTRAINT_SCHEMA = DATABASE()
              AND TABLE_NAME = 'bookmark'
              AND CONSTRAINT_NAME = 'PRIMARY'
              AND CONSTRAINT_TYPE = 'PRIMARY KEY'
        ) THEN
            ALTER TABLE `bookmark` DROP PRIMARY KEY;
        END IF;

        ALTER TABLE `bookmark` ADD PRIMARY KEY (`player_id`, `name`);

        /* Recria a foreign key se a tabela players existir. */
        IF EXISTS (
            SELECT 1
            FROM information_schema.TABLES
            WHERE TABLE_SCHEMA = DATABASE()
              AND TABLE_NAME = 'players'
        ) THEN
            ALTER TABLE `bookmark`
                ADD CONSTRAINT `bookmark_ibfk_1`
                FOREIGN KEY (`player_id`) REFERENCES `players` (`id`)
                ON DELETE CASCADE ON UPDATE CASCADE;
        END IF;
    END IF;

    DROP TABLE IF EXISTS `ingameshop`;
    DROP TABLE IF EXISTS `ingameshop_log`;
END$$

DELIMITER ;

CALL `migrate_aion_bookmark_mariadb`();
DROP PROCEDURE IF EXISTS `migrate_aion_bookmark_mariadb`;

SET FOREIGN_KEY_CHECKS=1;
