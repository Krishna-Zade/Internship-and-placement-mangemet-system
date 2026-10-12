import java.sql.*;
import java.util.Scanner;

public class Main {

    static final String URL = "jdbc:mysql://localhost:3306/placement_management_system";
    static final String USER = "root";
    static final String PASSWORD = "Pass@123";

    static Connection con;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database Connected Successfully!\n");

            int choice;
            do {
                System.out.println("===== INTERNSHIP AND PLACEMENT MANAGEMENT SYSTEM =====");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Search Student by Roll No");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1: addStudent(); break;
                    case 2: viewStudents(); break;
                    case 3: updateStudent(); break;
                    case 4: deleteStudent(); break;
                    case 5: searchStudent(); break;
                    case 0: System.out.println("Exiting... Thank you!"); break;
                    default: System.out.println("Invalid choice, try again.");
                }
                System.out.println();
            } while (choice != 0);

            con.close();

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }

    static void addStudent() {
        try {
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Roll No: ");
            String rollNo = sc.nextLine();
            System.out.print("Enter Branch: ");
            String branch = sc.nextLine();
            System.out.print("Enter CGPA: ");
            double cgpa = Double.parseDouble(sc.nextLine());
            System.out.print("Enter Contact: ");
            String contact = sc.nextLine();
            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            String query = "INSERT INTO students (name, roll_no, branch, cgpa, contact, email) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, rollNo);
            ps.setString(3, branch);
            ps.setDouble(4, cgpa);
            ps.setString(5, contact);
            ps.setString(6, email);

            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Student added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    static void viewStudents() {
        try {
            String query = "SELECT * FROM students";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("\n--- Student List ---");
            System.out.printf("%-5s %-15s %-10s %-10s %-6s %-12s %-25s%n",
                    "ID", "Name", "Roll No", "Branch", "CGPA", "Contact", "Email");
            System.out.println("---------------------------------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-15s %-10s %-10s %-6.2f %-12s %-25s%n",
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("roll_no"),
                        rs.getString("branch"),
                        rs.getDouble("cgpa"),
                        rs.getString("contact"),
                        rs.getString("email"));
            }
            if (!found) System.out.println("No students found.");

        } catch (SQLException e) {
            System.out.println("Error fetching students: " + e.getMessage());
        }
    }

    static void updateStudent() {
        try {
            System.out.print("Enter Roll No of student to update: ");
            String rollNo = sc.nextLine();

            System.out.print("Enter new CGPA: ");
            double cgpa = Double.parseDouble(sc.nextLine());
            System.out.print("Enter new Contact: ");
            String contact = sc.nextLine();

            String query = "UPDATE students SET cgpa = ?, contact = ? WHERE roll_no = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setDouble(1, cgpa);
            ps.setString(2, contact);
            ps.setString(3, rollNo);

            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Student updated successfully!");
            else System.out.println("No student found with that Roll No.");

        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    static void deleteStudent() {
        try {
            System.out.print("Enter Roll No of student to delete: ");
            String rollNo = sc.nextLine();

            String query = "DELETE FROM students WHERE roll_no = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, rollNo);

            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Student deleted successfully!");
            else System.out.println("No student found with that Roll No.");

        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    static void searchStudent() {
        try {
            System.out.print("Enter Roll No to search: ");
            String rollNo = sc.nextLine();

            String query = "SELECT * FROM students WHERE roll_no = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, rollNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\n--- Student Found ---");
                System.out.println("ID: " + rs.getInt("student_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Roll No: " + rs.getString("roll_no"));
                System.out.println("Branch: " + rs.getString("branch"));
                System.out.println("CGPA: " + rs.getDouble("cgpa"));
                System.out.println("Contact: " + rs.getString("contact"));
                System.out.println("Email: " + rs.getString("email"));
            } else {
                System.out.println("No student found with that Roll No.");
            }

        } catch (SQLException e) {
            System.out.println("Error searching student: " + e.getMessage());
        }
    }
}