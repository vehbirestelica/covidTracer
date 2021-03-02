import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.JOptionPane.showMessageDialog;

public class Main extends JFrame{
    private JPanel panel;
    private JTextField searchID;
    private JButton addNewCaseButton;
    private JTable table1;
    private JButton searchButton;
    public JScrollPane tableSP;
    private JTable table2;
    private JScrollPane tableSP1;
    private JLabel last24hourLabel;
    private JButton updateButton;
    private JButton deleteButton;
    FirebaseDatabase database;
    DatabaseReference ref, ref2;

    public Main(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(700, 380));
        setContentPane(panel);
        setTitle("Check the COVID-19 Patients");
        pack();
        setVisible(true);
        setLocationRelativeTo(null);

        try {
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/resources/firebaseKey/covid19-tracer-896d2-firebase-adminsdk-avyl7-51f5f28c94.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://covid19-tracer-896d2-default-rtdb.firebaseio.com/")
                    .build();
            if (FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(options);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("NewCases");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DefaultTableModel tableModel = new DefaultTableModel();
                tableModel.addColumn("ID");
                tableModel.addColumn("Name");
                tableModel.addColumn("Age");
                tableModel.addColumn("City");
                tableModel.addColumn("TimeStamp");
                tableModel.addColumn("Disease");
                tableModel.addColumn("Risk");
                Object[] row = new Object[7];
                tableModel.setNumRows(0);

                StringBuilder builder = new StringBuilder();
                CasesList casesList = new CasesList();
//                Runnable r = new Runnable() {
//                    @Override
//                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            casesList.setID(ds.child("id").getValue(Integer.class));
                            casesList.setName(ds.child("name").getValue().toString());
                            casesList.setAge(ds.child("age").getValue(Integer.class));
                            casesList.setCity(ds.child("city").getValue().toString());
                            casesList.setTimeStamp(ds.child("timeStamp").getValue().toString());
                            casesList.setDisease(ds.child("disease").getValue().toString());
                            casesList.setRisk(ds.child("risk").getValue().toString());

                            builder.append(row).append("\n");
                            row[0] = casesList.getID();
                            row[1] = casesList.getName();
                            row[2] = casesList.getAge();
                            row[3] = casesList.getCity();
                            row[4] = casesList.getTimeStamp();
                            row[5] = casesList.getDisease();
                            row[6] = casesList.getRisk();

                        tableModel.addRow(row);
                        }
                        table1.setModel(tableModel);
                    }
//                };
//                new Thread(r).start();
//            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails());
            }
        });
        addNewCaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewCase();
            }
        });
        panel.setLayout(null);
        tableSP.setVisible(false);
        last24hourLabel.setBounds(15,110,150,15);
        addNewCaseButton.setBounds(559,95,116,25);
        updateButton.setBounds(475,95,76,25);
        deleteButton.setBounds(391,95,75,25);
        tableSP.setBounds(15,45,660,39);
        tableSP1.setBounds(15,125,660,210);
        updateButton.setVisible(false);
        deleteButton.setVisible(false);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int searchInt = Integer.parseInt(searchID.getText());
                tableSP.setVisible(true);
                ref.orderByChild("id").equalTo(searchInt).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DefaultTableModel tableModel = new DefaultTableModel();
                        tableModel.addColumn("ID");
                        tableModel.addColumn("Name");
                        tableModel.addColumn("Age");
                        tableModel.addColumn("City");
                        tableModel.addColumn("TimeStamp");
                        tableModel.addColumn("Disease");
                        tableModel.addColumn("Risk");

                        StringBuilder builder2 = new StringBuilder();
                        CasesList casesList = new CasesList();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            casesList.setID(ds.child("id").getValue(Integer.class));
                            casesList.setName(ds.child("name").getValue().toString());
                            casesList.setAge(ds.child("age").getValue(Integer.class));
                            casesList.setCity(ds.child("city").getValue().toString());
                            casesList.setTimeStamp(ds.child("timeStamp").getValue().toString());
                            casesList.setDisease(ds.child("disease").getValue().toString());
                            casesList.setRisk(ds.child("risk").getValue().toString());

                        String[] rows = {String.valueOf(casesList.getID()),casesList.getName(), String.valueOf(casesList.getAge()),casesList.getCity(), casesList.getTimeStamp(),casesList.getDisease(), casesList.getRisk()};
                            tableModel.addRow(rows);
                        }
                        table2.setVisible(true);
                        table2.setModel(tableModel);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                updateButton.setVisible(true);
                deleteButton.setVisible(true);
            }
        });
        ref2 = database.getReference("NewCases/");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int searchInt2 = Integer.parseInt(searchID.getText());
                ref2.child("id").equalTo(searchInt2).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DefaultTableModel tableModel = new DefaultTableModel();
                        int column = table2.getSelectedColumn();
                        int row = table2.getSelectedRow();
                        String value = String.valueOf(table2.getModel().getValueAt(row, column));
                        String tableHeader = table2.getColumnName(column).toLowerCase();
                        System.out.println(value+"  "+column+" "+tableHeader);
                        System.out.println(searchInt2);
                        String caseID = String.valueOf(searchInt2);

                        ref2.child("id").equalTo(caseID).getRef().child(tableHeader).setValueAsync(value);

//                        if (column==1){
//                            ref2.child("name").setValueAsync(value);
//                        } else if (column==2){
//                            ref2.child("age").setValueAsync(value);
//                        } else if (column==3){
//                            ref2.child("city").setValueAsync(value);
//                        } else if (column==4){
//                            ref2.child("timeStamp").setValueAsync(value);
//                        } else if (column==5){
//                            ref2.child("disease").setValueAsync(value);
//                        } else if (column==6){
//                            ref2.child("risk").setValueAsync(value);
//                        } else {
//                            showMessageDialog(null,"ID cannot be changed!");
//                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int searchInt2 = Integer.parseInt(searchID.getText());
                ref2.child("id").equalTo(searchInt2).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int columnDelete = table2.getSelectedColumn();
                        int rowDelete = table2.getSelectedRow();
                        String value = String.valueOf(table2.getModel().getValueAt(rowDelete, columnDelete));
                        System.out.println(value+"  "+columnDelete+" "+rowDelete);
                        System.out.println(searchInt2);
                        String caseID = String.valueOf(searchInt2);

                        DefaultTableModel tableModel = new DefaultTableModel();
                        table2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                        if (table2.getSelectedRow() != -1){
                            tableModel.removeRow(rowDelete);
                        }
                        table2.setModel(tableModel);
                        table1.setModel(tableModel);
                        ref.child(String.valueOf(columnDelete)).removeValueAsync();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public static void main(String[] args) {
//        new LogIn();
        new Main();
    }
}
