import java.util.ArrayList;
import java.util.Scanner;

class Student {
    private final int id;
    private String name;
    private String department;
    private double marks;

    public Student(int id, String name, String department, double marks) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getMarks() {
        return marks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public String getGrade() {
        if (marks >= 80) return "A+";
        if (marks >= 75) return "A";
        if (marks >= 70) return "A-";
        if (marks >= 65) return "B+";
        if (marks >= 60) return "B";
        if (marks >= 55) return "B-";
        if (marks >= 50) return "C";
        if (marks >= 40) return "D";
        return "F";
    }

    @Override
    public String toString() {
        return String.format(
            "ID: %-6d | Name: %-20s | Department: %-10s | Marks: %6.2f | Grade: %s",
            id, name, department, marks, getGrade()
        );
    }
}

public class StudentManagementSystem {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {

        boolean running = true;

        System.out.println("==============================================");
        System.out.println("        STUDENT MANAGEMENT SYSTEM");
        System.out.println("        Java OOP Portfolio Project");
        System.out.println("==============================================");

        while (running) {
            displayMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> displayStudents();
                case 3 -> searchStudent();
                case 4 -> updateStudent();
                case 5 -> deleteStudent();
                case 6 -> displayStatistics();
                case 7 -> {
                    running = false;
                    System.out.println("\nThank you for using the system.");
                }
                default -> System.out.println("\nInvalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n---------------- MENU ----------------");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. View Statistics");
        System.out.println("7. Exit");
        System.out.println("--------------------------------------");
    }

    private static void addStudent() {

        System.out.println("\n========== ADD STUDENT ==========");

        int id = readInt("Student ID: ");

        if (findStudent(id) != null) {
            System.out.println("A student with this ID already exists.");
            return;
        }

        String name = readText("Student Name: ");
        String department = readText("Department: ");
        double marks = readMarks();

        students.add(new Student(id, name, department, marks));

        System.out.println("\nStudent added successfully.");
    }

    private static void displayStudents() {

        System.out.println("\n========== STUDENT RECORDS ==========");

        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }

        for (Student student : students) {
            System.out.println(student);
        }

        System.out.println("Total Students: " + students.size());
    }

    private static void searchStudent() {

        System.out.println("\n========== SEARCH STUDENT ==========");

        int id = readInt("Enter Student ID: ");
        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println("\nStudent Found:");
            System.out.println(student);
        }
    }

    private static void updateStudent() {

        System.out.println("\n========== UPDATE STUDENT ==========");

        int id = readInt("Enter Student ID: ");
        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        String name = readText("New Name: ");
        String department = readText("New Department: ");
        double marks = readMarks();

        student.setName(name);
        student.setDepartment(department);
        student.setMarks(marks);

        System.out.println("\nStudent information updated successfully.");
    }

    private static void deleteStudent() {

        System.out.println("\n========== DELETE STUDENT ==========");

        int id = readInt("Enter Student ID: ");
        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Selected Student: " + student.getName());

        String confirmation = readText("Confirm deletion? (yes/no): ");

        if (confirmation.equalsIgnoreCase("yes")) {
            students.remove(student);
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void displayStatistics() {

        System.out.println("\n========== CLASS STATISTICS ==========");

        if (students.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        double totalMarks = 0;
        double highestMarks = students.get(0).getMarks();
        double lowestMarks = students.get(0).getMarks();

        Student topStudent = students.get(0);

        for (Student student : students) {

            double marks = student.getMarks();

            totalMarks += marks;

            if (marks > highestMarks) {
                highestMarks = marks;
                topStudent = student;
            }

            if (marks < lowestMarks) {
                lowestMarks = marks;
            }
        }

        double average = totalMarks / students.size();

        System.out.printf("Total Students : %d%n", students.size());
        System.out.printf("Average Marks  : %.2f%n", average);
        System.out.printf("Highest Marks  : %.2f%n", highestMarks);
        System.out.printf("Lowest Marks   : %.2f%n", lowestMarks);
        System.out.println("Top Student    : " + topStudent.getName());
    }

    private static Student findStudent(int id) {

        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }

        return null;
    }

    private static int readInt(String message) {

        while (true) {
            System.out.print(message);

            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readMarks() {

        while (true) {

            System.out.print("Marks (0-100): ");

            try {
                double marks = Double.parseDouble(scanner.nextLine().trim());

                if (marks >= 0 && marks <= 100) {
                    return marks;
                }

                System.out.println("Marks must be between 0 and 100.");

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readText(String message) {

        while (true) {

            System.out.print(message);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("Input cannot be empty.");
        }
    }
}
