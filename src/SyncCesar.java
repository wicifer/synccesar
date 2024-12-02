import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class SyncCesar {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JPanel calendarPanel;
    private JLabel monthLabel;
    private Calendar currentMonth;
    private Map<String, List<String>> events;
    private List<Task> tasks;
    private List<Evento> eventos;
    private List<Nota> notas = new ArrayList<>();
    private JPanel dayHeaders;
    private CardLayout cardLayout;
    private JPanel contentPanel;

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

    private JPanel createMainPanel() {
        frame.getContentPane().removeAll();
    
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PURPLE_BACKGROUND);
    
        JPanel sideMenu = new JPanel();
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        sideMenu.setPreferredSize(new Dimension(150, frame.getHeight()));
        sideMenu.setBorder(BorderFactory.createEtchedBorder());
        sideMenu.setBackground(PURPLE_PRIMARY);
    
        String[] menuItems = {"Dashboard", "Calendário", "Notificações", "Configurações"};
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(PURPLE_BACKGROUND);
    
        JPanel dashboardPanel = createDashboardContent();
        JPanel calendarPanel = createCalendarContentWithHours();
        JPanel notificationsPanel = createNotificationsContent();
        JPanel settingsPanel = createSettingsContent();
    
        contentPanel.add(dashboardPanel, "Dashboard");
        contentPanel.add(calendarPanel, "Calendário");
        contentPanel.add(notificationsPanel, "Notificações");
        contentPanel.add(settingsPanel, "Configurações");
    
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
    
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(PURPLE_LIGHT);
    
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(PURPLE_LIGHT);
    
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setForeground(Color.DARK_GRAY);
        searchField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        searchField.setToolTipText("Digite algo e pressione Enter");
    
        JLabel searchIcon = new JLabel(new ImageIcon("resources/icons8-pesquisa-30.png"));
        searchPanel.add(searchIcon);
        searchPanel.add(searchField);
    
        searchField.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Digite algo para pesquisar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Resultado não encontrado para: " + query, "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
            }
            searchField.setText("");
        });
    
        topPanel.add(searchPanel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);
    
        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();
        return mainPanel;
    }

    private JPanel createDashboardContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PURPLE_BACKGROUND);
    
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(PURPLE_BACKGROUND);
    
        JLabel titleLabel = new JLabel("Visão geral de cada processo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(PURPLE_DARK);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JLabel settingsLabel = new JLabel("⚙");
        settingsLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        settingsLabel.setForeground(Color.BLACK);
        settingsLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        settingsLabel.setToolTipText("Configurações");
    
        settingsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardLayout.show(contentPanel, "Configurações");
            }
        });
        topPanel.add(settingsLabel, BorderLayout.EAST);
    
        panel.add(topPanel, BorderLayout.NORTH);
    
        JPanel mainContentPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainContentPanel.setBackground(PURPLE_BACKGROUND);
    
        JPanel progressPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        progressPanel.setBackground(PURPLE_BACKGROUND);
    
        progressPanel.add(createCircularTaskCard(
            "Lançamento de Notas",
            50,
            Color.ORANGE,
            Arrays.asList(
                "01 - Turma B - Noite !",
                "02 - Subir notas de Gestão B ✓",
                "03 - Revisar eventuais erros",
                "04 - Garantir entrega até o prazo"
            )
        ));
    
        progressPanel.add(createCircularTaskCard(
            "Pagamentos",
            90,
            Color.GREEN,
            Arrays.asList(
                "01 - Atualizar lista de pagamentos ✓",
                "02 - Confirmar transferências ✓",
                "03 - Revisar relatórios financeiros"
            )
        ));
    
        progressPanel.add(createCircularTaskCard(
            "Reuniões Mensais ✓",
            100,
            Color.CYAN,
            Arrays.asList(
                "01 - Alinhamento Semestre✓",
                "02 - Reunião Coordenação ✓"
            )
        ));
    
        mainContentPanel.add(progressPanel);
    
        JPanel taskSectionsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        taskSectionsPanel.setBackground(PURPLE_BACKGROUND);
    
        taskSectionsPanel.add(createTaskSection("A Fazer", PURPLE_LIGHT));
        taskSectionsPanel.add(createTaskSection("Em Progresso", Color.ORANGE));
        taskSectionsPanel.add(createTaskSection("Concluído", Color.GREEN));
    
        mainContentPanel.add(taskSectionsPanel);
        panel.add(mainContentPanel, BorderLayout.CENTER);
    
        JPanel notificationsPanel = buildNotificationPanel(cardLayout, contentPanel);
        panel.add(notificationsPanel, BorderLayout.SOUTH);
    
        return panel;
    }

    private JPanel createTaskSection(String title, Color backgroundColor) {

        JPanel taskPanel = new JPanel(new BorderLayout());
        taskPanel.setBackground(backgroundColor);
        taskPanel.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 1));
        taskPanel.setPreferredSize(new Dimension(120, 80));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(Color.BLACK);
        taskPanel.add(titleLabel, BorderLayout.NORTH);

        JTextArea taskArea = new JTextArea();
        taskArea.setLineWrap(true);
        taskArea.setWrapStyleWord(true);
        taskArea.setFont(new Font("Arial", Font.PLAIN, 10));
        taskArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        taskArea.setPreferredSize(new Dimension(100, 40));

        JScrollPane scrollPane = new JScrollPane(taskArea);
        scrollPane.setPreferredSize(new Dimension(100, 40));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        taskPanel.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("+ Add atividade");
        addButton.setFocusPainted(false);
        addButton.setBackground(PURPLE_PRIMARY);
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.PLAIN, 10));
        addButton.setPreferredSize(new Dimension(100, 20));
        addButton.addActionListener(e -> {
            String newTask = JOptionPane.showInputDialog("Digite uma nova tarefa:");
            if (newTask != null && !newTask.trim().isEmpty()) {
                taskArea.append(newTask + "\n");
            }
        });
        taskPanel.add(addButton, BorderLayout.SOUTH);
    
        return taskPanel;
    }

    private JPanel createGraphPanel(String title, String subtitle, int percentage, Color progressColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(75, 0, 130));
        panel.add(titleLabel, BorderLayout.NORTH);
    
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(percentage);
        progressBar.setStringPainted(true);
        progressBar.setString(percentage + "%");
        progressBar.setForeground(progressColor);
        progressBar.setPreferredSize(new Dimension(150, 20));
    
        panel.add(progressBar, BorderLayout.CENTER);
    
        JLabel subtitleLabel = new JLabel(subtitle, SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        subtitleLabel.setForeground(Color.GRAY);
        panel.add(subtitleLabel, BorderLayout.SOUTH);
    
        return panel;
    }

    private JPanel createTaskListPanel(String title, Color backgroundColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
    
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLACK);
        panel.add(titleLabel, BorderLayout.NORTH);
    
        JPanel taskList = new JPanel();
        taskList.setLayout(new BoxLayout(taskList, BoxLayout.Y_AXIS));
        taskList.setBackground(backgroundColor);
    
        JButton addTaskButton = new JButton("+ Add atividade");
        addTaskButton.setFocusPainted(false);
        addTaskButton.setBackground(new Color(200, 200, 255));
        addTaskButton.addActionListener(e -> {
            String newTask = JOptionPane.showInputDialog("Digite uma nova tarefa:");
            if (newTask != null && !newTask.trim().isEmpty()) {
                JLabel taskLabel = new JLabel(newTask);
                taskLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                taskList.add(taskLabel);
                taskList.revalidate();
                taskList.repaint();
            }
        });
    
        panel.add(taskList, BorderLayout.CENTER);
        panel.add(addTaskButton, BorderLayout.SOUTH);
    
        return panel;
    }

    private JPanel createCircularTaskCard(String title, int percentage, Color progressColor, List<String> tasks) {
        JPanel circularTaskPanel = new JPanel();
        circularTaskPanel.setLayout(new BoxLayout(circularTaskPanel, BoxLayout.Y_AXIS));
        circularTaskPanel.setBackground(new Color(240, 240, 255));
        circularTaskPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PURPLE_LIGHT, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
    
        JLabel titleLabel = new JLabel(title + " ▼");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(PURPLE_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        JPanel circularProgressPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
                int diameter = Math.min(getWidth(), getHeight()) - 10;
                int x = (getWidth() - diameter) / 2;
                int y = (getHeight() - diameter) / 2;
    
                g2d.setColor(new Color(220, 220, 220));
                g2d.fillArc(x, y, diameter, diameter, 0, 360);
    
                g2d.setColor(progressColor);
                g2d.fillArc(x, y, diameter, diameter, 90, -(int) (3.6 * percentage));
    
                g2d.setColor(new Color(240, 240, 255));
                g2d.fillOval(x + 10, y + 10, diameter - 20, diameter - 20);
    
                g2d.setColor(PURPLE_DARK);
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                String text = percentage + "%";
                FontMetrics fm = g2d.getFontMetrics();
                int textX = getWidth() / 2 - fm.stringWidth(text) / 2;
                int textY = getHeight() / 2 + fm.getAscent() / 2;
                g2d.drawString(text, textX, textY);
            }
        };
        circularProgressPanel.setPreferredSize(new Dimension(100, 100));
        circularProgressPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        circularTaskPanel.add(titleLabel);
        circularTaskPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        circularTaskPanel.add(circularProgressPanel);
    
        titleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        titleLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            private boolean isExpanded = false;
    
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!isExpanded) {
                    
                    JPanel tasksPanel = new JPanel();
                    tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));
                    tasksPanel.setBackground(new Color(250, 250, 255));
                    tasksPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    
                    for (String task : tasks) {
                        JLabel taskLabel = new JLabel(task);
    
                        
                        if (task.contains("✓")) {
                            taskLabel.setForeground(Color.GREEN);
                        } else if (task.contains("!")) {
                            taskLabel.setForeground(Color.ORANGE);
                        } else {
                            taskLabel.setForeground(Color.RED);
                        }
    
                        taskLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                        tasksPanel.add(taskLabel);
                    }

                    JScrollPane scrollPane = new JScrollPane(tasksPanel);
                    scrollPane.setPreferredSize(new Dimension(180, 100));
                    scrollPane.setBorder(BorderFactory.createLineBorder(PURPLE_LIGHT));
    
                    circularTaskPanel.add(scrollPane);
                    circularTaskPanel.revalidate();
                    circularTaskPanel.repaint();
                    titleLabel.setText(title + " ▲");
                } else {
                    circularTaskPanel.remove(3);
                    circularTaskPanel.revalidate();
                    circularTaskPanel.repaint();
                    titleLabel.setText(title + " ▼");
                }
                isExpanded = !isExpanded;
            }
        });
    
        return circularTaskPanel;
    }
    
    

    private JPanel createNotificationCard(String title, String message, CardLayout cardLayout, JPanel contentPanel) {
        JPanel notificationCard = new JPanel(new BorderLayout());
        notificationCard.setBackground(new Color(240, 248, 255));
        notificationCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    
        JLabel titleLabel = new JLabel("🔔 " + title);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        titleLabel.setForeground(Color.BLACK);
    
        JTextArea messageLabel = new JTextArea("✅ " + message);
        messageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        messageLabel.setForeground(Color.DARK_GRAY);
        messageLabel.setWrapStyleWord(true);
        messageLabel.setLineWrap(true);
        messageLabel.setOpaque(false);
        messageLabel.setEditable(false);
        messageLabel.setFocusable(false);
    
        notificationCard.add(titleLabel, BorderLayout.NORTH);
        notificationCard.add(messageLabel, BorderLayout.CENTER);
    
        notificationCard.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardLayout.show(contentPanel, "Notificações");
            }
    
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                notificationCard.setBackground(new Color(230, 240, 250));
            }
    
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                notificationCard.setBackground(new Color(240, 248, 255));
            }
        });
    
        return notificationCard;
    }

    private JPanel buildNotificationPanel(CardLayout cardLayout, JPanel contentPanel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PURPLE_BACKGROUND);
    
        JLabel titleLabel = new JLabel("Notificações para essa semana", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        titleLabel.setForeground(PURPLE_DARK);
        panel.add(titleLabel, BorderLayout.NORTH);
    
        JPanel notificationList = new JPanel();
        notificationList.setLayout(new BoxLayout(notificationList, BoxLayout.Y_AXIS));
        notificationList.setBackground(PURPLE_BACKGROUND);
    
        notificationList.add(createNotificationCard(
            "🔔 Lembrete de prazo",
            "Faltam 3 dias para o término do prazo da etapa de revisão do projeto.",
            cardLayout, contentPanel
        ));
        notificationList.add(Box.createRigidArea(new Dimension(0, 10)));
    
        notificationList.add(createNotificationCard(
            "✅ Conclusão de Etapa",
            "A etapa 'Planejamento' foi concluída. A próxima etapa inicia em 01/11.",
            cardLayout, contentPanel
        ));
        notificationList.add(Box.createRigidArea(new Dimension(0, 10)));
    
        notificationList.add(createNotificationCard(
            "⚠️ Atualização no sistema",
            "O sistema estará indisponível para manutenção no próximo final de semana.",
            cardLayout, contentPanel
        ));
    
        notificationList.add(Box.createRigidArea(new Dimension(0, 20)));
    
        JLabel moreNotificationsLabel = new JLabel("Há mais notificações... Clique aqui para ver todas");
        moreNotificationsLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        moreNotificationsLabel.setForeground(PURPLE_DARK);
        moreNotificationsLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
        moreNotificationsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardLayout.show(contentPanel, "Notificações");
            }
    
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                moreNotificationsLabel.setForeground(PURPLE_LIGHT);
            }
    
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                moreNotificationsLabel.setForeground(PURPLE_DARK);
            }
        });
    
        notificationList.add(moreNotificationsLabel);
    
        JScrollPane scrollPane = new JScrollPane(notificationList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);
    
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

    private JPanel createWeekDaysHeader() {
        JPanel weekDaysPanel = new JPanel(new GridLayout(1, 7));
        weekDaysPanel.setBackground(PURPLE_PRIMARY);
    
        String[] daysOfWeek = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setForeground(Color.WHITE);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
            weekDaysPanel.add(dayLabel);
        }
    
        return weekDaysPanel;
    }

    private JPanel createSettingsContent() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBackground(PURPLE_BACKGROUND);
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        JLabel titleLabel = new JLabel("Configurações do Sistema");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(PURPLE_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsPanel.add(titleLabel);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    
        JPanel profileSection = createProfileSection();
        settingsPanel.add(profileSection);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    
        JPanel themeSection = createThemeSection();
        settingsPanel.add(themeSection);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    
        JPanel languageSection = createLanguageSection();
        settingsPanel.add(languageSection);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    
        JPanel accessibilitySection = createAccessibilitySection();
        settingsPanel.add(accessibilitySection);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton saveButton = new JButton("Salvar Alterações");
        saveButton.setFont(new Font("Arial", Font.BOLD, 18));
        saveButton.setBackground(PURPLE_PRIMARY);
        saveButton.setForeground(Color.WHITE);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    settingsPanel,
                    "Deseja salvar as alterações?",
                    "Salvar Alterações",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(settingsPanel, "Alterações salvas com sucesso!");
            }
        });
        settingsPanel.add(saveButton);
    
        return settingsPanel;
    }
    
    private JPanel createProfileSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(PURPLE_LIGHT), "Perfil do Usuário"));
    
        JPanel profileInfoPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        profileInfoPanel.setBackground(Color.WHITE);
    
        JLabel nameLabel = new JLabel("Nome:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField nameField = new JTextField("teste");
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
    
        JLabel emailLabel = new JLabel("E-mail:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField emailField = new JTextField("email@exemplo.com");
        emailField.setFont(new Font("Arial", Font.PLAIN, 16));
    
        profileInfoPanel.add(nameLabel);
        profileInfoPanel.add(nameField);
        profileInfoPanel.add(emailLabel);
        profileInfoPanel.add(emailField);
    
        JPanel profileImagePanel = new JPanel(new BorderLayout());
        JLabel profileImage = new JLabel(new ImageIcon(new ImageIcon("resources/icons8-conta-de-teste-48.png").getImage()
                .getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        profileImage.setHorizontalAlignment(SwingConstants.CENTER);
        profileImagePanel.setBackground(Color.WHITE);
        profileImagePanel.add(profileImage, BorderLayout.CENTER);
    
        JButton resetPasswordButton = new JButton("Redefinir Senha");
        resetPasswordButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetPasswordButton.setBackground(PURPLE_PRIMARY);
        resetPasswordButton.setForeground(Color.WHITE);
        resetPasswordButton.addActionListener(e -> JOptionPane.showMessageDialog(panel, "Funcionalidade para redefinir senha."));
    
        panel.add(profileInfoPanel, BorderLayout.CENTER);
        panel.add(profileImagePanel, BorderLayout.WEST);
        panel.add(resetPasswordButton, BorderLayout.SOUTH);
    
        return panel;
    }
    
    private JPanel createThemeSection() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(PURPLE_LIGHT), "Tema"));
    
        JButton lightThemeButton = new JButton("Claro");
        JButton darkThemeButton = new JButton("Escuro");
        lightThemeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        darkThemeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        lightThemeButton.setBackground(Color.LIGHT_GRAY);
        darkThemeButton.setBackground(Color.DARK_GRAY);
        lightThemeButton.setForeground(Color.BLACK);
        darkThemeButton.setForeground(Color.WHITE);
    
        lightThemeButton.addActionListener(e -> JOptionPane.showMessageDialog(panel, "Tema Claro selecionado."));
        darkThemeButton.addActionListener(e -> JOptionPane.showMessageDialog(panel, "Tema Escuro selecionado."));
    
        panel.add(lightThemeButton);
        panel.add(darkThemeButton);
    
        return panel;
    }
    
    private JPanel createLanguageSection() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(PURPLE_LIGHT), "Idioma"));
    
        JLabel languageLabel = new JLabel("Selecione o idioma:");
        languageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
    
        JComboBox<String> languageComboBox = new JComboBox<>(new String[]{"Português", "Inglês", "Espanhol"});
        languageComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        languageComboBox.addActionListener(e -> {
            String selectedLanguage = (String) languageComboBox.getSelectedItem();
            JOptionPane.showMessageDialog(panel, "Idioma selecionado: " + selectedLanguage);
        });
    
        panel.add(languageLabel);
        panel.add(languageComboBox);
    
        return panel;
    }
    
    private JPanel createAccessibilitySection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(PURPLE_LIGHT), "Acessibilidade"));
    
        JLabel fontSizeLabel = new JLabel("Tamanho da Fonte:");
        fontSizeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        fontSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
        JSlider fontSizeSlider = new JSlider(10, 30, 16);
        fontSizeSlider.setMajorTickSpacing(5);
        fontSizeSlider.setMinorTickSpacing(1);
        fontSizeSlider.setPaintTicks(true);
        fontSizeSlider.setPaintLabels(true);
        fontSizeSlider.addChangeListener(e -> adjustFontSize(fontSizeSlider.getValue()));
    
        panel.add(fontSizeLabel, BorderLayout.NORTH);
        panel.add(fontSizeSlider, BorderLayout.CENTER);
    
        return panel;
    }
    
    private void adjustFontSize(int fontSize) {
        SwingUtilities.invokeLater(() -> {
            for (Component component : frame.getContentPane().getComponents()) {
                updateFont(component, new Font("Arial", Font.PLAIN, fontSize));
            }
            frame.revalidate();
            frame.repaint();
        });
    }
    
    private void updateFont(Component component, Font font) {
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                updateFont(child, font);
            }
        }
    }
    
    private JPanel createCalendarContentWithHours() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevMonthButton = new JButton("<");
        JButton nextMonthButton = new JButton(">");
        monthLabel = new JLabel("", SwingConstants.CENTER);
        currentMonth = Calendar.getInstance();
        updateMonthLabel();
    
        prevMonthButton.addActionListener(e -> {
            changeMonth(-1);
            updateCalendarGrid();
        });
    
        nextMonthButton.addActionListener(e -> {
            changeMonth(1);
            updateCalendarGrid();
        });
    
        headerPanel.add(prevMonthButton);
        headerPanel.add(monthLabel);
        headerPanel.add(nextMonthButton);
    
        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel calendarGrid = new JPanel(new BorderLayout());
        JPanel dayHeaders = new JPanel(new GridLayout(1, 8));
        dayHeaders.setBackground(PURPLE_PRIMARY);
    
        JLabel emptyLabel = new JLabel("");
        dayHeaders.add(emptyLabel);
    
        String[] daysOfWeek = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};
        for (String day : daysOfWeek) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(Color.WHITE);
            dayHeaders.add(label);
        }
    
        calendarGrid.add(dayHeaders, BorderLayout.NORTH);
    
        JPanel timeAndDays = new JPanel(new GridLayout(24, 8));
        timeAndDays.setBackground(PURPLE_BACKGROUND);
    
        Map<String, Map<String, List<String>>> eventsByMonth = new HashMap<>();
        String currentMonthKey = getMonthKey(currentMonth);
    
        Calendar tempCalendar = (Calendar) currentMonth.clone();
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        tempCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfWeek);
    
        for (int h = 0; h < 24; h++) {
            JLabel timeLabel = new JLabel(String.format("%02d:00", h), SwingConstants.CENTER);
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            timeLabel.setForeground(PURPLE_DARK);
            timeLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, PURPLE_PRIMARY));
            timeAndDays.add(timeLabel);
    
            for (int d = 0; d < 7; d++) {
                JPanel cellPanel = new JPanel(new BorderLayout());
                cellPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, PURPLE_PRIMARY));
                cellPanel.setBackground(Color.WHITE);
    
                JPanel eventPanel = new JPanel();
                eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
                eventPanel.setBackground(Color.WHITE);
    
                final int day = tempCalendar.get(Calendar.DAY_OF_MONTH);
                final int month = tempCalendar.get(Calendar.MONTH);
                final int year = tempCalendar.get(Calendar.YEAR);
                String dateKey = String.format("%02d/%02d/%04d", day, month + 1, year);
    
                if (eventsByMonth.containsKey(currentMonthKey) && eventsByMonth.get(currentMonthKey).containsKey(dateKey)) {
                    List<String> dayEvents = eventsByMonth.get(currentMonthKey).get(dateKey);
                    for (String event : dayEvents) {
                        JLabel eventLabel = new JLabel(event);
                        eventLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                        eventLabel.setForeground(PURPLE_DARK);

                        eventLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mouseClicked(java.awt.event.MouseEvent e) {
                                int confirm = JOptionPane.showConfirmDialog(
                                    frame,
                                    "Deseja excluir o evento '" + event + "'?",
                                    "Excluir Evento",
                                    JOptionPane.YES_NO_OPTION
                                );
    
                                if (confirm == JOptionPane.YES_OPTION) {
                                    dayEvents.remove(event);
                                    eventPanel.remove(eventLabel);
                                    saveEventsToFile();
                                    eventPanel.revalidate();
                                    eventPanel.repaint();
                                    JOptionPane.showMessageDialog(frame, "Evento excluído com sucesso!");
                                }
                            }
                        });
    
                        eventPanel.add(eventLabel);
                    }
                }
    
                cellPanel.add(eventPanel, BorderLayout.CENTER);
    
                cellPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        String monthKey = getMonthKey(currentMonth);
                        Map<String, List<String>> currentMonthEvents = eventsByMonth.computeIfAbsent(monthKey, k -> new HashMap<>());
                        List<String> dayEvents = currentMonthEvents.computeIfAbsent(dateKey, k -> new ArrayList<>());
    
                        String event = JOptionPane.showInputDialog(frame, "Adicione um evento para " + dateKey + ":");
                        if (event != null && !event.isEmpty()) {
                            dayEvents.add(event);
    
                            JLabel eventLabel = new JLabel(event);
                            eventLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                            eventLabel.setForeground(PURPLE_DARK);

                            eventLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                                @Override
                                public void mouseClicked(java.awt.event.MouseEvent e) {
                                    int confirm = JOptionPane.showConfirmDialog(
                                        frame,
                                        "Deseja excluir o evento '" + event + "'?",
                                        "Excluir Evento",
                                        JOptionPane.YES_NO_OPTION
                                    );
    
                                    if (confirm == JOptionPane.YES_OPTION) {
                                        dayEvents.remove(event);
                                        eventPanel.remove(eventLabel);
                                        saveEventsToFile();
                                        eventPanel.revalidate();
                                        eventPanel.repaint();
                                        JOptionPane.showMessageDialog(frame, "Evento excluído com sucesso!");
                                    }
                                }
                            });
    
                            eventPanel.add(eventLabel);
                            eventPanel.revalidate();
                            eventPanel.repaint();
                            JOptionPane.showMessageDialog(frame, "Evento adicionado para " + dateKey);
                        }
                    }
                });
    
                timeAndDays.add(cellPanel);
                tempCalendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
    
        calendarGrid.add(timeAndDays, BorderLayout.CENTER);
        panel.add(calendarGrid, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(PURPLE_BACKGROUND);
    
        JButton addEventButton = new JButton("Adicionar Evento");
        addEventButton.addActionListener(e -> showAddEventDialog(timeAndDays));
        bottomPanel.add(addEventButton);
    
        JButton addNoteButton = new JButton("Adicionar Nota");
        addNoteButton.addActionListener(e -> showAddNoteDialog());
        bottomPanel.add(addNoteButton);
    
        panel.add(bottomPanel, BorderLayout.SOUTH);
    
        return panel;
    }

    private void configureBottomPanel(JPanel panel) { 
        JButton addEventButton = new JButton("Adicionar Evento");
        addEventButton.addActionListener(e -> showSimpleAddEventDialog());
        panel.add(addEventButton);
    
        JButton addNoteButton = new JButton("Adicionar Nota");
        addNoteButton.addActionListener(e -> showAddNoteToDateDialog());
        panel.add(addNoteButton);
    }

