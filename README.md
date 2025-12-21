# 🧺 Laundry Management System

Aplikasi manajemen laundry modern berbasis **Java Swing** dengan antarmuka pengguna yang menarik dan fungsionalitas lengkap untuk mengelola bisnis laundry Anda.

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Swing](https://img.shields.io/badge/Java%20Swing-Desktop%20App-blue?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-3.8+-red?style=for-the-badge&logo=apache-maven)

---

## 📋 Daftar Isi

- [Tentang Proyek](#-tentang-proyek)
- [Fitur Utama](#-fitur-utama)
- [Teknologi yang Digunakan](#-teknologi-yang-digunakan)
- [Persyaratan Sistem](#-persyaratan-sistem)
- [Instalasi](#-instalasi)
- [Cara Penggunaan](#-cara-penggunaan)
- [Struktur Proyek](#-struktur-proyek)
- [Fitur Detail](#-fitur-detail)

---

## 🎯 Tentang Proyek

Aplikasi ini dirancang untuk membantu pemilik bisnis laundry dalam mengelola pesanan, pelanggan, dan laporan keuangan dengan mudah. Dengan antarmuka yang modern dan intuitif, aplikasi ini menyediakan semua fitur yang dibutuhkan untuk menjalankan operasional laundry secara efisien.

---

## ✨ Fitur Utama

### 🔐 **Sistem Autentikasi**
- Login dan registrasi pengguna
- Manajemen akun dengan enkripsi password
- Keamanan data pengguna

### 📊 **Dashboard Overview**
- Statistik pesanan real-time
- Ringkasan pesanan pending dan selesai
- Total pendapatan hari ini
- Tabel overview pesanan terbaru

### 📝 **Manajemen Pesanan**
- **Tambah Pesanan Baru**: Input data pelanggan, jenis layanan, berat, dan tanggal
- **Edit Pesanan**: Ubah detail pesanan (kecuali yang sudah selesai)
- **Hapus Pesanan**: Hapus pesanan yang belum selesai
- **Tandai Selesai**: Update status pesanan menjadi selesai
- **Pencarian**: Cari pesanan berdasarkan nama pelanggan atau ID
- **Filter Status**: Tampilkan pesanan berdasarkan status (Pending/Selesai)

### 👥 **Manajemen User**
- Daftar semua pengguna terdaftar
- Edit informasi pengguna
- Hapus pengguna
- Validasi username unik

### 📈 **Laporan & Analitik**
- **Daily Revenue Chart**: Grafik batang pendapatan harian (7 hari terakhir)
- **Date Range Filter**: Filter laporan berdasarkan rentang tanggal
- **Detailed Transactions**: Tabel detail transaksi dengan informasi lengkap
- **Revenue Calculation**: Perhitungan otomatis berdasarkan jenis layanan

### 🎨 **UI/UX Modern**
- Desain antarmuka yang menarik dengan gradient dan rounded corners
- Sidebar navigasi yang responsif
- Tabel dengan styling modern
- Dialog dan alert yang konsisten
- Hover dan press effects pada tombol
- Color-coded status badges

---

## 🛠 Teknologi yang Digunakan

- **Java 17** - Bahasa pemrograman utama
- **Java Swing** - Framework untuk GUI desktop
- **Maven** - Build tool dan dependency management
- **CSV** - Penyimpanan data (pesanan dan user)
- **Apache POI** - Library untuk ekspor data (opsional)

---

## 💻 Persyaratan Sistem

- **Java Development Kit (JDK) 17** atau lebih tinggi
- **Maven 3.8+** (untuk build project)
- **Sistem Operasi**: Windows, macOS, atau Linux

---

## 🚀 Instalasi

### 1. Clone Repository

```bash
git clone <repository-url>
cd UAP-Pemrograman-Lanjut/uap
```

### 2. Build Project

```bash
mvn clean compile
```

### 3. Run Application

```bash
mvn exec:java -Dexec.mainClass="laundry.Main"
```

Atau build JAR dan jalankan:

```bash
mvn package
java -jar target/laundry-app-1.0.0.jar
```

---

## 📖 Cara Penggunaan

### 1. **Registrasi & Login**
   - Buka aplikasi, klik tombol "Daftar" untuk membuat akun baru
   - Masukkan username dan password
   - Setelah registrasi, login dengan kredensial yang telah dibuat

### 2. **Dashboard Overview**
   - Setelah login, Anda akan melihat dashboard dengan statistik
   - Overview menampilkan ringkasan pesanan dan pendapatan

### 3. **Tambah Pesanan Baru**
   - Klik menu "Input Data" di sidebar
   - Isi form:
     - Nama Pelanggan
     - Jenis Layanan (Cuci Kering, Setrika, atau Cuci Setrika)
     - Berat (dalam kg)
     - Tanggal Order (otomatis, tidak bisa diubah)
     - Tanggal Pickup (default 1 hari setelah order)
     - Status (Pending/Selesai)
   - Klik "Simpan" untuk menyimpan pesanan

### 4. **Kelola Pesanan**
   - Klik menu "List Data" untuk melihat semua pesanan
   - Gunakan search bar untuk mencari pesanan
   - Klik "Edit" untuk mengubah pesanan (kecuali yang sudah selesai)
   - Klik "Tandai Selesai" untuk mengubah status pesanan
   - Klik "Hapus" untuk menghapus pesanan (hanya yang belum selesai)

### 5. **Manajemen User**
   - Klik menu "Users" untuk melihat daftar pengguna
   - Klik "Edit" untuk mengubah informasi user
   - Klik "Hapus" untuk menghapus user

### 6. **Lihat Laporan**
   - Klik menu "Report" untuk melihat laporan pendapatan
   - Grafik batang menampilkan pendapatan 7 hari terakhir
   - Klik "Date Range" untuk memfilter berdasarkan tanggal
   - Klik "Reset" untuk mengembalikan filter ke default

---

## 📁 Struktur Proyek

```
uap/
├── src/
│   └── main/
│       ├── java/
│       │   └── laundry/
│       │       ├── Main.java                 # Entry point aplikasi
│       │       ├── model/                    # Model data
│       │       │   ├── Laundry.java          # Model pesanan
│       │       │   ├── User.java             # Model user
│       │       │   ├── ServiceType.java      # Enum jenis layanan
│       │       │   └── OrderStatus.java       # Enum status pesanan
│       │       ├── repo/                     # Repository layer
│       │       │   ├── Csv.java              # CSV handler untuk pesanan
│       │       │   └── UserCsv.java          # CSV handler untuk user
│       │       ├── service/                  # Business logic
│       │       │   └── LaundryService.java   # Service untuk validasi & kalkulasi
│       │       ├── ui/                       # User interface
│       │       │   ├── LoginFrame.java       # Frame login & registrasi
│       │       │   └── Menu.java             # Dashboard utama
│       │       ├── exception/                # Custom exceptions
│       │       │   ├── Validation.java      # Exception untuk validasi
│       │       │   └── Data.java            # Exception untuk data
│       │       └── util/                     # Utilities
│       │           └── CsvUtil.java          # Utility untuk CSV
│       └── resources/
│           └── data/
│               ├── pesanan.csv               # Data pesanan
│               └── users.csv                 # Data user
├── pom.xml                                    # Maven configuration
└── README.md                                  # Dokumentasi
```

---

## 🎨 Fitur Detail

### Jenis Layanan
- **Cuci Kering**: Rp 5.000/kg
- **Setrika**: Rp 4.000/kg
- **Cuci Setrika**: Rp 8.000/kg

### Status Pesanan
- **PENDING**: Pesanan sedang diproses (warna orange)
- **SELESAI**: Pesanan sudah selesai (warna hijau)

### Validasi Data
- Nama pelanggan wajib diisi
- Berat harus lebih dari 0
- Tanggal pickup tidak boleh sebelum tanggal order
- Username harus unik

### Keamanan
- Pesanan yang sudah selesai tidak dapat diedit atau dihapus
- Password disimpan dalam format plain text (untuk keperluan demo)

---

## 📝 Catatan

- Data disimpan dalam format CSV di folder `src/main/resources/data/`
- File `pesanan.csv` menyimpan data pesanan
- File `users.csv` menyimpan data pengguna
- Aplikasi menggunakan CardLayout untuk navigasi antar halaman
- Semua dialog menggunakan custom styling yang konsisten

---

## 👨‍💻 Developer

Proyek ini dikembangkan oleh:
- **Naufal Muammar** (202410370110027)
- **Santun** (202410370110429)

Dibuat untuk Ujian Akhir Praktikum (UAP) Pemrograman Lanjut.
---

## 🙏 Terima Kasih

Terima kasih telah menggunakan aplikasi Laundry Management System ini! Jika ada pertanyaan atau saran, silakan buat issue di repository ini.

---
