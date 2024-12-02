import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.List;

public class SyncCesar {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private ArrayList<Task> tasks;
    private ArrayList<Evento> eventos;

    private final Color PURPLE_PRIMARY = new Color(102, 51, 153);
    private final Color PURPLE_LIGHT = new Color(147, 112, 219);
    private final Color PURPLE_DARK = new Color(75, 0, 130);
    private final Color PURPLE_BACKGROUND = new Color(147, 112, 219);

    private static final String DATA_DIR = "data";
    private static final String EVENTOS_FILE = DATA_DIR + "/eventos.txt";

    public SyncCesar() {
        frame = new JFrame("SyncCesar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.getContentPane().setBackground(PURPLE_BACKGROUND);
        tasks = new ArrayList<>();
        eventos = new ArrayList<>();

        // Criar diretório de dados se não existir
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            if (dataDir.mkdirs()) {
                System.out.println("Diretório criado: " + DATA_DIR);
            } else {
                System.err.println("Não foi possível criar o diretório: " + DATA_DIR);
            }
        }

        // Criar arquivo de eventos se não existir
        File eventosFile = new File(EVENTOS_FILE);
        if (!eventosFile.exists()) {
            try {
                if (eventosFile.createNewFile()) {
                    System.out.println("Arquivo criado: " + EVENTOS_FILE);
                }
            } catch (IOException e) {
                System.err.println("Erro ao criar arquivo: " + e.getMessage());
            }
        }

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

        String[] menuItems = {"Dashboard", "Calendário", "Notificações"};
        CardLayout cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(PURPLE_BACKGROUND);

        JPanel dashboardPanel = createDashboardContent();
        JPanel calendarPanel = createCalendarContent();
        JPanel notificationsPanel = createNotificationsContent();

        contentPanel.add(dashboardPanel, "Dashboard");
        contentPanel.add(calendarPanel, "Calendário");
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

        // Painel principal com GridBagLayout para melhor controle do layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(PURPLE_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Painel para as quatro seções de status
        JPanel statusPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statusPanel.setBackground(PURPLE_BACKGROUND);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Criar as quatro seções de status
        String[][] sections = {
            {"Provas", "60"},
            {"Pagamentos", "45"},
            {"Aulas", "75"},
            {"Atividade 4", "100"}
        };

        for (String[] section : sections) {
            JPanel sectionPanel = createSectionPanel(section[0], Integer.parseInt(section[1]));
            statusPanel.add(sectionPanel);
        }

        // Adicionar painel de status
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(statusPanel, gbc);

        // Criar seção de pendências
        JPanel pendenciasPanel = createPendenciasDashboardPanel();
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(pendenciasPanel, gbc);

        panel.add(mainPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPendenciasDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PURPLE_PRIMARY, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Cabeçalho do quadro
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(PURPLE_PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Painel esquerdo para título e contador
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(PURPLE_PRIMARY);
        
        JLabel titleLabel = new JLabel("Eventos Pendentes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel);

        // Contador de pendências
        int pendenciasCount = countPendencias();
        JLabel countLabel = new JLabel(pendenciasCount + " pendência(s)");
        countLabel.setForeground(Color.WHITE);
        leftPanel.add(countLabel);

        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Botão de notificação
        if (hasPendencias()) {
            JButton notifyButton = new JButton("Notificar Responsáveis");
            notifyButton.setBackground(Color.WHITE);
            notifyButton.setForeground(PURPLE_DARK);
            notifyButton.setFont(new Font("Arial", Font.BOLD, 12));
            notifyButton.setFocusPainted(false);
            notifyButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            notifyButton.addActionListener(e -> notifyResponsaveis());
            
            // Efeito hover
            notifyButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    notifyButton.setBackground(PURPLE_LIGHT);
                    notifyButton.setForeground(Color.WHITE);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    notifyButton.setBackground(Color.WHITE);
                    notifyButton.setForeground(PURPLE_DARK);
                }
            });
            
            headerPanel.add(notifyButton, BorderLayout.EAST);
        }

        panel.add(headerPanel, BorderLayout.NORTH);

        // Painel de eventos pendentes com scroll
        JPanel eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        eventsPanel.setBackground(Color.WHITE);

        // Verifica se há eventos para processar
        if (!eventos.isEmpty()) {
            Calendar hoje = Calendar.getInstance();
            hoje.set(Calendar.HOUR_OF_DAY, 0);
            hoje.set(Calendar.MINUTE, 0);
            hoje.set(Calendar.SECOND, 0);
            hoje.set(Calendar.MILLISECOND, 0);

            for (Evento evento : eventos) {
                try {
                    Calendar dataEvento = Calendar.getInstance();
                    String[] dataParts = evento.getData().split("/");
                    int dia = Integer.parseInt(dataParts[0]);
                    int mes = Integer.parseInt(dataParts[1]) - 1; // Mês começa em 0
                    int ano = Integer.parseInt(dataParts[2]);
                    
                    dataEvento.set(ano, mes, dia, 0, 0, 0);
                    dataEvento.set(Calendar.MILLISECOND, 0);

                    if (dataEvento.before(hoje) && "pendente".equalsIgnoreCase(evento.getStatus())) {
                        JPanel eventCard = createPendenciaMiniCard(evento);
                        eventsPanel.add(eventCard);
                        eventsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar data do evento: " + evento.getTitulo());
                    e.printStackTrace();
                }
            }

            if (eventsPanel.getComponentCount() == 0) {
                JPanel emptyPanel = new JPanel(new BorderLayout());
                emptyPanel.setBackground(Color.WHITE);
                JLabel emptyLabel = new JLabel("Não há eventos pendentes", SwingConstants.CENTER);
                emptyLabel.setForeground(PURPLE_DARK);
                emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                emptyPanel.add(emptyLabel, BorderLayout.CENTER);
                eventsPanel.add(emptyPanel);
            }
        } else {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(Color.WHITE);
            JLabel emptyLabel = new JLabel("Não há eventos cadastrados", SwingConstants.CENTER);
            emptyLabel.setForeground(PURPLE_DARK);
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            emptyPanel.add(emptyLabel, BorderLayout.CENTER);
            eventsPanel.add(emptyPanel);
        }

        JScrollPane scrollPane = new JScrollPane(eventsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Método auxiliar para contar pendências
    private int countPendencias() {
        Calendar hoje = Calendar.getInstance();
        hoje.set(Calendar.HOUR_OF_DAY, 0);
        hoje.set(Calendar.MINUTE, 0);
        hoje.set(Calendar.SECOND, 0);
        hoje.set(Calendar.MILLISECOND, 0);

        int count = 0;
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
                        count++;
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar data do evento: " + evento.getTitulo());
                }
            }
        }
        return count;
    }

