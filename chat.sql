/*
SQLyog Ultimate v11.13 (64 bit)
MySQL - 5.7.23 : Database - chat
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`chat` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `chat`;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `head_img` varchar(255) DEFAULT NULL,
  `my_desc` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`username`,`password`,`head_img`,`my_desc`,`email`) values (1,'李沫熙','6512bd43d9caa6e02c990b0a82652dca','http://phxh0zvpl.bkt.clouddn.com/1547958142623779','今天有个小仙女给我准备了惊喜，喜欢',NULL),(2,'张爱吉','b6d767d2f8ed5d21a44b0e5886680cb9','http://pgfgqbd3k.bkt.clouddn.com/20180518090848273.jpg','今天有个小仙女给我准备了惊喜，喜欢',NULL),(3,'刘亦菲','182be0c5cdcd5072bb1864cdee4d3d6e','http://pgfgqbd3k.bkt.clouddn.com/20180518090849745.jpg','今天有个小仙女给我准备了惊喜，喜欢',NULL),(4,'刘德华','f7177163c833dff4b38fc8d2872f1ec6','https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=4145689859,1209105028&fm=27&gp=0.jpg','昨天有个小仙女给我准备了惊喜，喜欢',NULL),(5,'杨宁宁','b53b3a3d6ab90ce0268229151c9bde11','http://phxh0zvpl.bkt.clouddn.com/1541772019279624','我是最美的小女生杨宁宁童鞋',NULL),(6,'李茂展','6512bd43d9caa6e02c990b0a82652dca','http://phxh0zvpl.bkt.clouddn.com/1541915931172711','这个人很懒，没有留下签名~','lmx110522@163.com');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
