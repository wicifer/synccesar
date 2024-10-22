import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SchoolManagementSystem {
    private JFrame frame;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private Calendar calendar;
    private ArrayList<Task> tasks;

    public SchoolManagementSystem() {
        frame = new JFrame("School Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();
        calendar = Calendar.getInstance();
        tasks = new ArrayList<>();

        createDashboardPanel();
        createCalendarPanel();
        createNotificationsPanel();

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new GridLayout(3, 1));
        dashboardPanel.add(createProcessSection("Lançamento de Notas", 75));
        dashboardPanel.add(createProcessSection("Pagamentos", 90));
        dashboardPanel.add(createPendingTasksButton());
        tabbedPane.addTab("Dashboard", dashboardPanel);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createProcessSection(String processName, int completionPercentage) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(processName + ": "));
        panel.add(new JProgressBar(0, 100));
        ((JProgressBar) panel.getComponent(1)).setValue(completionPercentage);
        return panel;
    }

    private JButton createPendingTasksButton() {
        JButton button = new JButton("Pendências e Atrasos");
        button.addActionListener(e -> showPendingTasks());
        return button;
    }

    private void showPendingTasks() {
        StringBuilder message = new StringBuilder("Pendências e Atrasos:\n");
        for (Task task : tasks) {
            if (task.isOverdue()) {
                message.append("- ").append(task.getDescription()).append(" (Atrasada)\n");
            } else if (task.isDueSoon()) {
                message.append("- ").append(task.getDescription()).append(" (Vence em breve)\n");
            }
        }
        JOptionPane.showMessageDialog(frame, message.toString());
    }

    private void createCalendarPanel() {
        JPanel calendarPanel = new JPanel(new BorderLayout());
        JButton addTaskButton = new JButton("Adicionar Tarefa");
        addTaskButton.addActionListener(e -> addNewTask());
        calendarPanel.add(addTaskButton, BorderLayout.NORTH);
        tabbedPane.addTab("Calendário", calendarPanel);
    }

    private void addNewTask() {
        String description = JOptionPane.showInputDialog(frame, "Descrição da tarefa:");
        if (description != null && !description.isEmpty()) {
            Task newTask = new Task(description, new Date());
            tasks.add(newTask);
            JOptionPane.showMessageDialog(frame, "Tarefa adicionada com sucesso!");
        }
    }

    private void createNotificationsPanel() {
        JPanel notificationsPanel = new JPanel();
        notificationsPanel.add(new JLabel("Notificações aparecerão aqui"));
        tabbedPane.addTab("Notificações", notificationsPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SchoolManagementSystem::new);
    }
}

class Task {
    private String description;
    private Date dueDate;

    public Task(String description, Date dueDate) {
        this.description = description;
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOverdue() {
        return new Date().after(dueDate);
    }

    public boolean isDueSoon() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 3);
        return dueDate.before(cal.getTime()) && !isOverdue();
    }
}
