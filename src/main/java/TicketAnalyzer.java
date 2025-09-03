import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TicketAnalyzer {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java TicketAnalyzer <path_to_tickets.json>");
            System.exit(1);
        }

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(args[0])));
            JsonObject jsonObject = JsonParser.parseString(jsonContent).getAsJsonObject();
            JsonArray tickets = jsonObject.getAsJsonArray("tickets");

            // Задача 1: Минимальное время полета для каждого перевозчика
            Map<String, Integer> minFlightTimes = calculateMinFlightTimes(tickets);

            // Задача 2: Разница между средней ценой и медианой
            double priceDifference = calculatePriceMedianDifference(tickets);

            // Вывод результатов
            System.out.println("Минимальное время полёта между городами Владивосток и Тель-Авив" +
                    " для каждого авиаперевозчика:");
            for (Map.Entry<String, Integer> entry : minFlightTimes.entrySet()) {
                int hours = entry.getValue() / 60; // Преобразуем минуты в часы
                int minutes = entry.getValue() % 60; // Получаем остаток минут
                System.out.printf("%s: %d часов %d минут%n",
                        entry.getKey(), hours, minutes);
            }

            System.out.printf("%nРазница между средней ценой и медианой для полёта между городами " +
                    "Владивосток и Тель-Авив: %.2f рублей%n", priceDifference);

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    // Метод для расчёта минимального времени полёта
    private static Map<String, Integer> calculateMinFlightTimes(JsonArray tickets) {
        Map<String, Integer> minTimes = new HashMap<>();

        for (int i = 0; i < tickets.size(); i++) {
            JsonObject ticket = tickets.get(i).getAsJsonObject();

            // Извлекаем данные из JSON
            String origin = ticket.get("origin").getAsString();
            String destination = ticket.get("destination").getAsString();
            String carrier = ticket.get("carrier").getAsString();

            // Проверяем маршрут Владивосток -> Тель-Авив
            if ("VVO".equals(origin) && "TLV".equals(destination)) {
                String departureTime = ticket.get("departure_time").getAsString();
                String arrivalTime = ticket.get("arrival_time").getAsString();

                // Длительность полета в минутах
                int flightDuration = calculateFlightDuration(departureTime, arrivalTime);

                // Обновляем минимальное время для перевозчика
                minTimes.putIfAbsent(carrier, Integer.MAX_VALUE);
                if (flightDuration < minTimes.get(carrier)) {
                    minTimes.put(carrier, flightDuration);
                }
            }
        }

        return minTimes;
    }

    // Метод для расчёта продолжительности полёта
    private static int calculateFlightDuration(String departure, String arrival) {
        try {
            // Разбиваем время на часы и минуты
            String[] depParts = departure.split(":");
            String[] arrParts = arrival.split(":");

            // Преобразуем строки в числа
            int depHours = Integer.parseInt(depParts[0]);
            int depMinutes = Integer.parseInt(depParts[1]);
            int arrHours = Integer.parseInt(arrParts[0]);
            int arrMinutes = Integer.parseInt(arrParts[1]);

            // Преобразуем время в общее количество минут
            int totalDepMinutes = depHours * 60 + depMinutes;
            int totalArrMinutes = arrHours * 60 + arrMinutes;

            // Если время прибытия меньше времени вылета, добавляем 24 часа
            if (totalArrMinutes < totalDepMinutes) {
                totalArrMinutes += 24 * 60;
            }

            return totalArrMinutes - totalDepMinutes;

        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    // Метод для вычисления разницы между средней арифметической ценой и медианной ценой
    private static double calculatePriceMedianDifference(JsonArray tickets) {
        List<Integer> prices = new ArrayList<>();

        for (int i = 0; i < tickets.size(); i++) {
            JsonObject ticket = tickets.get(i).getAsJsonObject();

            String origin = ticket.get("origin").getAsString();
            String destination = ticket.get("destination").getAsString();

            // Собираем цены для маршрута Владивосток -> Тель-Авив
            if ("VVO".equals(origin) && "TLV".equals(destination)) {
                prices.add(ticket.get("price").getAsInt());
            }
        }

        if (prices.isEmpty()) { // Если нет подходящих рейсов
            return 0;
        }

        // Вычисляем среднюю цену
        double average = prices.stream().mapToInt(Integer::intValue).average().orElse(0);

        // Вычисляем медиану
        Collections.sort(prices); // Сортируем цены по возрастанию
        double median;
        int size = prices.size();

        if (size % 2 == 0) {
            median = (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
            median = prices.get(size / 2);
        }

        return Math.abs(average - median);
    }
}