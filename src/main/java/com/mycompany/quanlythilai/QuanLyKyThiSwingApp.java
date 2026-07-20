package com.mycompany.quanlythilai;

import com.mycompany.quanlythilai.model.*;
import com.mycompany.quanlythilai.service.QuanLyKyThi;
import com.mycompany.quanlythilai.service.ThongKe;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;

/** Main desktop UI for the driving-license examination system. */
public final class QuanLyKyThiSwingApp extends JFrame {
    private final QuanLyKyThi manager = new QuanLyKyThi();
    private final DefaultTableModel candidateModel = new DefaultTableModel(new String[]{"Mã", "Họ tên", "Ngày sinh", "CCCD", "Hạng bằng", "Trạng thái hồ sơ"}, 0);
    private final DefaultTableModel examModel = new DefaultTableModel(new String[]{"Mã kỳ thi", "Tên kỳ thi", "Hạng", "Địa điểm", "Ngày thi", "Số người", "Trạng thái"}, 0);
    private final DefaultTableModel invigilatorModel = new DefaultTableModel(new String[]{"Mã", "Họ tên", "Ngày sinh", "Điện thoại", "Chức vụ"}, 0);
    private final JLabel statisticLabel = new JLabel();

    public QuanLyKyThiSwingApp() {
        setTitle("Quản lý các kỳ thi sát hạch lái xe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JLabel heading = new JLabel("QUẢN LÝ CÁC KỲ THI SÁT HẠCH LÁI XE", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setBorder(BorderFactory.createEmptyBorder(14, 5, 14, 5));
        add(heading, BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Người thi", candidatePanel());
        tabs.addTab("Kỳ thi và lịch thi", examPanel());
        tabs.addTab("Giám thị", invigilatorPanel());
        tabs.addTab("Thống kê", statisticPanel());
        add(tabs, BorderLayout.CENTER);
        loadDemoData();
    }

    private JPanel candidatePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(2, 6, 6, 6));
        JTextField id = field("Mã người thi"), name = field("Họ tên"), citizen = field("CCCD"), phone = field("Điện thoại");
        JDateChooser birth = dateField("Ngày sinh");
        JComboBox<String> license = licenseBox();
        for (JComponent input : new JComponent[]{id, name, birth, citizen, phone, license}) form.add(input);
        JButton add = new JButton("Thêm người thi");
        add.addActionListener(e -> run(() -> {
            NguoiThi candidate = new NguoiThi(id.getText(), name.getText(), toLocalDate(birth), citizen.getText(), "Chưa cập nhật", phone.getText(), (String) license.getSelectedItem(), 0);
            manager.addCandidate(candidate);
            candidateModel.addRow(new Object[]{candidate.getId(), candidate.getFullName(), candidate.getBirthDate(), candidate.getCitizenId(), candidate.getLicenseClass(), candidate.getProfileStatus()});
            clear(id, name, citizen, phone);
        }));
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(new JTable(candidateModel)), BorderLayout.CENTER);
        panel.add(add, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel examPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(2, 6, 6, 6));
        JTextField id = field("Mã kỳ thi"), name = field("Tên kỳ thi"), location = field("Địa điểm"), date = field("Ngày thi yyyy-MM-dd"), invigilator = field("Mã giám thị");
        JComboBox<String> license = licenseBox();
        for (JComponent input : new JComponent[]{id, name, license, location, date, invigilator}) form.add(input);
        JButton add = new JButton("Thêm kỳ thi");
        add.addActionListener(e -> run(() -> {
            KyThi exam = new KyThi(id.getText(), name.getText(), (String) license.getSelectedItem(), location.getText(), LocalDate.parse(date.getText()), invigilator.getText());
            manager.addExam(exam);
            examModel.addRow(new Object[]{exam.getId(), exam.getName(), exam.getLicenseClass(), exam.getLocation(), exam.getExamDate(), 0, exam.getStatus()});
            clear(id, name, location, date, invigilator);
        }));
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(new JTable(examModel)), BorderLayout.CENTER);
        panel.add(add, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel invigilatorPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(2, 5, 6, 6));
        JTextField id = field("Mã giám thị"), name = field("Họ tên"), phone = field("Điện thoại"), position = field("Chức vụ");
        JDateChooser birth = dateField("Ngày sinh");
        for (JComponent input : new JComponent[]{id, name, birth, phone, position}) form.add(input);
        JButton add = new JButton("Thêm giám thị");
        add.addActionListener(e -> run(() -> {
            GiamThi invigilator = new GiamThi(id.getText(), name.getText(), toLocalDate(birth), phone.getText(), position.getText());
            manager.addInvigilator(invigilator);
            invigilatorModel.addRow(new Object[]{invigilator.getId(), invigilator.getFullName(), invigilator.getBirthDate(), invigilator.getPhone(), invigilator.getPosition()});
            clear(id, name, phone, position);
        }));
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(new JTable(invigilatorModel)), BorderLayout.CENTER);
        panel.add(add, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel statisticPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        statisticLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statisticLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JButton refresh = new JButton("Cập nhật thống kê");
        refresh.addActionListener(e -> updateStatistics());
        panel.add(statisticLabel, BorderLayout.CENTER);
        panel.add(refresh, BorderLayout.SOUTH);
        updateStatistics();
        return panel;
    }

    private void updateStatistics() {
        statisticLabel.setText(String.format("Người thi: %d | Đạt: %d | Không đạt: %d | Tỷ lệ đậu: %.1f%% | Doanh thu: %d VNĐ", ThongKe.soNguoiThi(manager), ThongKe.soNguoiDat(manager), ThongKe.soNguoiKhongDat(manager), ThongKe.tyLeDau(manager), ThongKe.doanhThu(manager)));
    }

    private void loadDemoData() {
        GiamThi invigilator1 = new GiamThi("GT001", "Nguyễn Văn Minh", LocalDate.of(1985, 3, 12), "0901000001", "Giám thị trưởng");
        GiamThi invigilator2 = new GiamThi("GT002", "Trần Thị Lan", LocalDate.of(1990, 8, 25), "0901000002", "Giám thị");
        manager.addInvigilator(invigilator1);
        manager.addInvigilator(invigilator2);
        invigilatorModel.addRow(new Object[]{invigilator1.getId(), invigilator1.getFullName(), invigilator1.getBirthDate(), invigilator1.getPhone(), invigilator1.getPosition()});
        invigilatorModel.addRow(new Object[]{invigilator2.getId(), invigilator2.getFullName(), invigilator2.getBirthDate(), invigilator2.getPhone(), invigilator2.getPosition()});

        NguoiThi candidate1 = new NguoiThi("NT001", "Lê Hoàng Nam", LocalDate.of(2000, 4, 10), "001200000001", "Hà Nội", "0912000001", "B2", 450000);
        NguoiThi candidate2 = new NguoiThi("NT002", "Phạm Thu Hà", LocalDate.of(1998, 11, 2), "001200000002", "Hà Nội", "0912000002", "B2", 450000);
        NguoiThi candidate3 = new NguoiThi("NT003", "Đỗ Quang Huy", LocalDate.of(1995, 6, 18), "001200000003", "Bắc Ninh", "0912000003", "C", 500000);
        manager.addCandidate(candidate1); manager.addCandidate(candidate2); manager.addCandidate(candidate3);
        for (NguoiThi candidate : new NguoiThi[]{candidate1, candidate2, candidate3}) {
            candidateModel.addRow(new Object[]{candidate.getId(), candidate.getFullName(), candidate.getBirthDate(), candidate.getCitizenId(), candidate.getLicenseClass(), candidate.getProfileStatus()});
        }

        KyThi exam1 = new KyThi("KT001", "Sát hạch lái xe tháng 7", "B2", "Trung tâm Hà Nội", LocalDate.of(2026, 7, 25), invigilator1.getId());
        KyThi exam2 = new KyThi("KT002", "Sát hạch lái xe hạng C", "C", "Trung tâm Hà Nội", LocalDate.of(2026, 8, 5), invigilator2.getId());
        manager.addExam(exam1); manager.addExam(exam2);
        manager.addCandidateToExam(exam1.getId(), candidate1.getId()); manager.addCandidateToExam(exam1.getId(), candidate2.getId()); manager.addCandidateToExam(exam2.getId(), candidate3.getId());
        for (KyThi exam : new KyThi[]{exam1, exam2}) {
            examModel.addRow(new Object[]{exam.getId(), exam.getName(), exam.getLicenseClass(), exam.getLocation(), exam.getExamDate(), exam.getCandidateCount(), exam.getStatus()});
        }

        PhiDangKy fee1 = new PhiDangKy(candidate1.getId(), candidate1.getLicenseClass(), candidate1.getRegistrationFee());
        PhiDangKy fee2 = new PhiDangKy(candidate2.getId(), candidate2.getLicenseClass(), candidate2.getRegistrationFee());
        fee1.pay(); fee2.pay(); manager.addFee(fee1); manager.addFee(fee2);
        manager.addResult(new KetQuaThi(candidate1.getId(), exam1.getId(), 8.5, 8.0, 9.0, LocalDate.of(2026, 7, 25)));
        manager.addResult(new KetQuaThi(candidate2.getId(), exam1.getId(), 6.0, 4.0, 7.0, LocalDate.of(2026, 7, 25)));
        updateStatistics();
    }

    private static JTextField field(String tooltip) { JTextField field = new JTextField(); field.setToolTipText(tooltip); field.setBorder(BorderFactory.createTitledBorder(tooltip)); return field; }
    private static JComboBox<String> licenseBox() {
        JComboBox<String> box = new JComboBox<>(new String[]{"A1", "A2", "A", "B1", "B2", "C", "D", "E", "F"});
        box.setBorder(BorderFactory.createTitledBorder("Hạng bằng"));
        return box;
    }
    private static JDateChooser dateField(String title) {
        JDateChooser chooser = new JDateChooser();
        chooser.setDateFormatString("dd/MM/yyyy");
        chooser.setBorder(BorderFactory.createTitledBorder(title));
        chooser.setToolTipText("Bấm biểu tượng lịch để chọn ngày");
        return chooser;
    }
    private static LocalDate toLocalDate(JDateChooser chooser) {
        if (chooser.getDate() == null) throw new IllegalArgumentException("Ngày sinh không được trống");
        return chooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    private static void clear(JTextField... fields) { for (JTextField field : fields) field.setText(""); }
    private static void run(Runnable action) { try { action.run(); } catch (RuntimeException ex) { JOptionPane.showMessageDialog(null, ex.getMessage(), "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE); } }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new QuanLyKyThiSwingApp().setVisible(true)); }
}
