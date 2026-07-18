package com.mycompany.quanlythilai.model;

import java.time.LocalDate;

/** Value object for a scheduled exam, kept separate for future time-slot support. */
public record LichThi(String examId, LocalDate date, String location) {
    public LichThi {
        Person.requireText(examId, "Mã kỳ thi");
        if (date == null) throw new IllegalArgumentException("Ngày thi không được trống");
        Person.requireText(location, "Địa điểm");
    }
}
