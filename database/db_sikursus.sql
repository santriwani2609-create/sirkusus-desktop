-- ============================================================
-- SIKURSUS Desktop - db_sikursus (rekonstruksi lengkap)
-- Struktur disesuaikan dengan PesertaDAO / UserDAO pada project,
-- data peserta mengikuti screenshot terakhir (Pertemuan 10).
-- Cara pakai: phpMyAdmin > tab Import > pilih file ini > Go
--             (atau tab SQL > tempel seluruh isi file > Go)
-- ============================================================

CREATE DATABASE IF NOT EXISTS db_sikursus
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;
USE db_sikursus;

-- Dimatikan sementara agar DROP/CREATE tabel berelasi tidak terhalang foreign key
SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------------------------
-- Tabel peserta (Pertemuan 9-10)
-- Kolom: id_peserta, nim, nama, jenis_kelamin, prodi, no_hp, email
-- ------------------------------------------------------------
DROP TABLE IF EXISTS peserta;
CREATE TABLE peserta (
    id_peserta    INT AUTO_INCREMENT PRIMARY KEY,
    nim           VARCHAR(20)  NOT NULL,
    nama          VARCHAR(100) NOT NULL,
    jenis_kelamin ENUM('L','P') NOT NULL,
    prodi         VARCHAR(50)  NOT NULL,
    no_hp         VARCHAR(20)  NOT NULL DEFAULT '',
    email         VARCHAR(100) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO peserta (id_peserta, nim, nama, jenis_kelamin, prodi, no_hp, email) VALUES
(1, '2924008', 'Santri Wani',     'P', 'Informatika',      '081266385928', 'Santriwani@gmail.com'),
(3, '2924023', 'miftahuljanah',   'P', 'informatika',      '08999227272',  'miftah@gmail.com'),
(4, '2924020', 'ahmad',           'L', 'informatika',      '08231232323',  'ahmad@gmail.com'),
(6, '2429008', 'santriwani',      'L', 'Sistem Informasi', '0821213133',   'santriwani@gmail.com'),
(7, '2924001', 'yaya',            'P', 'Matematika',       '089223733',    'yaya@gmail.com');

ALTER TABLE peserta AUTO_INCREMENT = 8;

-- ------------------------------------------------------------
-- Tabel users (Pertemuan 11: login)
-- Password disimpan sebagai hash PBKDF2 (bukan teks biasa)
--   admin    / Admin@123    -> ADMIN
--   operator / Operator@123 -> OPERATOR
-- ------------------------------------------------------------
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id_user       INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          ENUM('ADMIN','OPERATOR') NOT NULL DEFAULT 'OPERATOR',
    aktif         BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO users (username, password_hash, role, aktif) VALUES
('admin',    '120000:3bDVDqt2KlJDY+Fpb2LSyQ==:c0U40qcFWeWieA/8I7JJNJyscPHwf0NAu+TMW42/O70=', 'ADMIN',    1),
('operator', '120000:yVTb1nclzLrcoQyAYy4aVw==:p3K0VohYo7wRhSStaF1txhruBdYFmm2RX9gdvhHcHgY=', 'OPERATOR', 1);

-- ============================================================
-- PERTEMUAN 13 - tabel relasional untuk laporan
-- Jalur relasi:
--   peserta -> pendaftaran -> kelas -> kursus / instruktur
--   pendaftaran -> pembayaran
-- ============================================================

DROP TABLE IF EXISTS pembayaran;
DROP TABLE IF EXISTS pendaftaran;
DROP TABLE IF EXISTS kelas;
DROP TABLE IF EXISTS kursus;
DROP TABLE IF EXISTS instruktur;

CREATE TABLE instruktur (
    id_instruktur INT AUTO_INCREMENT PRIMARY KEY,
    nama          VARCHAR(100) NOT NULL,
    no_hp         VARCHAR(20)  NOT NULL DEFAULT '',
    keahlian      VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE kursus (
    id_kursus   INT AUTO_INCREMENT PRIMARY KEY,
    kode_kursus VARCHAR(20)  NOT NULL UNIQUE,
    nama_kursus VARCHAR(100) NOT NULL,
    level       VARCHAR(20)  NOT NULL DEFAULT 'BASIC',
    biaya       DECIMAL(12,2) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE kelas (
    id_kelas      INT AUTO_INCREMENT PRIMARY KEY,
    nama_kelas    VARCHAR(50) NOT NULL,
    id_kursus     INT NOT NULL,
    id_instruktur INT NOT NULL,
    CONSTRAINT fk_kelas_kursus
        FOREIGN KEY (id_kursus) REFERENCES kursus (id_kursus)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_kelas_instruktur
        FOREIGN KEY (id_instruktur) REFERENCES instruktur (id_instruktur)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE pendaftaran (
    id_pendaftaran INT AUTO_INCREMENT PRIMARY KEY,
    id_peserta     INT NOT NULL,
    id_kelas       INT NOT NULL,
    tanggal_daftar DATE NOT NULL,
    status         ENUM('AKTIF','SELESAI','BATAL') NOT NULL DEFAULT 'AKTIF',
    CONSTRAINT uq_peserta_kelas UNIQUE (id_peserta, id_kelas),
    CONSTRAINT fk_pendaftaran_peserta
        FOREIGN KEY (id_peserta) REFERENCES peserta (id_peserta)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_pendaftaran_kelas
        FOREIGN KEY (id_kelas) REFERENCES kelas (id_kelas)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE pembayaran (
    id_pembayaran  INT AUTO_INCREMENT PRIMARY KEY,
    id_pendaftaran INT NOT NULL,
    tanggal_bayar  DATE NOT NULL,
    nominal        DECIMAL(12,2) NOT NULL,
    metode         ENUM('TUNAI','TRANSFER','QRIS') NOT NULL DEFAULT 'TUNAI',
    status         ENUM('LUNAS','PENDING') NOT NULL DEFAULT 'LUNAS',
    CONSTRAINT fk_pembayaran_pendaftaran
        FOREIGN KEY (id_pendaftaran) REFERENCES pendaftaran (id_pendaftaran)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO instruktur (id_instruktur, nama, no_hp, keahlian) VALUES
(1, 'Dina Pratama',  '081211110001', 'Java Desktop'),
(2, 'Rizal Maulana', '081211110002', 'Data Science'),
(3, 'Alya Rahma',    '081211110003', 'Web & UI/UX');

INSERT INTO kursus (id_kursus, kode_kursus, nama_kursus, level, biaya) VALUES
(1, 'JAVA-BSC', 'Java Desktop Fundamental', 'BASIC',        500000),
(2, 'DS-INT',   'Data Science',             'INTERMEDIATE', 650000),
(3, 'WEB-BSC',  'Web Programming',          'BASIC',        450000),
(4, 'UIUX-BSC', 'UI/UX Design',             'BASIC',        400000);

INSERT INTO kelas (id_kelas, nama_kelas, id_kursus, id_instruktur) VALUES
(1, 'Java-A',  1, 1),
(2, 'DS-A',    2, 2),
(3, 'Web-A',   3, 3),
(4, 'UIUX-A',  4, 3);

INSERT INTO pendaftaran (id_pendaftaran, id_peserta, id_kelas, tanggal_daftar, status) VALUES
(1, 1, 1, '2026-11-01', 'AKTIF'),
(2, 4, 1, '2026-11-02', 'AKTIF'),
(3, 3, 2, '2026-11-05', 'AKTIF'),
(4, 7, 3, '2026-11-10', 'AKTIF'),
(5, 6, 4, '2026-11-12', 'AKTIF');

INSERT INTO pembayaran (id_pembayaran, id_pendaftaran, tanggal_bayar, nominal, metode, status) VALUES
(1, 1, '2026-11-01', 500000, 'TRANSFER', 'LUNAS'),
(2, 2, '2026-11-03', 250000, 'TUNAI',    'PENDING'),
(3, 3, '2026-11-06', 650000, 'QRIS',     'LUNAS'),
(4, 4, '2026-11-12', 450000, 'TRANSFER', 'LUNAS'),
(5, 5, '2026-11-13', 400000, 'TUNAI',    'LUNAS');

ALTER TABLE instruktur  AUTO_INCREMENT = 4;
ALTER TABLE kursus      AUTO_INCREMENT = 5;
ALTER TABLE kelas       AUTO_INCREMENT = 5;
ALTER TABLE pendaftaran AUTO_INCREMENT = 6;
ALTER TABLE pembayaran  AUTO_INCREMENT = 6;

SET FOREIGN_KEY_CHECKS = 1;
