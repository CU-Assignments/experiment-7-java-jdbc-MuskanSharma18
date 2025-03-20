// Student.java (Model)
public class Student {
    private int studentID;
    private String name;
    private String department;
    private int marks;

    public Student(int studentID, String name, String department, int marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public int getMarks() { return marks; }
}

// StudentDAO.java (Controller)
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public void insertStudent(Student student) throws SQLException {
        String query = "INSERT INTO Students (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, student.getStudentID());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getDepartment());
            pstmt.setInt(4, student.getMarks());
            pstmt.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM Students";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("StudentID"),
                        rs.getString("Name"),
                        rs.getString("Department"),
                        rs.getInt("Marks")));
            }
        }
        return students;
    }

    public void updateStudent(Student student) throws SQLException {
        String query = "UPDATE Students SET Name=?, Department=?, Marks=? WHERE StudentID=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getDepartment());
            pstmt.setInt(3, student.getMarks());
            pstmt.setInt(4, student.getStudentID());
            pstmt.executeUpdate();
        }
    }

    public void deleteStudent(int studentID) throws SQLException {
        String query = "DELETE FROM Students WHERE StudentID = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, studentID);
            pstmt.executeUpdate();
        }
    }
}

// StudentApp.java (View)
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class StudentApp {
    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Insert Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter Student ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Department: ");
                        String department = scanner.nextLine();
                        System.out.print("Enter Marks: ");
                        int marks = scanner.nextInt();
                        dao.insertStudent(new Student(id, name, department, marks));
                        System.out.println("Student inserted successfully!");
                        break;
                    case 2:
                        List<Student> students = dao.getAllStudents();
                        for (Student s : students) {
                            System.out.println("ID: " + s.getStudentID() + ", Name: " + s.getName() + ", Dept: " + s.getDepartment() + ", Marks: " + s.getMarks());
                        }
                        break;
                    case 3:
                        System.out.print("Enter Student ID to update: ");
                        int updateID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter New Name: ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter New Department: ");
                        String newDept = scanner.nextLine();
                        System.out.print("Enter New Marks: ");
                        int newMarks = scanner.nextInt();
                        dao.updateStudent(new Student(updateID, newName, newDept, newMarks));
                        System.out.println("Student updated successfully!");
                        break;
                    case 4:
                        System.out.print("Enter Student ID to delete: ");
                        int deleteID = scanner.nextInt();
                        dao.deleteStudent(deleteID);
                        System.out.println("Student deleted successfully!");
                        break;
                    case 5:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
