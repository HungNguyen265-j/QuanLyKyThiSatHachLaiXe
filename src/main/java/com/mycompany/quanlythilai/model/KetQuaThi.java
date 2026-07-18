package com.mycompany.quanlythilai.model;

import java.time.LocalDate;
import static com.mycompany.quanlythilai.model.Enums.ResultStatus;

public final class KetQuaThi {
    private final String candidateId;
    private final String examId;
    private final double theoryScore;
    private final double simulationScore;
    private final double practiceScore;
    private final LocalDate issuedDate;

    public KetQuaThi(String candidateId, String examId, double theoryScore, double simulationScore,
                     double practiceScore, LocalDate issuedDate) {
        this.candidateId = Person.requireText(candidateId, "Mã người thi");
        this.examId = Person.requireText(examId, "Mã kỳ thi");
        validateScore(theoryScore); validateScore(simulationScore); validateScore(practiceScore);
        this.theoryScore = theoryScore; this.simulationScore = simulationScore; this.practiceScore = practiceScore;
        this.issuedDate = java.util.Objects.requireNonNull(issuedDate, "Ngày cấp không được trống");
    }
    private static void validateScore(double value) { if (value < 0 || value > 10) throw new IllegalArgumentException("Điểm phải từ 0 đến 10"); }
    public String getCandidateId() { return candidateId; }
    public String getExamId() { return examId; }
    public double getTheoryScore() { return theoryScore; }
    public double getSimulationScore() { return simulationScore; }
    public double getPracticeScore() { return practiceScore; }
    public LocalDate getIssuedDate() { return issuedDate; }
    public ResultStatus getStatus() { return theoryScore >= 5 && simulationScore >= 5 && practiceScore >= 5 ? ResultStatus.DAT : ResultStatus.KHONG_DAT; }
}
