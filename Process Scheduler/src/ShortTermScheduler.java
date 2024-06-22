import java.util.PriorityQueue; // Interface que representa uma fila de prioridade.
import java.util.Comparator; // Interface para comparar elementos.
import java.util.Queue; // Interface que representa uma fila de elementos.
import java.util.LinkedList; // Implementação de fila encadeada.
import java.util.List; // Interface que representa uma lista de elementos.
import java.util.ArrayList; // Implementação de lista baseada em array.
import java.util.concurrent.Executors; // Fornece métodos para criar executores que podem ser usados para gerenciar threads.
import java.util.concurrent.ScheduledExecutorService; // Interface que representa um executor que pode agendar comandos para execução futura.
import java.util.concurrent.TimeUnit; // Enum usado para especificar unidades de tempo.
import java.util.function.Consumer; // Fornece a interface funcional Consumer usada para passar funções lambda.

public class ShortTermScheduler extends Thread implements InterSchedulerInterface, NotificationInterface {
    private final PriorityQueue<Process> readyQueue; // Fila de processos prontos, ordenada por prioridade
    private final Queue<Process> blockedQueue; // Fila de processos bloqueados
    private final List<Process> finishedProcesses; // Lista de processos finalizados
    private final int quantum; // Quantum de tempo para o escalonamento Round-Robin
    private final ScheduledExecutorService scheduler; // Serviço de agendamento para executar tarefas periodicamente
    private final Consumer<String> outputConsumer; // Consumidor de saída para mensagens de status

    // Construtor que inicializa as variáveis
    public ShortTermScheduler(int quantum, Consumer<String> outputConsumer) {
        this.readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority).reversed()); // Inicializa a fila de prontos com ordenação por prioridade
        this.blockedQueue = new LinkedList<>(); // Inicializa a fila de bloqueados
        this.finishedProcesses = new ArrayList<>(); // Inicializa a lista de processos finalizados
        this.quantum = quantum; // Configura o quantum de tempo
        this.scheduler = Executors.newScheduledThreadPool(1); // Cria um agendador com um único thread
        this.outputConsumer = outputConsumer; // Configura o consumidor de saída
    }

    // Método que é chamado quando o thread é iniciado
    @Override
    public void run() {
        // Agendador que chama o método executeProcess a cada intervalo de quantum
        scheduler.scheduleAtFixedRate(() -> {
            if (!readyQueue.isEmpty()) { // Verifica se há processos na fila de prontos
                Process currentProcess = readyQueue.poll(); // Remove o processo da fila de prontos
                executeProcess(currentProcess); // Executa o processo
            }
        }, 0, quantum, TimeUnit.MILLISECONDS);
    }

    // Método que executa um processo
    private void executeProcess(Process process) {
        if (process.hasNextInstruction()) { // Verifica se o processo tem mais instruções
            String instruction = process.getNextInstruction(); // Obtém a próxima instrução
            if (instruction.startsWith("execute")) { // Verifica se a instrução é de execução
                outputConsumer.accept("Executing: " + instruction); // Exibe mensagem de execução
                process.incrementInstructionPointer(); // Incrementa o apontador de instruções

                long startTime = System.currentTimeMillis(); // Marca o início da execução
                scheduler.schedule(() -> {
                    long endTime = System.currentTimeMillis(); // Marca o fim da execução
                    process.addCpuTime(endTime - startTime); // Adiciona o tempo de CPU ao processo

                    if (process.hasNextInstruction()) {
                        // Processos CPU-bound voltam ao final da fila
                        process.setPriority(0); // Reseta a prioridade do processo CPU-bound
                        readyQueue.add(process); // Adiciona o processo de volta à fila de prontos
                    } else {
                        finishedProcesses.add(process); // Adiciona o processo à lista de finalizados
                        outputConsumer.accept("Process finished: " + process.getId()); // Exibe mensagem de finalização
                        outputConsumer.accept("Process " + process.getId() + " is " + process.getProcessType() + "."); // Exibe o tipo do processo (CPU-bound ou I/O-bound)
                    }
                }, quantum, TimeUnit.MILLISECONDS);
            } else if (instruction.startsWith("block")) { // Verifica se a instrução é de bloqueio
                int blockTime = Integer.parseInt(instruction.split(" ")[1]) * quantum; // Calcula o tempo de bloqueio
                outputConsumer.accept("Blocking for " + blockTime + "ms: " + instruction); // Exibe mensagem de bloqueio
                process.incrementInstructionPointer(); // Incrementa o apontador de instruções

                long startTime = System.currentTimeMillis(); // Marca o início do bloqueio
                scheduler.schedule(() -> {
                    long endTime = System.currentTimeMillis(); // Marca o fim do bloqueio
                    process.addIoTime(endTime - startTime); // Adiciona o tempo de I/O ao processo

                    // Processos I/O-bound retornam com prioridade aumentada
                    process.setPriority(process.getPriority() + 1); // Aumenta a prioridade do processo I/O-bound
                    readyQueue.add(process); // Adiciona o processo de volta à fila de prontos
                }, blockTime, TimeUnit.MILLISECONDS);
            }
        } else {
            finishedProcesses.add(process); // Adiciona o processo à lista de finalizados
            outputConsumer.accept("Process finished: " + process.getId()); // Exibe mensagem de finalização
            outputConsumer.accept("Process " + process.getId() + " is " + process.getProcessType() + "."); // Exibe o tipo do processo (CPU-bound ou I/O-bound)
        }
    }

    // Método para adicionar um processo à fila de prontos
    @Override
    public void addProcess(Process process) {
        readyQueue.add(process); // Adiciona o processo à fila de prontos
    }

    // Método para obter a carga de processos
    @Override
    public int getProcessLoad() {
        return readyQueue.size() + blockedQueue.size(); // Retorna o número de processos na fila de prontos e bloqueados
    }

    // Método para exibir informações
    @Override
    public void display(String info) {
        outputConsumer.accept(info); // Exibe a informação passada como parâmetro
    }
}