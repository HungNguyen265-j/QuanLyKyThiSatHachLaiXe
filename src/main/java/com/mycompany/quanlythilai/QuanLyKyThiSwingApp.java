package com.mycompany.quanlythilai;

import com.mycompany.quanlythilai.model.*;
import com.mycompany.quanlythilai.service.QuanLyKyThi;
import com.mycompany.quanlythilai.service.ThongKe;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.time.LocalDate;
import java.time.ZoneId;

/** Main desktop UI for the driving-license examination system. */
public final class QuanLyKyThiSwingApp extends JFrame {
    private static final Color NAVY = new Color(20, 42, 74);
    private static final Color BLUE = new Color(36, 112, 190);
    private static final Color LIGHT = new Color(244, 247, 251);
    private final QuanLyKyThi manager = new QuanLyKyThi();
    private final DefaultTableModel candidateModel = new DefaultTableModel(new String[]{"Mã", "Họ tên", "Ngày sinh", "CCCD", "Hạng bằng", "Trạng thái hồ sơ"}, 0);
    private final DefaultTableModel examModel = new DefaultTableModel(new String[]{"Mã kỳ thi", "Tên kỳ thi", "Hạng", "Địa điểm", "Ngày thi", "Số người", "Trạng thái"}, 0);
    private final DefaultTableModel invigilatorModel = new DefaultTableModel(new String[]{"Mã", "Họ tên", "Ngày sinh", "Điện thoại", "Chức vụ"}, 0);
    private final JLabel statisticLabel = new JLabel();
    private final StatisticsChart statisticsChart = new StatisticsChart();

    public QuanLyKyThiSwingApp() {
        setTitle("Quản lý các kỳ thi sát hạch lái xe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT);
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        JLabel heading = new JLabel("QUẢN LÝ CÁC KỲ THI SÁT HẠCH LÁI XE");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel subtitle = new JLabel("Quản lý hồ sơ, lịch thi, kết quả và lệ phí đăng ký");
        subtitle.setForeground(new Color(205, 220, 238));
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.add(heading); titles.add(Box.createVerticalStrut(4)); titles.add(subtitle);
        header.add(titles, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 14));
        tabs.setBackground(LIGHT);
        tabs.setForeground(NAVY);
        tabs.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        tabs.addTab("Người thi", candidatePanel());
        tabs.addTab("Kỳ thi và lịch thi", examPanel());
        tabs.addTab("Giám thị", invigilatorPanel());
        tabs.addTab("Thống kê", statisticPanel());
        add(tabs, BorderLayout.CENTER);
        loadDemoData();
    }

