-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 24, 2012 at 07:17 AM
-- Server version: 5.5.16
-- PHP Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `tis`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE IF NOT EXISTS `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `first_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `status` int(11) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `calendar` bigint(20) DEFAULT NULL,
  `role` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `FKB9D38A2DE482C19F` (`role`),
  KEY `FKB9D38A2D264BDC99` (`calendar`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=4 ;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`id`, `email`, `first_name`, `last_name`, `password`, `status`, `version`, `calendar`, `role`) VALUES
(1, 'admin@tis.teamspace.com', 'Đức Xuân', 'Nguyễn', '827ccb0eea8a706c4c34a16891f84e7b', 2, 1, 1, 1),
(2, 'abcxyz2357@yahoo.com', 'Văn Hoàng', 'Đinh', '827ccb0eea8a706c4c34a16891f84e7b', 2, 1, 2, 1),
(3, 'abcxyz2357@gmail.com', 'Bình ', 'Nguyễn', '827ccb0eea8a706c4c34a16891f84e7b', 2, 1, 3, 1);

-- --------------------------------------------------------

--
-- Table structure for table `application_role`
--

CREATE TABLE IF NOT EXISTS `application_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=3 ;

--
-- Dumping data for table `application_role`
--

INSERT INTO `application_role` (`id`, `name`, `version`) VALUES
(1, 'administrator', 1),
(2, 'user', 0);

-- --------------------------------------------------------

--
-- Table structure for table `application_role_permissions`
--

CREATE TABLE IF NOT EXISTS `application_role_permissions` (
  `application_role` bigint(20) NOT NULL,
  `permissions` bigint(20) NOT NULL,
  KEY `FK20C0794AE458472E` (`application_role`),
  KEY `FK20C0794AB4658D30` (`permissions`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `application_role_permissions`
--

INSERT INTO `application_role_permissions` (`application_role`, `permissions`) VALUES
(1, 4),
(1, 3),
(1, 2),
(1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `attachment`
--

CREATE TABLE IF NOT EXISTS `attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `display_file_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `real_file_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `work_item` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8AF759237E97DB62` (`work_item`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=3 ;

--
-- Dumping data for table `attachment`
--

INSERT INTO `attachment` (`id`, `display_file_name`, `real_file_name`, `version`, `work_item`) VALUES
(1, 'ThietKe_V1.0.docx', 'C:\\Users\\xx\\Documents\\workspace-sts-2.9.1.RELEASE\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\TIS\\uploadedfiles\\1', 2, 1),
(2, 'ThietKe_V1.1.docx', 'C:\\Users\\xx\\Documents\\workspace-sts-2.9.1.RELEASE\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\TIS\\uploadedfiles\\2', 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `calendar`
--

CREATE TABLE IF NOT EXISTS `calendar` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=5 ;

--
-- Dumping data for table `calendar`
--

INSERT INTO `calendar` (`id`, `version`) VALUES
(1, 7),
(2, 2),
(3, 2),
(4, 2);

-- --------------------------------------------------------

--
-- Table structure for table `calendar_events`
--

CREATE TABLE IF NOT EXISTS `calendar_events` (
  `calendars` bigint(20) NOT NULL,
  `events` bigint(20) NOT NULL,
  KEY `FK28E1613AE76D4E50` (`calendars`),
  KEY `FK28E1613ADEB66AB6` (`events`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `calendar_events`
--

INSERT INTO `calendar_events` (`calendars`, `events`) VALUES
(1, 2),
(1, 8),
(1, 10),
(4, 8),
(4, 10),
(2, 8),
(2, 10),
(3, 8),
(3, 10);

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment_date` datetime DEFAULT NULL,
  `content` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `project_member` bigint(20) DEFAULT NULL,
  `work_item` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK38A5EE5F7E97DB62` (`work_item`),
  KEY `FK38A5EE5FB98CB735` (`project_member`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=7 ;

--
-- Dumping data for table `comment`
--

INSERT INTO `comment` (`id`, `comment_date`, `content`, `version`, `project_member`, `work_item`) VALUES
(1, '2012-06-24 09:49:27', 'task được giao cho Văn Hoàng Đinh.', 0, 1, 1),
(2, '2012-06-24 09:50:00', 'ok. :D', 0, 2, 1),
(3, '2012-06-24 09:50:42', 'Đã xong thiết kế V1.0. Mọi người xem thử cần bổ sung gì không. @_@', 0, 2, 1),
(4, '2012-06-24 09:51:43', 'Theo t nghĩ nên bổ sung bảng Account. :))', 0, 3, 1),
(5, '2012-06-24 09:52:51', 'ok. thanks Bình Nguyễn. Đã sửa xong, V1.1', 0, 2, 1),
(6, '2012-06-24 09:53:50', 'good. task finish. 8-)', 0, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `event`
--

CREATE TABLE IF NOT EXISTS `event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `end_date` datetime NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `start_date` datetime NOT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=11 ;

