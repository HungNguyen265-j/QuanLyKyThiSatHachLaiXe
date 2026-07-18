package com.mycompany.quanlythilai;

import com.mycompany.quanlythilai.model.*;
import com.mycompany.quanlythilai.service.QuanLyKyThi;
import java.time.LocalDate;
import java.util.Scanner;

/** Minimal console menu for demonstrating the designed use cases. */
public final class QuanLyKyThiApp {
    private QuanLyKyThiApp() { }
    public static void main(String[] args) {
        QuanLyKyThi manager = new QuanLyKyThi();
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\n=== QUẢN LÝ KỲ THI ===");
                System.out.println("1. Thêm người thi | 2. Tìm người thi | 3. Xem lịch theo ngày");
                System.out.println("4. Thống kê | 0. Thoát");
                String choice = scanner.nextLine().trim();
                try {
                    switch (choice) {
                        case "1" -> addCandidate(scanner, manager);
                        case "2" -> manager.searchCandidates(prompt(scanner, "Từ khóa: ")).forEach(c -> System.out.println(c.getId() + " - " + c.getFullName()));
                        case "3" -> manager.examsOn(LocalDate.parse(prompt(scanner, "Ngày (yyyy-MM-dd): "))).forEach(e -> System.out.println(e.getId() + " - " + e.getName() + " - " + e.getLocation()));
                        case "4" -> System.out.printf("Người thi: %d, Đạt: %d, Không đạt: %d, Đã thu: %d%n", manager.getCandidates().size(), manager.passedCount(), manager.failedCount(), manager.totalCollected());
                        case "0" -> running = false;
                        default -> System.out.println("Lựa chọn không hợp lệ");
                    }
                } catch (RuntimeException ex) { System.out.println("Lỗi: " + ex.getMessage()); }
            }
        }
    }
    private static void addCandidate(Scanner scanner, QuanLyKyThi manager) {
        String id = prompt(scanner, "Mã người thi: ");
        manager.addCandidate(new NguoiThi(id, prompt(scanner, "Họ tên: "), LocalDate.parse(prompt(scanner, "Ngày sinh (yyyy-MM-dd): ")), prompt(scanner, "CCCD: "), prompt(scanner, "Địa chỉ: "), prompt(scanner, "Điện thoại: "), prompt(scanner, "Hạng bằng: "), Long.parseLong(prompt(scanner, "Phí: "))));
        System.out.println("Đã thêm người thi.");
    }
    private static String prompt(Scanner scanner, String label) { System.out.print(label); return scanner.nextLine().trim(); }
}
