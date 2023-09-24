import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {
    private StudentManagementSystem studentManagementSystem;
    private DefaultTableModel tableModel;
    private JTable table;

    private List<String> courses = new ArrayList<>();


    public Main() {
        studentManagementSystem = new StudentManagementSystem();

        // Create JFrame
        setTitle("Student Management System");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create JPanel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create table model and table
        String[] columnNames = {"Roll Number", "Name", "Course", "Total Marks", "Percentage"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells read-only
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        table.getTableHeader().setReorderingAllowed(false);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        // Create buttons
        JButton addButton = new JButton("Add Student");
        JButton Course = new JButton("Courses");
        JButton viewButton = new JButton("View Students");
        JButton searchButton = new JButton("Search Student");
        JButton viewDataButton = new JButton("View Student Data");
        JButton updateButton = new JButton("Update Student");
        JButton deleteButton = new JButton("Delete Student");
        JButton statisticsButton = new JButton("Calculate Statistics");

        // Add buttons to buttons panel
        buttonsPanel.add(addButton);
        buttonsPanel.add(Course);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(viewDataButton);
        buttonsPanel.add(searchButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(statisticsButton);

        // Add buttons panel to JPanel
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        // Add JPanel to JFrame
        add(panel);

        // Button Listeners
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open a dialog to enter student details
                String name = JOptionPane.showInputDialog("Enter student name:");
                if (name == null || name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Invalid name. Please enter a valid name.");
                    return;
                }

                String rollNumberStr = JOptionPane.showInputDialog("Enter student roll number:");
                if (rollNumberStr == null || rollNumberStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Invalid roll number. Please enter a valid roll number.");
                    return;
                }

                int rollNumber;
                try {
                    rollNumber = Integer.parseInt(rollNumberStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid roll number format. Please enter a valid roll number.");
                    return;
                }

                String course = JOptionPane.showInputDialog("Enter student course:");
                if (course == null || course.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Invalid course. Please enter a valid course.");
                    return;
                }

                // Check if student with the same name and roll number already exists
                boolean studentExists = false;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String existingName = tableModel.getValueAt(i, 1).toString();
                    int existingRollNumber = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
                    String existingCourse = tableModel.getValueAt(i, 2).toString();

                    if (name.equals(existingName) && rollNumber == existingRollNumber) {
                        studentExists = true;
                        break;
                    }
                    if (course.equals(existingCourse) && rollNumber == existingRollNumber) {
                        studentExists = true;
                        break;
                    }
                }

                if (studentExists) {
                    JOptionPane.showMessageDialog(null, "Student already exists.");
                    return;
                }

                String subjectsStr = JOptionPane.showInputDialog("Enter subjects (comma-separated) for the course:");
                if (subjectsStr == null || subjectsStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Invalid subjects. Please enter subjects for the course.");
                    return;
                }

                String[] subjectsArr = subjectsStr.split(",");
                int[] marks = new int[subjectsArr.length];
                String[] subjectNames = new String[subjectsArr.length];

                for (int i = 0; i < subjectsArr.length; i++) {
                    String subject = subjectsArr[i].trim();
                    subjectNames[i] = subject;

                    String marksStr = JOptionPane.showInputDialog("Enter marks for subject '" + subject + "':");
                    if (marksStr == null || marksStr.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Invalid marks. Please enter marks for subject '" + subject + "'.");
                        return;
                    }

                    int subjectMarks;
                    try {
                        subjectMarks = Integer.parseInt(marksStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid marks format. Please enter valid marks for subject '" + subject + "'.");
                        return;
                    }

                    while (subjectMarks < 0 || subjectMarks > 100) {
                        JOptionPane.showMessageDialog(null, "Invalid marks. Please enter marks between 0 and 100 for subject '" + subject + "'.");
                        marksStr = JOptionPane.showInputDialog("Enter marks for subject '" + subject + "':");
                        if (marksStr == null || marksStr.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Invalid marks. Please enter marks for subject '" + subject + "'.");
                            return;
                        }
                        try {
                            subjectMarks = Integer.parseInt(marksStr);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid marks format. Please enter valid marks for subject '" + subject + "'.");
                            return;
                        }
                    }

                    marks[i] = subjectMarks;
                }

                Student student = new Student(rollNumber, name, course, marks, subjectNames);
                studentManagementSystem.addStudent(student);
                JOptionPane.showMessageDialog(null, "Student added successfully.");

                // Refresh table
                refreshTable();
            }
        });

        Course.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (courses.isEmpty()) {
                    String courseName = JOptionPane.showInputDialog("Enter Course Name:");
                    if (courseName != null && !courseName.isEmpty()) {
                        courses.add(courseName);
                        JOptionPane.showMessageDialog(null, "Course added successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid course name.");
                    }
                } else {
                    courseList();
                }
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Student> students = studentManagementSystem.getAllStudents();
                if (students.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No students found.");
                } else {
                    displayStudentDetails(students);
                }
            }
        });

        viewDataButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String rollNumberStr = JOptionPane.showInputDialog("Enter student roll number:");
                if (rollNumberStr != null && !rollNumberStr.isEmpty()) {
                    int rollNumber;
                    try {
                        rollNumber = Integer.parseInt(rollNumberStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid roll number format. Please enter a valid roll number.");
                        return;
                    }

                    Student student = studentManagementSystem.getStudentByRollNumber(rollNumber);
                    if (student != null) {
                        showStudentDetails(student);
                    } else {
                        JOptionPane.showMessageDialog(null, "Student with roll number " + rollNumber + " not found.");
                    }
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchQuery = JOptionPane.showInputDialog("Enter student roll number or name:");
                if (searchQuery != null && !searchQuery.isEmpty()) {
                    List<Student> searchResults = studentManagementSystem.searchStudent(searchQuery);
                    if (searchResults.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No students found.");
                    } else {
                        displayStudentDetails(searchResults);
                    }
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String rollNumberStr = JOptionPane.showInputDialog("Enter student roll number:");
                if (rollNumberStr != null && !rollNumberStr.isEmpty()) {
                    int rollNumber;
                    try {
                        rollNumber = Integer.parseInt(rollNumberStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid roll number format. Please enter a valid roll number.");
                        return;
                    }

                    Student student = studentManagementSystem.getStudentByRollNumber(rollNumber);
                    if (student != null) {
                        // Create JFrame for updating student details
                        JFrame updateFrame = new JFrame("Update Student");
                        updateFrame.setSize(400, 300);
                        updateFrame.setLocationRelativeTo(null);
                        updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        // Create JPanel for student details
                        JPanel detailsPanel = new JPanel(new GridLayout(4, 2));
                        detailsPanel.add(new JLabel("Name:"));
                        JTextField nameField = new JTextField(student.getName());
                        detailsPanel.add(nameField);
                        detailsPanel.add(new JLabel("Roll Number:"));
                        JTextField rollNumberField = new JTextField(String.valueOf(student.getRollNumber()));
                        detailsPanel.add(rollNumberField);
                        detailsPanel.add(new JLabel("Course:"));
                        JTextField courseField = new JTextField(student.getCourse());
                        detailsPanel.add(courseField);

                        // Create table model and table for marks
                        String[] columnNames = {"Subject", "Marks"};
                        Object[][] data = new Object[student.getSubjectNames().length][2];

                        for (int i = 0; i < student.getSubjectNames().length; i++) {
                            data[i][0] = student.getSubjectNames()[i];
                            data[i][1] = student.getMarks()[i];
                        }

                        DefaultTableModel marksTableModel = new DefaultTableModel(data, columnNames);
                        JTable marksTable = new JTable(marksTableModel);

                        // Create JScrollPane for marks table
                        JScrollPane scrollPane = new JScrollPane(marksTable);

                        // Create JPanel for table and details
                        JPanel panel = new JPanel(new BorderLayout());
                        panel.add(detailsPanel, BorderLayout.NORTH);
                        panel.add(scrollPane, BorderLayout.CENTER);

                        // Add JPanel to JFrame
                        updateFrame.add(panel);

                        // Create JButton for updating student
                        JButton updateButton = new JButton("Update");
                        updateButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                // Update student details
                                String name = nameField.getText().trim();
                                if (name.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Invalid name. Please enter a valid name.");
                                    return;
                                }

                                String rollNumberStr = rollNumberField.getText().trim();
                                if (rollNumberStr.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Invalid roll number. Please enter a valid roll number.");
                                    return;
                                }

                                int rollNumber;
                                try {
                                    rollNumber = Integer.parseInt(rollNumberStr);
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(null, "Invalid roll number format. Please enter a valid roll number.");
                                    return;
                                }

                                String course = courseField.getText().trim();
                                if (course.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Invalid course. Please enter a valid course.");
                                    return;
                                }

                                int rowCount = marksTableModel.getRowCount();
                                int[] marks = new int[rowCount];
                                String[] subjectNames = new String[rowCount];
                                for (int i = 0; i < rowCount; i++) {
                                    subjectNames[i] = marksTableModel.getValueAt(i, 0).toString();
                                    String marksStr = marksTableModel.getValueAt(i, 1).toString();
                                    try {
                                        marks[i] = Integer.parseInt(marksStr);
                                    } catch (NumberFormatException ex) {
                                        JOptionPane.showMessageDialog(null, "Invalid marks format. Please enter valid marks.");
                                        return;
                                    }
                                }

                                // Update student
                                student.setName(name);
                                student.setRollNumber(rollNumber);
                                student.setCourse(course);
                                student.setSubjectNames(subjectNames);
                                student.setMarks(marks);

                                refreshTable();
                                // Display success message
                                JOptionPane.showMessageDialog(null, "Student details updated successfully.");

                                // Close the update window
                                updateFrame.dispose();
                            }

                        });

                        // Create JPanel for update button
                        JPanel buttonPanel = new JPanel();
                        buttonPanel.add(updateButton);

                        // Add JPanel to JFrame
                        updateFrame.add(buttonPanel, BorderLayout.SOUTH);

                        // Set JFrame visible
                        updateFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Student not found. Please enter a valid roll number.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid roll number. Please enter a valid roll number.");
                }

                //
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String rollNumberStr = JOptionPane.showInputDialog("Enter student roll number:");
                if (rollNumberStr != null && !rollNumberStr.isEmpty()) {
                    int rollNumber;
                    try {
                        rollNumber = Integer.parseInt(rollNumberStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid roll number format. Please enter a valid roll number.");
                        return;
                    }

                    boolean deleted = studentManagementSystem.deleteStudent(rollNumber);
                    if (deleted) {
                        JOptionPane.showMessageDialog(null, "Student deleted successfully.");

                        // Refresh table
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Student with roll number " + rollNumber + " not found.");
                    }
                }
            }
        });

        statisticsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Student> students = studentManagementSystem.getAllStudents();
                if (students.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No students found.");
                } else {
                    int totalStudents = students.size();
                    int totalMarks = studentManagementSystem.calculateTotalMarks(students);
                    double averageMarks = studentManagementSystem.calculateAverageMarks(students);
                    int highestMarks = studentManagementSystem.calculateHighestMarks(students);
                    int lowestMarks = studentManagementSystem.calculateLowestMarks(students);

                    String message = "Total Students: " + totalStudents + "\n" +
                            "Total Marks: " + totalMarks + "\n" +
                            "Average Marks: " + averageMarks + "\n" +
                            "Highest Marks: " + highestMarks + "\n" +
                            "Lowest Marks: " + lowestMarks;

                    JOptionPane.showMessageDialog(null, message);
                }
            }
        });
    }

    private void showStudentDetails(Student student) {
        // Create table model and table
        String[] columnNames = {"Subject", "Marks"};
        Object[][] data = new Object[student.getSubjectNames().length][2];

        for (int i = 0; i < student.getSubjectNames().length; i++) {
            data[i][0] = student.getSubjectNames()[i];
            data[i][1] = student.getMarks()[i];
        }


        TableModel model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        JTable studentTable = new JTable(model);

        // Create JPanel for student details
        JPanel detailsPanel = new JPanel(new GridLayout(4, 2));
        detailsPanel.add(new JLabel("Name:"));
        detailsPanel.add(new JLabel(student.getName()));
        detailsPanel.add(new JLabel("Roll Number:"));
        detailsPanel.add(new JLabel(String.valueOf(student.getRollNumber())));
        detailsPanel.add(new JLabel("Course:"));
        detailsPanel.add(new JLabel(student.getCourse()));

        // Create JPanel for table and details
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(detailsPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        studentTable.getTableHeader().setReorderingAllowed(false);

        JOptionPane.showMessageDialog(null, panel);
    }

    private void courseList() {
        // Create JFrame
        JFrame ChangeCourses = new JFrame("Courses");
        ChangeCourses.setSize(400, 300);
        ChangeCourses.setLocationRelativeTo(null);
        ChangeCourses.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create JPanel
        JPanel course_panel = new JPanel(new GridLayout(4, 2));

        // Create table model and table
        String[] columnNames = {"Course Name", "Subjects"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells read-only
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        course_panel.add(scrollPane, BorderLayout.CENTER);
        table.getTableHeader().setReorderingAllowed(false);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        // Create buttons
        JButton addCourse = new JButton("Add Course");
        JButton updateCourse = new JButton("Update Course");
        JButton deleteCourse = new JButton("Delete Course");

        // Add buttons to buttons panel
        buttonsPanel.add(addCourse);
        buttonsPanel.add(updateCourse);
        buttonsPanel.add(deleteCourse);

        // Add buttons panel to JPanel
        course_panel.add(buttonsPanel, BorderLayout.SOUTH);

        // Add JPanel to JFrame
        add(course_panel);
    }


    private void displayStudentDetails(List<Student> students) {
        tableModel.setRowCount(0); // Clear existing rows

        for (Student student : students) {
            int totalMarks = student.getTotalMarks();
            int maxMarks = student.getSubjectNames().length * 100;
            double percentage = (double) totalMarks / maxMarks * 100;

            Object[] row = {student.getName(), student.getRollNumber(), student.getCourse(), totalMarks, percentage + "%"};
            tableModel.addRow(row);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // Clear existing rows

        List<Student> students = studentManagementSystem.getAllStudents();

        for (Student student : students) {
            int totalMarks = student.getTotalMarks();
            int maxMarks = student.getSubjectNames().length * 100;
            double percentage = (double) totalMarks / maxMarks * 100;

            Object[] row = {student.getRollNumber(), student.getName(), student.getCourse(), totalMarks, percentage + "%"};
            tableModel.addRow(row);
        }
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main frame = new Main();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}