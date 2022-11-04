import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Courses extends JFrame {
    private JPanel coursePanel;
    private JComboBox selectPanel;
    private JButton viewCourseButton;
    private JButton enrollButton;
    private JLabel load;
    private JButton enrolledStudentsButton;

    public int sid;

    public Courses(){
        setTitle("Courses");
        setContentPane(coursePanel);
        setMinimumSize(new Dimension(500, 429));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        boolean hasRegisteredUsers = connectToDatabase();

        if (hasRegisteredUsers) {
            //show Login form
            LoginForm loginForm = new LoginForm(this);

            Student st = loginForm.st;


            if (st != null) {
                sid= st.id;
                load.setText("loading courses");
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
        }
        else {
            //show Registration form
            Registration registration = new Registration(this);
            Student st = registration.st;

            if (st != null) {
                sid= st.id;
                load.setText("loading courses");
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
        }


        viewCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showdetails((selectPanel.getSelectedItem()).toString());
            }
        });
        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enrollCourse((selectPanel.getSelectedItem()).toString());
            }
        });
        enrolledStudentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStudents((selectPanel.getSelectedItem()).toString());

            }
        });
    }

    String name;
    String email;
    Integer id;
    private void showStudents(String selectedItem) {
        final String DB_URL = "jdbc:mysql://localhost/studentmanagement";
        final String USERNAME = "root";
        final String PASSWORD = "root";
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            String sql= ("SELECT * FROM student WHERE course= ? ");
            PreparedStatement preparedStatement =conn.prepareStatement(sql);

            preparedStatement.setString(1, selectedItem);
            ResultSet resultSet = preparedStatement.executeQuery();

//            int addedRows = preparedStatement.executeUpdate();
            if (resultSet.next()){

                name = resultSet.getString("name");

                email = resultSet.getString("ermail");
                id = resultSet.getInt("sid");
                JOptionPane.showMessageDialog(this,
                        "Name - "+ name + "---- email- " + email + "---- ID- " + id.toString()
                );

            }



            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    Integer available_seats;
    String en_course;
    private void enrollCourse(String selectedItem) {
        final String DB_URL = "jdbc:mysql://localhost/studentmanagement";
        final String USERNAME = "root";
        final String PASSWORD = "root";
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            String sql= ("SELECT seats FROM courses WHERE course= ? ");
            PreparedStatement preparedStatement =conn.prepareStatement(sql);

            preparedStatement.setString(1, selectedItem);
            ResultSet resultSet = preparedStatement.executeQuery();

//            int addedRows = preparedStatement.executeUpdate();
            if (resultSet.next()){

                available_seats = resultSet.getInt("seats");

                if (available_seats>0){

                    conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                    statement = conn.createStatement();
                    sql= ("SELECT course from student WHERE sid= ? ");
                    preparedStatement =conn.prepareStatement(sql);
                    preparedStatement.setInt(1, sid);
                    ResultSet en = preparedStatement.executeQuery();

                    if (en.next()){

                        en_course= en.getString("course");
                        System.out.println(en_course);


                    }
//                    System.out.println(en_course);
                    if (en_course!= null && en_course.equals(selectedItem)){
                        JOptionPane.showMessageDialog(this, "You already have this course.");
                    }
                    else {
                        sql= ("UPDATE student SET course = ? WHERE sid= ?");
                        preparedStatement =conn.prepareStatement(sql);

                        preparedStatement.setString(1, selectedItem);
                        preparedStatement.setInt(2, sid);
                        preparedStatement.executeUpdate();

                        sql =("UPDATE courses SET seats = seats-1 WHERE course = ?");
                        preparedStatement =conn.prepareStatement(sql);

                        preparedStatement.setString(1, selectedItem);
                        preparedStatement.executeUpdate();
                    }




                }
                else {
                    JOptionPane.showMessageDialog(this, "Try other Sections! No seats remaining here!");
                }
            }




            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }






    public Course c;
    String course;
    String routine;
    Integer seats;
    private void showdetails(String selectedItem) {
        final String DB_URL = "jdbc:mysql://localhost/studentmanagement";
        final String USERNAME = "root";
        final String PASSWORD = "root";
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            String sql= ("SELECT * FROM courses WHERE course= ? ");
            PreparedStatement preparedStatement =conn.prepareStatement(sql);

            preparedStatement.setString(1, selectedItem);
            ResultSet resultSet = preparedStatement.executeQuery();

//            int addedRows = preparedStatement.executeUpdate();
            if (resultSet.next()){

                course = resultSet.getString("course");
                System.out.println(selectedItem);
                routine = resultSet.getString("routine");
                seats = resultSet.getInt("seats");
            }

            JOptionPane.showMessageDialog(this,
                    course+ "---- " + routine + "---- Seats remaining " + seats.toString()
                    );


            statement.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private boolean connectToDatabase() {
        boolean hasRegistredUsers = false;

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost/";
        final String DB_URL = "jdbc:mysql://localhost/studentmanagement";
        final String USERNAME = "root";
        final String PASSWORD = "root";

        try {
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS studentmanagement");
            statement.close();
            conn.close();


            //Second, connect to the database and create the table "student" if cot created
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS student ("
                    + "sid INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(45) NULL,"
                    + "ermail VARCHAR(45) NULL,"

                    + "password VARCHAR(45) NULL,"
                    + "course VARCHAR(45) NULL,"
                    + "PRIMARY KEY(sid)"
                    + ");";

            statement.executeUpdate(sql);


            //check if we have users in the table users
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM student");

            if (resultSet.next()) {
                int numUsers = resultSet.getInt(1);
                if (numUsers > 0) {
                    hasRegistredUsers = true;
                }
            }
            statement.close();
            conn.close();
        } catch(Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
        return hasRegistredUsers;
    }
    public static void main(String[] args) {
        Courses myForm = new Courses();
    }
}
