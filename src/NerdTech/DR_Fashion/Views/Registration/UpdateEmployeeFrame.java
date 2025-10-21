/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Registration;

import NerdTech.DR_Fashion.RoleItems.RoleItem;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import javax.swing.SwingUtilities;

/**
 *
 * @author MG_Pathum
 */
public class UpdateEmployeeFrame extends javax.swing.JFrame {

    /**
     * Creates new form UpdateEmployeeFrame
     */
    // Original NIC to track in update query
    private String originalNIC;
    private EmployeeRegistration employeeRegistrationPanel;

    public UpdateEmployeeFrame(EmployeeRegistration panel,
            String epfNumber,
            String fname,
            String lname,
            String nameInitial,
            String dob,
            String nic,
            String mobile,
            String fatherName,
            String motherName,
            String currentAddr,
            String permAddr,
            String elctroate,
            String nomineeName,
            int marriedStatusId,
            String districtName,
            String raceName,
            String genderValue,
            int designationId,
            int sectionId) {

        // STEP 1: Initialize components first
        initComponents();

        this.employeeRegistrationPanel = panel;
        this.originalNIC = nic;

        // Setup date chooser
        DoB.setDateFormatString("yyyy-MM-dd");
        DoB.setFont(new java.awt.Font("JetBrains Mono", 0, 18));
        ((javax.swing.JTextField) DoB.getDateEditor().getUiComponent()).setEditable(false);

        // STEP 2: Load combo boxes WITHOUT listeners
        loadMarriedStatus();
        loadSections();
        loadGenderOptions();
        loadDesignationsWithoutListener();

        // STEP 3: Set all text fields
        epfNo.setText(epfNumber);
        epfNo.setEnabled(false);
        fName.setText(fname);
        lName.setText(lname);
        NameInitial.setText(nameInitial);
        Nic.setText(nic);
        Phone.setText(mobile);
        father.setText(fatherName);
        mother.setText(motherName);
        CAddress.setText(currentAddr);
        PAddress.setText(permAddr);
        nominee.setText(nomineeName);
        distric.setText(districtName);
        race.setText(raceName);

        // Set date
        try {
            java.util.Date parsedDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dob);
            DoB.setDate(parsedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set married status
        for (int i = 0; i < married.getItemCount(); i++) {
            if (married.getItemAt(i).getId() == marriedStatusId) {
                married.setSelectedIndex(i);
                break;
            }
        }

        // Set gender
        setGenderFromDatabase(genderValue);

        // Set section
        for (int i = 0; i < section.getItemCount(); i++) {
            if (section.getItemAt(i).getId() == sectionId) {
                section.setSelectedIndex(i);
                break;
            }
        }

        // STEP 4: Load existing designation (this will set both designation1 and title)
        loadExistingDesignation(designationId);

        // STEP 5: NOW add listener for future changes
        designation1.addActionListener(e -> {
            if (designation1.getSelectedIndex() > 0) {
                loadTitlesBasedOnDesignation();
            }
        });
    }

    private void loadDesignationsWithoutListener() {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT DISTINCT capacity FROM designation ORDER BY capacity"); java.sql.ResultSet rs = stmt.executeQuery()) {

            designation1.removeAllItems();
            designation1.addItem(new RoleItem(0, "-- Select Designation --"));

            while (rs.next()) {
                String capacity = rs.getString("capacity");
                designation1.addItem(new RoleItem(0, capacity));
            }

            // NO listener added here!
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading designations: " + e.getMessage());
        }
    }

