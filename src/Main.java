import javax.swing.text.Style;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivilegedExceptionAction;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command = "Start";
        ArrayList<String[]> fileLines = new ArrayList<>();
        boolean execResult;

        while (!command.equals("E")){
            System.out.println("Введите команду:\t" +
                    "M-Месячный отчет\t" +
                    "Y-Годовой отчет\t" +
                    "E-Выход\t");
            command = scanner.nextLine();
            fileLines = getLines();//Считываем файл
            if (command.equals("S")){
                showFile(fileLines);
                }
            else if (command.equals("M")){
                System.out.println("Пожалуйста введите номер месяца:");
                    int month = Integer.parseInt(scanner.nextLine());
            }

        }
    }
    private static String readFiles(String path){
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            return null;
        }
    }
    private static ArrayList<String[]> getLines(){
        ArrayList<String[]> result = new ArrayList<>();
        for (int m = 1; m < 10; m++) {
            String file = readFiles("C:\\Users\\Admin\\IdeaProjects\\sprint1\\files\\m.20240"+m+".csv");
            if (file != null) {
                String[] lines = file.split("\\n");
                for (int i = 1; i < lines.length; i++) {
                        lines[i] = m+ "," + lines[i];
                        String[] columns = lines[i].split(",");
                        result.add(columns);
                }
            }
        }
        for (int m = 10; m < 13; m++) {
            String file = readFiles("C:\\Users\\Admin\\IdeaProjects\\sprint1\\files\\m.2024"+m+".csv");
            if (file != null) {
                String[] lines = file.split("\\n");
                for (int i = 1; i < lines.length; i++) {
                    lines[i] = m+ "," + lines[i];
                    String[] columns = lines[i].split(",");
                    result.add(columns);
                }
            }
        }
        return result;
    }
    private static void showFile(ArrayList<String[]> file){
        for (String[] value : file) {
            System.out.println(value[0]+" "+value[1]+" "+value[2]+" "+value[3]+" "+value[4]);
        }
    }
    private static void showArray(String[] file){
        for (String value : file) {
            System.out.println(value);
        }
    }
    private static void monthReport(ArrayList<String> fileLines, int month){
        String[][] result = new String[4][fileLines.size()];
            for (int i = 0; i < fileLines.size(); i++){
                String currLine = fileLines.get(i);
                String[] currLineValues = currLine.split(",");

            }
    }
    private static String choosePath (String fileName){
        System.out.println("Путь по умолчанию:\n" +
                "C:\\Users\\Admin\\IdeaProjects\\sprint1\n" +
                "Хотие изменить путь? (Y/N)");
        Scanner scanner = new Scanner(System.in);
        String choise = "0";
        choise = scanner.nextLine();
        if(choise.equals("Y")){
            System.out.println("Введите новый путь");
            Scanner path = new Scanner(System.in);
            return  readFiles(path+fileName);
        } else if (choise.equals("N")) {
            return readFiles("C:\\Users\\Admin\\IdeaProjects\\sprint1"+fileName);
        } else {
            System.out.println("Команда не верна. Введите Y или N");
            return "0";
        }
    }
}