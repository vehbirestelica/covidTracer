import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LogIn extends JFrame {
    private JPanel panel1;
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginBtn;
    private JLabel registerlbl;
    FirebaseDatabase database;
    DatabaseReference ref1, ref2;
    FirebaseAuth mAuth;

    public LogIn() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 500));
        setContentPane(panel1);
        setTitle("Log In");
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
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object document = dataSnapshot.getValue();
                    String username = idField.getText();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Font font = registerlbl.getFont();
        Map<TextAttribute, Object> underline = new HashMap<>(font.getAttributes());
        underline.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);

        Map<TextAttribute, Object> noUnderline = new HashMap<>(font.getAttributes());
        noUnderline.put(TextAttribute.UNDERLINE, -1);

        registerlbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerlbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new SignUp();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                registerlbl.setFont(font.deriveFont(underline));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerlbl.setFont(font.deriveFont(noUnderline));
            }
        });
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int doctorID = Integer.parseInt(idField.getText());
                String pass = new String(passwordField.getPassword());
                try {
                    if (doctorID>1000 && doctorID<3000){
                        new Main();
                    } else {
                        JOptionPane.showMessageDialog(null,"You must be an admin in order to LogIn!","ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }


}
