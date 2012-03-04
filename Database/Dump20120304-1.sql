CREATE DATABASE  IF NOT EXISTS `cde` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `cde`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: cde
-- ------------------------------------------------------
-- Server version	5.5.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `StatusID` int(11) NOT NULL AUTO_INCREMENT,
  `WorkItemTypeID` int(11) NOT NULL,
  `Name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`StatusID`),
  KEY `fk_Status_WorkItemType1` (`WorkItemTypeID`),
  CONSTRAINT `fk_Status_WorkItemType1` FOREIGN KEY (`WorkItemTypeID`) REFERENCES `workitemtype` (`WorkItemTypeID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process`
--

DROP TABLE IF EXISTS `process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `process` (
  `ProcessID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`ProcessID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process`
--

LOCK TABLES `process` WRITE;
/*!40000 ALTER TABLE `process` DISABLE KEYS */;
/*!40000 ALTER TABLE `process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workitem`
--

DROP TABLE IF EXISTS `workitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workitem` (
  `WorkItemID` int(11) NOT NULL AUTO_INCREMENT,
  `Title` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `Description` text COLLATE utf8_unicode_ci,
  `DateCreated` datetime NOT NULL,
  `Priority` int(11) DEFAULT NULL,
  `ChangeSet` int(11) DEFAULT NULL,
  `AdditionalFields` text COLLATE utf8_unicode_ci,
  `DueDate` datetime DEFAULT NULL,
  `LastModifyDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `WorkItemTypeID` int(11) NOT NULL,
  `StatusID` int(11) NOT NULL,
  `AuthorID` int(11) NOT NULL,
  `AsigneeID` int(11) NOT NULL,
  `WorkItemContainerID` int(11) NOT NULL,
  PRIMARY KEY (`WorkItemID`),
  KEY `fk_WorkItem_WorkItemType1` (`WorkItemTypeID`),
  KEY `fk_WorkItem_Status1` (`StatusID`),
  KEY `fk_WorkItem_Account1` (`AuthorID`),
  KEY `fk_WorkItem_Account2` (`AsigneeID`),
  KEY `fk_WorkItem_WorkItemContainer1` (`WorkItemContainerID`),
  CONSTRAINT `fk_WorkItem_WorkItemType1` FOREIGN KEY (`WorkItemTypeID`) REFERENCES `workitemtype` (`WorkItemTypeID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_WorkItem_Status1` FOREIGN KEY (`StatusID`) REFERENCES `status` (`StatusID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_WorkItem_Account1` FOREIGN KEY (`AuthorID`) REFERENCES `account` (`AccountID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_WorkItem_Account2` FOREIGN KEY (`AsigneeID`) REFERENCES `account` (`AccountID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_WorkItem_WorkItemContainer1` FOREIGN KEY (`WorkItemContainerID`) REFERENCES `workitemcontainer` (`WorkItemContainerID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workitem`
--

LOCK TABLES `workitem` WRITE;
/*!40000 ALTER TABLE `workitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `workitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workitemcomment`
--

DROP TABLE IF EXISTS `workitemcomment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workitemcomment` (
  `WorkItemCommentID` int(11) NOT NULL,
  `LastModifyDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Content` text COLLATE utf8_unicode_ci NOT NULL,
  `WorkItemID` int(11) NOT NULL,
  `AuthorID` int(11) NOT NULL,
  PRIMARY KEY (`WorkItemCommentID`),
  KEY `fk_WorkItemComment_WorkItem1` (`WorkItemID`),
  KEY `fk_WorkItemComment_Account1` (`AuthorID`),
  CONSTRAINT `fk_WorkItemComment_WorkItem1` FOREIGN KEY (`WorkItemID`) REFERENCES `workitem` (`WorkItemID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_WorkItemComment_Account1` FOREIGN KEY (`AuthorID`) REFERENCES `account` (`AccountID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workitemcomment`
--

LOCK TABLES `workitemcomment` WRITE;
/*!40000 ALTER TABLE `workitemcomment` DISABLE KEYS */;
/*!40000 ALTER TABLE `workitemcomment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dependencytype`
--

DROP TABLE IF EXISTS `dependencytype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dependencytype` (
  `DependencyTypeID` int(11) NOT NULL AUTO_INCREMENT,
  `ProcessID` int(11) NOT NULL,
  `Name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `WorkItemTypeID1` int(11) NOT NULL,
  `WorkItemTypeID2` int(11) NOT NULL,
  PRIMARY KEY (`DependencyTypeID`),
  KEY `fk_DependencyType_Process1` (`ProcessID`),
  KEY `fk_DependencyType_WorkItemType1` (`WorkItemTypeID1`),
  KEY `fk_DependencyType_WorkItemType2` (`WorkItemTypeID2`),
  CONSTRAINT `fk_DependencyType_Process1` FOREIGN KEY (`ProcessID`) REFERENCES `process` (`ProcessID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_DependencyType_WorkItemType1` FOREIGN KEY (`WorkItemTypeID1`) REFERENCES `workitemtype` (`WorkItemTypeID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_DependencyType_WorkItemType2` FOREIGN KEY (`WorkItemTypeID2`) REFERENCES `workitemtype` (`WorkItemTypeID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dependencytype`
--

LOCK TABLES `dependencytype` WRITE;
/*!40000 ALTER TABLE `dependencytype` DISABLE KEYS */;
/*!40000 ALTER TABLE `dependencytype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query`
--

DROP TABLE IF EXISTS `query`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `query` (
  `QueryID` int(11) NOT NULL AUTO_INCREMENT,
  `WorkItemContainerID` int(11) NOT NULL,
  `Name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `Condition` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`QueryID`),
  KEY `fk_table1_WorkItemContainer1` (`WorkItemContainerID`),
  CONSTRAINT `fk_table1_WorkItemContainer1` FOREIGN KEY (`WorkItemContainerID`) REFERENCES `workitemcontainer` (`WorkItemContainerID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `query`
--

LOCK TABLES `query` WRITE;
/*!40000 ALTER TABLE `query` DISABLE KEYS */;
/*!40000 ALTER TABLE `query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `calendar`
--

DROP TABLE IF EXISTS `calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calendar` (
  `CalendarID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`CalendarID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calendar`
--

LOCK TABLES `calendar` WRITE;
/*!40000 ALTER TABLE `calendar` DISABLE KEYS */;
/*!40000 ALTER TABLE `calendar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `class`
--

DROP TABLE IF EXISTS `class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `class` (
  `ClassID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `Description` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`ClassID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `class`
--

LOCK TABLES `class` WRITE;
/*!40000 ALTER TABLE `class` DISABLE KEYS */;
/*!40000 ALTER TABLE `class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workitemcontainer`
--

DROP TABLE IF EXISTS `workitemcontainer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workitemcontainer` (
  `WorkItemContainerID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `ParentID` int(11) NOT NULL,
  PRIMARY KEY (`WorkItemContainerID`),
  KEY `fk_WorkItemContainer_WorkItemContainer1` (`ParentID`),
  CONSTRAINT `fk_WorkItemContainer_WorkItemContainer1` FOREIGN KEY (`ParentID`) REFERENCES `workitemcontainer` (`WorkItemContainerID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workitemcontainer`
--

LOCK TABLES `workitemcontainer` WRITE;
/*!40000 ALTER TABLE `workitemcontainer` DISABLE KEYS */;
/*!40000 ALTER TABLE `workitemcontainer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `AccountID` int(11) NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `LastName` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `Password` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `Email` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `IsAdmin` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `CalendarID` int(11) NOT NULL,
  PRIMARY KEY (`AccountID`),
  KEY `fk_Account_Calendar1` (`CalendarID`),
  CONSTRAINT `fk_Account_Calendar1` FOREIGN KEY (`CalendarID`) REFERENCES `calendar` (`CalendarID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wikipage`
--

DROP TABLE IF EXISTS `wikipage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wikipage` (
  `WikiPageID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Summary` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Content` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ProjectID` int(11) NOT NULL,
  `AuthorID` int(11) NOT NULL,
  PRIMARY KEY (`WikiPageID`),
  KEY `fk_WikiPage_Project1` (`ProjectID`),
  KEY `fk_WikiPage_Account1` (`AuthorID`),
  CONSTRAINT `fk_WikiPage_Project1` FOREIGN KEY (`ProjectID`) REFERENCES `project` (`ProjectID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_WikiPage_Account1` FOREIGN KEY (`AuthorID`) REFERENCES `account` (`AccountID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wikipage`
--

LOCK TABLES `wikipage` WRITE;
/*!40000 ALTER TABLE `wikipage` DISABLE KEYS */;
/*!40000 ALTER TABLE `wikipage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workitemtype`
--

DROP TABLE IF EXISTS `workitemtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workitemtype` (
  `WorkItemTypeID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `AdditionalFieldsDefine` text COLLATE utf8_unicode_ci NOT NULL,
  `Process_ProcessID` int(11) NOT NULL,
  PRIMARY KEY (`WorkItemTypeID`),
  KEY `fk_WorkItemType_Process1` (`Process_ProcessID`),
  CONSTRAINT `fk_WorkItemType_Process1` FOREIGN KEY (`Process_ProcessID`) REFERENCES `process` (`ProcessID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workitemtype`
--

LOCK TABLES `workitemtype` WRITE;
/*!40000 ALTER TABLE `workitemtype` DISABLE KEYS */;
/*!40000 ALTER TABLE `workitemtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `iteration`
--

DROP TABLE IF EXISTS `iteration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iteration` (
  `IterationID` int(11) NOT NULL,
  PRIMARY KEY (`IterationID`),
  CONSTRAINT `fk_Iteration_WorkItemContainer1` FOREIGN KEY (`IterationID`) REFERENCES `workitemcontainer` (`WorkItemContainerID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `iteration`
--

LOCK TABLES `iteration` WRITE;
/*!40000 ALTER TABLE `iteration` DISABLE KEYS */;
/*!40000 ALTER TABLE `iteration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event` (
  `EventID` int(11) NOT NULL AUTO_INCREMENT,
  `DateTime` datetime NOT NULL,
  `Name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `Location` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Description` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CalendarID` int(11) NOT NULL,
  PRIMARY KEY (`EventID`),
  KEY `fk_Event_Calendar1` (`CalendarID`),
  CONSTRAINT `fk_Event_Calendar1` FOREIGN KEY (`CalendarID`) REFERENCES `calendar` (`CalendarID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `ProjectID` int(11) NOT NULL,
  `Descriptions` text COLLATE utf8_unicode_ci,
  `ClassID` int(11) NOT NULL,
  `ProcessID` int(11) NOT NULL,
  `CalendarID` int(11) NOT NULL,
  PRIMARY KEY (`ProjectID`),
  KEY `fk_Project_Class1` (`ClassID`),
  KEY `fk_Project_Process1` (`ProcessID`),
  KEY `fk_Project_Calendar1` (`CalendarID`),
  CONSTRAINT `fk_Project_WorkItemContainer1` FOREIGN KEY (`ProjectID`) REFERENCES `workitemcontainer` (`WorkItemContainerID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Project_Class1` FOREIGN KEY (`ClassID`) REFERENCES `class` (`ClassID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Project_Process1` FOREIGN KEY (`ProcessID`) REFERENCES `process` (`ProcessID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Project_Calendar1` FOREIGN KEY (`CalendarID`) REFERENCES `calendar` (`CalendarID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dependency`
--

DROP TABLE IF EXISTS `dependency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dependency` (
  `DependencyID` int(11) NOT NULL AUTO_INCREMENT,
  `DependencyTypeID` int(11) NOT NULL,
  `WorkItemID1` int(11) NOT NULL,
  `WorkItemID2` int(11) NOT NULL,
  PRIMARY KEY (`DependencyID`),
  KEY `fk_Dependency_DependencyType` (`DependencyTypeID`),
  KEY `fk_Dependency_WorkItem1` (`WorkItemID1`),
  KEY `fk_Dependency_WorkItem2` (`WorkItemID2`),
  CONSTRAINT `fk_Dependency_DependencyType` FOREIGN KEY (`DependencyTypeID`) REFERENCES `dependencytype` (`DependencyTypeID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Dependency_WorkItem1` FOREIGN KEY (`WorkItemID1`) REFERENCES `workitem` (`WorkItemID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Dependency_WorkItem2` FOREIGN KEY (`WorkItemID2`) REFERENCES `workitem` (`WorkItemID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dependency`
--

LOCK TABLES `dependency` WRITE;
/*!40000 ALTER TABLE `dependency` DISABLE KEYS */;
/*!40000 ALTER TABLE `dependency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `RoleID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `Description` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`RoleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wikicomment`
--

DROP TABLE IF EXISTS `wikicomment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wikicomment` (
  `WikiCommentID` int(11) NOT NULL AUTO_INCREMENT,
  `Content` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `WikiPage_WikiPageID` int(11) NOT NULL,
  `AccountID` int(11) NOT NULL,
  PRIMARY KEY (`WikiCommentID`),
  KEY `fk_WikiComment_WikiPage1` (`WikiPage_WikiPageID`),
  KEY `fk_WikiComment_Account1` (`AccountID`),
  CONSTRAINT `fk_WikiComment_WikiPage1` FOREIGN KEY (`WikiPage_WikiPageID`) REFERENCES `wikipage` (`WikiPageID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_WikiComment_Account1` FOREIGN KEY (`AccountID`) REFERENCES `account` (`AccountID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wikicomment`
--

LOCK TABLES `wikicomment` WRITE;
/*!40000 ALTER TABLE `wikicomment` DISABLE KEYS */;
/*!40000 ALTER TABLE `wikicomment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectaccountmapping`
--

DROP TABLE IF EXISTS `projectaccountmapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectaccountmapping` (
  `ProjectAccountMappingID` int(11) NOT NULL AUTO_INCREMENT,
  `ProjectID` int(11) NOT NULL,
  `AccountID` int(11) NOT NULL,
  `RoleID` int(11) NOT NULL,
  PRIMARY KEY (`ProjectAccountMappingID`),
  KEY `fk_ProjectAccountMapping_Project1` (`ProjectID`),
  KEY `fk_ProjectAccountMapping_Account1` (`AccountID`),
  KEY `fk_ProjectAccountMapping_Role1` (`RoleID`),
  CONSTRAINT `fk_ProjectAccountMapping_Project1` FOREIGN KEY (`ProjectID`) REFERENCES `project` (`ProjectID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProjectAccountMapping_Account1` FOREIGN KEY (`AccountID`) REFERENCES `account` (`AccountID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProjectAccountMapping_Role1` FOREIGN KEY (`RoleID`) REFERENCES `role` (`RoleID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectaccountmapping`
--

LOCK TABLES `projectaccountmapping` WRITE;
/*!40000 ALTER TABLE `projectaccountmapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectaccountmapping` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-03-04 23:02:14
