package com.careerbridge.swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

/**
 * CareerBridge Swing Admin Demo
 * Demonstrates: JFrame, JPanel, JButton, JTextField, JPasswordField, JComboBox,
 * JTable, JScrollPane, JOptionPane, JTabbedPane, JMenuBar, TableRowSorter,
 * GridBagLayout, BorderLayout, CardLayout, FlowLayout, and MVC concepts.
 *
 * RTU Syllabus: Java Swing, GUI Components, Event Handling, MVC Pattern.
 */
public class SwingApp extends JFrame {

    // Card layout container for switching between Login screen and Main Application
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel containerPanel = new JPanel(cardLayout);

    // Authentication components
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    // Main views
    private JTabbedPane tabbedPane;
    private JMenuItem logoutMenuItem;

    // Dashboard metrics
    private JLabel lblTotalJobs;
    private JLabel lblActiveJobs;
    private JLabel lblTotalUsers;
    private JLabel lblApplications;

    // Jobs table and model (Model in MVC)
    private DefaultTableModel jobsTableModel;
    private JTable jobsTable;
    private TableRowSorter<DefaultTableModel> jobSorter;
    private JTextField txtJobSearch;
    private int nextJobId = 108;

    // Users table and model
    private DefaultTableModel usersTableModel;
    private JTable usersTable;
    private TableRowSorter<DefaultTableModel> userSorter;
    private JTextField txtUserSearch;
    private int nextUserId = 206;

    // Settings fields
    private JTextField txtApiUrl;
    private JSpinner spinRefreshInterval;
    private JCheckBox chkEmailNotifications;
    private JCheckBox chkAutoSync;
    private JComboBox<String> cmbLogLevel;

    public SwingApp() {
        super("CareerBridge - Admin Management Console");
        initializeUI();
    }

    /**
     * Initializes the primary application window, menus, and cards.
     */
    private void initializeUI() {
        setSize(920, 620);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup Menu Bar
        setJMenuBar(createMenuBar());

        // Setup Cards: Login Card and Main Application Card
        JPanel loginPanel = createLoginPanel();
        JPanel mainPanel = createMainPanel();

        containerPanel.add(loginPanel, "LOGIN");
        containerPanel.add(mainPanel, "MAIN");

        setContentPane(containerPanel);
        cardLayout.show(containerPanel, "LOGIN");
    }

