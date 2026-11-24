-- ============================================================
-- Pertemuan 13 - query pemeriksaan (jalankan di phpMyAdmin > SQL)
-- Simpan screenshot hasilnya sebagai Bukti 1 dan Bukti 2.
-- ============================================================

-- TAHAP 2.1 / 1: pemeriksaan awal
SHOW TABLES;
SELECT COUNT(*) AS jumlah_peserta     FROM peserta;
SELECT COUNT(*) AS jumlah_pendaftaran FROM pendaftaran;
SELECT COUNT(*) AS jumlah_pembayaran  FROM pembayaran;

-- TAHAP 1A: definisi foreign key
SHOW CREATE TABLE kelas;
SHOW CREATE TABLE pendaftaran;
SHOW CREATE TABLE pembayaran;

-- TAHAP 1B: daftar foreign key (Bukti 1a)
SELECT TABLE_NAME, COLUMN_NAME, CONSTRAINT_NAME,
       REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'db_sikursus'
  AND REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY TABLE_NAME, COLUMN_NAME;

-- TAHAP 1C: orphan data (keduanya harus 0 baris) (Bukti 1b)
SELECT p.id_pendaftaran, p.id_peserta, p.id_kelas
FROM pendaftaran p
LEFT JOIN peserta ps ON p.id_peserta = ps.id_peserta
LEFT JOIN kelas kls ON p.id_kelas = kls.id_kelas
WHERE ps.id_peserta IS NULL OR kls.id_kelas IS NULL;

SELECT pb.id_pembayaran, pb.id_pendaftaran
FROM pembayaran pb
LEFT JOIN pendaftaran p ON pb.id_pendaftaran = p.id_pendaftaran
WHERE p.id_pendaftaran IS NULL;

-- TAHAP 2A: JOIN utama (Bukti 2)
SELECT p.id_pendaftaran, p.tanggal_daftar, p.status AS status_pendaftaran,
       ps.nim, ps.nama AS nama_peserta, ps.prodi, ps.no_hp,
       kls.nama_kelas, krs.kode_kursus, krs.nama_kursus, krs.level, krs.biaya,
       ins.nama AS nama_instruktur
FROM pendaftaran p
INNER JOIN peserta ps    ON p.id_peserta    = ps.id_peserta
INNER JOIN kelas kls     ON p.id_kelas      = kls.id_kelas
INNER JOIN kursus krs    ON kls.id_kursus   = krs.id_kursus
INNER JOIN instruktur ins ON kls.id_instruktur = ins.id_instruktur
ORDER BY p.id_pendaftaran;

-- TAHAP 2B: jumlah JOIN harus sama dengan jumlah pendaftaran
SELECT COUNT(*) AS jumlah_pendaftaran FROM pendaftaran;
SELECT COUNT(*) AS jumlah_join
FROM pendaftaran p
INNER JOIN peserta ps    ON p.id_peserta    = ps.id_peserta
INNER JOIN kelas kls     ON p.id_kelas      = kls.id_kelas
INNER JOIN kursus krs    ON kls.id_kursus   = krs.id_kursus
INNER JOIN instruktur ins ON kls.id_instruktur = ins.id_instruktur;

-- TAHAP 8A: query pembayaran (semua data)
SELECT pb.id_pembayaran, pb.tanggal_bayar, pb.nominal, pb.metode,
       pb.status AS status_pembayaran, p.id_pendaftaran,
       ps.nim, ps.nama AS nama_peserta, kls.nama_kelas, krs.nama_kursus
FROM pembayaran pb
INNER JOIN pendaftaran p ON pb.id_pendaftaran = p.id_pendaftaran
INNER JOIN peserta ps    ON p.id_peserta  = ps.id_peserta
INNER JOIN kelas kls     ON p.id_kelas    = kls.id_kelas
INNER JOIN kursus krs    ON kls.id_kursus = krs.id_kursus
ORDER BY pb.tanggal_bayar, pb.id_pembayaran;
