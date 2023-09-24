//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class StudentData {
//    private static final String FILE_PATH = "student_data.txt";
//
//    public static void saveStudentData(List<Student> students) throws IOException {
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
//            oos.writeObject(students);
//            System.out.println("Student data saved successfully.");
//        } catch (IOException e) {
//            System.err.println("Error saving student data: " + e.getMessage());
//            throw e; // Re-throw the exception to be handled by the calling method
//        }
//    }
//
//    public static List<Student> loadStudentData() throws IOException, ClassNotFoundException {
//        List<Student> students = new ArrayList<>();
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
//            students = (List<Student>) ois.readObject();
//            System.out.println("Student data loaded successfully.");
//        } catch (IOException | ClassNotFoundException e) {
//            System.err.println("Error loading student data: " + e.getMessage());
//            throw e; // Re-throw the exception to be handled by the calling method
//        }
//        return students;
//    }
//}