--
-- Dumping data for table `event`
--

INSERT INTO `event` (`id`, `description`, `end_date`, `name`, `start_date`, `version`) VALUES
(1, '', '2012-06-29 06:00:00', 'Hội thảo kĩ năng mềm', '2012-06-29 01:30:00', 1),
(2, '', '2012-06-29 06:00:00', 'Hội thảo kĩ năng mềm', '2012-06-29 01:30:00', 0),
(3, '', '2012-06-28 10:30:00', 'Gặp thầy hướng dẫn', '2012-06-28 07:00:00', 1),
(5, '', '2012-06-25 05:00:00', 'Nộp khóa luân', '2012-06-25 00:30:00', 1),
(7, '', '2012-06-25 02:30:00', 'Nộp khóa luận', '2012-06-25 00:00:00', 0),
(8, '', '2012-06-25 02:30:00', 'Nộp khóa luận', '2012-06-25 00:00:00', 0),
(9, '', '2012-06-28 12:00:00', 'Gặp thầy hướng dẫn', '2012-06-28 08:00:00', 0),
(10, '', '2012-06-28 12:00:00', 'Gặp thầy hướng dẫn', '2012-06-28 08:00:00', 0);

-- --------------------------------------------------------

--
-- Table structure for table `for_test_only`
--

CREATE TABLE IF NOT EXISTS `for_test_only` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `iteration`
--

