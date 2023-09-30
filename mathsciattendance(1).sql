-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 30, 2023 at 03:51 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mathsciattendance`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` int(5) NOT NULL DEFAULT 0,
  `name` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `password` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `email` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `address` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `city` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `contact` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `name`, `password`, `email`, `address`, `city`, `contact`) VALUES
(0, 'Benjie S. Juabot', 'Benjie14', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `attendance`
--

CREATE TABLE `attendance` (
  `eventCode` varchar(30) NOT NULL,
  `studRFID` varchar(20) NOT NULL,
  `timein` varchar(20) NOT NULL,
  `timeout` varchar(20) NOT NULL,
  `timeinBy` varchar(50) NOT NULL,
  `timeoutBy` varchar(50) NOT NULL,
  `yearlvl` varchar(25) NOT NULL,
  `fines` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `attendance`
--

INSERT INTO `attendance` (`eventCode`, `studRFID`, `timein`, `timeout`, `timeinBy`, `timeoutBy`, `yearlvl`, `fines`) VALUES
('test', '0013313567', '17:19:09.689', '17:19:48.725', 'Benjie S. Juabot', 'Benjie S. Juabot', '1st Year', 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `borrowed_equipment`
--

CREATE TABLE `borrowed_equipment` (
  `student_id` varchar(50) NOT NULL,
  `equipment_id` varchar(50) NOT NULL,
  `borrowed_date` date DEFAULT NULL,
  `returned_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `equipments`
--

CREATE TABLE `equipments` (
  `id` int(11) NOT NULL,
  `refID` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `manufacturer` varchar(100) NOT NULL,
  `model` varchar(100) NOT NULL,
  `dateAdded` date NOT NULL,
  `isBorrowed` tinyint(1) NOT NULL,
  `isAvailable` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `events`
--

CREATE TABLE `events` (
  `eventCode` varchar(30) NOT NULL,
  `eventName` varchar(150) NOT NULL,
  `eventDate` date NOT NULL,
  `timeIn` time NOT NULL,
  `timeOut` time NOT NULL,
  `fines` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `events`
--

INSERT INTO `events` (`eventCode`, `eventName`, `eventDate`, `timeIn`, `timeOut`, `fines`) VALUES
('test', 'testing', '2023-09-30', '16:18:00', '17:18:00', 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `facilitator`
--

CREATE TABLE `facilitator` (
  `id` int(5) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `address` varchar(200) NOT NULL,
  `city` varchar(100) NOT NULL,
  `contact` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `fines`
--

CREATE TABLE `fines` (
  `eventCode` varchar(30) NOT NULL,
  `studRFID` varchar(20) NOT NULL,
  `fines` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
  `rfid` varchar(20) NOT NULL,
  `eventCode` varchar(25) NOT NULL,
  `payment` decimal(5,2) NOT NULL,
  `receivedDate` date NOT NULL,
  `receivedBy` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `id` int(11) NOT NULL,
  `studentID` varchar(20) NOT NULL,
  `firstName` varchar(50) DEFAULT NULL,
  `middleName` varchar(50) DEFAULT NULL,
  `lastName` varchar(50) DEFAULT NULL,
  `extension` varchar(10) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `mobileNo` varchar(20) DEFAULT NULL,
  `registration` varchar(20) DEFAULT NULL,
  `qr` varchar(200) DEFAULT NULL,
  `addedby` varchar(50) DEFAULT NULL,
  `dateAdded` date DEFAULT NULL,
  `timeAdded` time DEFAULT NULL,
  `rfid` varchar(20) NOT NULL,
  `img` varchar(150) NOT NULL,
  `yearlvl` varchar(10) NOT NULL,
  `subj1` varchar(20) NOT NULL,
  `subj2` varchar(20) NOT NULL,
  `subj3` varchar(20) NOT NULL,
  `subj4` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`id`, `studentID`, `firstName`, `middleName`, `lastName`, `extension`, `email`, `mobileNo`, `registration`, `qr`, `addedby`, `dateAdded`, `timeAdded`, `rfid`, `img`, `yearlvl`, `subj1`, `subj2`, `subj3`, `subj4`) VALUES
(1, '21-0308-733', 'Benjie', 'Solera', 'Juabot', '', '', '', 'Registered', 'C:\\Documents\\CompesaEventHandler\\CompesaAutomatedEventsHandler\\qrcode\\qrcode\\21-0308-733.PNG', 'User', '2023-09-30', '16:31:00', '0013313567', 'C:\\Documents\\CompesaEventHandler\\CompesaAutomatedEventsHandler\\img\\img\\cpelogo.png', '1st Year', 'MTW2-T302', 'Not-Enrolled', 'Not-Enrolled', 'Not-Enrolled'),
(2, '20-3213-123', 'Test', '', 'Student', '', '', '', 'Registered', 'C:\\Documents\\CompesaEventHandler\\CompesaAutomatedEventsHandler\\qrcode\\qrcode\\20-3213-123.PNG', 'Benjie S. Juabot', '2023-09-30', '20:42:00', '332434', 'C:\\Documents\\CompesaEventHandler\\CompesaAutomatedEventsHandler\\img\\img\\cpelogo.png', '', 'Stas-T302', '', '', ''),
(3, '22-2333-222', 'Another', 'Test', 'Student', '', '', '', 'Registered', 'C:\\Documents\\CompesaEventHandler\\CompesaAutomatedEventsHandler\\qrcode\\qrcode\\22-2333-222.PNG', 'Benjie S. Juabot', '2023-09-30', '20:44:00', '', 'C:\\Documents\\CompesaEventHandler\\CompesaAutomatedEventsHandler\\img\\img\\cpelogo.png', '', 'Stas-T302', 'MTW2-T302', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `subjects`
--

CREATE TABLE `subjects` (
  `subjName` varchar(100) NOT NULL,
  `subjCode` varchar(20) NOT NULL,
  `subjSection` varchar(10) NOT NULL,
  `classIdentity` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `subjects`
--

INSERT INTO `subjects` (`subjName`, `subjCode`, `subjSection`, `classIdentity`) VALUES
('Math in the Modern World', 'MTW2', 'T302', 'MTW2-T302'),
('Science technology and society', 'Stas', 'T302', 'Stas-T302'),
('', 'Not', 'Enrolled', 'Not-Enrolled'),
('Science1', 'sci', 'T333', 'sci-T333');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attendance`
--
ALTER TABLE `attendance`
  ADD KEY `idx_eventCode` (`eventCode`),
  ADD KEY `studRFID` (`studRFID`);

--
-- Indexes for table `equipments`
--
ALTER TABLE `equipments`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_refID` (`refID`);

--
-- Indexes for table `events`
--
ALTER TABLE `events`
  ADD KEY `idx_eventCode` (`eventCode`);

--
-- Indexes for table `facilitator`
--
ALTER TABLE `facilitator`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `fines`
--
ALTER TABLE `fines`
  ADD KEY `fk_rfid` (`studRFID`),
  ADD KEY `fk_events` (`eventCode`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD KEY `fk-rfid` (`rfid`),
  ADD KEY `fk-evenCodeConstraint` (`eventCode`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uc_studentID` (`studentID`),
  ADD UNIQUE KEY `unique_rfid` (`rfid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `equipments`
--
ALTER TABLE `equipments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `facilitator`
--
ALTER TABLE `facilitator`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `students`
--
ALTER TABLE `students`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `attendance`
--
ALTER TABLE `attendance`
  ADD CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`studRFID`) REFERENCES `students` (`rfid`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_eventCode` FOREIGN KEY (`eventCode`) REFERENCES `events` (`eventCode`) ON UPDATE CASCADE;

--
-- Constraints for table `fines`
--
ALTER TABLE `fines`
  ADD CONSTRAINT `fk_events` FOREIGN KEY (`eventCode`) REFERENCES `events` (`eventCode`),
  ADD CONSTRAINT `fk_rfid` FOREIGN KEY (`studRFID`) REFERENCES `students` (`rfid`) ON UPDATE NO ACTION;

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `fk-evenCodeConstraint` FOREIGN KEY (`eventCode`) REFERENCES `fines` (`eventCode`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
