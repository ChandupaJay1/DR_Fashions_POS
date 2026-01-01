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
import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;

public class UpdateEmployeeFrame extends javax.swing.JFrame {

    // Original NIC to track in update query
    private String originalNIC;
    private EmployeeRegistration employeeRegistrationPanel;

    public UpdateEmployeeFrame(
            EmployeeRegistration panel,
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
            int capacityId,
            int sectionId,
            String religionValue) {  // ✅ Add religion parameter

        // STEP 1: Initialize components first
        initComponents();

        this.employeeRegistrationPanel = panel;
        this.originalNIC = nic;

        // Setup date chooser
        DoB.setDateFormatString("yyyy-MM-dd");
        DoB.setFont(new java.awt.Font("JetBrains Mono", 0, 18));
        ((javax.swing.JTextField) DoB.getDateEditor().getUiComponent()).setEditable(false);

        // ✅ Set recruited date to today
        recruitedDate.setDate(new java.util.Date());
        recruitedDate.setDateFormatString("yyyy-MM-dd");
        recruitedDate.setFont(new java.awt.Font("JetBrains Mono", 0, 18));
        ((javax.swing.JTextField) recruitedDate.getDateEditor().getUiComponent()).setEditable(false);

        // STEP 2: Load combo boxes
        loadMarriedStatus();
        loadSections();
        loadGenderOptions();
        loadDesignations();
        loadCapacities();

        // STEP 3: Set all text fields
        epfNo.setText(epfNumber);
        epfNo.setEnabled(false);
        fName.setText(fname);
        initials.setText(lname);
        NameInitial.setText(nameInitial);
        Nic.setText(nic);
        Phone.setText(mobile);
        father.setText(fatherName);
        mother.setText(motherName);
        CAddress.setText(currentAddr);
        PAddress.setText(permAddr);
        elctroateField.setText(elctroate);
        nominee.setText(nomineeName);
        distric.setText(districtName);
        race.setText(raceName);

        // ✅ Set religion field - MOVE THIS AFTER initComponents()
        if (religion != null && religionValue != null) {
            religion.setText(religionValue);
        }

        // Set surname field
        if (surname != null) {
            surname.setText(lname);
        }

        // Set date
        try {
            java.util.Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
            DoB.setDate(parsedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set married status
        for (int i = 0; i < marriedStatusCombo.getItemCount(); i++) {
            if (marriedStatusCombo.getItemAt(i).getId() == marriedStatusId) {
                marriedStatusCombo.setSelectedIndex(i);
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

        // Set designation
        for (int i = 0; i < designation.getItemCount(); i++) {
            if (designation.getItemAt(i).getId() == designationId) {
                designation.setSelectedIndex(i);
                break;
            }
        }

        // Set capacity
        for (int i = 0; i < capacity.getItemCount(); i++) {
            if (capacity.getItemAt(i).getId() == capacityId) {
                capacity.setSelectedIndex(i);
                break;
            }
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
        marriedStatusCombo.removeAllItems();
        marriedStatusCombo.addItem(new RoleItem(1, "Married"));
        marriedStatusCombo.addItem(new RoleItem(2, "Unmarried"));
    }

    private void loadSections() {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT id, section_name FROM section"); java.sql.ResultSet rs = stmt.executeQuery()) {

            section.removeAllItems();
            section.addItem(new RoleItem(0, "-- Select Section --"));

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
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT id, title FROM designation ORDER BY title"); java.sql.ResultSet rs = stmt.executeQuery()) {

            System.out.println("Loading designations...");

            designation.removeAllItems();
            designation.addItem(new RoleItem(0, "-- Select Designation --"));

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                System.out.println("Adding designation: " + id + " - " + title);
                designation.addItem(new RoleItem(id, title));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading designations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadCapacities() {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM capacity ORDER BY name"); java.sql.ResultSet rs = stmt.executeQuery()) {

            System.out.println("Loading capacities...");

            capacity.removeAllItems();
            capacity.addItem(new RoleItem(0, "-- Select Capacity --"));

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println("Adding capacity: " + id + " - " + name);
                capacity.addItem(new RoleItem(id, name));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading capacities: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearFields() {
        fName.setText("");
        initials.setText("");
        surname.setText("");
        NameInitial.setText("");
        Nic.setText("");
        DoB.setDate(null);
        Phone.setText("");
        epfNo.setText("");
        father.setText("");
        mother.setText("");
        religion.setText("");
        elctroateField.setText("");
        CAddress.setText("");
        PAddress.setText("");
        nominee.setText("");
        distric.setText("");
        race.setText("");

        // Reset recruited date to today
        recruitedDate.setDate(new java.util.Date());

        if (Gender.getItemCount() > 0) {
            Gender.setSelectedIndex(0);
        }

        if (marriedStatusCombo.getItemCount() > 0) {
            marriedStatusCombo.setSelectedIndex(0);
        }
        if (designation.getItemCount() > 0) {
            designation.setSelectedIndex(0);
        }
        if (section.getItemCount() > 0) {
            section.setSelectedIndex(0);
        }
        if (capacity.getItemCount() > 0) {
            capacity.setSelectedIndex(0);
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
        initials = new javax.swing.JTextField();
        DoB = new com.toedter.calendar.JDateChooser();
        Phone = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        designation = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        NameInitial = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        mother = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        father = new javax.swing.JTextField();
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
        marriedStatusCombo = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        capacity = new javax.swing.JComboBox<>();
        Gender = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        surname = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        elctroateField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        CAddress = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        PAddress = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        religion = new javax.swing.JTextField();
        recruitedDate = new com.toedter.calendar.JDateChooser();

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
        jLabel8.setText("Designation");

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel6.setText("Mobile");

        fName.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        initials.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        DoB.setForeground(new java.awt.Color(255, 255, 255));

        Phone.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel5.setText("Date of Birth");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel3.setText("Initials");

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel7.setText("NIC");

        designation.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

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

        marriedStatusCombo.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel20.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel20.setText("Capacity");

        capacity.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        Gender.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel21.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel21.setText("Gender");

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel4.setText("Surname");

        surname.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel24.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel24.setText("Elctroate");

        elctroateField.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        elctroateField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elctroateFieldActionPerformed(evt);
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

        jLabel22.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel22.setText("Religion");

        jLabel23.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel23.setText("recruited_date");

        religion.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(surname, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 320, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(fName, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel22))
                                .addGap(200, 208, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(PAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(distric, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nominee, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(marriedStatusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(section, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(designation, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(religion)
                                        .addComponent(mother, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(epfNo, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(72, 72, 72)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel20))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(elctroateField, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Gender, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(race, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(capacity, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel23))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(initials, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                                    .addComponent(NameInitial, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                                    .addComponent(Nic, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                                    .addComponent(DoB, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                                    .addComponent(father, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                                    .addComponent(recruitedDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(19, 19, 19))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(epfNo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel19)
                    .addComponent(NameInitial, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(fName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(initials, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(surname, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addComponent(Nic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(2, 2, 2))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(DoB, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mother, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(father, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(religion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recruitedDate, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(39, 39, 39)
                                .addComponent(jLabel16)
                                .addGap(36, 36, 36)
                                .addComponent(jLabel14)
                                .addGap(34, 34, 34)
                                .addComponent(jLabel15)
                                .addGap(43, 43, 43)
                                .addComponent(jLabel8))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(CAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(elctroateField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(Gender, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(race, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(capacity, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(31, 31, 31)
                        .addComponent(jLabel18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(PAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(distric, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(nominee, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(marriedStatusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(designation, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(section, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel24)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel21)
                        .addGap(34, 34, 34)
                        .addComponent(jLabel17)
                        .addGap(43, 43, 43)
                        .addComponent(jLabel20)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(843, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(34, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(924, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(87, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // ✅ Validate required fields
            if (epfNo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "EPF Number is required!",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ✅ Validate Recruited Date
            if (recruitedDate.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Recruited Date is required!",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            java.util.Date recruitedDateValue = recruitedDate.getDate();

            // ✅ Validate Gender
            RoleItem selectedGender = (RoleItem) Gender.getSelectedItem();
            if (selectedGender == null || selectedGender.getId() == 1) {
                JOptionPane.showMessageDialog(this, "Please select gender!",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String genderValue = selectedGender.getName();

            // ✅ Validate Date of Birth
            if (DoB.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date of Birth is required!",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            java.util.Date dob = DoB.getDate();

            // ✅ Validate NIC
            String nicValue = Nic.getText().trim();
            if (nicValue.isEmpty()) {
                JOptionPane.showMessageDialog(this, "NIC is required!",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ✅ Validate Phone
            String phone = Phone.getText().trim();
            if (phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mobile number is required!",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ✅ Validate Section
            RoleItem selectedSection = (RoleItem) section.getSelectedItem();
            if (selectedSection == null || selectedSection.getId() == 0) {
                JOptionPane.showMessageDialog(this, "Please select a section!",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int sectionId = selectedSection.getId();

            // ✅ Validate Designation Title
            RoleItem selectedDesignation = (RoleItem) designation.getSelectedItem();
            if (selectedDesignation == null || selectedDesignation.getId() == 0) {
                JOptionPane.showMessageDialog(this, "Please select a valid designation",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int designationId = selectedDesignation.getId();

            // ✅ Validate Capacity
            RoleItem selectedCapacity = (RoleItem) capacity.getSelectedItem();
            if (selectedCapacity == null || selectedCapacity.getId() == 0) {
                JOptionPane.showMessageDialog(this, "Please select a valid capacity",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int capacityId = selectedCapacity.getId();

            // ✅ Calculate Service End Date (60 years from DOB)
            java.util.Calendar calService = java.util.Calendar.getInstance();
            calService.setTime(dob);
            calService.add(java.util.Calendar.YEAR, 60);
            java.util.Date serviceEndDate = calService.getTime();

            // ✅ Calculate Days to Service End
            java.util.Date currentDate = new java.util.Date();
            long diffInMillies = serviceEndDate.getTime() - currentDate.getTime();
            long daysToServiceEnd = java.util.concurrent.TimeUnit.DAYS.convert(
                    diffInMillies, java.util.concurrent.TimeUnit.MILLISECONDS);

            // ✅ Calculate As Today (current date)
            java.util.Date asToday = new java.util.Date();

            // ✅ Calculate Confirmation Date (3 months from recruited date)
            java.util.Calendar calConfirmation = java.util.Calendar.getInstance();
            calConfirmation.setTime(recruitedDateValue);
            calConfirmation.add(java.util.Calendar.MONTH, 3);
            java.util.Date confirmationDate = calConfirmation.getTime();

            // ✅ Get religion value
            String religionValue = religion.getText().trim();
            if (religionValue.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Religion is required!",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ✅ UPDATE SQL Query (not INSERT)
            String sql = "UPDATE employee SET "
                    + "name_with_initial = ?, fname = ?, initials = ?, surname = ?, "
                    + "gender = ?, dob = ?, nic = ?, mobile = ?, father = ?, mother = ?, religion = ?, "
                    + "electroate = ?, permanate_address = ?, current_address = ?, nominee = ?, married_status = ?, "
                    + "district = ?, race = ?, designation_id = ?, capacity_id = ?, section_id = ?, "
                    + "joined_date = ?, recruited_date = ?, as_today = ?, confirmation_date = ?, "
                    + "service_end_date = ?, date_to_service_end = ?, last_modified = NOW() "
                    + "WHERE nic = ?";

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            int paramIndex = 1;

            // Personal Information
            stmt.setString(paramIndex++, NameInitial.getText().trim());
            stmt.setString(paramIndex++, fName.getText().trim());
            stmt.setString(paramIndex++, initials.getText().trim());
            stmt.setString(paramIndex++, surname.getText().trim().isEmpty() ? null : surname.getText().trim());
            stmt.setString(paramIndex++, genderValue);
            stmt.setString(paramIndex++, new SimpleDateFormat("yyyy-MM-dd").format(dob));
            stmt.setString(paramIndex++, nicValue.toUpperCase());
            stmt.setString(paramIndex++, phone);
            stmt.setString(paramIndex++, father.getText().trim().isEmpty() ? null : father.getText().trim());
            stmt.setString(paramIndex++, mother.getText().trim().isEmpty() ? null : mother.getText().trim());
            stmt.setString(paramIndex++, religionValue); // Already validated above

            // Address Information
            stmt.setString(paramIndex++, elctroateField.getText().trim().isEmpty() ? null : elctroateField.getText().trim());
            stmt.setString(paramIndex++, PAddress.getText().trim().isEmpty() ? null : PAddress.getText().trim());
            stmt.setString(paramIndex++, CAddress.getText().trim().isEmpty() ? null : CAddress.getText().trim());
            stmt.setString(paramIndex++, nominee.getText().trim().isEmpty() ? null : nominee.getText().trim());

            // ✅ FIX: Married Status - Use Integer instead of String
            RoleItem selectedMarriedStatus = (RoleItem) marriedStatusCombo.getSelectedItem();
            int marriedStatusId = 1; // Default to Married
            if (selectedMarriedStatus != null) {
                marriedStatusId = selectedMarriedStatus.getId(); // This should be 1 for Married, 2 for Unmarried
            }
            stmt.setInt(paramIndex++, marriedStatusId);

            stmt.setString(paramIndex++, distric.getText().trim().isEmpty() ? null : distric.getText().trim());
            stmt.setString(paramIndex++, race.getText().trim().isEmpty() ? null : race.getText().trim());

            // Foreign keys
            stmt.setInt(paramIndex++, designationId);
            stmt.setInt(paramIndex++, capacityId);
            stmt.setInt(paramIndex++, sectionId);

            // Dates
            stmt.setString(paramIndex++, new SimpleDateFormat("yyyy-MM-dd").format(recruitedDateValue)); // joined_date
            stmt.setString(paramIndex++, new SimpleDateFormat("yyyy-MM-dd").format(recruitedDateValue)); // recruited_date
            stmt.setString(paramIndex++, new SimpleDateFormat("yyyy-MM-dd").format(asToday)); // as_today
            stmt.setString(paramIndex++, new SimpleDateFormat("yyyy-MM-dd").format(confirmationDate)); // confirmation_date
            stmt.setString(paramIndex++, new SimpleDateFormat("yyyy-MM-dd").format(serviceEndDate)); // service_end_date
            stmt.setLong(paramIndex++, daysToServiceEnd); // date_to_service_end

            // WHERE clause - use original NIC to identify the record
            stmt.setString(paramIndex++, originalNIC);

            // ✅ Debug: Print parameter count and married status
            System.out.println("Total parameters set: " + (paramIndex - 1));
            System.out.println("Original NIC: " + originalNIC);
            System.out.println("Married Status ID: " + marriedStatusId);
            System.out.println("Selected Married Status: " + (selectedMarriedStatus != null ? selectedMarriedStatus.getName() : "null"));

            int res = stmt.executeUpdate();

            if (res > 0) {
                JOptionPane.showMessageDialog(this,
                        "Employee Updated Successfully!\n"
                        + "Name: " + fName.getText() + " " + initials.getText() + "\n"
                        + "EPF No: " + epfNo.getText() + "\n"
                        + "Married Status: " + (selectedMarriedStatus != null ? selectedMarriedStatus.getName() : "Unknown") + "\n"
                        + "Recruited Date: " + new SimpleDateFormat("yyyy-MM-dd").format(recruitedDateValue) + "\n"
                        + "Confirmation Date: " + new SimpleDateFormat("yyyy-MM-dd").format(confirmationDate) + "\n"
                        + "Service End Date: " + new SimpleDateFormat("yyyy-MM-dd").format(serviceEndDate),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                if (employeeRegistrationPanel != null) {
                    employeeRegistrationPanel.refreshTable();
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to update employee. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error updating employee: " + e.getMessage(),
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

    private void nomineeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomineeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomineeActionPerformed

    private void districActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_districActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_districActionPerformed

    private void raceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_raceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_raceActionPerformed

    private void elctroateFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elctroateFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elctroateFieldActionPerformed

    private void CAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CAddressActionPerformed

    private void PAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PAddressActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateEmployeeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // new UpdateEmployeeFrame().setVisible(true);
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
    private javax.swing.JComboBox<RoleItem> capacity;
    private javax.swing.JComboBox<RoleItem> designation;
    private javax.swing.JTextField distric;
    private javax.swing.JTextField elctroateField;
    private javax.swing.JTextField epfNo;
    private javax.swing.JTextField fName;
    private javax.swing.JTextField father;
    private javax.swing.JTextField initials;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JComboBox<RoleItem> marriedStatusCombo;
    private javax.swing.JTextField mother;
    private javax.swing.JTextField nominee;
    private javax.swing.JTextField race;
    private com.toedter.calendar.JDateChooser recruitedDate;
    private javax.swing.JTextField religion;
    private javax.swing.JComboBox<RoleItem> section;
    private javax.swing.JTextField surname;
    // End of variables declaration//GEN-END:variables
}
