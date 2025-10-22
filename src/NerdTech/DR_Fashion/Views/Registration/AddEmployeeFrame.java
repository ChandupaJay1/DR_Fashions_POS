package NerdTech.DR_Fashion.Views.Registration;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.RoleItems.RoleItem;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 *
 * @author MG_Pathum
 */
public class AddEmployeeFrame extends javax.swing.JFrame {

    private EmployeeRegistration employeeRegistrationPanel;

    public AddEmployeeFrame(EmployeeRegistration employeeRegistrationPanel) {
        this.employeeRegistrationPanel = employeeRegistrationPanel;
        initComponents();
        DoB.setDateFormatString("yyyy-MM-dd");
        DoB.setFont(new java.awt.Font("JetBrains Mono", 0, 18));
        ((javax.swing.JTextField) DoB.getDateEditor().getUiComponent()).setEditable(false);

        loadMarriedStatus();
        loadDesignations();
        loadSections();
        loadGenderOptions();
    }

    private AddEmployeeFrame() {
        initComponents();
        DoB.setDateFormatString("yyyy-MM-dd");
        DoB.setFont(new java.awt.Font("JetBrains Mono", 0, 18));
        ((javax.swing.JTextField) DoB.getDateEditor().getUiComponent()).setEditable(false);
    }

    private void loadSections() {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT id, section_name FROM section"); ResultSet rs = stmt.executeQuery()) {

            System.out.println("Connection OK, querying sections...");

            section.removeAllItems();
            section.addItem(new RoleItem(0, "-- Select Section --"));

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("section_name");
                System.out.println("Adding section: " + id + " - " + name);
                section.addItem(new RoleItem(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading sections: " + e.getMessage());
        }
    }

    private void loadDesignations() {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT capacity FROM designation"); ResultSet rs = stmt.executeQuery()) {

            designation.removeAllItems();
            designation.addItem(new RoleItem(0, "-- Select Designation --"));

            while (rs.next()) {
                String capacity = rs.getString("capacity");
                designation.addItem(new RoleItem(0, capacity));
            }

            for (ActionListener al : designation.getActionListeners()) {
                designation.removeActionListener(al);
            }
            designation.addActionListener(e -> loadTitlesBasedOnDesignation());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading designations: " + e.getMessage());
        }
    }

    private void loadTitlesBasedOnDesignation() {
        title.removeAllItems();
        RoleItem selected = (RoleItem) designation.getSelectedItem();
        if (selected == null) {
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, title FROM designation WHERE capacity = ?")) {

            stmt.setString(1, selected.getName());
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    title.addItem(new RoleItem(0, "No titles available"));
                    return;
                }
                while (rs.next()) {
                    title.addItem(new RoleItem(rs.getInt("id"), rs.getString("title")));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading titles: " + e.getMessage());
        }
    }

    private void loadMarriedStatus() {
        marriedStatusCombo.removeAllItems();
        marriedStatusCombo.addItem(new RoleItem(1, "Married"));
        marriedStatusCombo.addItem(new RoleItem(2, "Unmarried"));
    }