private void showSimpleAddEventDialog() {
    String date = JOptionPane.showInputDialog(frame, "Insira a data (dd/MM/yyyy):");
    if (!isValidDate(date)) {
        JOptionPane.showMessageDialog(frame, "Data inválida! Use o formato dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String eventTitle = JOptionPane.showInputDialog(frame, "Insira o título do evento:");
    if (eventTitle == null || eventTitle.trim().isEmpty()) {
        JOptionPane.showMessageDialog(frame, "O título do evento não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }

    Evento evento = new Evento(eventTitle, date, "", "", "", "");
    eventos.add(evento);

    JOptionPane.showMessageDialog(frame, "Evento adicionado com sucesso!");
    updateCalendarGrid();
}

private void showAddNoteToDateDialog() {
    String date = JOptionPane.showInputDialog(frame, "Insira a data para adicionar a nota (dd/MM/yyyy):");
    if (!isValidDate(date)) {
        JOptionPane.showMessageDialog(frame, "Data inválida! Use o formato dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String noteContent = JOptionPane.showInputDialog(frame, "Insira a nota/observação:");
    if (noteContent == null || noteContent.trim().isEmpty()) {
        JOptionPane.showMessageDialog(frame, "A nota não pode estar vazia.", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
    }

    Nota novaNota = new Nota(date, noteContent);
    notas.add(novaNota);

    JOptionPane.showMessageDialog(frame, "Nota adicionada com sucesso!");
    updateCalendarGrid();
}

private void showAddNoteDialog() {
    JDialog dialog = new JDialog(frame, "Adicionar Nota", true);
    dialog.setSize(300, 200);
    dialog.setLayout(new BorderLayout());

    JPanel inputPanel = new JPanel(new GridLayout(2, 1, 10, 10));
    JTextField dateField = new JTextField("dd/MM/yyyy");
    JTextArea observacoesField = new JTextArea(3, 20);

    inputPanel.add(new JLabel("Data (dd/MM/yyyy):"));
    inputPanel.add(dateField);
    inputPanel.add(new JLabel("Observações:"));
    inputPanel.add(new JScrollPane(observacoesField));

    dialog.add(inputPanel, BorderLayout.CENTER);

    JButton saveButton = new JButton("Salvar");
    saveButton.addActionListener(e -> {
        String date = dateField.getText();
        String observacoes = observacoesField.getText();

        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(dialog, "Data inválida! Use o formato dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Nota novaNota = new Nota(date, observacoes);
        notas.add(novaNota);

        dialog.dispose();
        updateCalendarGrid();
    });

    dialog.add(saveButton, BorderLayout.SOUTH);
    dialog.setVisible(true);
}

    private String formatEvents(List<String> events) {
        StringBuilder sb = new StringBuilder("<html>");
        for (String event : events) {
            sb.append(event).append("<br>");
        }
        sb.append("</html>");
        return sb.toString();
    }

    private String getMonthKey(Calendar calendar) {
        return String.format("%02d/%04d", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }
    private void updateDayHeaders() {
        dayHeaders.removeAll();
    
        Calendar temp = (Calendar) currentMonth.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
    
        int startDay = temp.get(Calendar.DAY_OF_WEEK) - 1;
        temp.add(Calendar.DAY_OF_MONTH, -startDay);
    
        for (int i = 0; i < 7; i++) {
            JLabel dayNumberLabel = new JLabel(String.valueOf(temp.get(Calendar.DAY_OF_MONTH)), SwingConstants.CENTER);
            dayNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));
            dayNumberLabel.setForeground(Color.WHITE);
            dayHeaders.add(dayNumberLabel);
            temp.add(Calendar.DAY_OF_MONTH, 1);
        }
    
        String[] daysOfWeek = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 12));
            dayLabel.setForeground(Color.WHITE);
            dayHeaders.add(dayLabel);
        }
    
        dayHeaders.revalidate();
        dayHeaders.repaint();
    }
    
    private void updateMonthLabel() {
        String month = currentMonth.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int year = currentMonth.get(Calendar.YEAR);
        monthLabel.setText(month + " " + year);
    }

    private void changeMonth(int offset) {
        currentMonth.add(Calendar.MONTH, offset);
        updateMonthLabel();
        updateCalendarGrid();
    }
    private void updateCalendarGrid() {
        calendarPanel.removeAll();
    

        JPanel updatedCalendar = new JPanel(new BorderLayout());
        JPanel timeAndDays = new JPanel(new GridLayout(24, 8));
        timeAndDays.setBackground(PURPLE_BACKGROUND);
    
        Calendar tempCalendar = (Calendar) currentMonth.clone();
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        tempCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfWeek);
    
        for (int h = 0; h < 24; h++) {
            JLabel timeLabel = new JLabel(String.format("%02d:00", h), SwingConstants.CENTER);
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            timeLabel.setForeground(PURPLE_DARK);
            timeAndDays.add(timeLabel);
    
            for (int d = 0; d < 7; d++) {
                JPanel cellPanel = new JPanel(new BorderLayout());
                cellPanel.setBackground(Color.WHITE);
    
                String dateKey = String.format("%02d/%02d/%04d",
                        tempCalendar.get(Calendar.DAY_OF_MONTH),
                        tempCalendar.get(Calendar.MONTH) + 1,
                        tempCalendar.get(Calendar.YEAR)
                );
    
                addEventToCalendarGrid(cellPanel, dateKey);
                addNoteToCalendarGrid(cellPanel, dateKey);
    
                timeAndDays.add(cellPanel);
                tempCalendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
    
        updatedCalendar.add(timeAndDays, BorderLayout.CENTER);
        calendarPanel.add(updatedCalendar, BorderLayout.CENTER);
    
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private void addEventToCalendarGrid(JPanel cellPanel, String dateKey) {
        JPanel eventPanel = new JPanel();
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
        eventPanel.setBackground(Color.WHITE);
    
        for (Evento evento : eventos) {
            if (evento.getData().equals(dateKey)) {
                JLabel eventLabel = new JLabel(evento.getTitulo());
                eventLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                eventLabel.setForeground(PURPLE_DARK);
    
                eventPanel.add(eventLabel);
            }
        }
    
        cellPanel.add(eventPanel, BorderLayout.CENTER);
    }

    private void addNoteToCalendarGrid(JPanel cellPanel, String dateKey) {
        JPanel notePanel = new JPanel();
        notePanel.setLayout(new BoxLayout(notePanel, BoxLayout.Y_AXIS));
        notePanel.setBackground(Color.WHITE);
    
        for (Nota nota : notas) {
            if (nota.getData().equals(dateKey)) {
                JLabel noteLabel = new JLabel(nota.getObservacoes());
                noteLabel.setFont(new Font("Arial", Font.ITALIC, 10));
                noteLabel.setForeground(PURPLE_LIGHT);
    
                notePanel.add(noteLabel);
            }
        }
    
        cellPanel.add(notePanel, BorderLayout.SOUTH);
    }
    
    private JPanel createNotificationsContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PURPLE_BACKGROUND);
    
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(PURPLE_BACKGROUND);
    
        ImageIcon bellIcon = new ImageIcon("resources/icons8-sino-64.png");
        JLabel bellLabel = new JLabel(bellIcon);
    
        JLabel titleLabel = new JLabel("Notificações");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PURPLE_DARK);
    
        headerPanel.add(bellLabel);
        headerPanel.add(titleLabel);
    
        panel.add(headerPanel, BorderLayout.NORTH);
    
        JPanel notificationList = new JPanel();
        notificationList.setLayout(new BoxLayout(notificationList, BoxLayout.Y_AXIS));
        notificationList.setBackground(PURPLE_BACKGROUND);
    
        notificationList.add(createStyledNotificationCard(
            "🔔 Lembrete de prazo",
            "Faltam 3 dias para o término do prazo da etapa de revisão do projeto."
        ));
        notificationList.add(Box.createRigidArea(new Dimension(0, 10)));
    
        notificationList.add(createStyledNotificationCard(
            "✅ Conclusão de Etapa",
            "A etapa 'Planejamento' foi concluída. A próxima etapa inicia em 01/11."
        ));
        notificationList.add(Box.createRigidArea(new Dimension(0, 10)));
    
        notificationList.add(createStyledNotificationCard(
            "⚠️ Atualização no sistema",
            "O sistema estará indisponível para manutenção no próximo final de semana."
        ));
        notificationList.add(Box.createRigidArea(new Dimension(0, 10)));
    
        notificationList.add(createStyledNotificationCard(
            "📅 Reunião marcada",
            "A reunião de planejamento foi agendada para 03/11 às 10:00."
        ));
        notificationList.add(Box.createRigidArea(new Dimension(0, 10)));
    
        notificationList.add(createStyledNotificationCard(
            "📢 Alerta de segurança",
            "Houve uma tentativa de acesso não autorizado. Verifique as configurações de segurança."
        ));
        notificationList.add(Box.createRigidArea(new Dimension(0, 10)));
    
        notificationList.add(createStyledNotificationCard(
            "🔍 Revisão dos processos",
            "A revisão de todos os processos internos está marcada para o dia 05/11 às 14:00."
        ));
        notificationList.add(Box.createRigidArea(new Dimension(0, 10)));
    
        notificationList.add(createStyledNotificationCard(
            "🛠️ Manutenção programada",
            "A manutenção no servidor será realizada no dia 07/11 das 02:00 às 06:00."
        ));
        notificationList.add(Box.createRigidArea(new Dimension(0, 10)));
    
        notificationList.add(createStyledNotificationCard(
            "🚨 Alerta de prazo",
            "O prazo para submissão dos relatórios está se aproximando! Envie até 10/11."
        ));
        notificationList.add(Box.createRigidArea(new Dimension(0, 10)));
    
        notificationList.add(createStyledNotificationCard(
            "📊 Relatórios disponíveis",
            "Os relatórios financeiros do mês anterior já estão disponíveis para consulta."
        ));
        notificationList.add(Box.createRigidArea(new Dimension(0, 10)));
    
        JScrollPane scrollPane = new JScrollPane(notificationList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        return panel;
    }
    

    private JPanel createStyledNotificationCard(String title, String message) {
        JPanel notificationCard = new JPanel(new BorderLayout());
        notificationCard.setBackground(Color.WHITE);
        notificationCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PURPLE_LIGHT, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        titleLabel.setForeground(PURPLE_DARK);

        JLabel messageLabel = new JLabel("<html>" + message + "</html>");
        messageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        messageLabel.setForeground(Color.GRAY);

        notificationCard.add(titleLabel, BorderLayout.NORTH);
        notificationCard.add(messageLabel, BorderLayout.CENTER);

        notificationCard.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                notificationCard.setBackground(new Color(230, 240, 250));
            }
    
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                notificationCard.setBackground(Color.WHITE);
            }
        });
    
        return notificationCard;
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
    
            Evento evento = new Evento(title, date, time, type, status, description);
            eventos.add(evento);
    
            dialog.dispose();
            updateCalendarGrid();
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

    private void saveEventsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("eventos.txt", false))) {
            for (Evento evento : eventos) {
                writer.write(evento.getTitulo() + "," +
                            evento.getData() + "," +
                            evento.getHora() + "," +
                            evento.getTipo() + "," +
                            evento.getStatus() + "," +
                            evento.getDescricao());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao salvar eventos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteEvent(String dateKey, String eventTitle) {
        eventos.removeIf(event -> event.getData().equals(dateKey) && event.getTitulo().equals(eventTitle));
        JOptionPane.showMessageDialog(frame, "Evento '" + eventTitle + "' excluído com sucesso!");

        updateCalendarGrid();
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
                    Integer.parseInt(dataParts[2]),
                    Integer.parseInt(dataParts[1]) - 1,
                    Integer.parseInt(dataParts[0])
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

class Nota {
    private String data;
    private String observacoes;

    public Nota(String data, String observacoes) {
        this.data = data;
        this.observacoes = observacoes;
    }

    public String getData() {
        return data;
    }

    public String getObservacoes() {
        return observacoes;
    }
}

