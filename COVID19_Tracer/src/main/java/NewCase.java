import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.TextAttribute;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static javax.swing.JOptionPane.showMessageDialog;

public class NewCase extends JFrame{
    private JPanel panel1;
    private JTextField nameField;
    private JTextField timeStampField;
    private JTextField diseaseField;
    private JComboBox cityComboBox;
    private JTextField ageField;
    private JRadioButton lowRadioButton;
    private JRadioButton moderateRadioButton;
    private JRadioButton highRadioButton;
    private JButton addCaseButton;
    private JLabel patientID;
    private JCheckBox byCreatingAnAccountCheckBox;
    private JLabel errorLabel;
    private ButtonGroup group;
    DatabaseReference ref2;

    public NewCase(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(600, 380));
        setContentPane(panel1);
        setTitle("Register New Case");
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        getContentPane().requestFocusInWindow();

        Font font = diseaseField.getFont();
        Map map = font.getAttributes();
        map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        nameField.setFont(font.deriveFont(map));
        diseaseField.setFont(font.deriveFont(map));
        ageField.setFont(font.deriveFont(map));
        timeStampField.setFont(font.deriveFont(map));

        group = new ButtonGroup();
        lowRadioButton.setActionCommand("Low");
        moderateRadioButton.setActionCommand("Moderate");
        highRadioButton.setActionCommand("High");
        group.add(lowRadioButton);
        group.add(moderateRadioButton);
        group.add(highRadioButton);
        Color color2 = new Color(34,133,145);
        cityComboBox.addItem("Select your City: ");
        cityComboBox.addItem("Prishtina");
        cityComboBox.addItem("Prizren");
        cityComboBox.addItem("Podujeva");
        cityComboBox.addItem("Peja");
        cityComboBox.addItem("Lipjan");
        cityComboBox.addItem("Ferizaj");
        cityComboBox.addItem("Gjilan");
        cityComboBox.setBorder(BorderFactory.createLineBorder(color2,1));

        Font font2 = new Font("Cansolas", Font.ITALIC,16);
        nameField.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        nameField.setText("Please enter patient's name: ");
        nameField.setForeground(Color.darkGray);
        nameField.setFont(font2);
        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                nameField.setText("");
                nameField.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().equals("")){
                    nameField.setText("Please enter patient's name: ");
                    nameField.setForeground(Color.darkGray);
                }
            }
        });

        diseaseField.setText("Does the patient have any disease?");
        diseaseField.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        diseaseField.setForeground(Color.darkGray);
        diseaseField.setFont(font2);
        diseaseField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                diseaseField.setText("");
                diseaseField.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (diseaseField.getText().equals("")){
                    diseaseField.setText("Does the patient have any disease?");
                    diseaseField.setForeground(Color.darkGray);
                }
            }
        });

        timeStampField.setText("For how long has the patient been infected?");
        timeStampField.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        timeStampField.setForeground(Color.darkGray);
        timeStampField.setFont(font2);
        timeStampField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                timeStampField.setText("");
                timeStampField.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (timeStampField.getText().equals("")){
                    timeStampField.setText("For how long has the patient been infected?");
                    timeStampField.setForeground(Color.darkGray);
                }
            }
        });

        ageField.setText("Enter patient's age...");
        ageField.setForeground(Color.darkGray);
        ageField.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        ageField.setFont(font2);
        ageField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                ageField.setText("");
                ageField.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ageField.getText().equals("")){
                    ageField.setText("Enter patient's age...");
                    ageField.setForeground(Color.darkGray);
                }
            }
        });

        Random randomNum = new Random();
        patientID.setText(String.valueOf(randomNum.nextInt(Integer.MAX_VALUE)));
        Font font3 = patientID.getFont();
        Map map2 = font3.getAttributes();
        map2.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        patientID.setFont(font3.deriveFont(map2));

        Color color = new Color(255, 255, 255);

        nameField.setBackground(color);
        cityComboBox.setBackground(color);
        diseaseField.setBackground(color);
        ageField.setBackground(color);
        timeStampField.setBackground(color);
        firebaseConn();

        addCaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!byCreatingAnAccountCheckBox.isSelected()) {
                    errorLabel.setText("*You have to agree our Terms and Conditions!");
                }
                else{
                    insertNewCase();
                }
            }
        });
    }
    public void firebaseConn(){
        try {
            FileInputStream serciveAccount =
                    new FileInputStream("src/main/resources/firebaseKey/covid19-tracer-896d2-firebase-adminsdk-avyl7-51f5f28c94.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serciveAccount))
                    .setDatabaseUrl("https://covid19-tracer-896d2-default-rtdb.firebaseio.com/")
                    .build();
            FirebaseApp firebaseApp = null;
            List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
            if (firebaseApps != null && !firebaseApps.isEmpty()) {
                for (FirebaseApp app : firebaseApps) {
                    if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                        firebaseApp = app;
                }
            } else
                firebaseApp = FirebaseApp.initializeApp(options);

        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
    private void insertNewCase(){
        ref2 = FirebaseDatabase.getInstance().getReference().child("NewCases");
        String name = nameField.getText();
        String city = cityComboBox.getSelectedItem().toString();
        String disease = diseaseField.getText();
        int id = Integer.parseInt(patientID.getText());
        int age = Integer.parseInt(ageField.getText());
        String risk = group.getSelection().getActionCommand();
        String timeStamp = timeStampField.getText();

        CasesList addCase = new CasesList(id,name,age,city,timeStamp,disease,risk);
        ref2.push().setValueAsync(addCase);
        showMessageDialog(null, "New Case Added!");

    }
}
