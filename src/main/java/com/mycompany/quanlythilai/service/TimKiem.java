package com.mycompany.quanlythilai.service;

import com.mycompany.quanlythilai.model.*;
import java.time.LocalDate;
import java.util.List;

public final class TimKiem {
    private TimKiem() { }
    public static List<NguoiThi> nguoiThi(QuanLyKyThi manager, String keyword) { return manager.searchCandidates(keyword); }
    public static List<KyThi> kyThi(QuanLyKyThi manager, String keyword) { return manager.searchExams(keyword); }
    public static List<KyThi> kyThiTheoNgay(QuanLyKyThi manager, LocalDate date) { return manager.examsOn(date); }
}
