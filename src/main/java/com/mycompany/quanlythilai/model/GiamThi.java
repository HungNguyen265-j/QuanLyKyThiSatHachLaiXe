package com.mycompany.quanlythilai.model;

import java.time.LocalDate;

public final class GiamThi extends Person {
    private String position;
    private String assignedExamId;

    public GiamThi(String id, String fullName, LocalDate birthDate, String phone, String position) {
        super(id, fullName, birthDate, phone);
        setPosition(position);
    }

    public String getPosition() { return position; }
    public String getAssignedExamId() { return assignedExamId; }
    public void setPosition(String value) { position = requireText(value, "Chức vụ"); }
    public void assignTo(String examId) { assignedExamId = requireText(examId, "Mã kỳ thi"); }
}