    private JPanel createPendenciaMiniCard(Evento evento) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, PURPLE_LIGHT),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Painel esquerdo para título e data
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(evento.getTitulo());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(PURPLE_DARK);
        
        JLabel dateLabel = new JLabel(evento.getData() + " " + evento.getHora());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(Color.GRAY);

        leftPanel.add(titleLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        leftPanel.add(dateLabel);

        // Painel direito para tipo
        JLabel typeLabel = new JLabel(evento.getTipo());
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        typeLabel.setForeground(PURPLE_PRIMARY);

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(typeLabel, BorderLayout.EAST);

        return card;
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
        progressBar.setStringPainted(false);  // Removida a string da barra
        progressBar.setForeground(PURPLE_PRIMARY);
        progressBar.setBackground(Color.LIGHT_GRAY);
        progressBar.setPreferredSize(new Dimension(150, 20));
        progressBar.setMaximumSize(new Dimension(150, 20));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);


        sectionPanel.add(titleLabel);
        sectionPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sectionPanel.add(percentageLabel);
        sectionPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sectionPanel.add(progressBar);

        return sectionPanel;
    }

    private JPanel createCalendarContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PURPLE_BACKGROUND);
        
        // Painel superior com título e botão
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PURPLE_BACKGROUND);
        
        // Título do Calendário
        JLabel titleLabel = new JLabel("Calendário de Eventos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PURPLE_DARK);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Botão de adicionar com texto roxo
        JButton addEventButton = new JButton("Adicionar Evento");
        addEventButton.setBackground(Color.WHITE);
        addEventButton.setForeground(PURPLE_DARK);  // Texto roxo escuro
        addEventButton.setFont(new Font("Arial", Font.BOLD, 12));
        addEventButton.setFocusPainted(false);
        addEventButton.setBorder(BorderFactory.createLineBorder(PURPLE_PRIMARY));
        addEventButton.addActionListener(e -> showAddEventDialog(panel));
        
        // Efeito hover para o botão
        addEventButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addEventButton.setBackground(PURPLE_LIGHT);
                addEventButton.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                addEventButton.setBackground(Color.WHITE);
                addEventButton.setForeground(PURPLE_DARK);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(PURPLE_BACKGROUND);
        buttonPanel.add(addEventButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);

        // Recarregar eventos antes de exibir
        loadEventsFromFile();
        
        // Painel de eventos
        JPanel eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        eventsPanel.setBackground(PURPLE_BACKGROUND);
        
        // Organizar eventos por data
        Map<String, java.util.List<Evento>> eventosPorData = new TreeMap<>();
        for (Evento evento : eventos) {
            eventosPorData.computeIfAbsent(evento.getData(), k -> new ArrayList<>()).add(evento);
        }
        
        // Adicionar eventos organizados por data
        for (Map.Entry<String, java.util.List<Evento>> entry : eventosPorData.entrySet()) {
            // Adicionar cabeçalho da data
            JPanel dateHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
            dateHeader.setBackground(PURPLE_LIGHT);
            dateHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            JLabel dateLabel = new JLabel(formatDate(entry.getKey()));
            dateLabel.setForeground(Color.WHITE);
            dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
            dateHeader.add(dateLabel);
            eventsPanel.add(dateHeader);
            
            // Adicionar eventos do dia
            for (Evento evento : entry.getValue()) {
                JPanel eventCard = createEventCard(evento);
                eventsPanel.add(eventCard);
                eventsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(eventsPanel);
        scrollPane.setBackground(PURPLE_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    // Método auxiliar para formatar a data
    private String formatDate(String date) {
        try {
            String[] parts = date.split("/");
            String[] months = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", 
                             "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
            int month = Integer.parseInt(parts[1]) - 1;
            return parts[0] + " de " + months[month] + " de " + parts[2];
        } catch (Exception e) {
            return date;
        }
    }

    private void updateEventCards(JPanel eventsPanel) {
        eventsPanel.removeAll();
        
        for (Evento evento : eventos) {
            JPanel eventCard = createEventCard(evento);
            eventsPanel.add(eventCard);
            eventsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between cards
        }
        
        eventsPanel.revalidate();
        eventsPanel.repaint();
    }

    private JPanel createEventCard(Evento evento) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PURPLE_PRIMARY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Title with edit button
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel(evento.getTitulo());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(PURPLE_DARK);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        // Edit button
        JButton editButton = new JButton("Editar");
        editButton.setBackground(PURPLE_PRIMARY);
        editButton.setForeground(PURPLE_DARK);
        editButton.setFont(new Font("Arial", Font.BOLD, 12));
        editButton.setBorderPainted(false);
        editButton.setFocusPainted(false);
        editButton.addActionListener(e -> editEvent(evento));

        // Delete button
        JButton deleteButton = new JButton("Excluir");
        deleteButton.setBackground(Color.WHITE);
        deleteButton.setForeground(PURPLE_DARK);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
        deleteButton.setBorderPainted(true);
        deleteButton.setBorder(BorderFactory.createLineBorder(PURPLE_PRIMARY));
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteEvent(evento));

        // Status button
        JButton statusButton = new JButton(evento.getStatus());
        statusButton.setBackground("pendente".equals(evento.getStatus()) ? PURPLE_LIGHT : PURPLE_PRIMARY);
        statusButton.setForeground(Color.BLACK);
        statusButton.setFont(new Font("Arial", Font.BOLD, 12));
        statusButton.setBorderPainted(false);
        statusButton.setFocusPainted(false);
        statusButton.addActionListener(e -> toggleEventStatus(evento));

        buttonPanel.add(statusButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        titlePanel.add(buttonPanel, BorderLayout.EAST);

        // Event details
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        detailsPanel.setBackground(Color.WHITE);
        
        // Labels com texto roxo escuro
        JLabel dataLabel = new JLabel("Data: " + evento.getData() + " " + evento.getHora());
        JLabel tipoLabel = new JLabel("Tipo: " + evento.getTipo());
        JLabel statusLabel = new JLabel("Status: " + evento.getStatus());
        JLabel descLabel = new JLabel("Descrição: " + evento.getDescricao());
        
        // Definir cor roxa escura para todos os labels
        dataLabel.setForeground(PURPLE_DARK);
        tipoLabel.setForeground(PURPLE_DARK);
        statusLabel.setForeground(PURPLE_DARK);
        descLabel.setForeground(PURPLE_DARK);
        
        detailsPanel.add(dataLabel);
        detailsPanel.add(tipoLabel);
        detailsPanel.add(statusLabel);
        detailsPanel.add(descLabel);

        card.add(titlePanel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(detailsPanel);

        return card;
    }

    private void editEvent(Evento evento) {
        JDialog dialog = new JDialog(frame, "Editar Evento", true);
        dialog.setSize(300, 450);
        dialog.setLayout(new GridLayout(7, 2, 10, 10));

        JTextField titleField = new JTextField(evento.getTitulo());
        JTextField dateField = new JTextField(evento.getData());
        JTextField timeField = new JTextField(evento.getHora());
        JTextField typeField = new JTextField(evento.getTipo());
        JTextField statusField = new JTextField(evento.getStatus());
        JTextArea descriptionField = new JTextArea(evento.getDescricao());

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
            eventos.remove(evento);
            Evento updatedEvento = new Evento(
                titleField.getText(),
                dateField.getText(),
                timeField.getText(),
                typeField.getText(),
                statusField.getText(),
                descriptionField.getText()
            );
            eventos.add(updatedEvento);
            saveEventsToFile();
            updateEventCards((JPanel)((JScrollPane)((JPanel)frame.getContentPane()
                .getComponent(0)).getComponent(1)).getViewport().getView());
            dialog.dispose();
        });
        dialog.add(saveButton);

        dialog.setVisible(true);
    }

    private void deleteEvent(Evento evento) {
        int confirm = JOptionPane.showConfirmDialog(
            frame,
            "Tem certeza que deseja excluir este evento?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            eventos.remove(evento);
            saveEventsToFile();
            updateEventCards((JPanel)((JScrollPane)((JPanel)frame.getContentPane()
                .getComponent(0)).getComponent(1)).getViewport().getView());
        }
    }

    private void toggleEventStatus(Evento evento) {
        String newStatus = "pendente".equals(evento.getStatus()) ? "concluído" : "pendente";
        Evento updatedEvento = new Evento(
            evento.getTitulo(),
            evento.getData(),
            evento.getHora(),
            evento.getTipo(),
            newStatus,
            evento.getDescricao()
        );
        eventos.remove(evento);
        eventos.add(updatedEvento);
        saveEventsToFile();
        updateEventCards((JPanel)((JScrollPane)((JPanel)frame.getContentPane()
            .getComponent(0)).getComponent(1)).getViewport().getView());
    }

    private void saveEventsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EVENTOS_FILE))) {
            for (Evento evento : eventos) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s",
                    evento.getTitulo(),
                    evento.getData(),
                    evento.getHora(),
                    evento.getTipo(),
                    evento.getStatus(),
                    evento.getDescricao()
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            // Recriar o painel de calendário
            Component[] components = calendarPanel.getComponents();
            calendarPanel.removeAll();
            
            // Recriar o cabeçalho
            JPanel headerPanel = (JPanel) components[0];
            calendarPanel.add(headerPanel, BorderLayout.NORTH);
            
            // Recriar o painel de eventos
            JPanel eventsPanel = new JPanel();
            eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
            eventsPanel.setBackground(PURPLE_BACKGROUND);
            
            // Organizar eventos por data
            Map<String, java.util.List<Evento>> eventosPorData = new TreeMap<>();
            for (Evento evt : eventos) {
                eventosPorData.computeIfAbsent(evt.getData(), k -> new ArrayList<>()).add(evt);
            }
            
            // Adicionar eventos organizados por data
            for (Map.Entry<String, java.util.List<Evento>> entry : eventosPorData.entrySet()) {
                JPanel dateHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
                dateHeader.setBackground(PURPLE_LIGHT);
                dateHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                JLabel dateLabel = new JLabel(formatDate(entry.getKey()));
                dateLabel.setForeground(Color.WHITE);
                dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
                dateHeader.add(dateLabel);
                eventsPanel.add(dateHeader);
                
                for (Evento evt : entry.getValue()) {
                    JPanel eventCard = createEventCard(evt);
                    eventsPanel.add(eventCard);
                    eventsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
            
            JScrollPane scrollPane = new JScrollPane(eventsPanel);
            scrollPane.setBackground(PURPLE_BACKGROUND);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            calendarPanel.add(scrollPane, BorderLayout.CENTER);
            
            calendarPanel.revalidate();
            calendarPanel.repaint();
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EVENTOS_FILE, true))) {
            String eventoStr = String.format("%s,%s,%s,%s,%s,%s",
                evento.getTitulo().trim(),
                evento.getData().trim(),
                evento.getHora().trim(),
                evento.getTipo().trim(),
                evento.getStatus().trim(),
                evento.getDescricao().trim()
            );
            writer.write(eventoStr);
            writer.newLine();
            System.out.println("Evento salvo: " + eventoStr);
        } catch (IOException e) {
            System.err.println("Erro ao salvar evento: " + e.getMessage());
        }
    }

    private void loadEventsFromFile() {
        File eventosFile = new File(EVENTOS_FILE);
        if (!eventosFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(eventosFile))) {
            String line;
            eventos.clear();
            
            while ((line = reader.readLine()) != null) {
                String[] eventDetails = line.split(",");
                if (eventDetails.length >= 5) {
                    Evento evento = new Evento(
                        eventDetails[0].trim(),
                        eventDetails[1].trim(),
                        eventDetails[2].trim(),
                        eventDetails[3].trim(),
                        eventDetails[4].trim(),
                        eventDetails.length > 5 ? eventDetails[5].trim() : ""
                    );
                    eventos.add(evento);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
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
            // Verifica se o status é pendente
            if ("pendente".equalsIgnoreCase(evento.getStatus())) {
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

                    // Verifica se a data é anterior à atual
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
