import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class SchoolManagementSystem {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private ArrayList<Task> tasks;
    private ArrayList<Evento> eventos;

    public SchoolManagementSystem() {
        frame = new JFrame("School Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        tabbedPane = new JTabbedPane();
        tasks = new ArrayList<>();
        eventos = new ArrayList<>();

        loadEventsFromFile();

        createRegistrationPanel();

        frame.setVisible(true);
    }

    private void createRegistrationPanel() {
        JPanel registrationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        ImageIcon logoIcon = new ImageIcon("resources/logoprojeto.png.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registrationPanel.add(logoLabel, gbc);

        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        registrationPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        registrationPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        registrationPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        registrationPanel.add(passwordField, gbc);

        JLabel hintLabel = new JLabel("Dica de Segurança:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        registrationPanel.add(hintLabel, gbc);

        JTextField hintField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        registrationPanel.add(hintField, gbc);

        JButton registerButton = new JButton("Cadastrar");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registrationPanel.add(registerButton, gbc);

        JButton loginOptionButton = new JButton("Já tem uma conta? Entrar");
        loginOptionButton.setBorderPainted(false);
        loginOptionButton.setContentAreaFilled(false);
        loginOptionButton.setForeground(Color.BLUE);
        loginOptionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 5;
        registrationPanel.add(loginOptionButton, gbc);

        registerButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String hint = hintField.getText();

            if (email.isEmpty() || password.isEmpty() || hint.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                saveUserToFile(email, password, hint);
                JOptionPane.showMessageDialog(frame, "Usuário cadastrado com sucesso!");
                frame.getContentPane().removeAll();
                createDashboardPanel();
                createCalendarPanel();
                createNotificationsPanel();
                frame.add(tabbedPane);
                frame.revalidate();
                frame.repaint();
            }
        });

        loginOptionButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            createLoginPanel();
            frame.revalidate();
            frame.repaint();
        });

        frame.getContentPane().removeAll();
        frame.add(registrationPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void saveUserToFile(String email, String password, String hint) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("usuarios.txt", true))) {
            writer.write(email + "," + password + "," + hint);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        ImageIcon logoIcon = new ImageIcon("resources/logoprojeto.png.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(logoLabel, gbc);

        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        JButton forgotPasswordButton = new JButton("Esqueceu a senha?");
        gbc.gridy = 4;
        loginPanel.add(forgotPasswordButton, gbc);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticateUser(email, password)) {
                JOptionPane.showMessageDialog(frame, "Login bem-sucedido! Bem-vindo(a), " + email);
                frame.getContentPane().removeAll();
                createDashboardPanel();
                createCalendarPanel();
                createNotificationsPanel();
                frame.add(tabbedPane);
                frame.revalidate();
                frame.repaint();
            } else {
                JOptionPane.showMessageDialog(frame, "Email ou senha incorretos. Tente novamente.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            }

            emailField.setText("");
            passwordField.setText("");
        });

        forgotPasswordButton.addActionListener(e -> {
            String email = emailField.getText();
            String hint = getHintForUser(email);
            if (hint != null) {
                JOptionPane.showMessageDialog(frame, "Dica de Segurança: " + hint, "Esqueceu a Senha?", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Email não encontrado. Verifique o email e tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.getContentPane().removeAll();
        frame.add(loginPanel);
        frame.revalidate();
        frame.repaint();
    }

    private boolean authenticateUser(String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("usuarios.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(email) && userDetails[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getHintForUser(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader("usuarios.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(email)) {
                    return userDetails[2];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new GridLayout(3, 1));
        dashboardPanel.add(new JLabel("Bem-vindo ao Painel!"));
        tabbedPane.addTab("Dashboard", dashboardPanel);
    }

    private void createCalendarPanel() {
        JPanel calendarPanel = new JPanel(new BorderLayout());

        JButton addEventButton = new JButton("Adicionar Evento");
        addEventButton.addActionListener(e -> showAddEventDialog(calendarPanel));
        calendarPanel.add(addEventButton, BorderLayout.NORTH);

        JTextArea monthView = new JTextArea();
        monthView.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(monthView);
        calendarPanel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Calendário", calendarPanel);

        updateMonthView(monthView);
    }

    private void showAddEventDialog(JPanel calendarPanel) {
        JDialog dialog = new JDialog(frame, "Adicionar Evento", true);
        dialog.setSize(300, 450);
        dialog.setLayout(new GridLayout(7, 2, 10, 10));

        JTextField titleField = new JTextField();
        JTextField dateField = new JTextField("dd/MM/yyyy");
        JTextField timeField = new JTextField("HH:mm");
        JTextField typeField = new JTextField("Reunião/Lembrete");
        JTextField statusField = new JTextField("pendente");
        JTextArea descriptionField = new JTextArea(3, 20);

        dialog.add(new JLabel("Título:"));
        dialog.add(titleField);
        dialog.add(new JLabel("Data:"));
        dialog.add(dateField);
        dialog.add(new JLabel("Hora:"));
        dialog.add(timeField);
        dialog.add(new JLabel("Tipo:"));
        dialog.add(typeField);
        dialog.add(new JLabel("Status:"));
        dialog.add(statusField);
        dialog.add(new JLabel("Descrição:"));
        dialog.add(new JScrollPane(descriptionField));

        JButton saveButton = new JButton("Salvar");
        saveButton.addActionListener(e -> {
            String title = titleField.getText();
            String date = dateField.getText();
            String time = timeField.getText();
            String type = typeField.getText();
            String status = statusField.getText();
            String description = descriptionField.getText();

            if (!isValidDate(date)) {
                JOptionPane.showMessageDialog(dialog,
                    "Data inválida! Use o formato dd/MM/yyyy",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidTime(time)) {
                JOptionPane.showMessageDialog(dialog,
                    "Hora inválida! Use o formato HH:mm",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Evento evento = new Evento(title, date, time, type, status, description);
            eventos.add(evento);

            saveEventToFile(evento);

            dialog.dispose();
            updateMonthView((JTextArea) ((JScrollPane) calendarPanel.getComponent(1)).getViewport().getView());
        });
        dialog.add(saveButton);

        dialog.setVisible(true);
    }

    private boolean isValidDate(String date) {
        if (date == null || !date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            return false;
        }
        try {
            String[] parts = date.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            if (year < 1900 || year > 2100) return false;
            if (month < 1 || month > 12) return false;
            if (day < 1) return false;

            int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                daysInMonth[1] = 29;
            }

            return day <= daysInMonth[month - 1];
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidTime(String time) {
        if (time == null || !time.matches("\\d{2}:\\d{2}")) {
            return false;
        }
        try {
            String[] parts = time.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            return hours >= 0 && hours < 24 && minutes >= 0 && minutes < 60;
        } catch (Exception e) {
            return false;
        }
    }

    private void updateMonthView(JTextArea monthView) {
        StringBuilder eventsText = new StringBuilder("Eventos do Mês:\n");
        for (Evento evento : eventos) {
            eventsText.append("Título: ").append(evento.getTitulo()).append("\n")
                      .append("Data e Hora: ").append(evento.getData()).append(" ").append(evento.getHora()).append("\n")
                      .append("Tipo: ").append(evento.getTipo()).append("\n")
                      .append("Status: ").append(evento.getStatus()).append("\n")
                      .append("Descrição: ").append(evento.getDescricao()).append("\n\n");
        }
        monthView.setText(eventsText.toString());
    }

    private void saveEventToFile(Evento evento) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("eventos.txt", true))) {
            writer.write(evento.getTitulo() + "," + evento.getData() + "," + evento.getHora() + "," + 
                        evento.getTipo() + "," + evento.getStatus() + "," + evento.getDescricao());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadEventsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("eventos.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] eventDetails = line.split(",");
                if (eventDetails.length == 6) {
                    Evento evento = new Evento(eventDetails[0], eventDetails[1], eventDetails[2], 
                                             eventDetails[3], eventDetails[4], eventDetails[5]);
                    eventos.add(evento);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

class Evento {
    private String titulo;
    private String data;
    private String hora;
    private String tipo;
    private String status;
    private String descricao;

    public Evento(String titulo, String data, String hora, String tipo, String status, String descricao) {
        this.titulo = titulo;
        this.data = data;
        this.hora = hora;
        this.tipo = tipo;
        this.status = status;
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public String getTipo() {
        return tipo;
    }

    public String getStatus() {
        return status;
    }

    public String getDescricao() {
        return descricao;
    }
}
