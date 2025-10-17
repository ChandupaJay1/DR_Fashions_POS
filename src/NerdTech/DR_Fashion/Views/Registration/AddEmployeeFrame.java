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
        loadDesignations();  // title listener ekath meken setup wenawa
        loadSections();
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

            // 👉 Default item
            section.addItem(new RoleItem(0, "-- Select Section --"));

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("section_name");
                System.out.println("Adding section: " + id + " - " + name); // debug
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

            // 👉 Default item
            designation.addItem(new RoleItem(0, "-- Select Designation --"));

            while (rs.next()) {
                String capacity = rs.getString("capacity");
                designation.addItem(new RoleItem(0, capacity)); // id not needed here
            }

            // Listener එක add කරන්න. Repeated add වෙන්නෙ නැති විදිහට check කරන්න
            for (ActionListener al : designation.getActionListeners()) {
                designation.removeActionListener(al); // remove old listeners if any
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
        marriedStatusCombo.addItem(new RoleItem(1, "Select Status"));
        marriedStatusCombo.addItem(new RoleItem(2, "Married"));
        marriedStatusCombo.addItem(new RoleItem(3, "Unmarried"));
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
        marriedStatusCombo = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        title = new javax.swing.JComboBox<>();
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

        jLabel19.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel19.setText("Epf No");

        epfNo.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel20.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel20.setText("Title");

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
                    .addComponent(jLabel8)
                    .addComponent(jLabel16)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(father, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(marriedStatusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(distric, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(designation, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fName, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(epfNo, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(79, 79, 79)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lName, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nominee, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(race, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(section, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(DoB, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(280, 280, 280)
                                .addComponent(NameInitial, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
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
                                        .addComponent(jLabel7)
                                        .addGap(238, 238, 238)
                                        .addComponent(Nic, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(90, 90, 90))
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
                                        .addComponent(marriedStatusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(distric, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(designation, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel20))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(fName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(392, 392, 392)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(section, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18))
                                .addGap(4, 4, 4)))
                        .addGap(215, 215, 215))
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
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(248, 248, 248)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(nominee, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel14))
                                        .addGap(24, 24, 24)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(race, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel17)))
                                    .addComponent(DoB, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(87, 87, 87))))
        );

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Add Employee");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 717, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
            String gender = "Male";

            // SQL query - EMAIL එක REMOVE කරලා (20 parameters only)
            String sql = "INSERT INTO employee (epf_no, name_with_initial, fname, lname, gender, "
                    + "dob, nic, mobile, status, father, mother, permanate_address, "
                    + "current_address, nominee, married_status, district, race, "
                    + "designation_id, section_id, joined_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Set all parameters (EMAIL නැතුව)
            stmt.setString(1, epfNo.getText().trim());
            stmt.setString(2, NameInitial.getText().trim());
            stmt.setString(3, fName.getText().trim());
            stmt.setString(4, lName.getText().trim());
            stmt.setString(5, gender);
            stmt.setString(6, new SimpleDateFormat("yyyy-MM-dd").format(DoB.getDate()));
            stmt.setString(7, nic.toUpperCase());
            stmt.setString(8, phone);
            stmt.setString(9, "active");
            stmt.setString(10, father.getText().trim());
            stmt.setString(11, mother.getText().trim());
            stmt.setString(12, PAddress.getText().trim());
            stmt.setString(13, CAddress.getText().trim());
            stmt.setString(14, nominee.getText().trim());
            stmt.setInt(15, ((RoleItem) marriedStatusCombo.getSelectedItem()).getId());

            stmt.setString(16, distric.getText().trim());
            stmt.setString(17, race.getText().trim());

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
            stmt.setInt(18, selectedTitle.getId());

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
            stmt.setInt(19, selectedSection.getId());

            // Joined date - අද දිනය
            stmt.setString(20, new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));

            // Execute insert
            int res = stmt.executeUpdate();

            if (res > 0) {
                JOptionPane.showMessageDialog(this,
                        "Employee Registered Successfully!\n"
                        + "Name: " + fName.getText() + " " + lName.getText() + "\n"
                        + "EPF No: " + epfNo.getText(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                clearFields();

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

    private void clearFields() {
        fName.setText("");
        lName.setText("");
        NameInitial.setText("");
        Nic.setText("");
        // Email.setText("");  // <-- මේක comment out කරන්න හෝ delete කරන්න
        DoB.setDate(null);
        Phone.setText("");
        epfNo.setText("");
        father.setText("");
        mother.setText("");
        CAddress.setText("");
        PAddress.setText("");
        nominee.setText("");
        distric.setText("");
        race.setText("");

        // ComboBox reset කරන්න
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
                // Testing mode - employeeRegistrationPanel නැතුව run කරන්න පුළුවන්
                new AddEmployeeFrame(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CAddress;
    private com.toedter.calendar.JDateChooser DoB;
    private javax.swing.JTextField NameInitial;
    private javax.swing.JTextField Nic;
    private javax.swing.JTextField PAddress;
    private javax.swing.JTextField Phone;
    private javax.swing.JComboBox<RoleItem> designation;
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
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JComboBox<RoleItem> title;
    // End of variables declaration//GEN-END:variables
}