    private JPanel candidatePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(LIGHT);
        JPanel form = new JPanel(new GridLayout(2, 6, 6, 6));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JTextField id = field("Mã người thi"), name = field("Họ tên"), citizen = field("CCCD"), phone = field("Điện thoại");
        id.setEditable(false);
        id.setText("Tự động");
        JDateChooser birth = dateField("Ngày sinh");
        JComboBox<String> license = licenseBox();
        for (JComponent input : new JComponent[]{id, name, birth, citizen, phone, license}) form.add(input);
        JButton add = new JButton("Thêm người thi");
        styleButton(add);
        add.addActionListener(e -> run(() -> {
            id.setText(manager.nextCandidateId());
            NguoiThi candidate = new NguoiThi(id.getText(), name.getText(), toLocalDate(birth), citizen.getText(), "Chưa cập nhật", phone.getText(), (String) license.getSelectedItem(), 0);
            manager.addCandidate(candidate);
            candidateModel.addRow(new Object[]{candidate.getId(), candidate.getFullName(), candidate.getBirthDate(), candidate.getCitizenId(), candidate.getLicenseClass(), candidate.getProfileStatus()});
            clear(id, name, citizen, phone);
            id.setText("Tự động");
        }));
        panel.add(form, BorderLayout.NORTH);
        JTable table = new JTable(candidateModel);
        styleTable(table);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(add, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel examPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(LIGHT);
        JPanel form = new JPanel(new GridLayout(2, 6, 6, 6));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JTextField id = field("Mã kỳ thi"), name = field("Tên kỳ thi"), location = field("Địa điểm"), invigilator = field("Mã giám thị");
        JDateChooser date = dateField("Ngày thi");
        JComboBox<String> license = licenseBox();
        for (JComponent input : new JComponent[]{id, name, license, location, date, invigilator}) form.add(input);
        JButton add = new JButton("Thêm kỳ thi");
        styleButton(add);
        add.addActionListener(e -> run(() -> {
            KyThi exam = new KyThi(id.getText(), name.getText(), (String) license.getSelectedItem(), location.getText(), toLocalDate(date), invigilator.getText());
            manager.addExam(exam);
            examModel.addRow(new Object[]{exam.getId(), exam.getName(), exam.getLicenseClass(), exam.getLocation(), exam.getExamDate(), 0, exam.getStatus()});
            clear(id, name, location, invigilator);
        }));
        panel.add(form, BorderLayout.NORTH);
        JTable table = new JTable(examModel);
        styleTable(table);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(add, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel invigilatorPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(LIGHT);
        JPanel form = new JPanel(new GridLayout(2, 5, 6, 6));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JTextField id = field("Mã giám thị"), name = field("Họ tên"), phone = field("Điện thoại"), position = field("Chức vụ");
        id.setEditable(false);
        id.setText("Tự động");
        JDateChooser birth = dateField("Ngày sinh");
        for (JComponent input : new JComponent[]{id, name, birth, phone, position}) form.add(input);
        JButton add = new JButton("Thêm giám thị");
        styleButton(add);
        add.addActionListener(e -> run(() -> {
            id.setText(manager.nextInvigilatorId());
            GiamThi invigilator = new GiamThi(id.getText(), name.getText(), toLocalDate(birth), phone.getText(), position.getText());
            manager.addInvigilator(invigilator);
            invigilatorModel.addRow(new Object[]{invigilator.getId(), invigilator.getFullName(), invigilator.getBirthDate(), invigilator.getPhone(), invigilator.getPosition()});
            clear(id, name, phone, position);
            id.setText("Tự động");
        }));
        panel.add(form, BorderLayout.NORTH);
        JTable table = new JTable(invigilatorModel);
        styleTable(table);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(add, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel statisticPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT);
        statisticLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statisticLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statisticLabel.setForeground(NAVY);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        actions.setOpaque(false);
        JButton refresh = new JButton("Cập nhật thống kê");
        JButton print = new JButton("In thống kê");
        styleButton(refresh); styleButton(print);
        refresh.addActionListener(e -> updateStatistics());
        print.addActionListener(e -> printStatistics());
        actions.add(refresh); actions.add(print);
        panel.add(statisticLabel, BorderLayout.NORTH);
        panel.add(statisticsChart, BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);
        updateStatistics();
        return panel;
    }

    private void updateStatistics() {
        long registered = ThongKe.soNguoiThi(manager);
        long tested = manager.testedCount();
        statisticLabel.setText(String.format("<html><div style='text-align:center'>ĐĂNG KÝ: %d&nbsp;&nbsp;&nbsp; ĐÃ THI: %d&nbsp;&nbsp;&nbsp; CHƯA THI: %d<br>ĐẠT: %d&nbsp;&nbsp;&nbsp; KHÔNG ĐẠT: %d&nbsp;&nbsp;&nbsp; TỶ LỆ ĐẬU: %.1f%%&nbsp;&nbsp;&nbsp; DOANH THU: %d VNĐ</div></html>", registered, tested, Math.max(0, registered - tested), ThongKe.soNguoiDat(manager), ThongKe.soNguoiKhongDat(manager), ThongKe.tyLeDau(manager), ThongKe.doanhThu(manager)));
        statisticsChart.setValues(registered, tested);
    }

    private void printStatistics() {
        long registered = ThongKe.soNguoiThi(manager);
        long tested = manager.testedCount();
        JTextArea report = new JTextArea(String.format("THỐNG KÊ KỲ THI SÁT HẠCH LÁI XE\n\nTổng người đăng ký: %d\nĐã thi: %d\nChưa thi: %d\nĐạt: %d\nKhông đạt: %d\nTỷ lệ đậu: %.1f%%\nDoanh thu: %d VNĐ\n", registered, tested, Math.max(0, registered - tested), ThongKe.soNguoiDat(manager), ThongKe.soNguoiKhongDat(manager), ThongKe.tyLeDau(manager), ThongKe.doanhThu(manager)));
        try { report.print(); } catch (PrinterException ex) { JOptionPane.showMessageDialog(this, "Không thể in thống kê: " + ex.getMessage(), "Lỗi in", JOptionPane.ERROR_MESSAGE); }
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
    private static void styleButton(JButton button) {
        button.setBackground(BLUE);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
    }
    private static void styleTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setGridColor(new Color(220, 228, 238));
        table.setSelectionBackground(new Color(210, 229, 248));
        table.setSelectionForeground(Color.BLACK);
        table.getTableHeader().setBackground(NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
    }
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

    private static final class StatisticsChart extends JPanel {
        private long registered;
        private long tested;
        private StatisticsChart() { setPreferredSize(new Dimension(700, 300)); setBackground(Color.WHITE); }
        private void setValues(long registered, long tested) { this.registered = registered; this.tested = tested; repaint(); }
        @Override protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int base = getHeight() - 55, maxHeight = Math.max(30, getHeight() - 105);
            long max = Math.max(1, Math.max(registered, tested));
            drawBar(g, "Đăng ký", registered, 35, base, maxHeight, max, BLUE);
            drawBar(g, "Đã thi", tested, 190, base, maxHeight, max, new Color(44, 166, 120));
            g.dispose();
        }
        private void drawBar(Graphics2D g, String label, long value, int x, int base, int maxHeight, long max, Color color) {
            int height = (int) (maxHeight * (value / (double) max));
            g.setColor(color); g.fillRoundRect(x, base - height, 110, height, 12, 12);
            g.setColor(NAVY); g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString(String.valueOf(value), x + 47, base - height - 10);
            g.drawString(label, x + 23, base + 25);
        }
    }

    private static final class LoginFrame extends JFrame {
        private final JTextField username = new JTextField();
        private final JPasswordField password = new JPasswordField();

        private LoginFrame() {
            setTitle("Đăng nhập hệ thống sát hạch lái xe");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(420, 250);
            setLocationRelativeTo(null);
            JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
            form.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
            form.add(new JLabel("Tài khoản:")); form.add(username);
            form.add(new JLabel("Mật khẩu:")); form.add(password);
            JButton login = new JButton("Đăng nhập");
            form.add(new JLabel()); form.add(login);
            login.addActionListener(e -> authenticate());
            getRootPane().setDefaultButton(login);
            add(form);
        }

        private void authenticate() {
            if ("admin".equals(username.getText().trim()) && "123456".equals(String.valueOf(password.getPassword()))) {
                dispose();
                new QuanLyKyThiSwingApp().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không đúng", "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
                password.setText("");
            }
        }
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true)); }
}
