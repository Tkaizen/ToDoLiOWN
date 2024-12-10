import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
    // ang frame ni is need ug lain nga color change color lng //
public class ToDoListApp extends JFrame {
    private DefaultListModel<String> taskModel;
    private JList<String> taskList;
    private Color primaryColor = new Color(58, 123, 213);  //colors
    private Color secondaryColor = new Color(255, 105, 180);  
    private Color backgroundColor = new Color(240, 240, 240);  
    
    public ToDoListApp() {
        setTitle("To-do List");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);

      
      //todo list main
        taskModel = new DefaultListModel<>();
        taskList = new JList<>(taskModel);
        taskList.setBackground(Color.WHITE);
        taskList.setForeground(Color.BLACK);
        taskList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBackground(backgroundColor);

      
        JButton addTaskButton = createButton("Add Task", "icons/add_icon.png");// need ni sila ug icon dapat naa sa folder tawagon lng ang name 
        JButton editTaskButton = createButton("Edit Task", "icons/edit_icon.png");// kani sad
        JButton finishTaskButton = createButton("Finish Task", "icons/delete_icon.png");//kani sad

        buttonPanel.add(addTaskButton);
        buttonPanel.add(editTaskButton);
        buttonPanel.add(finishTaskButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // kani para ma edit ang task ma edit na sya promise
        addTaskButton.addActionListener(e -> openTaskEditor(null));
        editTaskButton.addActionListener(e -> {
            String selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                openTaskEditor(selectedTask);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to edit.");
            }
 });
        finishTaskButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                taskModel.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to finish.");
            }
        });
    }
            //para ni sa buttons para naay on hover murag css//
    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setBackground(primaryColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setIcon(new ImageIcon(iconPath));
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(primaryColor, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 50));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(secondaryColor);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(primaryColor);
            }
        });
        return button;
    }
        // wala nani self explanatory nani
    private void openTaskEditor(String existingTask) {
        TaskEditor editor = new TaskEditor(this, existingTask);
        editor.setVisible(true);
        editor.setLocationRelativeTo(this);
    }

    public void addTask(String task) {
        taskModel.addElement(task);
    }

    public void editTask(String oldTask, String newTask) {
        int index = taskModel.indexOf(oldTask);
        if (index != -1) {
            taskModel.set(index, newTask);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ToDoListApp app = new ToDoListApp();
            app.setVisible(true);
        });
    }
}

class TaskEditor extends JDialog {
    private JTextField taskNameField;
    private JTextArea taskDescriptionArea;
    private JTextField dateField;
    private JTextField timeField;
    private String existingTask;

    public TaskEditor(ToDoListApp parent, String task) {
        super(parent, "Add/Edit Task", true);
        existingTask = task;

        setSize(300, 400);
        setLayout(new GridLayout(6, 1, 10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        // name sa task ni dari
        taskNameField = new JTextField();
        taskNameField.setBorder(BorderFactory.createTitledBorder("Task Name"));
        taskNameField.setBackground(Color.WHITE);
        taskNameField.setForeground(Color.BLACK);
        taskNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(taskNameField);

        // mag himo ug description kay ari
        taskDescriptionArea = new JTextArea();
        taskDescriptionArea.setBorder(BorderFactory.createTitledBorder("Description"));
        taskDescriptionArea.setBackground(Color.WHITE);
        taskDescriptionArea.setForeground(Color.BLACK);
        taskDescriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(new JScrollPane(taskDescriptionArea));

        // deadline sa date
        dateField = new JTextField();
        dateField.setBorder(BorderFactory.createTitledBorder("Date (D/M/YR)"));
        dateField.setBackground(Color.WHITE);
        dateField.setForeground(Color.BLACK);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(dateField);
 // deadline sa time
        timeField = new JTextField();
        timeField.setBorder(BorderFactory.createTitledBorder("Time (HH:MM)"));
        timeField.setBackground(Color.WHITE);
        timeField.setForeground(Color.BLACK);
        timeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(timeField);

        // mao ni ang mo gawas sa todolist 
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setBackground(new Color(58, 123, 213));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(e -> {
            String taskName = taskNameField.getText();
            String description = taskDescriptionArea.getText();
            String date = dateField.getText();
            String time = timeField.getText();
            if (!taskName.isEmpty()) {
                String newTask = taskName + " - " + description + " (Due: " + date + " " + time + ")";
                if (existingTask != null) {
                    parent.editTask(existingTask, newTask);
                } else {
                    parent.addTask(newTask);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Task name cannot be empty.");
            }
        });
            //buttons ambot lng
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(255, 105, 180));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel);
    }
}

                                         