    private void loadExistingDesignation(int designationId) {
        System.out.println("\n=== DEBUG: Loading Designation ID: " + designationId + " ===");

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Query to get designation details
            String sql = "SELECT id, capacity, title FROM designation WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, designationId);
            java.sql.ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ ERROR: Designation ID " + designationId + " NOT FOUND!");
                JOptionPane.showMessageDialog(this,
                        "Designation ID " + designationId + " not found in database.",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String capacity = rs.getString("capacity");
            String titleName = rs.getString("title");

            System.out.println("✓ Found in DB:");
            System.out.println("  Capacity: [" + capacity + "]");
            System.out.println("  Title: [" + titleName + "]");

            // Find and set capacity in designation1 combo
            boolean foundCapacity = false;
            for (int i = 0; i < designation1.getItemCount(); i++) {
                RoleItem item = designation1.getItemAt(i);
                if (item.getName().trim().equalsIgnoreCase(capacity.trim())) {
                    System.out.println("✓ Setting designation1 to: " + capacity);
                    designation1.setSelectedIndex(i);
                    foundCapacity = true;
                    break;
                }
            }

            if (!foundCapacity) {
                System.out.println("❌ Capacity not found in combo!");
                JOptionPane.showMessageDialog(this,
                        "Capacity '" + capacity + "' not found in dropdown.",
                        "Load Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Load titles for this capacity
            String titleQuery = "SELECT id, title FROM designation WHERE capacity = ? ORDER BY title";
            PreparedStatement titleStmt = conn.prepareStatement(titleQuery);
            titleStmt.setString(1, capacity);
            java.sql.ResultSet titleRs = titleStmt.executeQuery();

            title.removeAllItems();

            int titleCount = 0;
            while (titleRs.next()) {
                int id = titleRs.getInt("id");
                String t = titleRs.getString("title");
                title.addItem(new RoleItem(id, t));
                titleCount++;
                System.out.println("  Loaded title: ID=" + id + ", Name=[" + t + "]");
            }

            System.out.println("✓ Total titles loaded: " + titleCount);

            // Find and set title
            boolean foundTitle = false;
            for (int i = 0; i < title.getItemCount(); i++) {
                RoleItem item = title.getItemAt(i);
                if (item.getId() == designationId) {
                    System.out.println("✓ Setting title to: " + item.getName());
                    title.setSelectedIndex(i);
                    foundTitle = true;
                    break;
                }
            }

            if (!foundTitle) {
                System.out.println("❌ Title ID " + designationId + " not found!");
                JOptionPane.showMessageDialog(this,
                        "Warning: Title ID " + designationId + " not found.\n"
                        + "Title '" + titleName + "' may not match capacity '" + capacity + "'",
                        "Load Warning", JOptionPane.WARNING_MESSAGE);
            }

            titleRs.close();
            titleStmt.close();
            rs.close();
            stmt.close();

            System.out.println("=== DEBUG END ===\n");

        } catch (Exception e) {
            System.out.println("❌ EXCEPTION:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading designation: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGenderOptions() {
        Gender.removeAllItems();
        Gender.addItem(new RoleItem(1, "-- Select Gender --"));
        Gender.addItem(new RoleItem(2, "Male"));
        Gender.addItem(new RoleItem(3, "Female"));
    }

    private void setGenderFromDatabase(String genderValue) {
        for (int i = 0; i < Gender.getItemCount(); i++) {
            if (Gender.getItemAt(i).getName().equals(genderValue)) {
                Gender.setSelectedIndex(i);
                break;
            }
        }
    }

    private void loadMarriedStatus() {
        married.removeAllItems();
        married.addItem(new RoleItem(1, "Married"));
        married.addItem(new RoleItem(2, "Unmarried"));
    }

    private void loadSections() {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT id, section_name FROM section"); java.sql.ResultSet rs = stmt.executeQuery()) {

            section.removeAllItems();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("section_name");
                section.addItem(new RoleItem(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading sections: " + e.getMessage());
        }
    }

    private void loadDesignations() {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT DISTINCT capacity FROM designation ORDER BY capacity"); java.sql.ResultSet rs = stmt.executeQuery()) {

            designation1.removeAllItems();
            designation1.addItem(new RoleItem(0, "-- Select Designation --"));

            while (rs.next()) {
                String capacity = rs.getString("capacity");
                designation1.addItem(new RoleItem(0, capacity));
            }

            // Remove old listeners
            for (java.awt.event.ActionListener al : designation1.getActionListeners()) {
                designation1.removeActionListener(al);
            }

            // Add new listener
            designation1.addActionListener(e -> {
                if (designation1.getSelectedIndex() > 0) {
                    loadTitlesBasedOnDesignation();
                }
            });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading designations: " + e.getMessage());
        }
    }

    private void loadTitlesBasedOnDesignation() {
        RoleItem selected = (RoleItem) designation1.getSelectedItem();

        System.out.println("loadTitlesBasedOnDesignation called");
        System.out.println("Selected designation: " + (selected != null ? selected.getName() : "NULL"));

        title.removeAllItems();

        if (selected == null || selected.getId() == 0) {
            System.out.println("✗ No valid selection, adding default item");
            title.addItem(new RoleItem(0, "-- Select Title --"));
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, title FROM designation WHERE capacity = ? ORDER BY title")) {

            stmt.setString(1, selected.getName());
            System.out.println("✓ Querying titles for capacity: " + selected.getName());

            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                title.removeAllItems();

                if (!rs.isBeforeFirst()) {
                    System.out.println("✗ No titles found for this capacity");
                    title.addItem(new RoleItem(0, "No titles available"));
                    return;
                }

                int count = 0;
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String titleName = rs.getString("title");
                    title.addItem(new RoleItem(id, titleName));
                    count++;
                    System.out.println("  Added title: ID=" + id + ", Name=" + titleName);
                }

                System.out.println("✓ Total titles loaded: " + count);
            }
        } catch (Exception e) {
            System.out.println("✗ Exception in loadTitlesBasedOnDesignation:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading titles: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        Nic = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        fName = new javax.swing.JTextField();
        lName = new javax.swing.JTextField();
        DoB = new com.toedter.calendar.JDateChooser();
        Phone = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        Gender = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        NameInitial = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        mother = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        father = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        CAddress = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        PAddress = new javax.swing.JTextField();
        nominee = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        distric = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        race = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        section = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        epfNo = new javax.swing.JTextField();
        married = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        title = new javax.swing.JComboBox<>();
        designation1 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Update Employee");

        Nic.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Update");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel2.setText("First Name");

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel8.setText("Gender");

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel6.setText("Mobile");

        fName.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        lName.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        DoB.setForeground(new java.awt.Color(255, 255, 255));

        Phone.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel5.setText("Date of Birth");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel3.setText("Last Name");

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel7.setText("NIC");

        Gender.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel9.setText("Name with Initial");

        NameInitial.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel10.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel10.setText("Mother");

        mother.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        mother.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                motherActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel11.setText("Father");

        father.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        father.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fatherActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel12.setText("Current Address");

        CAddress.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        CAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CAddressActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel13.setText("Permanate Address");

        PAddress.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        PAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PAddressActionPerformed(evt);
            }
        });

        nominee.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        nominee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomineeActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel14.setText("nominee");

        jLabel15.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel15.setText("Married Status");

        distric.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        distric.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                districActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel16.setText("District");

        jLabel17.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel17.setText("Race");

        race.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        race.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                raceActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel18.setText("Section");

        section.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel19.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel19.setText("Epf No");

        epfNo.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        married.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel20.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel20.setText("Title");

        title.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        designation1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel21.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel21.setText("Designation");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13)
                    .addComponent(jLabel15)
                    .addComponent(jLabel20)
                    .addComponent(jLabel18)
                    .addComponent(jLabel8)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(father, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(married, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Gender, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fName, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(epfNo, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(designation1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(79, 79, 79)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(nominee, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(race, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lName, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(DoB, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(32, 32, 32)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel12)
                                            .addGap(70, 70, 70)
                                            .addComponent(CAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addGap(196, 196, 196)
                                                .addComponent(mother, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel7)
                                                    .addComponent(jLabel16))
                                                .addGap(168, 168, 168)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(distric, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(Nic, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(280, 280, 280)
                                        .addComponent(NameInitial, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(90, 90, 90))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(section, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(epfNo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(32, 32, 32)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(father, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10)
                                    .addComponent(mother, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(PAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(Nic, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel15)
                                        .addComponent(married, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(distric, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Gender, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(nominee, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14)
                                    .addComponent(designation1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel21))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel20))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(fName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(392, 392, 392)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17)
                                    .addComponent(race, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(3, 3, 3)))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(section, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(67, 67, 67))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(NameInitial, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(jLabel5))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addGap(26, 26, 26)
                                .addComponent(DoB, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 745, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // Validate කරන්න සියලුම required fields fill කරලා තියෙනවාද (EMAIL නැතුව)
            if (fName.getText().trim().isEmpty() || lName.getText().trim().isEmpty()
                    || NameInitial.getText().trim().isEmpty() || Nic.getText().trim().isEmpty()
                    || Phone.getText().trim().isEmpty()
                    || DoB.getDate() == null || epfNo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill all required fields:\n"
                        + "- First Name\n- Last Name\n- Name with Initial\n- NIC\n"
                        + "- Phone\n- Date of Birth\n- EPF No",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Phone validation (10 digits)
            String phone = Phone.getText().trim();
            if (!phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this,
                        "Phone number must be 10 digits",
                        "Invalid Phone",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // NIC validation (9 digits + V or 12 digits)
            String nic = Nic.getText().trim();
            if (!nic.matches("\\d{9}[Vv]|\\d{12}")) {
                JOptionPane.showMessageDialog(this,
                        "NIC must be in format: 123456789V or 123456789012",
                        "Invalid NIC",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Gender - Default value (හැබැයි gender combo එකක් add කරන්න හොඳයි)
            RoleItem selectedGender = (RoleItem) Gender.getSelectedItem();
            if (selectedGender == null || selectedGender.getId() == 1) {
                JOptionPane.showMessageDialog(this,
                        "Please select a gender",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String gender = selectedGender.getName();

            // SQL query - EMAIL එක REMOVE කරලා (20 parameters only)
            String sql = "UPDATE employee SET "
                    + "epf_no = ?, name_with_initial = ?, fname = ?, lname = ?, gender = ?, "
                    + "dob = ?, nic = ?, mobile = ?, father = ?, mother = ?, "
                    + "permanate_address = ?, current_address = ?, nominee = ?, "
                    + "married_status = ?, district = ?, race = ?, "
                    + "designation_id = ?, section_id = ? "
                    + "WHERE nic = ?";

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Set all parameters (EMAIL නැතුව)
            stmt.setString(1, epfNo.getText().trim());
            stmt.setString(2, NameInitial.getText().trim());
            stmt.setString(3, fName.getText().trim());
            stmt.setString(4, lName.getText().trim());
            stmt.setString(5, gender);
            stmt.setString(6, new java.text.SimpleDateFormat("yyyy-MM-dd").format(DoB.getDate()));
            stmt.setString(7, nic.toUpperCase());
            stmt.setString(8, phone);
            stmt.setString(9, father.getText().trim());
            stmt.setString(10, mother.getText().trim());
            stmt.setString(11, PAddress.getText().trim());
            stmt.setString(12, CAddress.getText().trim());
            stmt.setString(13, nominee.getText().trim());
            int marriedId = ((RoleItem) married.getSelectedItem()).getId();
            String marriedEnumValue = (marriedId == 1) ? "Married" : "Unmarried";
            stmt.setString(14, marriedEnumValue);
            stmt.setString(15, distric.getText().trim());
            stmt.setString(16, race.getText().trim());

            // Get designation ID from title combo box
            RoleItem selectedTitle = (RoleItem) title.getSelectedItem();
            if (selectedTitle == null || selectedTitle.getId() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Please select a valid designation title",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                stmt.close();
                conn.close();
                return;
            }
            stmt.setInt(17, selectedTitle.getId());

            // Get section ID
            RoleItem selectedSection = (RoleItem) section.getSelectedItem();
            if (selectedSection == null || selectedSection.getId() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Please select a valid section",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                stmt.close();
                conn.close();
                return;
            }
            stmt.setInt(18, selectedSection.getId());

// WHERE condition - original NIC use karanna
            stmt.setString(19, originalNIC);

            // Execute insert
            int res = stmt.executeUpdate();

            if (res > 0) {
                JOptionPane.showMessageDialog(this,
                        "Employee Updated Successfully!\n"
                        + "Name: " + fName.getText() + " " + lName.getText() + "\n"
                        + "EPF No: " + epfNo.getText(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Table එක refresh කරන්න (null check කරලා)
                if (employeeRegistrationPanel != null) {
                    employeeRegistrationPanel.refreshTable();
                }

                // Dialog එක close කරන්න
                this.dispose();
            }

            stmt.close();
            conn.close();

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            String errorMsg = e.getMessage();
            if (errorMsg.contains("epf_no")) {
                JOptionPane.showMessageDialog(this,
                        "This EPF Number already exists in the system!\n"
                        + "Please use a different EPF Number.",
                        "Duplicate EPF Number",
                        JOptionPane.ERROR_MESSAGE);
            } else if (errorMsg.contains("nic")) {
                JOptionPane.showMessageDialog(this,
                        "This NIC already exists in the system!\n"
                        + "Please check the NIC number.",
                        "Duplicate NIC",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Database constraint error:\n" + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error registering employee:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel?\nAll entered data will be lost.",
                "Confirm Cancel",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            // Table එක refresh කරන්න (null check කරලා)
            if (employeeRegistrationPanel != null) {
                employeeRegistrationPanel.refreshTable();
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void motherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_motherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_motherActionPerformed

    private void fatherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fatherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fatherActionPerformed

    private void CAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CAddressActionPerformed

    private void PAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PAddressActionPerformed

    private void nomineeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomineeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomineeActionPerformed

    private void districActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_districActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_districActionPerformed

    private void raceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_raceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_raceActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UpdateEmployeeFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateEmployeeFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateEmployeeFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateEmployeeFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CAddress;
    private com.toedter.calendar.JDateChooser DoB;
    private javax.swing.JComboBox<RoleItem> Gender;
    private javax.swing.JTextField NameInitial;
    private javax.swing.JTextField Nic;
    private javax.swing.JTextField PAddress;
    private javax.swing.JTextField Phone;
    private javax.swing.JComboBox<RoleItem> designation1;
    private javax.swing.JTextField distric;
    private javax.swing.JTextField epfNo;
    private javax.swing.JTextField fName;
    private javax.swing.JTextField father;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField lName;
    private javax.swing.JComboBox<RoleItem> married;
    private javax.swing.JTextField mother;
    private javax.swing.JTextField nominee;
    private javax.swing.JTextField race;
    private javax.swing.JComboBox<RoleItem> section;
    private javax.swing.JComboBox<RoleItem> title;
    // End of variables declaration//GEN-END:variables
}
