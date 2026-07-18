package com.mycompany.quanlythilai.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.mycompany.quanlythilai.model.Enums.ExamStatus;

public final class KyThi {
    private final String id;
    private String name;
    private String licenseClass;
    private String location;
    private LocalDate examDate;
    private String chiefInvigilatorId;
    private ExamStatus status = ExamStatus.CHUA_TO_CHUC;
    private final List<String> candidateIds = new ArrayList<>();

    public KyThi(String id, String name, String licenseClass, String location, LocalDate examDate, String chiefInvigilatorId) {
        this.id = Person.requireText(id, "Mã kỳ thi");
        setName(name); setLicenseClass(licenseClass); setLocation(location); setExamDate(examDate);
        this.chiefInvigilatorId = Person.requireText(chiefInvigilatorId, "Mã giám thị");
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getLicenseClass() { return licenseClass; }
    public String getLocation() { return location; }
    public LocalDate getExamDate() { return examDate; }
    public String getChiefInvigilatorId() { return chiefInvigilatorId; }
    public ExamStatus getStatus() { return status; }
    public int getCandidateCount() { return candidateIds.size(); }
    public List<String> getCandidateIds() { return Collections.unmodifiableList(candidateIds); }
    public void setName(String value) { name = Person.requireText(value, "Tên kỳ thi"); }
    public void setLicenseClass(String value) { licenseClass = Person.requireText(value, "Hạng bằng"); }
    public void setLocation(String value) { location = Person.requireText(value, "Địa điểm"); }
    public void setExamDate(LocalDate value) { examDate = java.util.Objects.requireNonNull(value, "Ngày thi không được trống"); }
    public void setChiefInvigilatorId(String value) { chiefInvigilatorId = Person.requireText(value, "Mã giám thị"); }
    public void setStatus(ExamStatus value) { status = java.util.Objects.requireNonNull(value); }
    public void addCandidate(String candidateId) { if (!candidateIds.contains(candidateId)) candidateIds.add(candidateId); }
    public void removeCandidate(String candidateId) { candidateIds.remove(candidateId); }
}