CREATE TABLE IF NOT EXISTS `iteration` (
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8904EEDD73F39ABB` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `iteration`
--

INSERT INTO `iteration` (`id`) VALUES
(2),
(3);

-- --------------------------------------------------------

--
-- Table structure for table `member_information`
--

CREATE TABLE IF NOT EXISTS `member_information` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `account` bigint(20) NOT NULL,
  `member_role` bigint(20) NOT NULL,
  `project` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK99584707275EF875` (`project`),
  KEY `FK995847079239B408` (`member_role`),
  KEY `FK99584707BFE5769D` (`account`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=4 ;

--
-- Dumping data for table `member_information`
--

INSERT INTO `member_information` (`id`, `deleted`, `version`, `account`, `member_role`, `project`) VALUES
(1, '0', 0, 1, 1, 1),
(2, '0', 0, 2, 1, 1),
(3, '0', 0, 3, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `member_role`
--

CREATE TABLE IF NOT EXISTS `member_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `ref_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=3 ;

--
-- Dumping data for table `member_role`
--

INSERT INTO `member_role` (`id`, `name`, `ref_name`, `version`) VALUES
(1, 'project manager', 'projectmanager', 1),
(2, 'project member', 'projectmember', 1);

-- --------------------------------------------------------

--
-- Table structure for table `member_role_permissions`
--

CREATE TABLE IF NOT EXISTS `member_role_permissions` (
  `member_roles` bigint(20) NOT NULL,
  `permissions` bigint(20) NOT NULL,
  KEY `FK3A07F1403D0515E5` (`member_roles`),
  KEY `FK3A07F140B4658D30` (`permissions`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `member_role_permissions`
--

INSERT INTO `member_role_permissions` (`member_roles`, `permissions`) VALUES
(1, 7),
(1, 8),
(1, 6),
(1, 9),
(1, 5);

-- --------------------------------------------------------

--
-- Table structure for table `permission`
--

CREATE TABLE IF NOT EXISTS `permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ref_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=10 ;

--
-- Dumping data for table `permission`
--

INSERT INTO `permission` (`id`, `ref_name`, `version`) VALUES
(1, 'project:*', 0),
(2, 'studyclass:*', 0),
(3, 'workitem:read', 0),
(4, 'workitem:list', 0),
(5, 'project:read', 0),
(6, 'project:list', 0),
(7, 'project:update', 0),
(8, 'project:create', 0),
(9, 'workitem:*', 0);

-- --------------------------------------------------------

--
-- Table structure for table `priority`
--

CREATE TABLE IF NOT EXISTS `priority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=6 ;

--
-- Dumping data for table `priority`
--

INSERT INTO `priority` (`id`, `name`, `version`) VALUES
(1, 'High', 0),
(2, 'Medium', 0),
(3, 'Low', 0),
(4, 'Urgent', 0),
(5, 'Immediate', 0);

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

CREATE TABLE IF NOT EXISTS `project` (
  `description` longtext COLLATE utf8_unicode_ci,
  `status` int(11) NOT NULL,
  `id` bigint(20) NOT NULL,
  `calendar` bigint(20) NOT NULL,
  `project_process` bigint(20) NOT NULL,
  `study_class` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKED904B1939CEBA77` (`id`),
  KEY `FKED904B1973F39ABB` (`id`),
  KEY `FKED904B19F25770BC` (`project_process`),
  KEY `FKED904B19264BDC99` (`calendar`),
  KEY `FKED904B195B6938E` (`study_class`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `project`
--

INSERT INTO `project` (`description`, `status`, `id`, `calendar`, `project_process`, `study_class`) VALUES
('Hệ thống hỗ trợ việc làm đồ án của sinh viên', 0, 1, 4, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `project_process`
--

CREATE TABLE IF NOT EXISTS `project_process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `process_template_file` longblob NOT NULL,
  `unique_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_name` (`unique_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

--
-- Dumping data for table `project_process`
--

INSERT INTO `project_process` (`id`, `description`, `is_deleted`, `name`, `process_template_file`, `unique_name`, `version`) VALUES
(1, 'Des', '0', 'Scrum', 0x3c3f786d6c2076657273696f6e3d22312e30223f3e3c7850726f6a65637450726f63657373206964656e746974793d22736372756d22206e616d653d22536372756d2220786d6c6e733d22687474703a2f2f7777772e77337363686f6f6c732e636f6d2220786d6c6e733a7873693d22687474703a2f2f7777772e77332e6f72672f323030312f584d4c536368656d612d696e7374616e636522207873693a736368656d614c6f636174696f6e3d22687474703a2f2f7777772e77337363686f6f6c732e636f6d2050726f6365737354656d706c617465536368656d612e787364223e3c78576f726b4974656d733e3c78576f726b4974656d206e616d653d225461736b22207265664e616d653d227461736b223e3c78507265446566696e65644669656c64733e3c2f78507265446566696e65644669656c64733e3c78416464696f6e616c4669656c64733e3c2f78416464696f6e616c4669656c64733e3c2f78576f726b4974656d3e3c78576f726b4974656d206e616d653d22557365722073746f727922207265664e616d653d227573657253746f7279223e3c78507265446566696e65644669656c64733e3c2f78507265446566696e65644669656c64733e3c78416464696f6e616c4669656c64733e3c784669656c64207265664e616d653d2273746f7279506f696e7422206e616d653d2253746f727920706f696e742220747970653d22646967697473223e3c2f784669656c643e3c784669656c64207265664e616d653d22646966666963756c747922206e616d653d22446966666963756c74792220747970653d22646967697473223e3c2f784669656c643e3c2f78416464696f6e616c4669656c64733e3c2f78576f726b4974656d3e3c78576f726b4974656d206e616d653d2242756722207265664e616d653d22627567223e3c78507265446566696e65644669656c64733e3c2f78507265446566696e65644669656c64733e3c78416464696f6e616c4669656c64733e3c784669656c64206e616d653d2253657665726974792220747970653d22737472696e6722207265664e616d653d227365766572697479223e3c7843686f696365733e3c7843686f6963653e437269746963616c3c2f7843686f6963653e3c7843686f6963653e4e6f726d616c3c2f7843686f6963653e3c7843686f6963653e4d696e6f723c2f7843686f6963653e3c2f7843686f696365733e3c2f784669656c643e3c784669656c64206e616d653d22456e7669726f6e6d656e742220747970653d22737472696e6722207265664e616d653d22656e7669726f6e6d656e74223e3c7844656661756c7456616c75653e4d7920656e7669726f6e6d656e743c2f7844656661756c7456616c75653e3c2f784669656c643e3c784669656c64206e616d653d22747970652220747970653d226e756d62657222207265664e616d653d22747970652220726571756965643d2274727565223e3c2f784669656c643e3c2f78416464696f6e616c4669656c64733e3c2f78576f726b4974656d3e3c2f78576f726b4974656d733e3c784465736372697074696f6e3e4465733c2f784465736372697074696f6e3e3c2f7850726f6a65637450726f636573733e, 'scrum', 0);

-- --------------------------------------------------------

--
-- Table structure for table `study_class`
--

CREATE TABLE IF NOT EXISTS `study_class` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `work_item`
--

CREATE TABLE IF NOT EXISTS `work_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `additional_fields` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_last_edit` datetime DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `due_date` datetime DEFAULT NULL,
  `title` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `version` int(11) DEFAULT NULL,
  `asignee` bigint(20) DEFAULT NULL,
  `author` bigint(20) NOT NULL,
  `priority` bigint(20) NOT NULL,
  `status` bigint(20) NOT NULL,
  `user_last_edit` bigint(20) DEFAULT NULL,
  `work_item_container` bigint(20) NOT NULL,
  `work_item_type` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK40FB2F81B09ED965` (`priority`),
  KEY `FK40FB2F818A0056E3` (`work_item_container`),
  KEY `FK40FB2F81BF1677F3` (`work_item_type`),
  KEY `FK40FB2F8136F49A40` (`author`),
  KEY `FK40FB2F813F8B0B25` (`status`),
  KEY `FK40FB2F8160291F54` (`user_last_edit`),
  KEY `FK40FB2F8160395B73` (`asignee`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=14 ;

--
-- Dumping data for table `work_item`
--

INSERT INTO `work_item` (`id`, `additional_fields`, `date_created`, `date_last_edit`, `description`, `due_date`, `title`, `version`, `asignee`, `author`, `priority`, `status`, `user_last_edit`, `work_item_container`, `work_item_type`) VALUES
(1, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 09:47:45', '2012-06-24 10:42:39', 'Thiết kế cơ sở dữ liêu cho hệ thống quản lý tương tác.\r\nXem danh sách chức năng trong đặc tả yêu cầu.', '2012-06-26 00:00:00', 'Viết báo cáo thiết kế cơ sở dữ liệu', 6, 2, 1, 1, 3, 3, 1, 1),
(2, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:16:01', '2012-06-24 11:06:02', '', '2012-06-05 00:00:00', 'Viết báo cáo về kiến trúc', 3, 1, 3, 1, 1, 1, 1, 1),
(3, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:16:41', '2012-06-24 10:26:05', '', NULL, 'Viết đặc tả yêu cầu', 1, NULL, 3, 1, 4, 3, 1, 1),
(4, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:18:08', '2012-06-24 11:06:09', '', '2012-06-17 00:00:00', 'Vẽ sơ đồ use case', 2, 1, 3, 1, 1, 1, 1, 1),
(5, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 10:18:59', '2012-06-24 11:06:49', '', '2012-06-26 00:00:00', 'Không thể cập nhật thông tin lớp học', 4, 1, 3, 1, 2, 1, 1, 2),
(6, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 10:19:25', '2012-06-24 11:06:55', '', '2012-06-29 00:00:00', 'Chức năng tạo dự án', 6, 1, 3, 1, 2, 1, 1, 2),
(7, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 10:20:21', '2012-06-24 11:08:42', '', '2012-06-25 00:00:00', 'Chức năng tạo work item', 6, 1, 3, 1, 1, 1, 1, 2),
(8, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 10:22:31', '2012-06-24 11:08:33', '', '2012-06-25 00:00:00', 'Trang hiển thị work item layout sai', 4, 1, 3, 1, 1, 1, 1, 3),
(9, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 10:24:46', '2012-06-24 11:06:27', '', '2012-06-18 00:00:00', 'Không thể tạo project', 7, 1, 3, 1, 2, 1, 1, 3),
(10, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 10:24:46', '2012-06-24 10:25:48', '', NULL, 'Không thể tạo account', 2, NULL, 3, 1, 5, 3, 1, 3),
(11, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:28:56', '2012-06-24 11:08:09', '', '2012-06-18 00:00:00', 'Thiết kế sơ đồ lớp mực phân tích', 2, 1, 3, 1, 1, 1, 1, 1),
(12, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:39:31', '2012-06-24 10:39:31', '', NULL, 'In báo cáo', 0, NULL, 1, 1, 1, 1, 1, 1),
(13, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:47:50', '2012-06-24 10:47:50', '', NULL, 'Ghi đĩa CD', 0, NULL, 2, 1, 1, 2, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `work_item_container`
--

CREATE TABLE IF NOT EXISTS `work_item_container` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `version` int(11) DEFAULT NULL,
  `parent_container` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK160CC943331590CC` (`parent_container`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=4 ;

--
-- Dumping data for table `work_item_container`
--

INSERT INTO `work_item_container` (`id`, `name`, `version`, `parent_container`) VALUES
(1, 'Hệ thống quản lý tương tác', 3, NULL),
(2, 'Iteration 1', 0, 1),
(3, 'Iteration 2', 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `work_item_history`
--

CREATE TABLE IF NOT EXISTS `work_item_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `additional_fields` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_last_edit` datetime DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `due_date` datetime DEFAULT NULL,
  `title` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `type` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `asignee` bigint(20) DEFAULT NULL,
  `author` bigint(20) NOT NULL,
  `changed_by` bigint(20) DEFAULT NULL,
  `priority` bigint(20) NOT NULL,
  `status` bigint(20) NOT NULL,
  `work_item` bigint(20) DEFAULT NULL,
  `work_item_container` bigint(20) NOT NULL,
  `work_item_type` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK20A7A9D6B09ED965` (`priority`),
  KEY `FK20A7A9D6BBC1A77` (`changed_by`),
  KEY `FK20A7A9D68A0056E3` (`work_item_container`),
  KEY `FK20A7A9D6BF1677F3` (`work_item_type`),
  KEY `FK20A7A9D636F49A40` (`author`),
  KEY `FK20A7A9D67E97DB62` (`work_item`),
  KEY `FK20A7A9D63F8B0B25` (`status`),
  KEY `FK20A7A9D660395B73` (`asignee`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=57 ;

--
-- Dumping data for table `work_item_history`
--

INSERT INTO `work_item_history` (`id`, `additional_fields`, `date_created`, `date_last_edit`, `description`, `due_date`, `title`, `type`, `version`, `asignee`, `author`, `changed_by`, `priority`, `status`, `work_item`, `work_item_container`, `work_item_type`) VALUES
(1, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 09:47:45', '2012-06-24 09:47:45', 'Thiết kế cơ sở dữ liêu cho hệ thống quản lý tương tác.\r\nXem danh sách chức năng trong đặc tả yêu cầu.', NULL, 'Thiết kế cơ sở dữ liệu', 0, 1, NULL, 1, 1, 1, 1, 1, 1, 1),
(2, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 09:53:55', '2012-06-24 09:53:55', 'Thiết kế cơ sở dữ liêu cho hệ thống quản lý tương tác.\r\nXem danh sách chức năng trong đặc tả yêu cầu.', NULL, 'Thiết kế cơ sở dữ liệu', 1, 0, 2, 1, 1, 1, 1, 1, 1, 1),
(3, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 09:55:05', '2012-06-24 09:53:55', 'Thiết kế cơ sở dữ liêu cho hệ thống quản lý tương tác.\r\nXem danh sách chức năng trong đặc tả yêu cầu.', NULL, 'Thiết kế cơ sở dữ liệu', 1, 0, 2, 1, 1, 1, 1, 1, 1, 1),
(4, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:16:01', '2012-06-24 10:16:01', '', '2012-06-05 00:00:00', 'Thiết kế cơ sở kiến trúc', 0, 1, NULL, 3, 3, 1, 1, 2, 1, 1),
(5, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:16:14', '2012-06-24 10:16:14', '', '2012-06-05 00:00:00', 'Thiết kế kiến trúc', 1, 0, NULL, 3, 3, 1, 1, 2, 1, 1),
(6, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:16:41', '2012-06-24 10:16:41', '', NULL, 'Viết đặc tả yêu cầu', 0, 1, NULL, 3, 3, 1, 1, 3, 1, 1),
(7, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:18:08', '2012-06-24 10:18:08', '', NULL, 'Vẽ sơ đồ use case', 0, 1, NULL, 3, 3, 1, 1, 4, 1, 1),
(8, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 10:18:59', '2012-06-24 10:18:59', '', NULL, 'Chức năng đăng nhập', 0, 1, NULL, 3, 3, 1, 1, 5, 1, 2),
(9, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 10:19:25', '2012-06-24 10:19:25', '', NULL, 'Chức năng tạo dự án', 0, 1, NULL, 3, 3, 1, 1, 6, 1, 2),
(10, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 10:20:21', '2012-06-24 10:20:21', '', NULL, 'Chức năng tạo work item', 0, 1, NULL, 3, 3, 1, 1, 7, 1, 2),
(11, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 10:22:31', '2012-06-24 10:22:31', '', NULL, 'Trang hiển thị work item layout sai', 0, 1, NULL, 3, 3, 1, 1, 8, 1, 3),
(12, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 10:24:46', '2012-06-24 10:24:46', '', NULL, 'Không thể tạo accout', 0, 1, NULL, 3, 3, 1, 1, 9, 1, 3),
(13, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 10:24:46', '2012-06-24 10:24:46', '', NULL, 'Không thể tạo accout', 0, 1, NULL, 3, 3, 1, 1, 10, 1, 3),
(14, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 10:25:12', '2012-06-24 10:25:12', '', NULL, 'Không thể tạo project', 1, 0, NULL, 3, 3, 1, 1, 9, 1, 3),
(15, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 10:25:20', '2012-06-24 10:25:20', '', NULL, 'Không thể tạo account', 1, 0, NULL, 3, 3, 1, 1, 10, 1, 3),
(16, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 10:25:48', '2012-06-24 10:25:48', '', NULL, 'Không thể tạo account', 1, 0, NULL, 3, 3, 1, 5, 10, 1, 3),
(17, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:26:05', '2012-06-24 10:26:05', '', NULL, 'Viết đặc tả yêu cầu', 1, 0, NULL, 3, 3, 1, 4, 3, 1, 1),
(18, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:26:26', '2012-06-24 10:26:26', 'Thiết kế cơ sở dữ liêu cho hệ thống quản lý tương tác.\r\nXem danh sách chức năng trong đặc tả yêu cầu.', '2012-06-26 00:00:00', 'Thiết kế cơ sở dữ liệu', 1, 0, 2, 1, 3, 1, 1, 1, 1, 1),
(19, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 10:26:44', '2012-06-24 10:26:44', '', '2012-06-27 00:00:00', 'Chức năng tạo work item', 1, 0, NULL, 3, 3, 1, 1, 7, 1, 2),
(20, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 10:26:59', '2012-06-24 10:26:59', '', '2012-06-29 00:00:00', 'Chức năng tạo dự án', 1, 0, NULL, 3, 3, 1, 1, 6, 1, 2),
(21, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 10:27:21', '2012-06-24 10:27:21', '', '2012-06-25 00:00:00', 'Chức năng đăng nhập', 1, 0, NULL, 3, 3, 1, 1, 5, 1, 2),
(22, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:27:50', '2012-06-24 10:27:50', '', '2012-06-17 00:00:00', 'Vẽ sơ đồ use case', 1, 0, NULL, 3, 3, 1, 1, 4, 1, 1),
(23, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:28:56', '2012-06-24 10:28:56', '', '2012-06-22 00:00:00', 'Thiết kế sơ đồ lớp mực phân tích', 0, 1, NULL, 3, 3, 1, 1, 11, 1, 1),
(24, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:30:57', '2012-06-24 10:30:57', '', '2012-06-05 00:00:00', 'Viết báo cáo về kiến trúc', 1, 0, NULL, 3, 3, 1, 1, 2, 1, 1),
(39, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 10:43:51', '2012-06-24 10:43:51', '', '2012-06-26 00:00:00', 'Không thể cập nhật thông tin lớp học', 1, 0, NULL, 3, 3, 1, 2, 5, 1, 2),
(40, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 10:44:09', '2012-06-24 10:44:09', '', '2012-06-27 00:00:00', 'Trang hiển thị work item layout sai', 1, 0, NULL, 3, 3, 1, 1, 8, 1, 3),
(41, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 10:44:20', '2012-06-24 10:44:20', '', '2012-06-30 00:00:00', 'Trang hiển thị work item layout sai', 1, 0, NULL, 3, 3, 1, 1, 8, 1, 3),
(42, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 10:46:58', '2012-06-24 10:46:58', '', '2012-06-28 00:00:00', 'Chức năng tạo dự án', 1, 0, NULL, 3, 3, 1, 2, 6, 1, 2),
(43, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 10:47:26', '2012-06-24 10:47:26', '', '2012-06-29 00:00:00', 'Chức năng tạo dự án', 1, 0, NULL, 3, 2, 1, 2, 6, 1, 2),
(44, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 10:47:50', '2012-06-24 10:47:50', '', NULL, 'Ghi đĩa CD', 0, 1, NULL, 2, 2, 1, 1, 13, 1, 1),
(45, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 10:48:22', '2012-06-24 10:48:22', '', '2012-06-18 00:00:00', 'Không thể tạo project', 1, 0, NULL, 3, 1, 1, 2, 9, 1, 3),
(46, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 11:06:02', '2012-06-24 11:06:02', '', '2012-06-05 00:00:00', 'Viết báo cáo về kiến trúc', 1, 0, 1, 3, 1, 1, 1, 2, 1, 1),
(47, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 11:06:09', '2012-06-24 11:06:09', '', '2012-06-17 00:00:00', 'Vẽ sơ đồ use case', 1, 0, 1, 3, 1, 1, 1, 4, 1, 1),
(48, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 11:06:21', '2012-06-24 11:06:21', '', '2012-06-22 00:00:00', 'Thiết kế sơ đồ lớp mực phân tích', 1, 0, 1, 3, 1, 1, 1, 11, 1, 1),
(49, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 11:06:27', '2012-06-24 11:06:27', '', '2012-06-18 00:00:00', 'Không thể tạo project', 1, 0, 1, 3, 1, 1, 2, 9, 1, 3),
(50, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 11:06:36', '2012-06-24 11:06:36', '', '2012-06-30 00:00:00', 'Chức năng tạo work item', 1, 0, 1, 3, 1, 1, 1, 7, 1, 2),
(51, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 11:06:43', '2012-06-24 11:06:43', '', '2012-06-30 00:00:00', 'Trang hiển thị work item layout sai', 1, 0, 1, 3, 1, 1, 1, 8, 1, 3),
(52, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 11:06:49', '2012-06-24 11:06:49', '', '2012-06-26 00:00:00', 'Không thể cập nhật thông tin lớp học', 1, 0, 1, 3, 1, 1, 2, 5, 1, 2),
(53, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 11:06:55', '2012-06-24 11:06:55', '', '2012-06-29 00:00:00', 'Chức năng tạo dự án', 1, 0, 1, 3, 1, 1, 2, 6, 1, 2),
(54, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', '2012-06-24 11:08:09', '2012-06-24 11:08:09', '', '2012-06-18 00:00:00', 'Thiết kế sơ đồ lớp mực phân tích', 1, 0, 1, 3, 1, 1, 1, 11, 1, 1),
(55, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity">Critical</xField><xField ref="environment">My environment</xField><xField ref="type">1</xField></xAdditionalFields>', '2012-06-24 11:08:33', '2012-06-24 11:08:33', '', '2012-06-25 00:00:00', 'Trang hiển thị work item layout sai', 1, 0, 1, 3, 1, 1, 1, 8, 1, 3),
(56, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"></xField><xField ref="difficulty"></xField></xAdditionalFields>', '2012-06-24 11:08:42', '2012-06-24 11:08:42', '', '2012-06-25 00:00:00', 'Chức năng tạo work item', 1, 0, 1, 3, 1, 1, 1, 7, 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `work_item_status`
--

CREATE TABLE IF NOT EXISTS `work_item_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `closed` bit(1) NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=6 ;

--
-- Dumping data for table `work_item_status`
--

INSERT INTO `work_item_status` (`id`, `closed`, `name`, `version`) VALUES
(1, '0', 'New', 0),
(2, '0', 'In Process', 0),
(3, '1', 'Resolved', 0),
(4, '1', 'Closed', 0),
(5, '1', 'Rejected', 0);

-- --------------------------------------------------------

--
-- Table structure for table `work_item_subcribers`
--

CREATE TABLE IF NOT EXISTS `work_item_subcribers` (
  `work_item` bigint(20) NOT NULL,
  `subcribers` bigint(20) NOT NULL,
  KEY `FK24E2EFDCCC64D13` (`subcribers`),
  KEY `FK24E2EFDC7E97DB62` (`work_item`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `work_item_subcribers`
--

INSERT INTO `work_item_subcribers` (`work_item`, `subcribers`) VALUES
(1, 3);

-- --------------------------------------------------------

--
-- Table structure for table `work_item_type`
--

CREATE TABLE IF NOT EXISTS `work_item_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `additional_fields_define` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `ref_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `version` int(11) DEFAULT NULL,
  `project_process` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK393CA138F25770BC` (`project_process`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=4 ;

--
-- Dumping data for table `work_item_type`
--

INSERT INTO `work_item_type` (`id`, `additional_fields_define`, `name`, `ref_name`, `version`, `project_process`) VALUES
(1, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"/>', 'Task', 'task', 0, 1),
(2, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="storyPoint"/><xField ref="difficulty"/></xAdditionalFields>', 'User story', 'userStory', 0, 1),
(3, '<?xml version="1.0" encoding="UTF-8"?>\r\n<xAdditionalFields xmlns="http://www.w3schools.com"><xField ref="severity"/><xField ref="environment"/><xField ref="type"/></xAdditionalFields>', 'Bug', 'bug', 0, 1);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `FKB9D38A2D264BDC99` FOREIGN KEY (`calendar`) REFERENCES `calendar` (`id`),
  ADD CONSTRAINT `FKB9D38A2DE482C19F` FOREIGN KEY (`role`) REFERENCES `application_role` (`id`);

--
-- Constraints for table `application_role_permissions`
--
ALTER TABLE `application_role_permissions`
  ADD CONSTRAINT `FK20C0794AB4658D30` FOREIGN KEY (`permissions`) REFERENCES `permission` (`id`),
  ADD CONSTRAINT `FK20C0794AE458472E` FOREIGN KEY (`application_role`) REFERENCES `application_role` (`id`);

--
-- Constraints for table `attachment`
--
ALTER TABLE `attachment`
  ADD CONSTRAINT `FK8AF759237E97DB62` FOREIGN KEY (`work_item`) REFERENCES `work_item` (`id`);

--
-- Constraints for table `calendar_events`
--
ALTER TABLE `calendar_events`
  ADD CONSTRAINT `FK28E1613ADEB66AB6` FOREIGN KEY (`events`) REFERENCES `event` (`id`),
  ADD CONSTRAINT `FK28E1613AE76D4E50` FOREIGN KEY (`calendars`) REFERENCES `calendar` (`id`);

--
-- Constraints for table `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `FK38A5EE5FB98CB735` FOREIGN KEY (`project_member`) REFERENCES `member_information` (`id`),
  ADD CONSTRAINT `FK38A5EE5F7E97DB62` FOREIGN KEY (`work_item`) REFERENCES `work_item` (`id`);

--
-- Constraints for table `iteration`
--
ALTER TABLE `iteration`
  ADD CONSTRAINT `FK8904EEDD73F39ABB` FOREIGN KEY (`id`) REFERENCES `work_item_container` (`id`);

--
-- Constraints for table `member_information`
--
ALTER TABLE `member_information`
  ADD CONSTRAINT `FK99584707BFE5769D` FOREIGN KEY (`account`) REFERENCES `account` (`id`),
  ADD CONSTRAINT `FK99584707275EF875` FOREIGN KEY (`project`) REFERENCES `project` (`id`),
  ADD CONSTRAINT `FK995847079239B408` FOREIGN KEY (`member_role`) REFERENCES `member_role` (`id`);

--
-- Constraints for table `member_role_permissions`
--
ALTER TABLE `member_role_permissions`
  ADD CONSTRAINT `FK3A07F140B4658D30` FOREIGN KEY (`permissions`) REFERENCES `permission` (`id`),
  ADD CONSTRAINT `FK3A07F1403D0515E5` FOREIGN KEY (`member_roles`) REFERENCES `member_role` (`id`);

--
-- Constraints for table `project`
--
ALTER TABLE `project`
  ADD CONSTRAINT `FKED904B195B6938E` FOREIGN KEY (`study_class`) REFERENCES `study_class` (`id`),
  ADD CONSTRAINT `FKED904B19264BDC99` FOREIGN KEY (`calendar`) REFERENCES `calendar` (`id`),
  ADD CONSTRAINT `FKED904B1939CEBA77` FOREIGN KEY (`id`) REFERENCES `project` (`id`),
  ADD CONSTRAINT `FKED904B1973F39ABB` FOREIGN KEY (`id`) REFERENCES `work_item_container` (`id`),
  ADD CONSTRAINT `FKED904B19F25770BC` FOREIGN KEY (`project_process`) REFERENCES `project_process` (`id`);

--
-- Constraints for table `work_item`
--
ALTER TABLE `work_item`
  ADD CONSTRAINT `FK40FB2F8160395B73` FOREIGN KEY (`asignee`) REFERENCES `member_information` (`id`),
  ADD CONSTRAINT `FK40FB2F8136F49A40` FOREIGN KEY (`author`) REFERENCES `member_information` (`id`),
  ADD CONSTRAINT `FK40FB2F813F8B0B25` FOREIGN KEY (`status`) REFERENCES `work_item_status` (`id`),
  ADD CONSTRAINT `FK40FB2F8160291F54` FOREIGN KEY (`user_last_edit`) REFERENCES `member_information` (`id`),
  ADD CONSTRAINT `FK40FB2F818A0056E3` FOREIGN KEY (`work_item_container`) REFERENCES `work_item_container` (`id`),
  ADD CONSTRAINT `FK40FB2F81B09ED965` FOREIGN KEY (`priority`) REFERENCES `priority` (`id`),
  ADD CONSTRAINT `FK40FB2F81BF1677F3` FOREIGN KEY (`work_item_type`) REFERENCES `work_item_type` (`id`);

--
-- Constraints for table `work_item_container`
--
ALTER TABLE `work_item_container`
  ADD CONSTRAINT `FK160CC943331590CC` FOREIGN KEY (`parent_container`) REFERENCES `work_item_container` (`id`);

--
-- Constraints for table `work_item_history`
--
ALTER TABLE `work_item_history`
  ADD CONSTRAINT `FK20A7A9D660395B73` FOREIGN KEY (`asignee`) REFERENCES `member_information` (`id`),
  ADD CONSTRAINT `FK20A7A9D636F49A40` FOREIGN KEY (`author`) REFERENCES `member_information` (`id`),
  ADD CONSTRAINT `FK20A7A9D63F8B0B25` FOREIGN KEY (`status`) REFERENCES `work_item_status` (`id`),
  ADD CONSTRAINT `FK20A7A9D67E97DB62` FOREIGN KEY (`work_item`) REFERENCES `work_item` (`id`),
  ADD CONSTRAINT `FK20A7A9D68A0056E3` FOREIGN KEY (`work_item_container`) REFERENCES `work_item_container` (`id`),
  ADD CONSTRAINT `FK20A7A9D6B09ED965` FOREIGN KEY (`priority`) REFERENCES `priority` (`id`),
  ADD CONSTRAINT `FK20A7A9D6BBC1A77` FOREIGN KEY (`changed_by`) REFERENCES `member_information` (`id`),
  ADD CONSTRAINT `FK20A7A9D6BF1677F3` FOREIGN KEY (`work_item_type`) REFERENCES `work_item_type` (`id`);

--
-- Constraints for table `work_item_subcribers`
--
ALTER TABLE `work_item_subcribers`
  ADD CONSTRAINT `FK24E2EFDC7E97DB62` FOREIGN KEY (`work_item`) REFERENCES `work_item` (`id`),
  ADD CONSTRAINT `FK24E2EFDCCC64D13` FOREIGN KEY (`subcribers`) REFERENCES `member_information` (`id`);

--
-- Constraints for table `work_item_type`
--
ALTER TABLE `work_item_type`
  ADD CONSTRAINT `FK393CA138F25770BC` FOREIGN KEY (`project_process`) REFERENCES `project_process` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
