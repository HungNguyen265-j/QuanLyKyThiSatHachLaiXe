package com.mycompany.quanlythilai.model;

import java.time.LocalDate;
import static com.mycompany.quanlythilai.model.Enums.HoSoStatus;

public final class NguoiThi extends Person {
    private final String citizenId;
    private String address;
    private String licenseClass;
    private long registrationFee;
    private HoSoStatus profileStatus = HoSoStatus.CHUA_DUYET;

    public NguoiThi(String id, String fullName, LocalDate birthDate, String citizenId, String address,
                    String phone, String licenseClass, long registrationFee) {
        super(id, fullName, birthDate, phone);
        this.citizenId = requireText(citizenId, "Số CCCD");
        setAddress(address);
        setLicenseClass(licenseClass);
        setRegistrationFee(registrationFee);
    }
    public String getCitizenId() { return citizenId; }
    public String getAddress() { return address; }
    public String getLicenseClass() { return licenseClass; }
    public long getRegistrationFee() { return registrationFee; }
    public HoSoStatus getProfileStatus() { return profileStatus; }
    public void setAddress(String value) { address = requireText(value, "Địa chỉ"); }
    public void setLicenseClass(String value) { licenseClass = requireText(value, "Hạng bằng"); }
    public void setRegistrationFee(long value) { if (value < 0) throw new IllegalArgumentException("Phí không hợp lệ"); registrationFee = value; }
    public void setProfileStatus(HoSoStatus value) { profileStatus = value; }
}
