import java.io.IOException; // Classe de exceção usada para indicar problemas durante operações de E/S.
import java.util.function.Consumer; // Fornece a interface funcional Consumer usada para passar funções lambda.
import java.util.ArrayList; // Implementação de lista baseada em array.
import java.util.List; // Interface que representa uma lista de elementos.

public class UserInterface extends Thread implements ControlInterface, SubmissionInterface, NotificationInterface {
    private final LongTermScheduler longTermScheduler; // Referência ao escalonador de longo prazo
    private final Consumer<String> outputConsumer; // Consumidor de saída para mensagens de status
    private final List<Process> storedProcesses; // Armazena processos submetidos enquanto a simulação não está em execução
    private boolean isSimulationRunning; // Indica se a simulação está em execução

    // Construtor que inicializa as variáveis
    public UserInterface(LongTermScheduler longTermScheduler, ShortTermScheduler shortTermScheduler, Consumer<String> outputConsumer) {
        this.longTermScheduler = longTermScheduler; // Inicializa o escalonador de longo prazo
        this.outputConsumer = outputConsumer; // Inicializa o consumidor de saída
        this.storedProcesses = new ArrayList<>(); // Inicializa a lista de processos armazenados
        this.isSimulationRunning = false; // Inicializa o estado da simulação como não em execução
    }

    // Método que é chamado quando o thread é iniciado
    @Override
    public void run() {
        // Mantém a implementação para comandos de console se necessário
    }

    // Método para submeter um trabalho
    @Override
    public boolean submitJob(String fileName) {
        try {
            Process process = ProgramParser.parseFile(fileName); // Analisa o arquivo e cria um processo
            if (isSimulationRunning) { // Verifica se a simulação está em execução
                longTermScheduler.addProcess(process); // Adiciona o processo ao escalonador de longo prazo
                outputConsumer.accept("Process immediately moved to long term scheduler: " + process.getId());
            } else {
                storedProcesses.add(process); // Armazena o processo sem iniciar a simulação
                outputConsumer.accept("Job submitted: " + fileName);
            }
            return true;
        } catch (IOException e) {
            outputConsumer.accept("Failed to read file: " + e.getMessage());
            return false;
        }
    }

    // Método para exibir a fila de submissão
    @Override
    public void displaySubmissionQueue() {
        // Implementação para exibir a fila de submissão
    }

    // Método para iniciar a simulação
    @Override
    public void startSimulation() {
        if (!isSimulationRunning) { // Verifica se a simulação não está em execução
            isSimulationRunning = true; // Define o estado da simulação como em execução
            outputConsumer.accept("Simulation started.");
            moveStoredProcessesToLongTermScheduler(); // Move os processos armazenados para o escalonador de longo prazo
        } else {
            outputConsumer.accept("Simulation is already running.");
        }
    }

    // Método para mover os processos armazenados para o escalonador de longo prazo
    private void moveStoredProcessesToLongTermScheduler() {
        outputConsumer.accept("Moving stored processes to long term scheduler...");
        for (Process process : storedProcesses) {
            longTermScheduler.addProcess(process); // Adiciona o processo ao escalonador de longo prazo
            outputConsumer.accept("Process moved to long term scheduler: " + process.getId());
        }
        storedProcesses.clear(); // Limpa a lista de processos armazenados após iniciar a simulação
    }

    // Método para suspender a simulação
    @Override
    public void suspendSimulation() {
        isSimulationRunning = false; // Define o estado da simulação como não em execução
        outputConsumer.accept("Simulation suspended.");
        // Implementação para suspender a simulação
    }

    // Método para retomar a simulação
    @Override
    public void resumeSimulation() {
        if (!isSimulationRunning) { // Verifica se a simulação não está em execução
            isSimulationRunning = true; // Define o estado da simulação como em execução
            outputConsumer.accept("Simulation resumed.");
            moveStoredProcessesToLongTermScheduler(); // Move os processos armazenados para o escalonador de longo prazo
        } else {
            outputConsumer.accept("Simulation is already running.");
        }
    }

    // Método para parar a simulação
    @Override
    public void stopSimulation() {
        isSimulationRunning = false; // Define o estado da simulação como não em execução
        outputConsumer.accept("Simulation stopped.");
        // Implementação para parar a simulação
    }

    // Método para exibir as filas de processos
    @Override
    public void displayProcessQueues() {
        // Implementação para exibir as filas de processos
    }

    // Método para exibir uma mensagem
    @Override
    public void display(String info) {
        outputConsumer.accept(info); // Exibe a informação passada como parâmetro
    }

    // Método para pausar a simulação
    public void pauseSimulation() {
        isSimulationRunning = false; // Define o estado da simulação como não em execução
        outputConsumer.accept("Simulation paused.");
        // Implementação para pausar a simulação
    }
}