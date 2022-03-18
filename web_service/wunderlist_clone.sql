-- phpMyAdmin SQL Dump
-- version 4.9.5deb2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Mar 18, 2022 at 07:02 PM
-- Server version: 8.0.28-0ubuntu0.20.04.3
-- PHP Version: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `wunderlist_clone`
--

-- --------------------------------------------------------

--
-- Table structure for table `gorevler`
--

CREATE TABLE `gorevler` (
  `gorev_id` int NOT NULL,
  `gorev_icerik` varchar(450) CHARACTER SET utf8 COLLATE utf8_turkish_ci NOT NULL,
  `gorev_yazar` int NOT NULL,
  `gorev_liste` int NOT NULL,
  `gorev_tamamlandi` tinyint(1) NOT NULL DEFAULT '0',
  `gorev_tamamlanma_tarihi` datetime DEFAULT NULL,
  `gorev_tamamlayan` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `kullanicilar`
--

CREATE TABLE `kullanicilar` (
  `kullanici_id` int NOT NULL,
  `kullanici_ad` varchar(20) COLLATE utf8_turkish_ci NOT NULL,
  `kullanici_sifre` varchar(20) COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_turkish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `listeler`
--

CREATE TABLE `listeler` (
  `liste_id` int NOT NULL,
  `liste_baslik` varchar(50) COLLATE utf8_turkish_ci NOT NULL,
  `liste_arkaplan` tinyint NOT NULL DEFAULT '1',
  `liste_yonetici` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_turkish_ci;

--
-- Triggers `listeler`
--
DELIMITER $$
CREATE TRIGGER `gorevleri_temizle` BEFORE DELETE ON `listeler` FOR EACH ROW DELETE FROM gorevler WHERE gorev_liste = old.liste_id
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `uyeleri_temizle` BEFORE DELETE ON `listeler` FOR EACH ROW DELETE FROM liste_uyeleri WHERE liste_id = old.liste_id
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `liste_arkaplan`
--

CREATE TABLE `liste_arkaplan` (
  `arkaplan_id` int NOT NULL,
  `arkaplan_ad` varchar(30) COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_turkish_ci;

--
-- Dumping data for table `liste_arkaplan`
--

INSERT INTO `liste_arkaplan` (`arkaplan_id`, `arkaplan_ad`) VALUES
(1, 'arkaplan_1'),
(2, 'arkaplan_2'),
(3, 'arkaplan_3');

-- --------------------------------------------------------

--
-- Table structure for table `liste_uyeleri`
--

CREATE TABLE `liste_uyeleri` (
  `id` int NOT NULL,
  `liste_id` int NOT NULL,
  `kullanici_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_turkish_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `gorevler`
--
ALTER TABLE `gorevler`
  ADD PRIMARY KEY (`gorev_id`);

--
-- Indexes for table `kullanicilar`
--
ALTER TABLE `kullanicilar`
  ADD PRIMARY KEY (`kullanici_id`);

--
-- Indexes for table `listeler`
--
ALTER TABLE `listeler`
  ADD PRIMARY KEY (`liste_id`);

--
-- Indexes for table `liste_arkaplan`
--
ALTER TABLE `liste_arkaplan`
  ADD PRIMARY KEY (`arkaplan_id`);

--
-- Indexes for table `liste_uyeleri`
--
ALTER TABLE `liste_uyeleri`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `gorevler`
--
ALTER TABLE `gorevler`
  MODIFY `gorev_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT for table `kullanicilar`
--
ALTER TABLE `kullanicilar`
  MODIFY `kullanici_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `listeler`
--
ALTER TABLE `listeler`
  MODIFY `liste_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `liste_arkaplan`
--
ALTER TABLE `liste_arkaplan`
  MODIFY `arkaplan_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `liste_uyeleri`
--
ALTER TABLE `liste_uyeleri`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
