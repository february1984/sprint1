import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command = "Start";
        ArrayList<String> fileLines = new ArrayList<>();//Список для хранения данных из файлов
        ArrayList<String> finalRep = new ArrayList<>();//Список для хранения отчетов

        while (!command.equals("E")){
            System.out.println("Введите команду:\t" +
                    "M-Месячный отчет\t" +
                    "Y-Годовой отчет\t" +
                    "E-Выход\t");
            command = scanner.nextLine();
            if (command.equals("S")){
                System.out.println("Пожалуйста введите номер месяца:");
                String month = scanner.nextLine();
                fileLines = getLines(month);
                showFile(fileLines);
                }
            else if (command.equals("M")){
                System.out.println("Пожалуйста введите номер месяца:");
                    String month = scanner.nextLine();
                    fileLines = getLines(month);//Считываем файл
                    finalRep = monthReport(fileLines, month);
            }
            else if (command.equals("Y")){
                fileLines = getLines("0");//Считываем файл
                finalRep = yearReport(fileLines);
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
    private static ArrayList<String> getLines(String month){
        ArrayList<String> result = new ArrayList<>();
        if (!month.equals("0")) {
            if (Integer.parseInt(month) < 10) {
                String file = readFiles("C:\\Users\\Admin\\IdeaProjects\\sprint1\\files\\m.20240" + month + ".csv");
                if (file != null) {
                    String[] lines = file.split("\\n");
                    result.addAll(Arrays.asList(lines).subList(1, lines.length));
                }
            }
            if (Integer.parseInt(month) >= 10) {
                String file = readFiles("C:\\Users\\Admin\\IdeaProjects\\sprint1\\files\\m.2024" + month + ".csv");
                if (file != null) {
                    String[] lines = file.split("\\n");
                    result.addAll(Arrays.asList(lines).subList(1, lines.length));
                }
            }
        } else {
            for(int i = 0; i < 10; i++){
                String file = readFiles("C:\\Users\\Admin\\IdeaProjects\\sprint1\\files\\m.20240" + i + ".csv");
                if (file != null) {
                    String[] lines = file.split("\\n");
                    for (int j = 0; j < lines.length; j++){
                        lines[j]=i + "," + lines[j];
                    }
                    result.addAll(Arrays.asList(lines).subList(1, lines.length));
                }
            }
            for(int i = 10; i < 13; i++){
                String file = readFiles("C:\\Users\\Admin\\IdeaProjects\\sprint1\\files\\m.2024" + i + ".csv");
                if (file != null) {
                    String[] lines = file.split("\\n");
                    for (int j = 0; j < lines.length; j++){
                        lines[j]=i + "," + lines[j];
                    }
                    result.addAll(Arrays.asList(lines).subList(1, lines.length));
                }
            }
        }
        return result;
    }
    private static void showFile(ArrayList<String> file){
        for (String value : file) {
            System.out.println(value);
        }
    }
    private static ArrayList<String> monthReport(ArrayList<String> fileLines, String month){
        ArrayList<String> result = new ArrayList<>();//Результирующий список с отчетом
        String writeToReport = "0";//Строка для обработки записи в отчет
        for (String fileLine : fileLines) {//Запись в список уникальных пар Объект + Тип транзакции
            String[] readLine = fileLine.split(",");
                writeToReport = readLine[0] + " " + readLine[1];
                if (!result.contains(writeToReport)) {
                    result.add(writeToReport);
                }
        }
        for (String currResultLine : result) {//Проходимся по уникальным парам и плюсуем в них соответвующие транзакции
            double sum = 0;
            double cost = 0;
            for (String fileLine : fileLines) {
                String[] readLine = fileLine.split(",");
                    if (currResultLine.equals(readLine[0] + " " + readLine[1])) {
                        sum += Double.parseDouble(readLine[2]);
                        cost = Double.parseDouble(readLine[3]);
                    }
            }
            result.set(result.indexOf(currResultLine),currResultLine + " " + sum + " " + cost);
        }
        for (String curr :result){
            System.out.println(curr);
        }
        return result;
    }
    private static ArrayList<String> yearReport(ArrayList<String> fileLines){
        ArrayList<String> result = new ArrayList<>();//Результирующий список с отчетом
        String writeToReport = "0";//Строка для обработки записи в отчет
        for (String fileLine : fileLines) {//Запись в список уникальных пар месяц + Тип транзакции
            String[] readLine = fileLine.split(",");
            writeToReport = readLine[0] + "," + readLine[2];
            if (!result.contains(writeToReport)) {
                result.add(writeToReport);
            }
        }
        for (String currResultLine : result) {//Проходимся по уникальным парам и плюсуем в них соответвующие транзакции
            double sum = 0;
            for (String fileLine : fileLines) {
                String[] readLine = fileLine.split(",");
                if (currResultLine.equals(readLine[0] + "," + readLine[2])) {
                    sum += Double.parseDouble(readLine[3])*Double.parseDouble(readLine[4]);
                }
            }
            result.set(result.indexOf(currResultLine),currResultLine + "," + sum);
        }
        for (String curr :result){
            System.out.println(curr);
        }
        return result;
    }
    private static String choosePath (String fileName){
        System.out.println("""
                Путь по умолчанию:
                C:\\Users\\Admin\\IdeaProjects\\sprint1
                Хотие изменить путь? (Y/N)""");
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