    /**
     * Creates the application menu bar with File and Help menus.
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.setMnemonic(KeyEvent.VK_L);
        logoutMenuItem.setEnabled(false); // Enabled only after login
        logoutMenuItem.addActionListener(e -> performLogout());

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit CareerBridge Admin Console?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        fileMenu.add(logoutMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        JMenuItem aboutMenuItem = new JMenuItem("About CareerBridge");
        aboutMenuItem.setMnemonic(KeyEvent.VK_A);
        aboutMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "<html><h3>CareerBridge Swing Admin Console</h3>"
                        + "<p>Version 1.0 (RTU Advanced Java Demo)</p>"
                        + "<p>Architecture: Swing GUI with MVC separation</p>"
                        + "<p>Features: Interactive CRUD, Filtered JTables, Settings & Dashboard</p>"
                        + "<p>&copy; 2026 CareerBridge Team</p></html>",
                "About CareerBridge",
                JOptionPane.INFORMATION_MESSAGE
        ));

        helpMenu.add(aboutMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    /**
     * Creates the initial Login panel with username/password validation.
     */
    private JPanel createLoginPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(245, 247, 250));

        JPanel card = new JPanel(new GridBagLayout());
        card.setPreferredSize(new Dimension(420, 360));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 222), 1),
                new EmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Header Title
        JLabel lblTitle = new JLabel("CareerBridge Admin Portal", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(33, 50, 91));
        card.add(lblTitle, gbc);

        // Subtitle
        gbc.gridy++;
        JLabel lblSubtitle = new JLabel("Please enter administrator credentials", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(Color.GRAY);
        card.add(lblSubtitle, gbc);

        // Username
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(lblUser, gbc);

        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsername.setText("admin"); // Default hint
        card.add(txtUsername, gbc);

        // Password
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(lblPass, gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(15);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setText("admin123"); // Default hint
        card.add(txtPassword, gbc);

        // Buttons
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setOpaque(false);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogin.setBackground(new Color(33, 115, 215));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(100, 32));
        btnLogin.addActionListener(e -> performLogin());

        JButton btnReset = new JButton("Clear");
        btnReset.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnReset.setPreferredSize(new Dimension(90, 32));
        btnReset.addActionListener(e -> {
            txtUsername.setText("");
            txtPassword.setText("");
            txtUsername.requestFocusInWindow();
        });

        // Trigger login on ENTER inside password field
        txtPassword.addActionListener(e -> performLogin());
        txtUsername.addActionListener(e -> performLogin());

        btnPanel.add(btnLogin);
        btnPanel.add(btnReset);
        card.add(btnPanel, gbc);

        // Demo credentials hint
        gbc.gridy++;
        JLabel lblHint = new JLabel("Demo Access: admin / admin123", SwingConstants.CENTER);
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(new Color(120, 130, 140));
        card.add(lblHint, gbc);

        wrapper.add(card);
        return wrapper;
    }

    /**
     * Validates credentials and switches to dashboard.
     */
    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if ("admin".equals(username) && "admin123".equals(password)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Welcome back, Administrator!",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );
            logoutMenuItem.setEnabled(true);
            cardLayout.show(containerPanel, "MAIN");
            updateDashboardMetrics();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password. Please use admin / admin123.",
                    "Authentication Failed",
                    JOptionPane.ERROR_MESSAGE
            );
            txtPassword.setText("");
            txtPassword.requestFocusInWindow();
        }
    }

    /**
     * Logs out the user and returns to login view.
     */
    private void performLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            txtPassword.setText("");
            logoutMenuItem.setEnabled(false);
            cardLayout.show(containerPanel, "LOGIN");
        }
    }

    /**
     * Builds the main view after successful authentication.
     */
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tabbedPane.addTab("Dashboard", createDashboardTab());
        tabbedPane.addTab("Jobs Management", createJobsTab());
        tabbedPane.addTab("Users Directory", createUsersTab());
        tabbedPane.addTab("System Settings", createSettingsTab());

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Tab 1: Dashboard overview with statistics.
     */
    private JPanel createDashboardTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(248, 250, 252));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblHeader = new JLabel("System Overview & Key Metrics");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeader.setForeground(new Color(30, 41, 59));
        headerPanel.add(lblHeader, BorderLayout.WEST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Stats Cards Grid
        JPanel cardsGrid = new JPanel(new GridLayout(2, 2, 16, 16));
        cardsGrid.setOpaque(false);

        lblTotalJobs = new JLabel("0", SwingConstants.CENTER);
        lblActiveJobs = new JLabel("0", SwingConstants.CENTER);
        lblTotalUsers = new JLabel("0", SwingConstants.CENTER);
        lblApplications = new JLabel("142", SwingConstants.CENTER);

        cardsGrid.add(createMetricCard("Total Job Listings", lblTotalJobs, new Color(59, 130, 246)));
        cardsGrid.add(createMetricCard("Active / Published Jobs", lblActiveJobs, new Color(16, 185, 129)));
        cardsGrid.add(createMetricCard("Registered Users", lblTotalUsers, new Color(139, 92, 246)));
        cardsGrid.add(createMetricCard("Total Applications", lblApplications, new Color(245, 158, 11)));

        panel.add(cardsGrid, BorderLayout.CENTER);

        // Quick Actions Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        bottomPanel.setOpaque(false);

        JButton btnGoJobs = new JButton("Manage Jobs");
        btnGoJobs.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnGoJobs.addActionListener(e -> tabbedPane.setSelectedIndex(1));

        JButton btnGoUsers = new JButton("View Users");
        btnGoUsers.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnGoUsers.addActionListener(e -> tabbedPane.setSelectedIndex(2));

        JButton btnRefreshStats = new JButton("Refresh Statistics");
        btnRefreshStats.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefreshStats.addActionListener(e -> {
            updateDashboardMetrics();
            JOptionPane.showMessageDialog(this, "Dashboard metrics updated.", "Refreshed", JOptionPane.INFORMATION_MESSAGE);
        });

        bottomPanel.add(btnGoJobs);
        bottomPanel.add(btnGoUsers);
        bottomPanel.add(btnRefreshStats);

        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setForeground(new Color(100, 116, 139));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(accentColor);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    /**
     * Tab 2: Jobs Table with CRUD and Search.
     */
    private JPanel createJobsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Top Filter Bar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topBar.add(new JLabel("Search Jobs:"));
        txtJobSearch = new JTextField(20);
        topBar.add(txtJobSearch);

        JButton btnClearSearch = new JButton("Clear");
        btnClearSearch.addActionListener(e -> {
            txtJobSearch.setText("");
            jobSorter.setRowFilter(null);
        });
        topBar.add(btnClearSearch);

        panel.add(topBar, BorderLayout.NORTH);

        // Table Model & JTable
        String[] columns = {"ID", "Title", "Company", "Type", "Status"};
        jobsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only directly in table cells
            }
        };

        // Populate initial sample jobs
        jobsTableModel.addRow(new Object[]{101, "Senior Java Developer", "Google India", "Full-time", "Published"});
        jobsTableModel.addRow(new Object[]{102, "Full Stack Engineer", "Microsoft", "Full-time", "Published"});
        jobsTableModel.addRow(new Object[]{103, "Cloud DevOps Architect", "Amazon AWS", "Full-time", "Published"});
        jobsTableModel.addRow(new Object[]{104, "Backend Intern (Spring/Jakarta)", "Infosys", "Internship", "Published"});
        jobsTableModel.addRow(new Object[]{105, "React & UI Consultant", "TCS", "Contract", "Draft"});
        jobsTableModel.addRow(new Object[]{106, "Database Administrator", "Oracle", "Full-time", "Closed"});
        jobsTableModel.addRow(new Object[]{107, "Machine Learning Engineer", "IBM Research", "Part-time", "Draft"});

        jobsTable = new JTable(jobsTableModel);
        jobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobsTable.setRowHeight(26);
        jobsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        jobsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        jobSorter = new TableRowSorter<>(jobsTableModel);
        jobsTable.setRowSorter(jobSorter);

        // Filter listener
        txtJobSearch.addActionListener(e -> applyJobFilter());
        txtJobSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applyJobFilter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applyJobFilter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyJobFilter(); }
        });

        JScrollPane scrollPane = new JScrollPane(jobsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Action Bar
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));

        JButton btnAdd = new JButton("Add Job");
        btnAdd.setBackground(new Color(16, 185, 129));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> showJobDialog(null, -1));

        JButton btnEdit = new JButton("Edit Selected");
        btnEdit.addActionListener(e -> {
            int selectedRow = jobsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a job to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = jobsTable.convertRowIndexToModel(selectedRow);
            showJobDialog(modelRow, modelRow);
        });

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.setBackground(new Color(239, 68, 68));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(e -> deleteSelectedJob());

        actionsPanel.add(btnAdd);
        actionsPanel.add(btnEdit);
        actionsPanel.add(btnDelete);

        panel.add(actionsPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void applyJobFilter() {
        String text = txtJobSearch.getText().trim();
        if (text.isEmpty()) {
            jobSorter.setRowFilter(null);
        } else {
            jobSorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
        }
    }

    /**
     * Dialog for adding or editing a Job posting.
     */
    private void showJobDialog(Integer modelRow, int rowIndex) {
        boolean isEdit = (modelRow != null && modelRow >= 0);
        String title = isEdit ? "Edit Job Posting" : "Add New Job Posting";

        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Job Title
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Job Title:"), gbc);
        gbc.gridx = 1;
        JTextField txtTitle = new JTextField(18);
        formPanel.add(txtTitle, gbc);

        // Company
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Company:"), gbc);
        gbc.gridx = 1;
        JTextField txtCompany = new JTextField(18);
        formPanel.add(txtCompany, gbc);

        // Job Type
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Job Type:"), gbc);
        gbc.gridx = 1;
        String[] types = {"Full-time", "Part-time", "Internship", "Contract"};
        JComboBox<String> cmbType = new JComboBox<>(types);
        formPanel.add(cmbType, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        String[] statuses = {"Published", "Draft", "Closed"};
        JComboBox<String> cmbStatus = new JComboBox<>(statuses);
        formPanel.add(cmbStatus, gbc);

        // Prepopulate if editing
        if (isEdit) {
            txtTitle.setText((String) jobsTableModel.getValueAt(modelRow, 1));
            txtCompany.setText((String) jobsTableModel.getValueAt(modelRow, 2));
            cmbType.setSelectedItem(jobsTableModel.getValueAt(modelRow, 3));
            cmbStatus.setSelectedItem(jobsTableModel.getValueAt(modelRow, 4));
        }

        dialog.add(formPanel, BorderLayout.CENTER);

        // Dialog buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSave = new JButton(isEdit ? "Update" : "Create");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
            String jobTitle = txtTitle.getText().trim();
            String company = txtCompany.getText().trim();
            String jobType = (String) cmbType.getSelectedItem();
            String status = (String) cmbStatus.getSelectedItem();

            if (jobTitle.isEmpty() || company.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Title and Company are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (isEdit) {
                jobsTableModel.setValueAt(jobTitle, modelRow, 1);
                jobsTableModel.setValueAt(company, modelRow, 2);
                jobsTableModel.setValueAt(jobType, modelRow, 3);
                jobsTableModel.setValueAt(status, modelRow, 4);
                JOptionPane.showMessageDialog(this, "Job updated successfully.", "Saved", JOptionPane.INFORMATION_MESSAGE);
            } else {
                jobsTableModel.addRow(new Object[]{nextJobId++, jobTitle, company, jobType, status});
                JOptionPane.showMessageDialog(this, "New job created successfully.", "Created", JOptionPane.INFORMATION_MESSAGE);
            }
            updateDashboardMetrics();
            dialog.dispose();
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void deleteSelectedJob() {
        int selectedRow = jobsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = jobsTable.convertRowIndexToModel(selectedRow);
        String jobTitle = (String) jobsTableModel.getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete job: \"" + jobTitle + "\"?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            jobsTableModel.removeRow(modelRow);
            updateDashboardMetrics();
            JOptionPane.showMessageDialog(this, "Job deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Tab 3: Users Directory table with management.
     */
    private JPanel createUsersTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Filter Bar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topBar.add(new JLabel("Search Users:"));
        txtUserSearch = new JTextField(20);
        topBar.add(txtUserSearch);

        panel.add(topBar, BorderLayout.NORTH);

        String[] columns = {"ID", "Full Name", "Email Address", "Role", "Status"};
        usersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Populate sample users
        usersTableModel.addRow(new Object[]{201, "Rahul Sharma", "rahul.sharma@example.com", "Candidate", "Active"});
        usersTableModel.addRow(new Object[]{202, "Priya Patel", "priya.p@techcorp.in", "Employer", "Active"});
        usersTableModel.addRow(new Object[]{203, "Amit Verma", "amit.verma@example.com", "Candidate", "Active"});
        usersTableModel.addRow(new Object[]{204, "Sneha Kulkarni", "sneha@innovate.io", "Employer", "Suspended"});
        usersTableModel.addRow(new Object[]{205, "Vikram Malhotra", "admin@careerbridge.com", "Admin", "Active"});

        usersTable = new JTable(usersTableModel);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        usersTable.setRowHeight(26);
        usersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        userSorter = new TableRowSorter<>(usersTableModel);
        usersTable.setRowSorter(userSorter);

        txtUserSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applyUserFilter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applyUserFilter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyUserFilter(); }
        });

        panel.add(new JScrollPane(usersTable), BorderLayout.CENTER);

        // Bottom Actions
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));

        JButton btnToggleStatus = new JButton("Toggle Active/Suspended");
        btnToggleStatus.addActionListener(e -> {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a user to modify status.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = usersTable.convertRowIndexToModel(selectedRow);
            String currentStatus = (String) usersTableModel.getValueAt(modelRow, 4);
            String newStatus = "Active".equalsIgnoreCase(currentStatus) ? "Suspended" : "Active";
            usersTableModel.setValueAt(newStatus, modelRow, 4);
            JOptionPane.showMessageDialog(this, "Status changed to " + newStatus, "Status Updated", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnAddUser = new JButton("Add User");
        btnAddUser.addActionListener(e -> showAddUserDialog());

        actionsPanel.add(btnAddUser);
        actionsPanel.add(btnToggleStatus);
        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void applyUserFilter() {
        String text = txtUserSearch.getText().trim();
        if (text.isEmpty()) {
            userSorter.setRowFilter(null);
        } else {
            userSorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
        }
    }

    private void showAddUserDialog() {
        JDialog dialog = new JDialog(this, "Add New User", true);
        dialog.setSize(380, 260);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextField txtName = new JTextField();
        JTextField txtEmail = new JTextField();
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Candidate", "Employer", "Admin"});

        form.add(new JLabel("Full Name:"));
        form.add(txtName);
        form.add(new JLabel("Email Address:"));
        form.add(txtEmail);
        form.add(new JLabel("System Role:"));
        form.add(cmbRole);

        JButton btnSave = new JButton("Save User");
        btnSave.addActionListener(e -> {
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name and Email are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            usersTableModel.addRow(new Object[]{nextUserId++, name, email, cmbRole.getSelectedItem(), "Active"});
            updateDashboardMetrics();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "User registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnSave);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Tab 4: System settings form.
     */
    private JPanel createSettingsTab() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Application & Backend Preferences",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 13)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // API URL
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Backend Service Endpoint:"), gbc);
        gbc.gridx = 1;
        txtApiUrl = new JTextField("http://localhost:8080/careerbridge-web", 25);
        form.add(txtApiUrl, gbc);

        // Auto Refresh Interval
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Metrics Refresh Interval (seconds):"), gbc);
        gbc.gridx = 1;
        spinRefreshInterval = new JSpinner(new SpinnerNumberModel(30, 5, 300, 5));
        form.add(spinRefreshInterval, gbc);

        // Notification Checkbox
        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Notifications:"), gbc);
        gbc.gridx = 1;
        chkEmailNotifications = new JCheckBox("Enable Email Alerts on New Applications", true);
        form.add(chkEmailNotifications, gbc);

        // Auto Sync Checkbox
        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Data Synchronization:"), gbc);
        gbc.gridx = 1;
        chkAutoSync = new JCheckBox("Automatically Sync with Network Socket Server", true);
        form.add(chkAutoSync, gbc);

        // Log Level
        gbc.gridx = 0; gbc.gridy = 4;
        form.add(new JLabel("Console Logging Level:"), gbc);
        gbc.gridx = 1;
        cmbLogLevel = new JComboBox<>(new String[]{"INFO", "DEBUG", "WARN", "ERROR"});
        cmbLogLevel.setSelectedItem("INFO");
        form.add(cmbLogLevel, gbc);

        // Save Button
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSaveSettings = new JButton("Save Preferences");
        btnSaveSettings.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSaveSettings.setBackground(new Color(33, 115, 215));
        btnSaveSettings.setForeground(Color.WHITE);
        btnSaveSettings.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Preferences saved successfully:\n"
                            + "- Endpoint: " + txtApiUrl.getText() + "\n"
                            + "- Interval: " + spinRefreshInterval.getValue() + "s\n"
                            + "- Email Alerts: " + (chkEmailNotifications.isSelected() ? "Enabled" : "Disabled") + "\n"
                            + "- Log Level: " + cmbLogLevel.getSelectedItem(),
                    "Settings Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        savePanel.add(btnSaveSettings);
        form.add(savePanel, gbc);

        wrapper.add(form, BorderLayout.NORTH);
        return wrapper;
    }

    /**
     * Recalculates stats from the table models and refreshes dashboard labels.
     */
    private void updateDashboardMetrics() {
        if (jobsTableModel != null && lblTotalJobs != null) {
            int totalJobs = jobsTableModel.getRowCount();
            int activeJobs = 0;
            for (int i = 0; i < totalJobs; i++) {
                Object status = jobsTableModel.getValueAt(i, 4);
                if ("Published".equalsIgnoreCase(String.valueOf(status))) {
                    activeJobs++;
                }
            }
            lblTotalJobs.setText(String.valueOf(totalJobs));
            lblActiveJobs.setText(String.valueOf(activeJobs));
        }

        if (usersTableModel != null && lblTotalUsers != null) {
            lblTotalUsers.setText(String.valueOf(usersTableModel.getRowCount()));
        }
    }

    /**
     * Application entry point.
     */
    public static void main(String[] args) {
        // Set system look and feel if available
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fallback to default Swing Look & Feel
        }

        SwingUtilities.invokeLater(() -> {
            SwingApp app = new SwingApp();
            app.setVisible(true);
        });
    }
}
