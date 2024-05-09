import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command = "Start";
        ArrayList<String> fileLines = new ArrayList<>();//Список для хранения данных из файлов
        ArrayList<String> yearRep = new ArrayList<>();//Список для хранения отчетов
        ArrayList<String> monthRep = new ArrayList<>();//Список для хранения отчетов

        while (!command.equals("E")){
            System.out.println("Введите команду:\t" +
                    "M-Месячный отчет\t" +
                    "Y-Годовой отчет\t" +
                    "S-Вывести отчет\t" +
                    "С-Проверить отчет\t" +
                    "E-Выход\t");
            command = scanner.nextLine();
            switch (command) {
                case "S" -> {
                    System.out.println("Годовой отчет - Y, месячный отчет - M");
                    String month = scanner.nextLine();
                    if (month.equals("M")) {
                        showFile(monthRep);
                    }
                    if (month.equals("Y")) {
                        showFile(yearRep);
                    }
                }
                case "M" -> {
                    System.out.println("Пожалуйста введите номер месяца:");
                    String month = scanner.nextLine();
                    monthRep = monthReport(getLines(month), month);
                }
                case "Y" -> yearRep = yearReport(getLines("0"));
                case "C" -> checkReports();
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
//        for (String curr :result){
//            System.out.println(curr);
//        }
        System.out.println("Отчет за месяц построен");
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
//        for (String curr :result){
//            System.out.println(curr);
//        }
        System.out.println("Годовой отчет построен");
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
    private static void checkReports (){
        ArrayList<String> monthRep = new ArrayList<>();
        ArrayList<String> yearRep = new ArrayList<>();
        double income = 0;
        double outcome = 0;
        yearRep = yearReport(getLines("0"));
        for (int i = 1; i < yearRep.size()/2+1; i++){
            monthRep = monthReport(getLines(String.valueOf(i)), String.valueOf(i));
            income=outcome=0;
            for (String currLine : monthRep) {
                String[] currVal = currLine.split(" ");
                if (currVal[1].equals("TRUE")) {
                    outcome += Double.parseDouble(currVal[2]) * Double.parseDouble(currVal[3]);
                } else if (currVal[1].equals("FALSE")) {
                    income += Double.parseDouble(currVal[2]) * Double.parseDouble(currVal[3]);
                }
            }
            if (!(outcome == Double.parseDouble(yearRep.get(2*(i-1)).split(",")[2]))){
                System.out.println("Траты за месяц " + i + " не сходятся");
            }
            if (!(income == Double.parseDouble(yearRep.get(2*(i-1)+1).split(",")[2]))) {
                System.out.println("Доход за месяц " + i + " не сходится");
            }
        }
        System.out.println("Проверка завершена");
    }
}
