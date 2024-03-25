-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 25, 2024 at 04:41 AM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kasir_rafli`
--

-- --------------------------------------------------------

--
-- Table structure for table `detailpenjualan`
--

CREATE TABLE `detailpenjualan` (
  `DetailID` varchar(15) NOT NULL,
  `ProdukID` varchar(15) NOT NULL,
  `Harga` int(11) NOT NULL,
  `JumlahProduk` int(11) NOT NULL,
  `Subtotal` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `detailpenjualan`
--

INSERT INTO `detailpenjualan` (`DetailID`, `ProdukID`, `Harga`, `JumlahProduk`, `Subtotal`) VALUES
('DIDP003', '1', 10000, 10, 100000),
('DIDP006', '8991389230220', 3000, 100, 300000),
('DIDP007', '1', 10000, 990, 9900000),
('DIDP009', '4', 7000, 2, 14000),
('DIDP010', '4', 7000, 50, 350000),
('DIDP011', '5', 1000, 100, 100000),
('DIDP013', '3', 3000, 9, 27000);

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `LoginID` varchar(15) NOT NULL,
  `Username` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `HakAkses` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `login`
--

INSERT INTO `login` (`LoginID`, `Username`, `Password`, `HakAkses`) VALUES
('0', 'dede', '1234', 'admin'),
('1', 'ari', '123', 'Admin'),
('3', 'Adrian', '12345', 'Petugas'),
('4', 'Rafli', '12345', 'Admin'),
('1232', 'Naba', '1111', 'Petugas'),
('5', 'well', '123455', 'Petugas'),
('7', 'Azi', '12345', 'Admin');

-- --------------------------------------------------------

--
-- Table structure for table `pelanggan`
--

CREATE TABLE `pelanggan` (
  `PelangganID` varchar(15) NOT NULL,
  `NamaPelanggan` varchar(255) NOT NULL,
  `Alamat` text NOT NULL,
  `NomorTelepon` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pelanggan`
--

INSERT INTO `pelanggan` (`PelangganID`, `NamaPelanggan`, `Alamat`, `NomorTelepon`) VALUES
('132244', 'Rere', 'Singkup', '23425346'),
('3', 'naba', 'Sindangreret', '12344'),
('4', 'Atunk', 'Gobang', '0888'),
('5', 'Ridwan', 'Cianjur', '087676543');

-- --------------------------------------------------------

--
-- Table structure for table `penjualan`
--

CREATE TABLE `penjualan` (
  `PenjualanID` varchar(15) NOT NULL,
  `DetailID` varchar(15) NOT NULL,
  `TanggalPenjualan` date NOT NULL,
  `JamPenjualan` time NOT NULL,
  `TotalHarga` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `penjualan`
--

INSERT INTO `penjualan` (`PenjualanID`, `DetailID`, `TanggalPenjualan`, `JamPenjualan`, `TotalHarga`) VALUES
('IDP001', 'DIDP001', '2024-02-26', '15:03:27', 2000),
('IDP002', 'DIDP002', '2024-02-27', '09:00:05', 6000),
('IDP003', 'DIDP003', '2024-02-27', '09:31:27', 100000),
('IDP004', 'DIDP004', '2024-02-27', '09:31:42', 76000),
('IDP005', 'DIDP005', '2024-02-27', '09:31:57', 6000),
('IDP006', 'DIDP006', '2024-02-28', '07:26:20', 300000),
('IDP007', 'DIDP007', '2024-03-01', '09:57:34', 9900000),
('IDP008', 'DIDP008', '2024-03-14', '11:44:42', 15000),
('IDP009', 'DIDP009', '2024-03-14', '11:45:05', 14000),
('IDP010', 'DIDP010', '2024-03-14', '11:45:34', 350000),
('IDP011', 'DIDP011', '2024-03-14', '11:45:54', 100000),
('IDP012', 'DIDP012', '2024-03-21', '11:52:14', 15000),
('IDP013', 'DIDP013', '2024-03-21', '12:18:58', 27000);

-- --------------------------------------------------------

--
-- Table structure for table `produk`
--

CREATE TABLE `produk` (
  `ProdukID` varchar(15) NOT NULL,
  `NamaProduk` varchar(255) NOT NULL,
  `Harga` int(11) NOT NULL,
  `Stok` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `produk`
--

INSERT INTO `produk` (`ProdukID`, `NamaProduk`, `Harga`, `Stok`) VALUES
('1', 'bubur', 10000, 1000),
('2', 'bajigur', 1000, 170),
('3', 'kolak candil', 3000, 80),
('4', 'es campur', 7000, 200),
('5', 'bakwan', 1000, 1900);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `detailpenjualan`
--
ALTER TABLE `detailpenjualan`
  ADD PRIMARY KEY (`DetailID`),
  ADD KEY `ProdukID` (`ProdukID`);

--
-- Indexes for table `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`PelangganID`);

--
-- Indexes for table `penjualan`
--
ALTER TABLE `penjualan`
  ADD PRIMARY KEY (`PenjualanID`);

--
-- Indexes for table `produk`
--
ALTER TABLE `produk`
  ADD PRIMARY KEY (`ProdukID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
