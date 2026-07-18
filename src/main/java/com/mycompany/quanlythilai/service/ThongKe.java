package com.mycompany.quanlythilai.service;

public final class ThongKe {
    private ThongKe() { }
    public static long soNguoiThi(QuanLyKyThi manager) { return manager.getCandidates().size(); }
    public static long soNguoiDat(QuanLyKyThi manager) { return manager.passedCount(); }
    public static long soNguoiKhongDat(QuanLyKyThi manager) { return manager.failedCount(); }
    public static double tyLeDau(QuanLyKyThi manager) {
        long total = soNguoiDat(manager) + soNguoiKhongDat(manager);
        return total == 0 ? 0 : (double) soNguoiDat(manager) / total * 100;
    }
    public static long doanhThu(QuanLyKyThi manager) { return manager.totalCollected(); }
}
