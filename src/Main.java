import database.DatabaseConnection;
import database.DatabaseInitializer;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import mappers.*;
import models.*;
import utils.ValidationUtil;

public class Main {
    
    private final MenuMapper menuMapper;
    private final CustomerMapper customerMapper;
    private final TableMapper tableMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final Scanner scanner;

    public Main() {
        this.menuMapper = new MenuMapper();
        this.customerMapper = new CustomerMapper();
        this.tableMapper = new TableMapper();
        this.orderMapper = new OrderMapper();
        this.orderItemMapper = new OrderItemMapper();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        // Initialize Database
        DatabaseInitializer.initializeDatabase();
        DatabaseInitializer.insertSampleData();

        // Run Application
        Main app = new Main();
        app.run();

        // Close Database Connection
        DatabaseConnection.closeConnection();
    }

    public void run() {
        boolean running = true;
        
        System.out.println("\nSistem Manajemen Restoran siap digunakan!\n");
        
        while (running) {
            ValidationUtil.displayMenu();
            int choice = ValidationUtil.readInt("Pilih menu (1-5, 0=Exit): ");
            
            switch (choice) {
                case 1:
                    menuManagement();
                    break;
                case 2:
                    customerManagement();
                    break;
                case 3:
                    tableManagement();
                    break;
                case 4:
                    orderManagement();
                    break;
                case 5:
                    generateLaporan();
                    break;
                case 0:
                    running = false;
                    System.out.println("\nTerima kasih sudah menggunakan sistem ini!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private void menuManagement() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("        MENU MANAGEMENT");
            System.out.println("=".repeat(50));
            System.out.println("1. Lihat Semua Menu");
            System.out.println("2. Lihat Menu berdasarkan Kategori");
            System.out.println("3. Tambah Menu Baru");
            System.out.println("4. Update Menu");
            System.out.println("5. Hapus Menu");
            System.out.println("6. Kembali");
            System.out.println("=".repeat(50));
            
            int choice = ValidationUtil.readInt("Pilih (1-6): ");
            
            try {
                switch (choice) {
                    case 1:
                        displayAllMenus();
                        break;
                    case 2:
                        displayMenuByKategori();
                        break;
                    case 3:
                        addNewMenu();
                        break;
                    case 4:
                        updateMenu();
                        break;
                    case 5:
                        deleteMenu();
                        break;
                    case 6:
                        back = true;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private void displayAllMenus() throws SQLException {
        List<Menu> menus = menuMapper.findAll();
        if (menus.isEmpty()) {
            System.out.println("Tidak ada menu!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("DAFTAR MENU");
        System.out.println("=".repeat(80));
        System.out.printf("%-5s %-20s %-30s %-12s %-8s %-8s\n", "ID", "Nama", "Deskripsi", "Kategori", "Harga", "Stok");
        ValidationUtil.printSeparator();
        
        for (Menu menu : menus) {
            System.out.printf("%-5d %-20s %-30s %-12s %s %-8d\n", 
                menu.getId(), 
                truncateString(menu.getNama(), 20),
                truncateString(menu.getDeskripsi(), 30),
                menu.getKategori(),
                ValidationUtil.formatRupiah(menu.getHarga()),
                menu.getStok());
        }
        System.out.println("=".repeat(80));
    }

    private void displayMenuByKategori() throws SQLException {
        String kategori = ValidationUtil.readString("Masukkan kategori (Makanan/Minuman): ");
        List<Menu> menus = menuMapper.findByKategori(kategori);
        
        if (menus.isEmpty()) {
            System.out.println("Tidak ada menu dengan kategori: " + kategori);
            return;
        }
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("DAFTAR MENU - " + kategori.toUpperCase());
        System.out.println("=".repeat(80));
        System.out.printf("%-5s %-20s %-30s %-12s %-8s\n", "ID", "Nama", "Deskripsi", "Harga", "Stok");
        ValidationUtil.printSeparator();
        
        for (Menu menu : menus) {
            System.out.printf("%-5d %-20s %-30s %s %-8d\n", 
                menu.getId(), 
                truncateString(menu.getNama(), 20),
                truncateString(menu.getDeskripsi(), 30),
                ValidationUtil.formatRupiah(menu.getHarga()),
                menu.getStok());
        }
        System.out.println("=".repeat(80));
    }

    private void addNewMenu() throws SQLException {
        String nama = ValidationUtil.readString("Nama menu: ");
        String deskripsi = ValidationUtil.readString("Deskripsi: ");
        String kategori = ValidationUtil.readString("Kategori (Makanan/Minuman): ");
        double harga = ValidationUtil.readDouble("Harga: Rp. ");
        int stok = ValidationUtil.readInt("Stok: ");
        
        Menu menu = new Menu(0, nama, deskripsi, kategori, harga, stok);
        int menuId = menuMapper.insert(menu);
        
        if (menuId > 0) {
            System.out.println("Menu berhasil ditambahkan dengan ID: " + menuId);
        } else {
            System.out.println("Gagal menambahkan menu!");
        }
    }

    private void updateMenu() throws SQLException {
        int id = ValidationUtil.readInt("ID menu yang akan diupdate: ");
        Menu menu = menuMapper.findById(id);
        
        if (menu == null) {
            System.out.println("Menu tidak ditemukan!");
            return;
        }
        
        System.out.println("Menu ditemukan: " + menu.getNama());
        String nama = ValidationUtil.readString("Nama baru (tekan Enter untuk skip): ");
        if (!nama.isEmpty()) menu.setNama(nama);
        
        String deskripsi = ValidationUtil.readString("Deskripsi baru (tekan Enter untuk skip): ");
        if (!deskripsi.isEmpty()) menu.setDeskripsi(deskripsi);
        
        String kategori = ValidationUtil.readString("Kategori baru (tekan Enter untuk skip): ");
        if (!kategori.isEmpty()) menu.setKategori(kategori);
        
        System.out.print("Harga baru (tekan Enter untuk skip): ");
        String hargaStr = scanner.nextLine();
        if (!hargaStr.isEmpty()) {
            menu.setHarga(Double.parseDouble(hargaStr));
        }
        
        System.out.print("Stok baru (tekan Enter untuk skip): ");
        String stokStr = scanner.nextLine();
        if (!stokStr.isEmpty()) {
            menu.setStok(Integer.parseInt(stokStr));
        }
        
        menuMapper.update(menu);
        System.out.println("Menu berhasil diupdate!");
    }

    private void deleteMenu() throws SQLException {
        int id = ValidationUtil.readInt("ID menu yang akan dihapus: ");
        Menu menu = menuMapper.findById(id);
        
        if (menu == null) {
            System.out.println("Menu tidak ditemukan!");
            return;
        }
        
        System.out.println("Menu: " + menu.getNama());
        String confirm = ValidationUtil.readString("Yakin ingin menghapus? (y/n): ");
        
        if (confirm.equalsIgnoreCase("y")) {
            menuMapper.delete(id);
            System.out.println("Menu berhasil dihapus!");
        } else {
            System.out.println("Penghapusan dibatalkan!");
        }
    }

    private void customerManagement() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("      CUSTOMER MANAGEMENT");
            System.out.println("=".repeat(50));
            System.out.println("1. Lihat Semua Customer");
            System.out.println("2. Cari Customer");
            System.out.println("3. Tambah Customer Baru");
            System.out.println("4. Update Customer");
            System.out.println("5. Hapus Customer");
            System.out.println("6. Kembali");
            System.out.println("=".repeat(50));
            
            int choice = ValidationUtil.readInt("Pilih (1-6): ");
            
            try {
                switch (choice) {
                    case 1:
                        displayAllCustomers();
                        break;
                    case 2:
                        searchCustomer();
                        break;
                    case 3:
                        addNewCustomer();
                        break;
                    case 4:
                        updateCustomer();
                        break;
                    case 5:
                        deleteCustomer();
                        break;
                    case 6:
                        back = true;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private void displayAllCustomers() throws SQLException {
        List<Customer> customers = customerMapper.findAll();
        if (customers.isEmpty()) {
            System.out.println("Tidak ada customer!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(100));
        System.out.println("DAFTAR CUSTOMER");
        System.out.println("=".repeat(100));
        System.out.printf("%-5s %-20s %-15s %-25s %-30s\n", "ID", "Nama", "No. Telepon", "Email", "Alamat");
        ValidationUtil.printSeparator();
        
        for (Customer customer : customers) {
            System.out.printf("%-5d %-20s %-15s %-25s %-30s\n", 
                customer.getId(),
                truncateString(customer.getNama(), 20),
                truncateString(customer.getNoTelepon(), 15),
                truncateString(customer.getEmail(), 25),
                truncateString(customer.getAlamat(), 30));
        }
        System.out.println("=".repeat(100));
    }

    private void searchCustomer() throws SQLException {
        String nama = ValidationUtil.readString("Cari customer berdasarkan nama: ");
        List<Customer> customers = customerMapper.findByNama(nama);
        
        if (customers.isEmpty()) {
            System.out.println("Customer tidak ditemukan!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(100));
        System.out.println("HASIL PENCARIAN");
        System.out.println("=".repeat(100));
        System.out.printf("%-5s %-20s %-15s %-25s %-30s\n", "ID", "Nama", "No. Telepon", "Email", "Alamat");
        ValidationUtil.printSeparator();
        
        for (Customer customer : customers) {
            System.out.printf("%-5d %-20s %-15s %-25s %-30s\n", 
                customer.getId(),
                truncateString(customer.getNama(), 20),
                truncateString(customer.getNoTelepon(), 15),
                truncateString(customer.getEmail(), 25),
                truncateString(customer.getAlamat(), 30));
        }
        System.out.println("=".repeat(100));
    }

    private void addNewCustomer() throws SQLException {
        String nama = ValidationUtil.readString("Nama customer: ");
        String noTelepon = ValidationUtil.readString("No. Telepon: ");
        String email = ValidationUtil.readString("Email: ");
        String alamat = ValidationUtil.readString("Alamat: ");
        
        Customer customer = new Customer(0, nama, noTelepon, email, alamat);
        int customerId = customerMapper.insert(customer);
        
        if (customerId > 0) {
            System.out.println("Customer berhasil ditambahkan dengan ID: " + customerId);
        } else {
            System.out.println("Gagal menambahkan customer!");
        }
    }

    private void updateCustomer() throws SQLException {
        int id = ValidationUtil.readInt("ID customer yang akan diupdate: ");
        Customer customer = customerMapper.findById(id);
        
        if (customer == null) {
            System.out.println("Customer tidak ditemukan!");
            return;
        }
        
        System.out.println("Customer ditemukan: " + customer.getNama());
        
        String nama = ValidationUtil.readString("Nama baru (tekan Enter untuk skip): ");
        if (!nama.isEmpty()) customer.setNama(nama);
        
        String noTelepon = ValidationUtil.readString("No. Telepon baru (tekan Enter untuk skip): ");
        if (!noTelepon.isEmpty()) customer.setNoTelepon(noTelepon);
        
        String email = ValidationUtil.readString("Email baru (tekan Enter untuk skip): ");
        if (!email.isEmpty()) customer.setEmail(email);
        
        String alamat = ValidationUtil.readString("Alamat baru (tekan Enter untuk skip): ");
        if (!alamat.isEmpty()) customer.setAlamat(alamat);
        
        customerMapper.update(customer);
        System.out.println("Customer berhasil diupdate!");
    }

    private void deleteCustomer() throws SQLException {
        int id = ValidationUtil.readInt("ID customer yang akan dihapus: ");
        Customer customer = customerMapper.findById(id);
        
        if (customer == null) {
            System.out.println("Customer tidak ditemukan!");
            return;
        }
        
        System.out.println("Customer: " + customer.getNama());
        String confirm = ValidationUtil.readString("Yakin ingin menghapus? (y/n): ");
        
        if (confirm.equalsIgnoreCase("y")) {
            customerMapper.delete(id);
            System.out.println("Customer berhasil dihapus!");
        } else {
            System.out.println("Penghapusan dibatalkan!");
        }
    }

    private void tableManagement() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("      TABLE MANAGEMENT");
            System.out.println("=".repeat(50));
            System.out.println("1. Lihat Semua Meja");
            System.out.println("2. Lihat Meja Tersedia");
            System.out.println("3. Tambah Meja Baru");
            System.out.println("4. Update Status Meja");
            System.out.println("5. Hapus Meja");
            System.out.println("6. Kembali");
            System.out.println("=".repeat(50));
            
            int choice = ValidationUtil.readInt("Pilih (1-6): ");
            
            try {
                switch (choice) {
                    case 1:
                        displayAllTables();
                        break;
                    case 2:
                        displayAvailableTables();
                        break;
                    case 3:
                        addNewTable();
                        break;
                    case 4:
                        updateTableStatus();
                        break;
                    case 5:
                        deleteTable();
                        break;
                    case 6:
                        back = true;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private void displayAllTables() throws SQLException {
        List<Table> tables = tableMapper.findAll();
        if (tables.isEmpty()) {
            System.out.println("Tidak ada meja!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DAFTAR MEJA RESTORAN");
        System.out.println("=".repeat(60));
        System.out.printf("%-5s %-12s %-10s %-15s\n", "ID", "No. Meja", "Kapasitas", "Status");
        ValidationUtil.printSeparator();
        
        for (Table table : tables) {
            System.out.printf("%-5d %-12d %-10d %-15s\n", 
                table.getId(),
                table.getNomorMeja(),
                table.getKapasitas(),
                table.getStatus());
        }
        System.out.println("=".repeat(60));
    }

    private void displayAvailableTables() throws SQLException {
        List<Table> tables = tableMapper.findByStatus("Tersedia");
        if (tables.isEmpty()) {
            System.out.println("Tidak ada meja yang tersedia!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("MEJA TERSEDIA");
        System.out.println("=".repeat(60));
        System.out.printf("%-5s %-12s %-10s\n", "ID", "No. Meja", "Kapasitas");
        ValidationUtil.printSeparator();
        
        for (Table table : tables) {
            System.out.printf("%-5d %-12d %-10d\n", 
                table.getId(),
                table.getNomorMeja(),
                table.getKapasitas());
        }
        System.out.println("=".repeat(60));
    }

    private void addNewTable() throws SQLException {
        int nomorMeja = ValidationUtil.readInt("Nomor meja: ");
        int kapasitas = ValidationUtil.readInt("Kapasitas (jumlah orang): ");
        
        Table table = new Table(0, nomorMeja, kapasitas, "Tersedia");
        int tableId = tableMapper.insert(table);
        
        if (tableId > 0) {
            System.out.println("Meja berhasil ditambahkan dengan ID: " + tableId);
        } else {
            System.out.println("Gagal menambahkan meja!");
        }
    }

    private void updateTableStatus() throws SQLException {
        int id = ValidationUtil.readInt("ID meja: ");
        Table table = tableMapper.findById(id);
        
        if (table == null) {
            System.out.println("Meja tidak ditemukan!");
            return;
        }
        
        System.out.println("Meja No. " + table.getNomorMeja() + " - Status: " + table.getStatus());
        System.out.println("1. Tersedia");
        System.out.println("2. Terisi");
        
        int choice = ValidationUtil.readInt("Pilih status baru: ");
        if (choice == 1) {
            table.setStatus("Tersedia");
        } else if (choice == 2) {
            table.setStatus("Terisi");
        } else {
            System.out.println("Pilihan tidak valid!");
            return;
        }
        
        tableMapper.update(table);
        System.out.println("Status meja berhasil diupdate!");
    }

    private void deleteTable() throws SQLException {
        int id = ValidationUtil.readInt("ID meja yang akan dihapus: ");
        Table table = tableMapper.findById(id);
        
        if (table == null) {
            System.out.println("Meja tidak ditemukan!");
            return;
        }
        
        System.out.println("Meja No. " + table.getNomorMeja());
        String confirm = ValidationUtil.readString("Yakin ingin menghapus? (y/n): ");
        
        if (confirm.equalsIgnoreCase("y")) {
            tableMapper.delete(id);
            System.out.println("Meja berhasil dihapus!");
        } else {
            System.out.println("Penghapusan dibatalkan!");
        }
    }

    private void orderManagement() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("      ORDER MANAGEMENT");
            System.out.println("=".repeat(50));
            System.out.println("1. Buat Pesanan Baru");
            System.out.println("2. Lihat Pesanan Customer");
            System.out.println("3. Update Status Pesanan");
            System.out.println("4. Batalkan Pesanan");
            System.out.println("5. Kembali");
            System.out.println("=".repeat(50));
            
            int choice = ValidationUtil.readInt("Pilih (1-5): ");
            
            try {
                switch (choice) {
                    case 1:
                        createNewOrder();
                        break;
                    case 2:
                        viewCustomerOrders();
                        break;
                    case 3:
                        updateOrderStatus();
                        break;
                    case 4:
                        cancelOrder();
                        break;
                    case 5:
                        back = true;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private void createNewOrder() throws SQLException {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("BUAT PESANAN BARU");
        System.out.println("=".repeat(60));
        
        System.out.println("\nPILIH CUSTOMER:");
        List<Customer> customers = customerMapper.findAll();
        
        if (customers.isEmpty()) {
            System.out.println("Tidak ada customer! Silakan tambahkan customer terlebih dahulu.");
            return;
        }
        
        for (Customer c : customers) {
            System.out.println("  " + c.getId() + ". " + c.getNama() + " (" + c.getNoTelepon() + ")");
        }
        
        int customerId = ValidationUtil.readInt("ID Customer: ");
        Customer customer = customerMapper.findById(customerId);
        
        if (customer == null) {
            System.out.println("Customer tidak ditemukan!");
            return;
        }
        
        System.out.println("Customer dipilih: " + customer.getNama());
        
        System.out.println("\nPILIH MEJA:");
        List<Table> availableTables = tableMapper.findByStatus("Tersedia");
        
        if (availableTables.isEmpty()) {
            System.out.println("Tidak ada meja yang tersedia!");
            return;
        }
        
        for (Table t : availableTables) {
            System.out.println("  " + t.getId() + ". Meja No. " + t.getNomorMeja() + " (Kapasitas: " + t.getKapasitas() + " orang)");
        }
        
        int tableId = ValidationUtil.readInt("ID Meja: ");
        Table table = tableMapper.findById(tableId);
        
        if (table == null || !table.getStatus().equals("Tersedia")) {
            System.out.println("Meja tidak ditemukan atau tidak tersedia!");
            return;
        }
        
        System.out.println("Meja dipilih: Meja No. " + table.getNomorMeja());
        
        Order order = new Order(0, customerId, tableId, LocalDateTime.now(), "Pending", 0);
        int orderId = orderMapper.insert(order);
        
        if (orderId <= 0) {
            System.out.println("Gagal membuat pesanan!");
            return;
        }
        
        System.out.println("Pesanan dibuat dengan ID: " + orderId);
        
        System.out.println("\nTAMBAHKAN MENU:");
        List<Menu> allMenus = menuMapper.findAll();
        
        if (allMenus.isEmpty()) {
            System.out.println("Tidak ada menu!");
            return;
        }
        
        double totalHarga = 0;
        boolean addingItems = true;
        
        while (addingItems) {
            System.out.println("\nDAFTAR MENU:");
            for (Menu m : allMenus) {
                System.out.printf("  %d. %s - %s (Rp %.0f)\n", m.getId(), m.getNama(), m.getKategori(), m.getHarga());
            }
            
            int menuId = ValidationUtil.readInt("ID Menu (0 untuk selesai): ");
            
            if (menuId == 0) {
                break;
            }
            
            Menu selectedMenu = menuMapper.findById(menuId);
            if (selectedMenu == null) {
                System.out.println("Menu tidak ditemukan!");
                continue;
            }
            
            if (selectedMenu.getStok() <= 0) {
                System.out.println("Menu ini sudah tidak tersedia!");
                continue;
            }
            
            int kuantitas = ValidationUtil.readInt("Jumlah: ");
            if (kuantitas > selectedMenu.getStok()) {
                System.out.println("Stok tidak cukup! Stok tersedia: " + selectedMenu.getStok());
                continue;
            }
            
            // Create OrderItem
            OrderItem orderItem = new OrderItem(0, orderId, menuId, selectedMenu.getNama(), kuantitas, selectedMenu.getHarga());
            int itemId = orderItemMapper.insert(orderItem);
            
            if (itemId > 0) {
                totalHarga += orderItem.getSubtotal();
                selectedMenu.setStok(selectedMenu.getStok() - kuantitas);
                menuMapper.update(selectedMenu);
                System.out.println("Item ditambahkan: " + selectedMenu.getNama() + " x" + kuantitas + " = " + ValidationUtil.formatRupiah(orderItem.getSubtotal()));
            }
        }
        
        // 5. Update order total dan table status
        order.setTotalHarga(totalHarga);
        orderMapper.update(order);
        
        table.setStatus("Terisi");
        tableMapper.update(table);
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("RINGKASAN PESANAN");
        System.out.println("=".repeat(60));
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer: " + customer.getNama());
        System.out.println("Meja No: " + table.getNomorMeja());
        System.out.println("Waktu Pesanan: " + LocalDateTime.now());
        System.out.println("Status: " + order.getStatus());
        System.out.println("-".repeat(60));
        
        List<OrderItem> orderItems = orderItemMapper.findByOrderId(orderId);
        for (OrderItem item : orderItems) {
            System.out.printf("  %s x%d = %s\n", item.getMenuNama(), item.getKuantitas(), ValidationUtil.formatRupiah(item.getSubtotal()));
        }
        
        System.out.println("-".repeat(60));
        System.out.println("TOTAL: " + ValidationUtil.formatRupiah(totalHarga));
        System.out.println("=".repeat(60));
    }

    private void viewCustomerOrders() throws SQLException {
        int customerId = ValidationUtil.readInt("ID Customer: ");
        Customer customer = customerMapper.findById(customerId);
        
        if (customer == null) {
            System.out.println("Customer tidak ditemukan!");
            return;
        }
        
        List<Order> orders = orderMapper.findByCustomerId(customerId);
        
        if (orders.isEmpty()) {
            System.out.println("Customer ini belum memiliki pesanan!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("PESANAN - " + customer.getNama());
        System.out.println("=".repeat(80));
        
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getId() + " | Tgl: " + order.getTanggalPesanan() + " | Status: " + order.getStatus());
            System.out.println("Items:");
            
            for (OrderItem item : order.getOrderItems()) {
                System.out.printf("  - %s x%d = %s\n", item.getMenuNama(), item.getKuantitas(), ValidationUtil.formatRupiah(item.getSubtotal()));
            }
            
            System.out.println("Total: " + ValidationUtil.formatRupiah(order.getTotalHarga()));
            System.out.println("-".repeat(80));
        }
    }

    private void updateOrderStatus() throws SQLException {
        int orderId = ValidationUtil.readInt("ID Pesanan: ");
        Order order = orderMapper.findById(orderId);
        
        if (order == null) {
            System.out.println("Pesanan tidak ditemukan!");
            return;
        }
        
        System.out.println("Pesanan ID: " + orderId + " | Status saat ini: " + order.getStatus());
        System.out.println("1. Pending");
        System.out.println("2. Selesai");
        System.out.println("3. Dibatalkan");
        
        int choice = ValidationUtil.readInt("Pilih status baru: ");
        
        String newStatus = switch (choice) {
            case 1 -> "Pending";
            case 2 -> "Selesai";
            case 3 -> "Dibatalkan";
            default -> null;
        };
        
        if (newStatus == null) {
            System.out.println("Pilihan tidak valid!");
            return;
        }
        
        order.setStatus(newStatus);
        orderMapper.update(order);
        
        // Jika selesai, bebaskan meja
        if (newStatus.equals("Selesai")) {
            Table table = tableMapper.findById(order.getTableId());
            if (table != null) {
                table.setStatus("Tersedia");
                tableMapper.update(table);
            }
        }
        
        System.out.println("Status pesanan berhasil diupdate menjadi: " + newStatus);
    }

    private void cancelOrder() throws SQLException {
        int orderId = ValidationUtil.readInt("ID Pesanan: ");
        Order order = orderMapper.findById(orderId);
        
        if (order == null) {
            System.out.println("Pesanan tidak ditemukan!");
            return;
        }
        
        System.out.println("Pesanan ID: " + orderId);
        String confirm = ValidationUtil.readString("Yakin ingin membatalkan? (y/n): ");
        
        if (confirm.equalsIgnoreCase("y")) {
            order.setStatus("Dibatalkan");
            orderMapper.update(order);
            
            // Kembalikan stok menu
            for (OrderItem item : order.getOrderItems()) {
                Menu menu = menuMapper.findById(item.getMenuId());
                if (menu != null) {
                    menu.setStok(menu.getStok() + item.getKuantitas());
                    menuMapper.update(menu);
                }
            }
            
            // Bebaskan meja
            Table table = tableMapper.findById(order.getTableId());
            if (table != null) {
                table.setStatus("Tersedia");
                tableMapper.update(table);
            }
            
            System.out.println("Pesanan berhasil dibatalkan!");
        }
    }

    private void viewAllOrders(){
        try {
            List<Order> allOrders = orderMapper.findAll();
            
            if (allOrders.isEmpty()) {
                System.out.println("Tidak ada pesanan!");
                return;
            }
            
            System.out.println("\n" + "=".repeat(100));
            System.out.println("LAPORAN SEMUA PESANAN");
            System.out.println("=".repeat(100));
            System.out.printf("%-5s %-15s %-10s %-10s %-20s %-12s %-15s\n", 
                "ID", "Customer", "Tabel", "Items", "Tanggal", "Total", "Status");
            ValidationUtil.printSeparator();
            
            for (Order order : allOrders) {
                Customer customer = customerMapper.findById(order.getCustomerId());
                Table table = tableMapper.findById(order.getTableId());
                
                System.out.printf("%-5d %-15s %-10s %-10d %-20s %s %-15s\n", 
                    order.getId(),
                    truncateString(customer != null ? customer.getNama() : "N/A", 15),
                    table != null ? table.getNomorMeja() : "N/A",
                    order.getOrderItems().size(),
                    order.getTanggalPesanan().toString().substring(0, 10),
                    ValidationUtil.formatRupiah(order.getTotalHarga()),
                    order.getStatus());
            }
            
            System.out.println("=".repeat(100));
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void generateLaporan() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("           LAPORAN");
            System.out.println("=".repeat(50));
            System.out.println("1. Laporan Pesanan");
            System.out.println("2. Laporan Keuangan");
            System.out.println("3. Laporan Inventory");
            System.out.println("4. Kembali");
            System.out.println("=".repeat(50));
            
            int choice = ValidationUtil.readInt("Pilih laporan (1-4): ");
            
            try {
                switch (choice) {
                    case 1:
                        laporanPesanan();
                        break;
                    case 2:
                        laporanKeuangan();
                        break;
                    case 3:
                        laporanInventory();
                        break;
                    case 4:
                        back = true;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private void laporanPesanan() throws SQLException {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        LAPORAN PESANAN");
        System.out.println("=".repeat(50));
        System.out.println("1. Semua Pesanan");
        System.out.println("2. Filter berdasarkan Status");
        System.out.println("3. Filter berdasarkan Customer");
        System.out.println("4. Kembali");
        System.out.println("=".repeat(50));
        
        int choice = ValidationUtil.readInt("Pilih (1-4): ");
        
        switch (choice) {
            case 1:
                displayAllOrdersReport();
                break;
            case 2:
                displayOrdersByStatus();
                break;
            case 3:
                displayOrdersByCustomer();
                break;
            case 4:
                break;
            default:
                System.out.println("Pilihan tidak valid!");
        }
    }

    private void displayAllOrdersReport() throws SQLException {
        List<Order> allOrders = orderMapper.findAll();
        
        if (allOrders.isEmpty()) {
            System.out.println("Tidak ada pesanan!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(110));
        System.out.println("LAPORAN SEMUA PESANAN");
        System.out.println("=".repeat(110));
        System.out.printf("%-5s %-20s %-10s %-10s %-15s %-15s %-15s\n", 
            "ID", "Customer", "Meja", "Items", "Tanggal", "Total", "Status");
        System.out.println("-".repeat(110));
        
        for (Order order : allOrders) {
            Customer customer = customerMapper.findById(order.getCustomerId());
            Table table = tableMapper.findById(order.getTableId());
            
            System.out.printf("%-5d %-20s %-10d %-10d %-15s %s %-15s\n", 
                order.getId(),
                truncateString(customer != null ? customer.getNama() : "N/A", 20),
                table != null ? table.getNomorMeja() : 0,
                order.getOrderItems().size(),
                order.getTanggalPesanan().toString().substring(0, 10),
                ValidationUtil.formatRupiah(order.getTotalHarga()),
                order.getStatus());
        }
        System.out.println("=".repeat(110));
        System.out.println("Total Pesanan: " + allOrders.size());
    }

    private void displayOrdersByStatus() throws SQLException {
        System.out.println("Status Pesanan:");
        System.out.println("1. Pending");
        System.out.println("2. Selesai");
        System.out.println("3. Dibatalkan");
        
        int choice = ValidationUtil.readInt("Pilih status (1-3): ");
        
        String status = switch (choice) {
            case 1 -> "Pending";
            case 2 -> "Selesai";
            case 3 -> "Dibatalkan";
            default -> null;
        };
        
        if (status == null) {
            System.out.println("Pilihan tidak valid!");
            return;
        }
        
        List<Order> allOrders = orderMapper.findAll();
        List<Order> filteredOrders = allOrders.stream()
                .filter(o -> o.getStatus().equals(status))
                .toList();
        
        if (filteredOrders.isEmpty()) {
            System.out.println("Tidak ada pesanan dengan status: " + status);
            return;
        }
        
        System.out.println("\n" + "=".repeat(110));
        System.out.println("LAPORAN PESANAN - STATUS: " + status);
        System.out.println("=".repeat(110));
        System.out.printf("%-5s %-20s %-10s %-10s %-15s %-15s\n", 
            "ID", "Customer", "Meja", "Items", "Tanggal", "Total");
        System.out.println("-".repeat(110));
        
        double totalPendapatan = 0;
        for (Order order : filteredOrders) {
            Customer customer = customerMapper.findById(order.getCustomerId());
            Table table = tableMapper.findById(order.getTableId());
            
            System.out.printf("%-5d %-20s %-10d %-10d %-15s %s\n", 
                order.getId(),
                truncateString(customer != null ? customer.getNama() : "N/A", 20),
                table != null ? table.getNomorMeja() : 0,
                order.getOrderItems().size(),
                order.getTanggalPesanan().toString().substring(0, 10),
                ValidationUtil.formatRupiah(order.getTotalHarga()));
            
            if (status.equals("Selesai")) {
                totalPendapatan += order.getTotalHarga();
            }
        }
        
        System.out.println("=".repeat(110));
        System.out.println("Total Pesanan: " + filteredOrders.size());
        if (status.equals("Selesai")) {
            System.out.println("Total Pendapatan: " + ValidationUtil.formatRupiah(totalPendapatan));
        }
    }

    private void displayOrdersByCustomer() throws SQLException {
        int customerId = ValidationUtil.readInt("ID Customer: ");
        Customer customer = customerMapper.findById(customerId);
        
        if (customer == null) {
            System.out.println("Customer tidak ditemukan!");
            return;
        }
        
        List<Order> orders = orderMapper.findByCustomerId(customerId);
        
        if (orders.isEmpty()) {
            System.out.println("Customer ini belum memiliki pesanan!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(110));
        System.out.println("LAPORAN PESANAN - CUSTOMER: " + customer.getNama());
        System.out.println("=".repeat(110));
        System.out.printf("%-5s %-10s %-10s %-15s %-15s\n", 
            "ID", "Meja", "Items", "Tanggal", "Total");
        System.out.println("-".repeat(110));
        
        double totalSpending = 0;
        for (Order order : orders) {
            Table table = tableMapper.findById(order.getTableId());
            
            System.out.printf("%-5d %-10d %-10d %-15s %s\n", 
                order.getId(),
                table != null ? table.getNomorMeja() : 0,
                order.getOrderItems().size(),
                order.getTanggalPesanan().toString().substring(0, 10),
                ValidationUtil.formatRupiah(order.getTotalHarga()));
            
            totalSpending += order.getTotalHarga();
        }
        
        System.out.println("=".repeat(110));
        System.out.println("Total Pesanan: " + orders.size());
        System.out.println("Total Pengeluaran: " + ValidationUtil.formatRupiah(totalSpending));
    }

    private void laporanKeuangan() throws SQLException {
        List<Order> allOrders = orderMapper.findAll();
        List<Order> selesaiOrders = allOrders.stream()
                .filter(o -> o.getStatus().equals("Selesai"))
                .toList();
        
        double totalPendapatan = selesaiOrders.stream()
                .mapToDouble(Order::getTotalHarga)
                .sum();
        
        double rataPendapatan = selesaiOrders.isEmpty() ? 0 : totalPendapatan / selesaiOrders.size();
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("LAPORAN KEUANGAN");
        System.out.println("=".repeat(80));
        System.out.println("Total Pesanan: " + allOrders.size());
        System.out.println("Pesanan Selesai: " + selesaiOrders.size());
        System.out.println("Total Pendapatan: " + ValidationUtil.formatRupiah(totalPendapatan));
        System.out.println("Rata-rata Nilai Pesanan: " + ValidationUtil.formatRupiah(rataPendapatan));
        System.out.println();
        
        // Top Customers
        System.out.println("TOP CUSTOMERS:");
        System.out.println("-".repeat(80));
        List<Customer> topCustomers = calculateTopCustomers(allOrders);
        int rank = 1;
        for (Customer c : topCustomers) {
            double spending = allOrders.stream()
                    .filter(o -> o.getCustomerId() == c.getId() && o.getStatus().equals("Selesai"))
                    .mapToDouble(Order::getTotalHarga)
                    .sum();
            long count = allOrders.stream()
                    .filter(o -> o.getCustomerId() == c.getId())
                    .count();
            System.out.printf("%d. %-20s - %d pesanan - Total %s\n", rank++, c.getNama(), count, ValidationUtil.formatRupiah(spending));
        }
        
        System.out.println();
        
        // Most Popular Menu
        System.out.println("MENU TERPOPULER:");
        System.out.println("-".repeat(80));
        List<Menu> topMenus = calculateTopMenus(allOrders);
        rank = 1;
        for (Menu m : topMenus) {
            long count = allOrders.stream()
                    .flatMap(o -> o.getOrderItems().stream())
                    .filter(oi -> oi.getMenuId() == m.getId())
                    .count();
            System.out.printf("%d. %-30s - Dipesan %d kali\n", rank++, m.getNama(), count);
        }
        
        System.out.println("=".repeat(80));
    }

    private List<Customer> calculateTopCustomers(List<Order> allOrders) throws SQLException {
        java.util.Map<Integer, Double> customerSpending = new java.util.HashMap<>();
        
        for (Order order : allOrders) {
            if (order.getStatus().equals("Selesai")) {
                customerSpending.put(
                    order.getCustomerId(), 
                    customerSpending.getOrDefault(order.getCustomerId(), 0.0) + order.getTotalHarga()
                );
            }
        }
        
        return customerSpending.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(5)
                .map(entry -> {
                    try {
                        return customerMapper.findById(entry.getKey());
                    } catch (SQLException e) {
                        return null;
                    }
                })
                .filter(c -> c != null)
                .toList();
    }

    private List<Menu> calculateTopMenus(List<Order> allOrders) throws SQLException {
        java.util.Map<Integer, Integer> menuCount = new java.util.HashMap<>();
        
        for (Order order : allOrders) {
            for (OrderItem item : order.getOrderItems()) {
                menuCount.put(item.getMenuId(), menuCount.getOrDefault(item.getMenuId(), 0) + item.getKuantitas());
            }
        }
        
        return menuCount.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(entry -> {
                    try {
                        return menuMapper.findById(entry.getKey());
                    } catch (SQLException e) {
                        return null;
                    }
                })
                .filter(m -> m != null)
                .toList();
    }

    private void laporanInventory() throws SQLException {
        List<Menu> allMenus = menuMapper.findAll();
        
        if (allMenus.isEmpty()) {
            System.out.println("Tidak ada menu!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(100));
        System.out.println("LAPORAN INVENTORY");
        System.out.println("=".repeat(100));
        System.out.printf("%-5s %-30s %-15s %-12s %-15s\n", "ID", "Menu", "Kategori", "Stok", "Status");
        System.out.println("-".repeat(100));
        
        int stokHabis = 0;
        int stokMenipis = 0;
        
        for (Menu menu : allMenus) {
            String status;
            if (menu.getStok() == 0) {
                status = "🔴 HABIS";
                stokHabis++;
            } else if (menu.getStok() < 10) {
                status = "⚠️  Stok Menipis";
                stokMenipis++;
            } else {
                status = "✓ OK";
            }
            
            System.out.printf("%-5d %-30s %-15s %-12d %s\n", 
                menu.getId(),
                truncateString(menu.getNama(), 30),
                menu.getKategori(),
                menu.getStok(),
                status);
        }
        
        System.out.println("=".repeat(100));
        System.out.println("Ringkasan Inventory:");
        System.out.println("  Total Menu: " + allMenus.size());
        System.out.println("  Stok OK: " + (allMenus.size() - stokHabis - stokMenipis));
        System.out.println("  Stok Menipis (< 10): " + stokMenipis);
        System.out.println("  Stok Habis (0): " + stokHabis);
        System.out.println("=".repeat(100));
        
        if (stokHabis > 0 || stokMenipis > 0) {
            System.out.println("\n⚠️  PERINGATAN: Ada " + (stokHabis + stokMenipis) + " menu yang perlu perhatian!");
        }
    }

    private String truncateString(String str, int length) {
        if (str == null) return "";
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }
}
