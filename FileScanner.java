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
}
