import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a directory or a TXT file path as an argument.");
            return;
        }

        String path = args[0];
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("The specified path does not exist.");
            return;
        }

        if (file.isDirectory()) {
            // Si es un directorio, listar su contenido en "resultado.txt"
            File outputFile = new File("resultado.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                listContent(file, " ", writer);
                System.out.println("The directory listing has been saved in resultado.txt");
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        } else if (file.isFile() && file.getName().endsWith(".txt")) {
            // Si es un archivo TXT, leer y mostrar su contenido
            readTxtFile(file);
        } else {
            System.out.println("The specified path is neither a directory nor a TXT file.");
        }
    }

    public static void listContent(File directory, String tabulador, BufferedWriter writer) throws IOException {
        tabulador += "   ";

        if (!directory.exists() || !directory.isDirectory()) {
            writer.write("Specified directory does not exist or is invalid: " + directory.getAbsolutePath());
            writer.newLine();
            return;
        }

        File[] content = directory.listFiles();
        if (content == null || content.length == 0) {
            writer.write("Directory is empty: " + directory.getAbsolutePath());
            writer.newLine();
        } else {
            Arrays.sort(content, Comparator.comparing(File::getName));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (File f : content) {
                String lastModified = dateFormat.format(f.lastModified());
                String line;

                if (f.isDirectory()) {
                    line = tabulador + "[D] " + f.getName() + " (Last modified: " + lastModified + ")";
                    writer.write(line);
                    writer.newLine();
                    listContent(f, tabulador, writer);
                } else {
                    line = tabulador + "[F] " + f.getName() + " (Last modified: " + lastModified + ")";
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }

    public static void readTxtFile(File file) {
        System.out.println("Reading content of: " + file.getName());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}