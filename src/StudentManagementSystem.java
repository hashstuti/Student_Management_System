import java.util.ArrayList;
import java.util.List;

public class StudentManagementSystem {
    private List<Student> students;

    public StudentManagementSystem() {
        students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public List<Student> searchStudent(String query) {
        List<Student> searchResults = new ArrayList<>();

        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(query) || String.valueOf(student.getRollNumber()).equals(query)) {
                searchResults.add(student);
            }
        }

        return searchResults;
    }

    public Student getStudentByRollNumber(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }

        return null;
    }

    public boolean deleteStudent(int rollNumber) {
        Student student = getStudentByRollNumber(rollNumber);
        if (student != null) {
            students.remove(student);
            return true;
        }
        return false;
    }


    public String calculateStatistics() {
        int totalStudents = students.size();
        int totalMarks = calculateTotalMarks(students);
        double averageMarks = calculateAverageMarks(students);
        int highestMarks = calculateHighestMarks(students);
        int lowestMarks = calculateLowestMarks(students);

        String statistics = "Total Students: " + totalStudents + "\n" +
                "Total Marks: " + totalMarks + "\n" +
                "Average Marks: " + averageMarks + "\n" +
                "Highest Marks: " + highestMarks + "\n" +
                "Lowest Marks: " + lowestMarks;

        return statistics;
    }

    int calculateTotalMarks(List<Student> students) {
        int totalMarks = 0;
        for (Student student : students) {
            totalMarks += student.getTotalMarks();
        }
        return totalMarks;
    }

    double calculateAverageMarks(List<Student> students) {
        int totalMarks = calculateTotalMarks(students);
        return (double) totalMarks / students.size();
    }

    int calculateHighestMarks(List<Student> students) {
        int highestMarks = 0;
        for (Student student : students) {
            if (student.getTotalMarks() > highestMarks) {
                highestMarks = student.getTotalMarks();
            }
        }
        return highestMarks;
    }

    int calculateLowestMarks(List<Student> students) {
        int lowestMarks = Integer.MAX_VALUE;
        for (Student student : students) {
            if (student.getTotalMarks() < lowestMarks) {
                lowestMarks = student.getTotalMarks();
            }
        }
        return lowestMarks;
    }
}
