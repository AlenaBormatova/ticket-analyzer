# Ticket Analyzer

Программа для анализа авиабилетов из JSON файла.

## Функциональность

- Расчет минимального времени полета между Владивостоком и Тель-Авивом для каждого перевозчика
- Расчет разницы между средней ценой и медианой

## Запуск из командной строки Linux

### Требования
- Java 11 или выше
- Maven 3.6+ (для сборки)
- Файл tickets.json

### 1. Клонирование и подготовка проекта

```bash
# Клонируйте репозиторий
git clone https://github.com/AlenaBormatova/ticket-analyzer.git
cd ticket-analyzer

# Убедитесь, что файл tickets.json находится в корне проекта
ls -la tickets.json
```

### 2. Сборка проекта (создание JAR файла)

```bash
# Сборка с Maven
mvn clean package

# После успешной сборки JAR файл будет в target/
ls -la target/ticket-analyzer-*.jar
```

### 3. Запуск программы

#### Основной способ (через JAR):
```bash
# Базовый вызов (файл tickets.json в текущей директории)
java -jar target/ticket-analyzer-1.0-SNAPSHOT.jar tickets.json

# С указанием полного пути к файлу
java -jar target/ticket-analyzer-1.0-SNAPSHOT.jar /полный/путь/к/tickets.json

# С относительным путём
java -jar target/ticket-analyzer-1.0-SNAPSHOT.jar ../data/tickets.json
```

#### Альтернативные способы запуска:
```bash
# Запуск напрямую через Maven
mvn exec:java -Dexec.mainClass="TicketAnalyzer" -Dexec.args="tickets.json"

# Запуск скомпилированных классов (без JAR)
java -cp target/classes:target/dependency/* TicketAnalyzer tickets.json
```

### 4. Пример успешного выполнения

```bash
$ java -jar target/ticket-analyzer-1.0-SNAPSHOT.jar tickets.json
Минимальное время полёта между городами Владивосток и Тель-Авив для каждого авиаперевозчика:
SU: 6 часов 0 минут
S7: 6 часов 30 минут
TK: 5 часов 50 минут
BA: 8 часов 5 минут

Разница между средней ценой и медианой для полёта между городами Владивосток и Тель-Авив: 460,00 рублей
```

## Структура проекта

```
java-ticket-analyzer/
├── src/main/java/TicketAnalyzer.java  # Основной класс
├── pom.xml                            # Maven конфигурация
├── tickets.json                       # Пример данных
├── .gitignore                         # Игнорируемые файлы
└── README.md                          # Документация
```