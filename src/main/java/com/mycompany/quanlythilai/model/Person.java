package com.mycompany.quanlythilai.model;

import java.time.LocalDate;
import java.util.Objects;

/** Common state for candidates and invigilators. */
public abstract class Person {
    private final String id;
    private String fullName;
    private LocalDate birthDate;
    private String phone;

    protected Person(String id, String fullName, LocalDate birthDate, String phone) {
        this.id = requireText(id, "Mã");
        setFullName(fullName);
        setBirthDate(birthDate);
        setPhone(phone);
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getPhone() { return phone; }
    public final void setFullName(String value) { fullName = requireText(value, "Họ tên"); }
    public final void setBirthDate(LocalDate value) { birthDate = Objects.requireNonNull(value, "Ngày sinh không được trống"); }
    public final void setPhone(String value) { phone = requireText(value, "Số điện thoại"); }

    public static String requireText(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " không được trống");
        return value.trim();
    }
}
