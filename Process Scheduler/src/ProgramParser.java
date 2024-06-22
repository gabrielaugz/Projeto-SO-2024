import java.io.BufferedReader; // Classe para ler texto de uma entrada de caracteres, armazenando caracteres em buffer para fornecer uma leitura eficiente.
import java.io.FileReader; // Classe para ler caracteres de arquivos.
import java.io.IOException; // Classe de exceção usada para indicar problemas durante operações de E/S.
import java.util.ArrayList; // Implementação de lista baseada em array.
import java.util.List; // Interface que representa uma lista de elementos.

public class ProgramParser {
    // Método estático que analisa um arquivo e cria um processo a partir dele
    public static Process parseFile(String fileName) throws IOException {
        // Tenta abrir e ler o arquivo usando um BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine().trim(); // Lê a primeira linha e remove espaços em branco
            if (!line.startsWith("program ")) { // Verifica se a linha começa com "program "
                throw new IllegalArgumentException("Invalid program header"); // Lança exceção se o cabeçalho do programa for inválido
            }

            String programName = line.split(" ")[1]; // Extrai o nome do programa
            List<String> instructions = new ArrayList<>(); // Cria uma lista para armazenar as instruções
            reader.readLine(); // Pula a linha "begin"
            
            // Lê as linhas até encontrar "end"
            while (!(line = reader.readLine().trim()).equals("end")) {
                instructions.add(line); // Adiciona cada linha à lista de instruções
            }
            
            return new Process(programName, instructions); // Retorna um novo processo com o nome e as instruções
        }
    }
}
