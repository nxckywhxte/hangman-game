# Hangman Game

Классическая игра "Виселица" с GUI на Java Swing.

## Требования

- Java 25+
- Maven 3.9+

## Сборка

# Компиляция и тесты
```bash
mvn clean verify
```

# Запуск приложения
```bash
mvn exec:java
```

# Сборка portable .exe (Windows)
```bash
mvn clean package -Pportable
```

## Разработка

Проект использует:
- Spotless для автоматического форматирования
- Checkstyle для проверки стиля
- SpotBugs и PMD для статического анализа
- JUnit 5 + AssertJ для тестирования

## Установка хуков для проверки коммитов

```bash
./git-hooks/setup.sh
```
