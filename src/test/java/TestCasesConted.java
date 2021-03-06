import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;

public class TestCasesConted {
    private Statement statement;
    private Connection connection;

    @BeforeClass
    public void connect() throws SQLException {
        String url = "jdbc:mysql://database-techno.c771qxmldhez.us-east-2.rds.amazonaws.com:3306/serdar2225_students_new";
        String user = "serdar2225";
        String password = "serdar2225@hotmail.com";
        connection = DriverManager.getConnection(url, user, password);
        statement = connection.createStatement();

    }

    @AfterClass
    public void disconnect() throws SQLException {
        connection.close();
    }

    @Test
    public void test() throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT first_name, city, country, postal_code FROM students_new;");

        while (rs.next()) {
            String name = rs.getString("first_name");
            String country = rs.getString("country");
            String city = rs.getString("city");
            String postal_code = rs.getString("postal_code");
            System.out.println(name + " " + country + " " + city + " " + postal_code);
        }

    }

    @Test
    public void highestFeesTest() throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT first_name, email, fee, currency FROM students_new ORDER BY fee DESC limit 20;");

        while (rs.next()) {
            String name = rs.getString("first_name");
            String email = rs.getString("email");
            String fee = rs.getString("fee");
            String currency = rs.getString("currency");
            System.out.println(name + " " + email + " " + fee + " " + currency);
        }
    }
    @Test
    public void studentsInDifferentRows() throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT first_name, email, fee, currency FROM students_new ORDER BY fee DESC limit 20;");
        rs.absolute(5);
        System.out.println("Students at number 5 row: " + rs.getString("first_name")+" " + rs.getString("email") +" "+ rs.getString("fee")
        + rs.getString("currency"));

        rs.relative(3);
        System.out.println("Students at number 5 row: " + rs.getString("first_name")+" " + rs.getString("email") +" "+ rs.getString("fee")
                + rs.getString("currency"));
        rs.first();
        System.out.println("Students at number 5 row: " + rs.getString("first_name")+" " + rs.getString("email") +" "+ rs.getString("fee")
                + rs.getString("currency"));
        rs.last();
        System.out.println("Students at number 5 row: " + rs.getString("first_name")+" " + rs.getString("email") +" "+ rs.getString("fee")
                + rs.getString("currency"));

    }

    @Test
    public void averageFeeIncreaseTest() throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT first_name, country, avg(fee), currency FROM students_new GROUP BY currency, country limit 10;");
        while (rs.next()) {
            String name = rs.getString("first_name");
            String country = rs.getString("country");
            Double fee = rs.getDouble("avg(fee)");
            String currency = rs.getString("currency");
            System.out.println(name + " " + country + " " + (fee * 1.17) + " " + currency);
        }
    }

    @Test
    public void updateStudentInfoTest() throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT first_name, gender, country, fee FROM students_new WHERE gender = 'Male' and country = 'United States' limit 5;");
        while (rs.next()) {
            String name = rs.getString("first_name");
            String gender = rs.getString("gender");
            Double fee = rs.getDouble("fee");
            String country = rs.getString("country");

            System.out.println(name + " " + country + " " + gender + fee );
        }

        statement.executeUpdate("UPDATE students_new SET fee =  fee+10 WHERE gender = 'Male' and country = 'United States'");

        rs = statement.executeQuery("SELECT first_name, gender, country, fee FROM students_new WHERE gender = 'Male' and country = 'United States' limit 5;");
        while (rs.next()) {

            Double fee = rs.getDouble("fee");


            System.out.println("fee: " + fee );
        }
    }
    @Test
    public void usingPreparedStatement() throws SQLException {
        String gender="Male";
        String country = "United States";
        int toAdd= 10;

        PreparedStatement preparedStatement = connection.prepareStatement("select fee from students_new where gender = ? and country = ? limit 5");
        preparedStatement.setString(1,gender);
        preparedStatement.setString(2, country);


        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            Double fee = rs.getDouble(1);
            System.out.println("fee = "+ fee);

        }

        PreparedStatement updateStatement = connection.prepareStatement("UPDATE students_new SET fee = fee + ? WHERE gender = ? and country =?;");
        updateStatement.setInt(1, toAdd);
        updateStatement.setString(2, gender);
        updateStatement.setString(3, country);

        updateStatement.executeUpdate();

        rs = preparedStatement.executeQuery();
        while(rs.next()){
            Double fee = rs.getDouble(1);
            System.out.println("fee = " + fee);
        }

    }



    }
