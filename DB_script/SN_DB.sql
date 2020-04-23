-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.12-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for social_network
CREATE DATABASE IF NOT EXISTS `social_network` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `social_network`;

-- Dumping structure for table social_network.authorities
CREATE TABLE IF NOT EXISTS `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY `USERNAME_AUTHORITY` (`username`,`authority`),
  CONSTRAINT `authorities_users_username_fk` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table social_network.comments
CREATE TABLE IF NOT EXISTS `comments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(200) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `post_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `comments_posts_id_fk` (`post_id`),
  KEY `comments_users_id_fk` (`user_id`),
  CONSTRAINT `comments_posts_id_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `comments_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table social_network.likes
CREATE TABLE IF NOT EXISTS `likes` (
  `like_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `post_id` bigint(20) NOT NULL,
  PRIMARY KEY (`like_id`),
  UNIQUE KEY `UQ_UserID_PostID` (`user_id`,`post_id`),
  KEY `likes_posts_id_fk` (`post_id`),
  CONSTRAINT `likes_posts_id_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `likes_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table social_network.	tures
CREATE TABLE IF NOT EXISTS `pictures` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` longblob DEFAULT NULL,
  `is_public` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table social_network.posts
CREATE TABLE IF NOT EXISTS `posts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `text` varchar(300) DEFAULT NULL,
  `picture` blob DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `is_public` tinyint(1) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `posts_users_id_fk` (`created_by`),
  CONSTRAINT `posts_users_id_fk` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table social_network.requests
CREATE TABLE IF NOT EXISTS `requests` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sender_id` bigint(20) NOT NULL,
  `receiver_id` bigint(20) NOT NULL,
  `is_accepted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `requests_users_id_fk` (`sender_id`),
  KEY `requests_users_id_fk_2` (`receiver_id`),
  CONSTRAINT `requests_users_id_fk` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`),
  CONSTRAINT `requests_users_id_fk_2` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table social_network.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(68) NOT NULL,
  `picture` longblob DEFAULT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT 1,
  `age` int(11) NOT NULL DEFAULT 0,
  `is_public` tinyint(1) DEFAULT 0,
  `job_title` varchar(255) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `cover_photo` longblob DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_authorities_username_fk` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table social_network.users_friends
CREATE TABLE IF NOT EXISTS `users_friends` (
  `user_id` bigint(20) NOT NULL,
  `friend_id` bigint(20) NOT NULL,
  UNIQUE KEY `USERS_FRIENDS_UK` (`user_id`,`friend_id`),
  KEY `users_friends_users_id_fk_2` (`friend_id`),
  CONSTRAINT `users_friends_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `users_friends_users_id_fk_2` FOREIGN KEY (`friend_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
