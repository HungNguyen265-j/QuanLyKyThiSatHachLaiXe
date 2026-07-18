package com.mycompany.quanlythilai.model;

import java.time.LocalDateTime;
import static com.mycompany.quanlythilai.model.Enums.PaymentStatus;

public final class PhiDangKy {
    private final String candidateId;
    private final String licenseClass;
    private final long amount;
    private PaymentStatus status;
    private LocalDateTime paidAt;

    public PhiDangKy(String candidateId, String licenseClass, long amount) {
        this.candidateId = Person.requireText(candidateId, "Mã người thi");
        this.licenseClass = Person.requireText(licenseClass, "Hạng bằng");
        if (amount < 0) throw new IllegalArgumentException("Mức phí không hợp lệ");
        this.amount = amount;
        this.status = PaymentStatus.CHUA_DONG;
    }
    public String getCandidateId() { return candidateId; }
    public String getLicenseClass() { return licenseClass; }
    public long getAmount() { return amount; }
    public PaymentStatus getStatus() { return status; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public void pay() { status = PaymentStatus.DA_DONG; paidAt = LocalDateTime.now(); }
}
