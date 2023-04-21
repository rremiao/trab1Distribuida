import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileScanner {
    public static void main(String[] args) {
    }

    public List<String> readPorts(String portFile) {
        List<String> list = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File("./portas/" + portFile));

            while (scanner.hasNextLine()) {
                list.add(scanner.nextLine());
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<String> readInstructions() {
        List<String> list = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File("arquivoInstrucoes.txt"));

            while (scanner.hasNextLine()) {
                list.add(scanner.nextLine());
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<String> readBaseFile() {
        List<String> list = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File("arquivoBase.txt"));

            while (scanner.hasNextLine()) {
                list.add(scanner.nextLine());
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }
}
