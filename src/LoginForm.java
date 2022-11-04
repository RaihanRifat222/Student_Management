import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog {
    private JLabel email;
    private JTextField emailField;
    private JLabel pwd;

    private JButton logInButton;
    private JButton canelButton;
    private JPanel loginPanel;
    private JPasswordField pwdField;


    public LoginForm(JFrame parent) {
        super(parent);
        setTitle("Create new account");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String pwd = String.valueOf(pwdField.getPassword());

                st = getAuthenticatedUser(email, pwd);

                if(st!= null){
//                    JOptionPane.showMessageDialog(LoginForm.this,
//                            "You are successfully logged in!"
//                    );
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or Password invalid"
                            );
                }
            }
        });
        canelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    public Student st;

    private Student getAuthenticatedUser(String email, String pwd) {
        Student st = null;

        final String DB_URL = "jdbc:mysql://localhost:3306/studentmanagement";
        try {
            // Create Connection and Statement


//            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection c = DriverManager.getConnection(DB_URL, "root", "root");
            Statement s = c.createStatement();
            String sql = "Select * from student where ermail = ? and password=?;";
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pwd);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                st= new Student();
                st.name= resultSet.getString("name");
                st.email= resultSet.getString("ermail");
                st.pwd= resultSet.getString("password");
                st.id=resultSet.getInt("sid");
            }

            s.close();
            c.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return st;
    }

        public static void main (String[]args){
            LoginForm loginForm = new LoginForm(null);
            Student st = loginForm.st;
            if(st !=null){
                System.out.println("Authetification successful of "+ st.name);
            }
            else {
                System.out.println("Log in failed");
            }
        }
    }







