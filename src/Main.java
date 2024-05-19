import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command = "Start";
        ArrayList<String> yearRep = new ArrayList<>();//Список для хранения отчетов
        ArrayList<String> monthRep = new ArrayList<>();//Список для хранения отчетов

        while (!command.equals("E")) {
            System.out.println("""
                    \nВведите команду:
                    M-Месячный отчет
                    Y-Годовой отчет
                    SM-Вывести месячный отчет
                    SY-Вывести годовой отчет
                    AM-Вывести анализ по всем месяцам
                    AY-Вывести анализ по всем годам
                    С-Проверить отчет
                    E-Выход
                    """);
            command = scanner.nextLine();
            switch (command) {
                case "SM" -> showFile(monthRep);
                case "AM" -> {
                    System.out.println("Пожалуйста введите год:");
                    String year = scanner.nextLine();
                    allMonthReports(Integer.parseInt(year));
                }
                case "AY" -> {
                    System.out.println("Пожалуйста введите первый подотчетный год:");
                    int yearFrom = Integer.parseInt(scanner.nextLine());
                    System.out.println("Пожалуйста введите последний подотчетный год:");
                    int yearTo = Integer.parseInt(scanner.nextLine());
                    allYearReports(yearFrom, yearTo);
                }
                case "SY" -> showFile(yearRep);
                case "M" -> {
                    System.out.println("Пожалуйста введите год:");
                    String year = scanner.nextLine();
                    System.out.println("Пожалуйста введите номер месяца:");
                    String month = scanner.nextLine();
                    monthRep = monthReport(getLines(month, Integer.parseInt(year)));
                    if (!monthRep.isEmpty()) {
                        System.out.println("Отчет за месяц построен");
                    } else System.out.println("За указанный период нет данных. Попробуйте снова");
                }
                case "Y" -> {
                    System.out.println("Пожалуйста введите год:");
                    String year = scanner.nextLine();
                    yearRep = yearReport(getLines("0", Integer.parseInt(year)));
                    if (!yearRep.isEmpty()) {
                        System.out.println("Годовой отчет построен");
                    } else System.out.println("За указанный период нет данных. Попробуйте снова");
                }
                case "C" -> {
                    System.out.println("Пожалуйста введите год:");
                    String year = scanner.nextLine();
                    checkReports(Integer.parseInt(year));
                }
                case "E" -> System.out.println("До свидания");
                default -> System.out.println("Неверная команда");
            }
        }
    }

    private static String readFiles(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            return null;
        }
    }

    private static ArrayList<String> getLines(String month, int year) {
        ArrayList<String> result = new ArrayList<>();
        if (!month.equals("0")) {
            String file = readFiles("C:\\Users\\Admin\\IdeaProjects\\sprint1\\files\\m." + year + monthZeroAdder(Integer.parseInt(month)) + ".csv");
            if (file != null) {
                String[] lines = file.split("\\n");
                result.addAll(Arrays.asList(lines).subList(1, lines.length));
            }
        } else {
            for (int i = 0; i < 13; i++) {
                String file = readFiles("C:\\Users\\Admin\\IdeaProjects\\sprint1\\files\\m." + year + monthZeroAdder(i) + ".csv");
                if (file != null) {
                    String[] lines = file.split("\\n");
                    for (int j = 0; j < lines.length; j++) {
                        lines[j] = i + "," + lines[j];
                    }
                    result.addAll(Arrays.asList(lines).subList(1, lines.length));
                }
            }
        }
        return result;
    }

    private static void showFile(ArrayList<String> file) {
        if (!file.isEmpty()) {
            for (String value : file) {
                System.out.println(value);
            }
        } else {
            System.out.println("Пожалуйста, для начала простройте отчет");
        }
    }

    private static ArrayList<String> monthReport(ArrayList<String> fileLines) {
        ArrayList<String> result = new ArrayList<>();//Результирующий список с отчетом
        String writeToReport;//Строка для обработки записи в отчет
        for (String fileLine : fileLines) {//Запись в список уникальных пар Объект + Тип транзакции
            String[] readLine = fileLine.split(",");
            writeToReport = readLine[0] + "," + readLine[1];
            if (!result.contains(writeToReport)) {
                result.add(writeToReport);
            }
        }
        for (String currResultLine : result) {//Проходимся по уникальным парам и плюсуем в них соответствующие транзакции
            double sum = 0;
            double cost = 0;
            for (String fileLine : fileLines) {
                String[] readLine = fileLine.split(",");
                if (currResultLine.equals(readLine[0] + "," + readLine[1])) {
                    sum += Double.parseDouble(readLine[2]);
                    cost = Double.parseDouble(readLine[3]);
                }
            }
            result.set(result.indexOf(currResultLine), currResultLine + "," + sum + "," + cost);
        }
        return result;
    }

    private static ArrayList<String> yearReport(ArrayList<String> fileLines) {
        ArrayList<String> result = new ArrayList<>();//Результирующий список с отчетом
        String writeToReport;//Строка для обработки записи в отчет
        for (String fileLine : fileLines) {//Запись в список уникальных пар месяц + Тип транзакции
            String[] readLine = fileLine.split(",");
            writeToReport = readLine[0] + "," + readLine[2];
            if (!result.contains(writeToReport)) {
                result.add(writeToReport);
            }
        }
        for (String currResultLine : result) {//Проходимся по уникальным парам и плюсуем в них соответствующие транзакции
            double sum = 0;
            for (String fileLine : fileLines) {
                String[] readLine = fileLine.split(",");
                if (currResultLine.equals(readLine[0] + "," + readLine[2])) {
                    sum += Double.parseDouble(readLine[3]) * Double.parseDouble(readLine[4]);
                }
            }
            result.set(result.indexOf(currResultLine), currResultLine + "," + sum);
        }
        return result;
    }

    private static void checkReports(int year) {
        ArrayList<String> monthRep;
        ArrayList<String> yearRep;
        double income;
        double outcome;
        yearRep = yearReport(getLines("0", year));
        for (int i = 1; i < yearRep.size() / 2 + 1; i++) {
            monthRep = monthReport(getLines(yearRep.get((i * 2) - 1).split(",")[0], year));
            income = outcome = 0;
            for (String currLine : monthRep) {
                String[] currVal = currLine.split(",");
                if (currVal[1].equals("TRUE")) {
                    outcome += Double.parseDouble(currVal[2]) * Double.parseDouble(currVal[3]);
                } else if (currVal[1].equals("FALSE")) {
                    income += Double.parseDouble(currVal[2]) * Double.parseDouble(currVal[3]);
                }
            }
            if (!(outcome == Double.parseDouble(yearRep.get(2 * (i - 1)).split(",")[2]))) {
                System.out.println("Траты за месяц " + i + " не сходятся");
            }
            if (!(income == Double.parseDouble(yearRep.get(2 * (i - 1) + 1).split(",")[2]))) {
                System.out.println("Доход за месяц " + i + " не сходится");
            }
        }
        System.out.println("Проверка завершена");
    }

    private static void allMonthReports(int year) {
        ArrayList<String> currMonthLines;
        double maxIncome = 0;
        double maxOutcome = 0;
        String maxProfitItemName = "";
        String maxWasteItemName = "";
        System.out.println("Пожалуйста введите год:");
        for (int i = 1; i < 13; i++) {
            currMonthLines = monthReport(getLines(String.valueOf(i), year));
            for (String fileLine : currMonthLines) {
                String[] currVal = fileLine.split(",");
                if (currVal[1].equals("TRUE") && Double.parseDouble(currVal[2]) * Double.parseDouble(currVal[3]) > maxOutcome) {
                    maxOutcome = Double.parseDouble(currVal[2]) * Double.parseDouble(currVal[3]);
                    maxWasteItemName = currVal[0];
                }
                if (currVal[1].equals("FALSE") && Double.parseDouble(currVal[2]) * Double.parseDouble(currVal[3]) > maxIncome) {
                    maxIncome = Double.parseDouble(currVal[2]) * Double.parseDouble(currVal[3]);
                    maxProfitItemName = currVal[0];
                }
            }
            if (maxOutcome != 0) {
                System.out.println("За месяц " + monthNumberToName(i) + " наибольшая трата была на товар " + maxWasteItemName + " и составила " + maxOutcome);
            } else System.out.println("За месяц " + monthNumberToName(i) + " трат не было");
            if (maxIncome != 0) {
                System.out.println("За месяц " + monthNumberToName(i) + " наибольшую прибыль принес товар " + maxProfitItemName + " равную " + maxIncome);
            } else System.out.println("За месяц " + monthNumberToName(i) + " прибыли не было");
            maxOutcome = maxIncome = 0;
        }
    }

    private static void allYearReports(int yearFrom, int yearTo) {
        ArrayList<String> currYearRep;
        double monthProfit;
        double incomeSum;
        double outcomeSum;
        int incomeCount;
        int outcomeCount;
        int currMonth;
        for (int i = yearFrom; i <= yearTo; i++) {
            monthProfit = incomeSum = outcomeSum = incomeCount = outcomeCount = 0;
            currMonth = 1;
            currYearRep = yearReport(getLines("0", i));
            for (int j = 0; j < currYearRep.size(); j++) {
                String[] readLine = currYearRep.get(j).split(",");
                if (readLine[0].equals(String.valueOf(currMonth)) && readLine[1].equals("TRUE")) {
                    monthProfit -= Double.parseDouble(readLine[2]);
                    outcomeSum += Double.parseDouble(readLine[2]);
                    outcomeCount++;
                } else if (readLine[0].equals(String.valueOf(currMonth)) && readLine[1].equals("FALSE")) {
                    monthProfit += Double.parseDouble(readLine[2]);
                    incomeSum += Double.parseDouble(readLine[2]);
                    incomeCount++;
                } else {
                    System.out.println("Год " + i + ": Прибыль за месяц " + monthNumberToName(currMonth) + " составила " + monthProfit);
                    monthProfit = 0;
                    currMonth = Integer.parseInt(readLine[0]);
                    j--;
                }
            }
            if (monthProfit == 0) {
                System.out.println("Год " + i + ": За данный год не было операций");
            } else {
                System.out.println("Год " + i + ": Прибыль за месяц " + monthNumberToName(currMonth) + " составила " + monthProfit);
                System.out.println("Год " + i + ": Средний расход составил " + Math.round(outcomeSum / outcomeCount));
                System.out.println("Год " + i + ": Средний доход составил " + Math.round(incomeSum / incomeCount));
            }
        }
    }

    private static String monthNumberToName(int monthNumber) {
        String monthName = "";
        switch (monthNumber) {
            case 1 -> monthName = "Январь";
            case 2 -> monthName = "Февраль";
            case 3 -> monthName = "Март";
            case 4 -> monthName = "Апрель";
            case 5 -> monthName = "Май";
            case 6 -> monthName = "Июнь";
            case 7 -> monthName = "Июль";
            case 8 -> monthName = "Август";
            case 9 -> monthName = "Сентябрь";
            case 10 -> monthName = "Октябрь";
            case 11 -> monthName = "Ноябрь";
            case 12 -> monthName = "Декабрь";
        }
        return monthName;
    }

    private static String monthZeroAdder(int monthNumber) {
        if (monthNumber < 10) {
            return "0" + monthNumber;
        } else return String.valueOf(monthNumber);
    }
}