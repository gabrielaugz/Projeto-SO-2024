import java.util.Queue; // Importa a interface Queue
import java.util.LinkedList; // Importa a implementação LinkedList
import java.util.concurrent.Executors; // Importa a classe Executors para gerenciar threads
import java.util.concurrent.ScheduledExecutorService; // Importa a interface ScheduledExecutorService
import java.util.concurrent.TimeUnit; // Importa a enumeração TimeUnit para especificar unidades de tempo
import java.util.function.Consumer; // Importa a interface Consumer para aceitar funções lambda

public class LongTermScheduler extends Thread implements InterSchedulerInterface, NotificationInterface {
    private final Queue<Process> processQueue; // Fila de processos a serem escalonados
    private final ShortTermScheduler shortTermScheduler; // Referência ao escalonador de curto prazo
    private final Consumer<String> outputConsumer; // Consumidor de saída para mensagens de status
    private final ScheduledExecutorService scheduler; // Serviço de agendamento para executar tarefas periodicamente

    // Construtor que inicializa as variáveis
    public LongTermScheduler(ShortTermScheduler shortTermScheduler, Consumer<String> outputConsumer) {
        this.processQueue = new LinkedList<>(); // Inicializa a fila de processos
        this.shortTermScheduler = shortTermScheduler; // Configura o escalonador de curto prazo
        this.outputConsumer = outputConsumer; // Configura o consumidor de saída
        this.scheduler = Executors.newScheduledThreadPool(1); // Cria um agendador com um único thread
    }

    // Método que é chamado quando o thread é iniciado
    @Override
    public void run() {
        // Agendador que chama o método scheduleProcesses a cada segundo
        scheduler.scheduleAtFixedRate(this::scheduleProcesses, 0, 1, TimeUnit.SECONDS);
    }

    // Método que move processos da fila de longo prazo para a de curto prazo
    private void scheduleProcesses() {
        // outputConsumer.accept("Scheduling processes..."); // Opcional: exibe mensagem de agendamento
        if (!processQueue.isEmpty() && shortTermScheduler.getProcessLoad() < 10) { // Verifica se há processos na fila e se há espaço no escalonador de curto prazo
            Process process = processQueue.poll(); // Remove o processo da fila de longo prazo
            shortTermScheduler.addProcess(process); // Adiciona o processo ao escalonador de curto prazo
            outputConsumer.accept("Process moved to short term scheduler: " + process.getId()); // Exibe mensagem informando que o processo foi movido
        }
    }

    // Método para adicionar um processo à fila de longo prazo
    @Override
    public void addProcess(Process process) {
        processQueue.add(process); // Adiciona o processo à fila
        outputConsumer.accept("Process added to long term scheduler: " + process.getId()); // Exibe mensagem informando que o processo foi adicionado
    }

    // Método para obter a carga de processos na fila de longo prazo
    @Override
    public int getProcessLoad() {
        return processQueue.size(); // Retorna o número de processos na fila
    }

    // Método para exibir informações
    @Override
    public void display(String info) {
        outputConsumer.accept(info); // Exibe a informação passada como parâmetro
    }
}
