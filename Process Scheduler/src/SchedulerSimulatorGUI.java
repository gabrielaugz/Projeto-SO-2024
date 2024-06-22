import java.awt.*; // Importa classes necessárias para a criação da interface gráfica
import javax.swing.*; // Importa classes necessárias para os componentes Swing
import java.io.File; // Importa a classe File para manipulação de arquivos

public class SchedulerSimulatorGUI {
    private final LongTermScheduler longTermScheduler; // Instância do escalonador de longo prazo
    private final ShortTermScheduler shortTermScheduler; // Instância do escalonador de curto prazo
    private final UserInterface userInterface; // Instância da interface do usuário
    private JTextArea outputArea; // Área de texto para exibição de mensagens

    // Construtor que inicializa os componentes principais
    public SchedulerSimulatorGUI(int quantum) {
        shortTermScheduler = new ShortTermScheduler(quantum, this::appendToOutput); // Inicializa o escalonador de curto prazo
        longTermScheduler = new LongTermScheduler(shortTermScheduler, this::appendToOutput); // Inicializa o escalonador de longo prazo
        userInterface = new UserInterface(longTermScheduler, shortTermScheduler, this::appendToOutput); // Inicializa a interface do usuário

        createAndShowGUI(); // Cria e exibe a interface gráfica
        startThreads(); // Inicia as threads principais
    }

    // Método que cria e exibe a interface gráfica
    private void createAndShowGUI() {
        JFrame frame = new JFrame("Scheduler Simulator"); // Cria o frame principal
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define a operação padrão de fechamento
        frame.setSize(500, 400); // Define o tamanho da janela
        frame.setLayout(new BorderLayout()); // Define o layout da janela

        JPanel controlPanel = new JPanel(); // Cria um painel para os controles
        controlPanel.setLayout(new FlowLayout()); // Define o layout do painel

        JLabel fileLabel = new JLabel("File Path:"); // Rótulo para o campo de caminho do arquivo
        JTextField filePathField = new JTextField(20); // Campo de texto para o caminho do arquivo
        JButton submitButton = new JButton("Submit"); // Botão para submeter um único arquivo
        JButton multiSubmitButton = new JButton("Submit Multiple"); // Botão para submeter múltiplos arquivos
        JButton startButton = new JButton("Start Simulation"); // Botão para iniciar a simulação
        JButton pauseButton = new JButton("Pause Simulation"); // Botão para pausar a simulação

        // Adiciona ação ao botão de submeter um único arquivo
        submitButton.addActionListener(e -> {
            String filePath = filePathField.getText();
            if (!filePath.isEmpty()) {
                userInterface.submitJob(filePath); // Submete o trabalho
            } else {
                appendToOutput("Filename required."); // Exibe mensagem de erro
            }
        });

        // Adiciona ação ao botão de submeter múltiplos arquivos
        multiSubmitButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(); // Cria um seletor de arquivos
            fileChooser.setMultiSelectionEnabled(true); // Habilita a seleção múltipla
            int result = fileChooser.showOpenDialog(frame); // Abre o diálogo de seleção de arquivos
            if (result == JFileChooser.APPROVE_OPTION) {
                File[] files = fileChooser.getSelectedFiles(); // Obtém os arquivos selecionados
                for (File file : files) {
                    userInterface.submitJob(file.getAbsolutePath()); // Submete cada arquivo
                }
            }
        });

        // Adiciona ação ao botão de iniciar a simulação
        startButton.addActionListener(e -> userInterface.startSimulation());
        // Adiciona ação ao botão de pausar a simulação
        pauseButton.addActionListener(e -> userInterface.pauseSimulation());

        // Adiciona os componentes ao painel de controle
        controlPanel.add(fileLabel);
        controlPanel.add(filePathField);
        controlPanel.add(submitButton);
        controlPanel.add(multiSubmitButton);
        controlPanel.add(startButton);
        controlPanel.add(pauseButton);

        // Cria a área de texto para exibição de mensagens
        outputArea = new JTextArea(15, 40);
        outputArea.setEditable(false); // Torna a área de texto não editável
        JScrollPane scrollPane = new JScrollPane(outputArea); // Adiciona a área de texto a um painel de rolagem

        frame.add(controlPanel, BorderLayout.NORTH); // Adiciona o painel de controle ao topo da janela
        frame.add(scrollPane, BorderLayout.CENTER); // Adiciona o painel de rolagem ao centro da janela

        frame.setVisible(true); // Torna a janela visível
    }

    // Método que adiciona uma mensagem à área de texto
    private void appendToOutput(String message) {
        SwingUtilities.invokeLater(() -> outputArea.append(message + "\n")); // Adiciona a mensagem na thread de eventos do Swing
    }

    // Método que inicia as threads principais
    private void startThreads() {
        new Thread(userInterface).start(); // Inicia a thread da interface do usuário
        new Thread(longTermScheduler).start(); // Inicia a thread do escalonador de longo prazo
        new Thread(shortTermScheduler).start(); // Inicia a thread do escalonador de curto prazo
    }

    // Método principal que inicia a aplicação
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java SchedulerSimulatorGUI <quantum>"); // Exibe mensagem de uso
            System.exit(1); // Encerra o programa
        }

        int quantum = Integer.parseInt(args[0]); // Converte o argumento de entrada em inteiro
        SwingUtilities.invokeLater(() -> new SchedulerSimulatorGUI(quantum)); // Inicia a aplicação na thread de eventos do Swing
    }
}
