package com.mycompany.quanlythilai.service;

import com.mycompany.quanlythilai.model.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/** Central application service. Collections are private to preserve encapsulation. */
public final class QuanLyKyThi {
    private final Map<String, NguoiThi> candidates = new LinkedHashMap<>();
    private final Map<String, GiamThi> invigilators = new LinkedHashMap<>();
    private final Map<String, KyThi> exams = new LinkedHashMap<>();
    private final Map<String, KetQuaThi> results = new LinkedHashMap<>();
    private final Map<String, PhiDangKy> fees = new LinkedHashMap<>();

    public void addCandidate(NguoiThi candidate) { putNew(candidates, candidate.getId(), candidate, "người thi"); }
    public void addInvigilator(GiamThi invigilator) { putNew(invigilators, invigilator.getId(), invigilator, "giám thị"); }
    public void updateCandidate(NguoiThi candidate) { replace(candidates, candidate.getId(), candidate, "người thi"); }
    public void updateInvigilator(GiamThi invigilator) { replace(invigilators, invigilator.getId(), invigilator, "giám thị"); }
    public void removeCandidate(String id) { candidates.remove(id); fees.remove(id); }
    public void removeInvigilator(String id) { invigilators.remove(id); }
    public void addExam(KyThi exam) {
        if (exams.values().stream().anyMatch(e -> e.getExamDate().equals(exam.getExamDate()) && e.getLocation().equalsIgnoreCase(exam.getLocation())))
            throw new IllegalArgumentException("Trùng lịch thi tại cùng địa điểm");
        requireExists(invigilators, exam.getChiefInvigilatorId(), "giám thị");
        putNew(exams, exam.getId(), exam, "kỳ thi");
    }
    public void updateExam(KyThi exam) { if (!exams.containsKey(exam.getId())) throw new NoSuchElementException("Không tìm thấy kỳ thi"); exams.put(exam.getId(), exam); }
    public void removeExam(String id) { exams.remove(id); }
    public void addCandidateToExam(String examId, String candidateId) { requireExists(exams, examId, "kỳ thi"); requireExists(candidates, candidateId, "người thi"); exams.get(examId).addCandidate(candidateId); }
    public void addResult(KetQuaThi result) { requireExists(exams, result.getExamId(), "kỳ thi"); requireExists(candidates, result.getCandidateId(), "người thi"); results.put(result.getCandidateId() + "@" + result.getExamId(), result); }
    public void addFee(PhiDangKy fee) { requireExists(candidates, fee.getCandidateId(), "người thi"); fees.put(fee.getCandidateId(), fee); }
    public void payFee(String candidateId) { requireExists(fees, candidateId, "khoản phí"); fees.get(candidateId).pay(); }
    public NguoiThi findCandidate(String id) { return candidates.get(id); }
    public List<NguoiThi> searchCandidates(String keyword) { return candidates.values().stream().filter(c -> contains(c.getId(), keyword) || contains(c.getFullName(), keyword) || contains(c.getCitizenId(), keyword)).toList(); }
    public List<KyThi> searchExams(String keyword) { return exams.values().stream().filter(e -> contains(e.getId(), keyword) || contains(e.getLocation(), keyword) || e.getExamDate().toString().contains(keyword)).toList(); }
    public List<GiamThi> searchInvigilators(String keyword) { return invigilators.values().stream().filter(i -> contains(i.getId(), keyword) || contains(i.getFullName(), keyword)).toList(); }
    public List<KyThi> examsOn(LocalDate date) { return exams.values().stream().filter(e -> e.getExamDate().equals(date)).toList(); }
    public Collection<KyThi> getExams() { return Collections.unmodifiableCollection(exams.values()); }
    public Collection<NguoiThi> getCandidates() { return Collections.unmodifiableCollection(candidates.values()); }
    public long totalCollected() { return fees.values().stream().filter(f -> f.getStatus() == Enums.PaymentStatus.DA_DONG).mapToLong(PhiDangKy::getAmount).sum(); }
    public long passedCount() { return results.values().stream().filter(r -> r.getStatus() == Enums.ResultStatus.DAT).count(); }
    public long failedCount() { return results.values().stream().filter(r -> r.getStatus() == Enums.ResultStatus.KHONG_DAT).count(); }

    private static <T> void putNew(Map<String, T> map, String id, T value, String label) { if (map.putIfAbsent(id, value) != null) throw new IllegalArgumentException("Trùng mã " + label); }
    private static <T> void replace(Map<String, T> map, String id, T value, String label) { if (!map.containsKey(id)) throw new NoSuchElementException("Không tìm thấy " + label + ": " + id); map.put(id, value); }
    private static <T> void requireExists(Map<String, T> map, String id, String label) { if (!map.containsKey(id)) throw new NoSuchElementException("Không tìm thấy " + label + ": " + id); }
    private static boolean contains(String source, String keyword) { return source.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT)); }
}
