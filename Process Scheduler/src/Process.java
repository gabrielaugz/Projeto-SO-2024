import java.util.List; // Interface que representa uma lista de elementos.

public class Process {
    private final String id; // Identificador único do processo
    private final List<String> instructions; // Lista de instruções do processo
    private int instructionPointer; // Apontador para a próxima instrução a ser executada
    private long cpuTime; // Tempo total de CPU utilizado pelo processo
    private long ioTime; // Tempo total de I/O utilizado pelo processo
    private int priority; // Prioridade do processo

    // Construtor que inicializa o processo com seu ID e lista de instruções
    public Process(String id, List<String> instructions) {
        this.id = id; // Inicializa o ID do processo
        this.instructions = instructions; // Inicializa a lista de instruções
        this.instructionPointer = 0; // Inicializa o apontador de instruções
        this.cpuTime = 0; // Inicializa o tempo de CPU
        this.ioTime = 0; // Inicializa o tempo de I/O
        this.priority = 0; // Inicializa a prioridade padrão
    }

    // Retorna o ID do processo
    public String getId() {
        return id;
    }

    // Retorna a lista de instruções do processo
    public List<String> getInstructions() {
        return instructions;
    }

    // Retorna o apontador de instruções atual
    public int getInstructionPointer() {
        return instructionPointer;
    }

    // Incrementa o apontador de instruções
    public void incrementInstructionPointer() {
        instructionPointer++;
    }

    // Verifica se há mais instruções a serem executadas
    public boolean hasNextInstruction() {
        return instructionPointer < instructions.size();
    }

    // Retorna a próxima instrução a ser executada
    public String getNextInstruction() {
        return instructions.get(instructionPointer);
    }

    // Adiciona tempo de CPU ao processo
    public void addCpuTime(long time) {
        this.cpuTime += time;
    }

    // Adiciona tempo de I/O ao processo
    public void addIoTime(long time) {
        this.ioTime += time;
    }

    // Retorna o tempo total de CPU utilizado
    public long getCpuTime() {
        return cpuTime;
    }

    // Retorna o tempo total de I/O utilizado
    public long getIoTime() {
        return ioTime;
    }

    // Determina se o processo é CPU-bound ou I/O-bound
    public String getProcessType() {
        return cpuTime > ioTime ? "CPU-bound" : "I/O-bound";
    }

    // Retorna a prioridade do processo
    public int getPriority() {
        return priority;
    }

    // Define a prioridade do processo
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
