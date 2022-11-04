import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.*;

public class Registration extends JDialog {
    private JPanel registerPanel;
    private JTextField nameField;
    private JTextField idField;
    private JTextField emailField;
    private JLabel name;
    private JLabel sid;
    private JLabel email;
    private JLabel pwd;
    private JPasswordField pwdfield;
    private JPasswordField cpwdfield;
    private JButton registerButton;
    private JButton cancelButton;
    private JButton btnClick;

    public Registration(JFrame parent) {
        super(parent);
        setTitle("Create new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        setVisible(true);
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                registerUser();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        int id = Integer.parseInt(idField.getText());
        String pwd = String.valueOf(pwdfield.getPassword());
        String cpwd = String.valueOf(cpwdfield.getPassword());

        if (name.isEmpty() || email.isEmpty() || id==0 || pwd.isEmpty() || cpwd.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please Enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!pwd.equals(cpwd)){
            JOptionPane.showMessageDialog(this,
                    "Passwords do not mtatch",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        st= addUserToDatabase(name, email, id, pwd);
        if (st!= null){
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this, "Failed to register new User",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public Student st;
    private Student addUserToDatabase(String name, String email, int id, String pwd) {
        Student st = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/studentmanagement";
        try {
            // Create Connection and Statement


//            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection c = DriverManager.getConnection( DB_URL,"root", "root");
            Statement s = c.createStatement();
            System.out.println("Connected");
            String sql=("INSERT INTO student (sid, name, ermail, password)" + "VALUES (?, ?, ?, ?);");
            PreparedStatement preparedStatement =c.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,email);
            preparedStatement.setString(4,pwd);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                st= new Student();
                st.id = id;
                st.name = name;
                st.email= email;
                st.pwd = pwd;

            }
            s.close();
            c.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return st;
    }


    public static void main(String[] args) {
        Registration form= new Registration( null);
        Student st = form.st;
        if (st != null){
            System.out.println("Successfully registered " + st.name);
        }
        else {
            System.out.println("Registration failed");
        }
    }
}
