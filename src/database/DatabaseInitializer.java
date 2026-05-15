package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();

            // Create Menu table
            String menuTable = "CREATE TABLE IF NOT EXISTS menu (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nama TEXT NOT NULL," +
                    "deskripsi TEXT," +
                    "kategori TEXT NOT NULL," +
                    "harga REAL NOT NULL," +
                    "stok INTEGER NOT NULL" +
                    ")";
            stmt.execute(menuTable);
            System.out.println("✓ Tabel menu dibuat/sudah ada");

            // Create Customer table
            String customerTable = "CREATE TABLE IF NOT EXISTS customer (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nama TEXT NOT NULL," +
                    "no_telepon TEXT," +
                    "email TEXT," +
                    "alamat TEXT" +
                    ")";
            stmt.execute(customerTable);
            System.out.println("✓ Tabel customer dibuat/sudah ada");

            // Create Table (Meja Restoran)
            String tableTable = "CREATE TABLE IF NOT EXISTS restaurant_table (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nomor_meja INTEGER NOT NULL UNIQUE," +
                    "kapasitas INTEGER NOT NULL," +
                    "status TEXT NOT NULL" +
                    ")";
            stmt.execute(tableTable);
            System.out.println("✓ Tabel restaurant_table dibuat/sudah ada");

            // Create Order table
            String orderTable = "CREATE TABLE IF NOT EXISTS orders (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customer_id INTEGER NOT NULL," +
                    "table_id INTEGER NOT NULL," +
                    "tanggal_pesanan DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "status TEXT NOT NULL," +
                    "total_harga REAL," +
                    "FOREIGN KEY (customer_id) REFERENCES customer(id)," +
                    "FOREIGN KEY (table_id) REFERENCES restaurant_table(id)" +
                    ")";
            stmt.execute(orderTable);
            System.out.println("✓ Tabel orders dibuat/sudah ada");

            // Create OrderItem table
            String orderItemTable = "CREATE TABLE IF NOT EXISTS order_item (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "order_id INTEGER NOT NULL," +
                    "menu_id INTEGER NOT NULL," +
                    "menu_nama TEXT NOT NULL," +
                    "kuantitas INTEGER NOT NULL," +
                    "harga_satuan REAL NOT NULL," +
                    "subtotal REAL NOT NULL," +
                    "FOREIGN KEY (order_id) REFERENCES orders(id)," +
                    "FOREIGN KEY (menu_id) REFERENCES menu(id)" +
                    ")";
            stmt.execute(orderItemTable);
            System.out.println("✓ Tabel order_item dibuat/sudah ada");

            stmt.close();
            System.out.println("\n=== Database Initialization Selesai ===\n");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static void insertSampleData() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();

            // Check jika sudah ada data
            String checkMenu = "SELECT COUNT(*) as count FROM menu";
            if (stmt.executeQuery(checkMenu).getInt("count") > 0) {
                System.out.println("Database sudah berisi data sample, skip insert data.");
                stmt.close();
                return;
            }

            // Insert Menu Makanan
            String[] menuItems = {
                    "INSERT INTO menu (nama, deskripsi, kategori, harga, stok) VALUES ('Nasi Goreng', 'Nasi goreng dengan telur dan sayuran', 'Makanan', 35000, 50)",
                    "INSERT INTO menu (nama, deskripsi, kategori, harga, stok) VALUES ('Soto Ayam', 'Soto ayam tradisional yang gurih', 'Makanan', 25000, 40)",
                    "INSERT INTO menu (nama, deskripsi, kategori, harga, stok) VALUES ('Rendang Daging', 'Rendang daging sapi yang empuk', 'Makanan', 50000, 30)",
                    "INSERT INTO menu (nama, deskripsi, kategori, harga, stok) VALUES ('Gado-gado', 'Gado-gado dengan kacang dan sayuran', 'Makanan', 20000, 45)",
                    "INSERT INTO menu (nama, deskripsi, kategori, harga, stok) VALUES ('Satay Ayam', 'Daging ayam tusuk dengan bumbu kacang', 'Makanan', 40000, 35)",
                    "INSERT INTO menu (nama, deskripsi, kategori, harga, stok) VALUES ('Es Teh Manis', 'Es teh manis yang segar', 'Minuman', 8000, 100)",
                    "INSERT INTO menu (nama, deskripsi, kategori, harga, stok) VALUES ('Kopi Hitam', 'Kopi hitam panas yang nikmat', 'Minuman', 15000, 60)",
                    "INSERT INTO menu (nama, deskripsi, kategori, harga, stok) VALUES ('Jus Mangga', 'Jus mangga segar tanpa gula', 'Minuman', 18000, 40)",
                    "INSERT INTO menu (nama, deskripsi, kategori, harga, stok) VALUES ('Smoothie Strawberry', 'Smoothie strawberry dengan yogurt', 'Minuman', 22000, 35)",
                    "INSERT INTO menu (nama, deskripsi, kategori, harga, stok) VALUES ('Air Putih', 'Air putih mineral dingin', 'Minuman', 5000, 200)"
            };

            for (String query : menuItems) {
                stmt.execute(query);
            }
            System.out.println("✓ 10 menu items berhasil ditambahkan");

            // Insert Sample Tables
            for (int i = 1; i <= 10; i++) {
                stmt.execute("INSERT INTO restaurant_table (nomor_meja, kapasitas, status) VALUES (" + i + ", " + (2 + (i % 4)) + ", 'Tersedia')");
            }
            System.out.println("✓ 10 meja restoran berhasil ditambahkan");

            // Insert Sample Customers
            String[] customers = {
                    "INSERT INTO customer (nama, no_telepon, email, alamat) VALUES ('Budi Santoso', '081234567890', 'budi@email.com', 'Jalan Merdeka No 10')",
                    "INSERT INTO customer (nama, no_telepon, email, alamat) VALUES ('Siti Nurhaliza', '082345678901', 'siti@email.com', 'Jalan Sudirman No 25')",
                    "INSERT INTO customer (nama, no_telepon, email, alamat) VALUES ('Doni Hermawan', '083456789012', 'doni@email.com', 'Jalan Gatot Subroto No 5')",
                    "INSERT INTO customer (nama, no_telepon, email, alamat) VALUES ('Eka Putri', '084567890123', 'eka@email.com', 'Jalan Ahmad Yani No 15')"
            };

            for (String query : customers) {
                stmt.execute(query);
            }
            System.out.println("✓ 4 customer sample berhasil ditambahkan");

            stmt.close();
            System.out.println("\n=== Data Sample Insertion Selesai ===\n");

        } catch (SQLException e) {
            System.err.println("Error inserting sample data: " + e.getMessage());
        }
    }
}
