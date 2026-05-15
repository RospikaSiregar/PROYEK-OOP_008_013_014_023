# PROYEK-PBO_008_013_014_023
Menerapkan JDBC (Java Database Connectivity), JCF (Java Collection Framework), Inheritance dan ORM (Object Relational Mapping)

# SISTEM MANAJEMEN RESTORAN

Buatlah sebuah program Sistem Manajemen Restoran berbasis Java yang mampu mengelola data menu, customer, meja restoran, pesanan, serta laporan sistem secara terintegrasi. Program harus menerapkan konsep Object Oriented Programming (OOP) dan menyediakan menu interaktif berbasis terminal atau console. Sistem yang dibuat harus mampu membantu operasional restoran mulai dari pengelolaan menu, pengelolaan pelanggan, pengelolaan meja, proses pemesanan makanan dan minuman, hingga pembuatan laporan sistem secara otomatis.

Program harus memiliki menu utama dan submenu untuk setiap fitur sehingga pengguna dapat mengakses seluruh fungsi sistem dengan mudah. Seluruh data harus dapat dikelola menggunakan operasi CRUD (Create, Read, Update, Delete). Program juga wajib memiliki validasi input, perhitungan otomatis, integrasi antar data, serta penyimpanan data menggunakan ArrayList atau database seperti SQLite/MySQL.

Pada fitur Menu Management, sistem harus mampu mengelola data makanan dan minuman restoran. Pengguna dapat menambahkan menu baru dengan informasi berupa ID menu, nama menu, deskripsi, kategori, harga, dan stok. Sistem juga harus mampu menampilkan daftar menu, menampilkan menu berdasarkan kategori tertentu, mengupdate informasi menu seperti harga, deskripsi, dan stok, serta menghapus menu dari sistem. Harga dan stok tidak boleh bernilai negatif serta ID menu tidak boleh duplikat.

Pada fitur Customer Management, sistem harus mampu mengelola data pelanggan restoran. Pengguna dapat menambahkan customer baru dengan data berupa ID customer, nama, nomor telepon, email, dan alamat. Sistem juga harus dapat menampilkan seluruh data customer, mencari customer berdasarkan nama, mengupdate informasi customer, dan menghapus data customer dari sistem. Nama customer minimal terdiri dari tiga karakter dan nomor telepon tidak boleh kosong. Setiap customer juga harus memiliki ID yang unik.

Pada fitur Table Management, sistem harus mampu mengelola data meja restoran. Pengguna dapat menambahkan meja baru dengan informasi ID meja, nomor meja, kapasitas, dan status meja. Sistem harus dapat menampilkan seluruh daftar meja beserta statusnya, mengupdate kapasitas meja, dan menghapus meja dari sistem. Nomor meja tidak boleh sama dan kapasitas meja minimal satu orang. Status meja terdiri dari “Tersedia” dan “Terisi”, serta harus berubah otomatis ketika meja digunakan dalam pesanan.
  
Pada fitur Order Management, sistem harus mampu mengelola pesanan customer secara lengkap dan terintegrasi dengan data customer, meja, dan menu. Saat membuat pesanan baru, sistem harus menampilkan daftar customer yang tersedia, kemudian pengguna memilih customer berdasarkan ID. Setelah itu sistem menampilkan daftar meja yang masih tersedia dan pengguna memilih meja yang akan digunakan. Selanjutnya sistem menampilkan daftar menu dan pengguna dapat menambahkan lebih dari satu item menu ke dalam satu pesanan beserta jumlah item yang dipesan.

Sistem wajib melakukan validasi terhadap stok menu sebelum item ditambahkan ke dalam pesanan. Jika stok tidak mencukupi maka pesanan tidak boleh diproses. Ketika item berhasil dimasukkan ke dalam pesanan, stok menu harus otomatis berkurang sesuai jumlah yang dipesan. Setelah pesanan berhasil dibuat, status meja juga harus otomatis berubah menjadi “Terisi”. Sistem harus menghitung subtotal setiap item dan total harga seluruh pesanan secara otomatis.

Program juga harus mendukung pengelolaan status pesanan. Status pesanan terdiri dari Pending, Selesai, dan Dibatalkan. Ketika pesanan selesai, status meja harus kembali menjadi “Tersedia”. Jika pesanan dibatalkan, maka stok seluruh item pesanan harus dikembalikan secara otomatis dan status meja juga kembali menjadi “Tersedia”. Sistem juga harus dapat menampilkan riwayat pesanan customer.

Selain pengelolaan data dan pesanan, program harus memiliki fitur Reporting System. Pada laporan pesanan, sistem harus dapat menampilkan seluruh data pesanan secara lengkap yang mencakup ID pesanan, nama customer, nomor meja, tanggal pesanan, status pesanan, total harga, dan detail item pesanan. Sistem juga harus mampu melakukan filter laporan berdasarkan status pesanan maupun customer tertentu.

Pada laporan keuangan, sistem harus mampu menghitung total seluruh pendapatan restoran, total pendapatan dari pesanan yang selesai, rata-rata nilai pesanan, customer dengan transaksi terbanyak, serta menu yang paling sering dipesan. Pada laporan inventory, sistem harus mampu menampilkan seluruh stok menu beserta status stoknya, menampilkan menu dengan stok menipis, menu dengan stok habis, serta riwayat perubahan stok. Threshold stok menipis dapat ditentukan sendiri oleh pembuat program.

Program minimal harus memiliki beberapa class utama seperti Menu, Customer, Meja, Order, dan OrderItem. Selain itu program juga harus memiliki service class untuk setiap fitur dan satu Main Class sebagai pusat eksekusi program. Program wajib menerapkan konsep OOP seperti class dan object, encapsulation, constructor, getter setter, composition, modular programming, dan penggunaan ArrayList.

Berikut adalah contoh tampilan menu utama program:

```bash
=== MENU UTAMA ===
1. Menu Management
2. Customer Management
3. Table Management
4. Order Management
5. Laporan
0. Exit
```

Berikut adalah contoh output ringkasan pesanan:

```bash
=== RINGKASAN PESANAN ===

Order ID : 1
Customer : Budi Santoso
Meja     : 2
Status   : Pending

Item Pesanan:
- Nasi Goreng x2 = Rp 70.000
- Es Teh Manis x2 = Rp 10.000

TOTAL : Rp 80.000

Pesanan berhasil dibuat!
```

