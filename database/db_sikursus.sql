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
