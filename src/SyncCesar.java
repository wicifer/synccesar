import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class SyncCesar {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private ArrayList<Task> tasks;
    private ArrayList<Evento> eventos;

    private final Color PURPLE_PRIMARY = new Color(102, 51, 153);
    private final Color PURPLE_LIGHT = new Color(147, 112, 219);
    private final Color PURPLE_DARK = new Color(75, 0, 130);
    private final Color PURPLE_BACKGROUND = new Color(147, 112, 219);

    public SyncCesar() {
        frame = new JFrame("SyncCesar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.getContentPane().setBackground(PURPLE_BACKGROUND);
        tasks = new ArrayList<>();
        eventos = new ArrayList<>();

        loadEventsFromFile();

        createLoginPanel();

        frame.setVisible(true);
    }

    private void createRegistrationPanel() {
        JPanel registrationPanel = new JPanel(new GridBagLayout());
        registrationPanel.setBackground(PURPLE_BACKGROUND);
    
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
        emailLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        registrationPanel.add(emailLabel, gbc);
    
        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        registrationPanel.add(emailField, gbc);
    
        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        registrationPanel.add(passwordLabel, gbc);
    
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        registrationPanel.add(passwordField, gbc);
    
        JLabel hintLabel = new JLabel("Dica de Segurança:");
        hintLabel.setForeground(Color.BLACK);
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
        loginOptionButton.setForeground(PURPLE_DARK);
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
                createLoginPanel();
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
        frame.getContentPane().setBackground(PURPLE_BACKGROUND);
        frame.revalidate();
        frame.repaint();
    }
    
    

    private void createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(PURPLE_BACKGROUND);
    
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
        emailLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(emailLabel, gbc);
    
        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setForeground(Color.BLACK);
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
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setForeground(PURPLE_DARK);
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 4;
        loginPanel.add(forgotPasswordButton, gbc);
    
        JButton registerOptionButton = new JButton("Criar uma conta");
        registerOptionButton.setBorderPainted(false);
        registerOptionButton.setContentAreaFilled(false);
        registerOptionButton.setForeground(PURPLE_DARK);
        registerOptionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 5;
        loginPanel.add(registerOptionButton, gbc);
    
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
    
            if (authenticateUser(email, password)) {
                JOptionPane.showMessageDialog(frame, "Login bem-sucedido! Bem-vindo(a), " + email);
    
                frame.getContentPane().removeAll();
                createMainPanel();
                frame.revalidate();
                frame.repaint();
            } else {
                JOptionPane.showMessageDialog(frame, "Email ou senha incorretos. Tente novamente.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        registerOptionButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            createRegistrationPanel();
            frame.revalidate();
            frame.repaint();
        });
    
        frame.getContentPane().removeAll();
        frame.add(loginPanel);
        frame.getContentPane().setBackground(PURPLE_BACKGROUND);
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

    private void createMainPanel() {
        frame.getContentPane().removeAll();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PURPLE_BACKGROUND);
        
        JPanel sideMenu = new JPanel();
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        sideMenu.setPreferredSize(new Dimension(150, frame.getHeight()));
        sideMenu.setBorder(BorderFactory.createEtchedBorder());
        sideMenu.setBackground(PURPLE_PRIMARY);

        String[] menuItems = {"Dashboard", "Calendário", "Pendências", "Notificações"};
        CardLayout cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(PURPLE_BACKGROUND);

        JPanel dashboardPanel = createDashboardContent();
        JPanel calendarPanel = createCalendarContent();
        JPanel pendenciasPanel = createPendenciasContent();
        JPanel notificationsPanel = createNotificationsContent();

        contentPanel.add(dashboardPanel, "Dashboard");
        contentPanel.add(calendarPanel, "Calendário");
        contentPanel.add(pendenciasPanel, "Pendências");
        contentPanel.add(notificationsPanel, "Notificações");

        for (String menuItem : menuItems) {
            JButton menuButton = new JButton(menuItem);
            menuButton.setMaximumSize(new Dimension(150, 40));
            menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            menuButton.setFocusPainted(false);
            menuButton.setBorderPainted(false);
            menuButton.setBackground(PURPLE_PRIMARY);
            menuButton.setForeground(Color.WHITE);
            
            menuButton.addActionListener(e -> cardLayout.show(contentPanel, menuItem));
            
            menuButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    menuButton.setBackground(PURPLE_LIGHT);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    menuButton.setBackground(PURPLE_PRIMARY);
                }
            });
            
            sideMenu.add(menuButton);
            sideMenu.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        mainPanel.add(sideMenu, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();
    }

    private JPanel createDashboardContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PURPLE_BACKGROUND);

        // Painel para o título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(PURPLE_BACKGROUND);
        JLabel titleLabel = new JLabel("Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PURPLE_DARK);
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Painel para as quatro seções
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setBackground(PURPLE_BACKGROUND);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Criar as quatro seções
        String[][] sections = {
            {"Provas", "60"},
            {"Pagamentos", "45"},
            {"Aulas", "75"},
            {"Atividade 4", "100"}
        };

        for (String[] section : sections) {
            JPanel sectionPanel = createSectionPanel(section[0], Integer.parseInt(section[1]));
            gridPanel.add(sectionPanel);
        }

        panel.add(gridPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSectionPanel(String title, int percentage) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PURPLE_PRIMARY, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(PURPLE_DARK);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(percentage);
        progressBar.setStringPainted(true);
        progressBar.setString(percentage + "%");
        progressBar.setForeground(PURPLE_PRIMARY);
        progressBar.setBackground(Color.LIGHT_GRAY);
        progressBar.setPreferredSize(new Dimension(150, 20));
        progressBar.setMaximumSize(new Dimension(150, 20));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);


        sectionPanel.add(titleLabel);
        sectionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sectionPanel.add(progressBar);

        return sectionPanel;
    }

    private JPanel createCalendarContent() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JButton addEventButton = new JButton("Adicionar Evento");
        addEventButton.addActionListener(e -> showAddEventDialog(panel));
        panel.add(addEventButton, BorderLayout.NORTH);

        JTextArea monthView = new JTextArea();
        monthView.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(monthView);
        panel.add(scrollPane, BorderLayout.CENTER);

        updateMonthView(monthView);
        
        return panel;
    }

    private JPanel createPendenciasContent() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea pendenciasArea = new JTextArea();
        pendenciasArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(pendenciasArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        if (hasPendencias()) {
            JButton notifyButton = new JButton("Notificar Responsáveis");
            notifyButton.addActionListener(e -> notifyResponsaveis());
            panel.add(notifyButton, BorderLayout.NORTH);
        }

        updatePendenciasView(pendenciasArea);
        
        return panel;
    }

    private JPanel createNotificationsContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Notificações aparecerão aqui", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
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
            
            for (Component comp : tabbedPane.getComponents()) {
                if (comp instanceof JPanel && tabbedPane.indexOfComponent(comp) == tabbedPane.indexOfTab("Pendências")) {
                    JPanel pendenciasPanel = (JPanel) comp;
                    
                    if (pendenciasPanel.getComponent(0) instanceof JPanel) {
                        pendenciasPanel.remove(0);
                    }
                    
                    if (hasPendencias()) {
                        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        JButton notifyButton = new JButton("Notificar Responsáveis");
                        notifyButton.addActionListener(evt -> notifyResponsaveis());
                        buttonPanel.add(notifyButton);
                        pendenciasPanel.add(buttonPanel, BorderLayout.NORTH);
                    }
                    
                    JTextArea pendenciasArea = (JTextArea) ((JScrollPane) pendenciasPanel.getComponent(pendenciasPanel.getComponentCount() - 1)).getViewport().getView();
                    updatePendenciasView(pendenciasArea);
                    
                    pendenciasPanel.revalidate();
                    pendenciasPanel.repaint();
                    break;
                }
            }
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

    private boolean hasPendencias() {
        Calendar hoje = Calendar.getInstance();
        hoje.set(Calendar.HOUR_OF_DAY, 0);
        hoje.set(Calendar.MINUTE, 0);
        hoje.set(Calendar.SECOND, 0);
        hoje.set(Calendar.MILLISECOND, 0);

        for (Evento evento : eventos) {
            if ("pendente".equalsIgnoreCase(evento.getStatus())) {
                try {
                    Calendar dataEvento = Calendar.getInstance();
                    String[] dataParts = evento.getData().split("/");
                    dataEvento.set(
                        Integer.parseInt(dataParts[2]),
                        Integer.parseInt(dataParts[1]) - 1,
                        Integer.parseInt(dataParts[0])
                    );
                    dataEvento.set(Calendar.HOUR_OF_DAY, 0);
                    dataEvento.set(Calendar.MINUTE, 0);
                    dataEvento.set(Calendar.SECOND, 0);
                    dataEvento.set(Calendar.MILLISECOND, 0);

                    if (dataEvento.before(hoje)) {
                        return true;
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar data do evento: " + evento.getTitulo());
                }
            }
        }
        return false;
    }

    private void notifyResponsaveis() {
        int pendenciasCount = 0;
        Calendar hoje = Calendar.getInstance();
        hoje.set(Calendar.HOUR_OF_DAY, 0);
        hoje.set(Calendar.MINUTE, 0);
        hoje.set(Calendar.SECOND, 0);
        hoje.set(Calendar.MILLISECOND, 0);

        for (Evento evento : eventos) {
            if ("pendente".equalsIgnoreCase(evento.getStatus())) {
                try {
                    Calendar dataEvento = Calendar.getInstance();
                    String[] dataParts = evento.getData().split("/");
                    dataEvento.set(
                        Integer.parseInt(dataParts[2]),
                        Integer.parseInt(dataParts[1]) - 1,
                        Integer.parseInt(dataParts[0])
                    );
                    dataEvento.set(Calendar.HOUR_OF_DAY, 0);
                    dataEvento.set(Calendar.MINUTE, 0);
                    dataEvento.set(Calendar.SECOND, 0);
                    dataEvento.set(Calendar.MILLISECOND, 0);

                    if (dataEvento.before(hoje)) {
                        pendenciasCount++;
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar data do evento: " + evento.getTitulo());
                }
            }
        }

        if (pendenciasCount > 0) {
            JOptionPane.showMessageDialog(frame,
                "Email enviado para os responsáveis de " + pendenciasCount + " evento(s) pendente(s).",
                "Notificação Enviada",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame,
                "Não há eventos pendentes para notificar.",
                "Notificação",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updatePendenciasView(JTextArea pendenciasArea) {
        StringBuilder pendenciasText = new StringBuilder("Eventos Pendentes:\n\n");
        Calendar hoje = Calendar.getInstance();
        hoje.set(Calendar.HOUR_OF_DAY, 0);
        hoje.set(Calendar.MINUTE, 0);
        hoje.set(Calendar.SECOND, 0);
        hoje.set(Calendar.MILLISECOND, 0);

        boolean temPendencias = false;

        for (Evento evento : eventos) {
            try {
                Calendar dataEvento = Calendar.getInstance();
                String[] dataParts = evento.getData().split("/");
                dataEvento.set(
                    Integer.parseInt(dataParts[2]), // ano
                    Integer.parseInt(dataParts[1]) - 1, // mês (0-11)
                    Integer.parseInt(dataParts[0]) // dia
                );
                dataEvento.set(Calendar.HOUR_OF_DAY, 0);
                dataEvento.set(Calendar.MINUTE, 0);
                dataEvento.set(Calendar.SECOND, 0);
                dataEvento.set(Calendar.MILLISECOND, 0);

                if (dataEvento.before(hoje)) {
                    temPendencias = true;
                    pendenciasText.append("Título: ").append(evento.getTitulo()).append("\n")
                                .append("Data e Hora: ").append(evento.getData()).append(" ")
                                .append(evento.getHora()).append("\n")
                                .append("Tipo: ").append(evento.getTipo()).append("\n")
                                .append("Status: ").append(evento.getStatus()).append("\n")
                                .append("Descrição: ").append(evento.getDescricao()).append("\n")
                                .append("----------------------------------------\n");
                }
            } catch (Exception e) {
                System.err.println("Erro ao processar data do evento: " + evento.getTitulo());
                e.printStackTrace();
            }
        }

        if (!temPendencias) {
            pendenciasText.append("Não há eventos pendentes atrasados.");
        }

        pendenciasArea.setText(pendenciasText.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SyncCesar::new);
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