    private void loadGenderOptions() {
        Gender.removeAllItems();
        Gender.addItem(new RoleItem(1, "-- Select Gender --"));
        Gender.addItem(new RoleItem(2, "Male"));
        Gender.addItem(new RoleItem(3, "Female"));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        title = new javax.swing.JComboBox<>();
        Gender = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        surname = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        elctroate = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        CAddress = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        PAddress = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        Nic.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Register ");
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

        lName.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        DoB.setForeground(new java.awt.Color(255, 255, 255));

        Phone.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel5.setText("Date of Birth");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel3.setText("Last Name");

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
        jLabel20.setText("Title");

        title.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        Gender.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel21.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel21.setText("Gender");

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel4.setText("Surname");

        surname.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel24.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel24.setText("Elctroate");

        elctroate.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        elctroate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elctroateActionPerformed(evt);
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
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(PAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(mother, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(surname, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(distric, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(nominee, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(marriedStatusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel18)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(section, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(designation, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(fName, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel19)
                                            .addGap(204, 204, 204)
                                            .addComponent(epfNo, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(66, 66, 66)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel20))
                                .addGap(56, 56, 56)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(elctroate, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Gender, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(race, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 19, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lName, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NameInitial, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Nic, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(DoB, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(father, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(19, 19, 19))))))
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
                    .addComponent(lName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(PAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(CAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(distric, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(elctroate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(nominee, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(Gender, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(marriedStatusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(race, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(designation, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(section, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Add Employee");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 61, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // ✅ Validate all required fields
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

            // Phone validation
            String phone = Phone.getText().trim();
            if (!phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this,
                        "Phone number must be 10 digits",
                        "Invalid Phone",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // NIC validation
            String nic = Nic.getText().trim();
            if (!nic.matches("\\d{9}[Vv]|\\d{12}")) {
                JOptionPane.showMessageDialog(this,
                        "NIC must be in format: 123456789V or 123456789012",
                        "Invalid NIC",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Gender validation
            RoleItem selectedGender = (RoleItem) Gender.getSelectedItem();
            if (selectedGender == null || selectedGender.getId() == 1) {
                JOptionPane.showMessageDialog(this,
                        "Please select a gender",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String gender = selectedGender.getName();

            // ✅ Get Date of Birth
            java.util.Date dob = DoB.getDate();
            java.util.Date currentDate = new java.util.Date();

            // ✅ AUTO CALCULATE Service End Date (DOB + 60 years)
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(dob);
            cal.add(java.util.Calendar.YEAR, 60); // Retirement at 60 years
            java.util.Date serviceEndDate = cal.getTime();

            // ✅ AUTO CALCULATE Date To Service End (days between current date and service end date)
            long diffInMillies = Math.abs(serviceEndDate.getTime() - currentDate.getTime());
            long daysToServiceEnd = diffInMillies / (1000 * 60 * 60 * 24);

            // ✅ CORRECTED SQL query with AUTO CALCULATED fields
            String sql = "INSERT INTO employee (epf_no, name_with_initial, fname, lname, surname, "
                    + "gender, dob, nic, mobile, status, father, mother, "
                    + "electroate, permanate_address, current_address, nominee, married_status, "
                    + "district, race, designation_id, section_id, joined_date, "
                    + "service_end_date, date_to_service_end) " // AUTO CALCULATED fields
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            int paramIndex = 1;

            stmt.setString(paramIndex++, epfNo.getText().trim());                    // 1
            stmt.setString(paramIndex++, NameInitial.getText().trim());              // 2
            stmt.setString(paramIndex++, fName.getText().trim());                    // 3
            stmt.setString(paramIndex++, lName.getText().trim());                    // 4
            stmt.setString(paramIndex++, surname.getText().trim().isEmpty() ? null : surname.getText().trim()); // 5
            stmt.setString(paramIndex++, gender);                                    // 6
            stmt.setString(paramIndex++, new SimpleDateFormat("yyyy-MM-dd").format(dob)); // 7
            stmt.setString(paramIndex++, nic.toUpperCase());                         // 8
            stmt.setString(paramIndex++, phone);                                     // 9
            stmt.setString(paramIndex++, "active");                                  // 10
            stmt.setString(paramIndex++, father.getText().trim().isEmpty() ? null : father.getText().trim()); // 11
            stmt.setString(paramIndex++, mother.getText().trim().isEmpty() ? null : mother.getText().trim()); // 12
            stmt.setString(paramIndex++, elctroate.getText().trim().isEmpty() ? null : elctroate.getText().trim()); // 13
            stmt.setString(paramIndex++, PAddress.getText().trim().isEmpty() ? null : PAddress.getText().trim()); // 14
            stmt.setString(paramIndex++, CAddress.getText().trim().isEmpty() ? null : CAddress.getText().trim()); // 15
            stmt.setString(paramIndex++, nominee.getText().trim().isEmpty() ? null : nominee.getText().trim()); // 16

            // Married status
            RoleItem selectedMarriedStatus = (RoleItem) marriedStatusCombo.getSelectedItem();
            String marriedStatus = "0";
            if (selectedMarriedStatus != null) {
                if (selectedMarriedStatus.getName().equals("Married")) {
                    marriedStatus = "1";
                } else if (selectedMarriedStatus.getName().equals("Unmarried")) {
                    marriedStatus = "0";
                }
            }
            stmt.setString(paramIndex++, marriedStatus);                             // 17

            stmt.setString(paramIndex++, distric.getText().trim().isEmpty() ? null : distric.getText().trim()); // 18
            stmt.setString(paramIndex++, race.getText().trim().isEmpty() ? null : race.getText().trim()); // 19

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
            stmt.setInt(paramIndex++, selectedTitle.getId());                        // 20

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
            stmt.setInt(paramIndex++, selectedSection.getId());                      // 21

            // joined_date
            stmt.setString(paramIndex++, new SimpleDateFormat("yyyy-MM-dd").format(currentDate)); // 22

            // ✅ AUTO CALCULATED: Service End Date and Date To Service End
            stmt.setString(paramIndex++, new SimpleDateFormat("yyyy-MM-dd").format(serviceEndDate)); // 23
            stmt.setLong(paramIndex++, daysToServiceEnd);                            // 24

            // Debug information
            System.out.println("AUTO CALCULATED VALUES:");
            System.out.println("Service End Date: " + new SimpleDateFormat("yyyy-MM-dd").format(serviceEndDate));
            System.out.println("Days to Service End: " + daysToServiceEnd + " days");

            // Execute insert
            int res = stmt.executeUpdate();

            if (res > 0) {
                JOptionPane.showMessageDialog(this,
                        "Employee Registered Successfully!\n"
                        + "Name: " + fName.getText() + " " + lName.getText() + "\n"
                        + "EPF No: " + epfNo.getText() + "\n"
                        + "Service End Date: " + new SimpleDateFormat("yyyy-MM-dd").format(serviceEndDate) + "\n"
                        + "Remaining Service Days: " + daysToServiceEnd + " days",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                clearFields();

                // Refresh the table in EmployeeRegistration panel
                if (employeeRegistrationPanel != null) {
                    employeeRegistrationPanel.refreshTable();
                }

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

    private void elctroateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elctroateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elctroateActionPerformed

    private void PAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PAddressActionPerformed

    private void CAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CAddressActionPerformed

    private void clearFields() {
        fName.setText("");
        lName.setText("");
        surname.setText("");
        NameInitial.setText("");
        Nic.setText("");
        DoB.setDate(null);
        Phone.setText("");
        epfNo.setText("");
        father.setText("");
        mother.setText("");
        elctroate.setText("");
        CAddress.setText("");
        PAddress.setText("");
        nominee.setText("");
        distric.setText("");
        race.setText("");

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
        if (title.getItemCount() > 0) {
            title.setSelectedIndex(0);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacLightLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddEmployeeFrame(null).setVisible(true);
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
    private javax.swing.JComboBox<RoleItem> designation;
    private javax.swing.JTextField distric;
    private javax.swing.JTextField elctroate;
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
    private javax.swing.JTextField lName;
    private javax.swing.JComboBox<RoleItem> marriedStatusCombo;
    private javax.swing.JTextField mother;
    private javax.swing.JTextField nominee;
    private javax.swing.JTextField race;
    private javax.swing.JComboBox<RoleItem> section;
    private javax.swing.JTextField surname;
    private javax.swing.JComboBox<RoleItem> title;
    // End of variables declaration//GEN-END:variables
}
