import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.messaging.FirebaseMessagingException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;

public class SignUp extends JFrame{
    private JTextField nameField;
    private JTextField emailField;
    private JTextField idField;
    private JPasswordField passwordField;
    private JPanel panel2;
    private JButton addButton;
    FirebaseAuth mAuth;
    String name;
    int ID;
    String date_of_birth;
    String timestap;
    FirebaseDatabase database;
    DatabaseReference ref1, ref2;

    public SignUp(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(500,500));
        setContentPane(panel2);
        setTitle("Sign Up");
        pack();
        setVisible(true);
        setLocationRelativeTo(null);

        try{
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/resources/firebaseKey/covid19-tracer-896d2-firebase-adminsdk-avyl7-51f5f28c94.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://covid19-tracer-896d2-default-rtdb.firebaseio.com/")
                    .build();
            FirebaseApp firebaseApp = null;
            List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
            if(firebaseApps!=null && !firebaseApps.isEmpty()){
                for(FirebaseApp app : firebaseApps){
                    if(app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                        firebaseApp = app;
                }
            }
            else
                firebaseApp = FirebaseApp.initializeApp(options);
            database = FirebaseDatabase.getInstance();
            ref1 = database.getReference("Users");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertUserData();
                new LogIn();
            }

        });
    }

    private void insertUserData(){
        ref2 = FirebaseDatabase.getInstance().getReference().child("Users");
        String username1 = nameField.getText();
        String email1 = emailField.getText();
        String pass1 = new String(passwordField.getPassword());
        String id1 = idField.getText().toString();

        Users users = new Users(username1,email1,pass1,id1);
        ref2.push().setValueAsync(users);
        showMessageDialog(null, "User Added!");
    }
//int ID,String name,int age, String city,String timeStamp,String disease, String risk)
    public static class Users {
        String username;
        String email;
        String pass;
        String id;

        public Users(String username, String email, String pass, String id) {
            this.username = username;
            this.email = email;
            this.pass = pass;
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getPass() {
            return pass;
        }

        public String getId() {
            return id;
        }
    }
}
