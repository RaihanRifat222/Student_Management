//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package student_management;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class Conn {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // Create Connection and Statement

            Connection c;
            Statement s;
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:///studentmanagement", "root", "root");
            s = c.createStatement();
//            s.execute("insert into student values(2,'Rifat','rifat@gmail.com', '1234');");
//
//            c.close();
            ResultSet resultSet= s.executeQuery("select * from student;");
            while (resultSet.next()){
                System.out.print(resultSet.getInt("sid")+" ");
                System.out.print(resultSet.getString("name")+" ");

                System.out.println();
            }
            System.out.println("Please enter name");
            String name = sc.nextLine();
            System.out.println("Please enter SID");
            String sid = sc.nextLine();
            Integer id=Integer.parseInt(sid);
            System.out.println("Please enter email");
            String email = sc.nextLine();
            System.out.println("Please enter password");
            String pwd = sc.nextLine();
            String query = "Insert into student ( sid, name, email, password) values(?, ?, ?, ?)";
            PreparedStatement preparedStatement = c.prepareStatement(query);

            preparedStatement.setInt(1, id);
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,email);
            preparedStatement.setString(4,pwd);
            preparedStatement.executeUpdate();
            System.out.println("2........................");
            resultSet = s.executeQuery("select * from student");
            while(resultSet.next()) {
                System.out.print(resultSet.getInt("sid")+" ");
                System.out.print(resultSet.getString("name")+" ");
                System.out.print(resultSet.getString("email")+ " ");
                System.out.println();
            }
            c.close();
            System.out.println("Connected");
        } catch (Exception var2) {
            System.out.println(var2);
        }
    }

}
