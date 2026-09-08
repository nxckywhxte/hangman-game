package io.github.nxckywhxte.hangman.game.model;

import java.util.HashSet;
import java.util.Set;

public record HangmanGame(
    String secretWord, Set<Character> guessedLetters, int errors, int maxErrors, GameState state) {
  public static HangmanGame create(String word, Difficulty difficulty) {
    return new HangmanGame(word, Set.of(), 0, difficulty.maxErrors(), new InProgress());
  }

  public GuessResult guessLetter(char letter) {
    // 1. Нормализация регистра
    char upperLetter = Character.toUpperCase(letter);

    // 2. Проверяем, была ли буква уже угадана
    if (guessedLetters.contains(upperLetter)) {
      return new GuessResult(GuessStatus.ALREADY_GUESSED, this);
    }

    // 3. Добавляем букву в список угаданных
    Set<Character> newGuessedLetters = new HashSet<>(guessedLetters);
    newGuessedLetters.add(upperLetter);

    // 4. Проверяем есть ли буква в секретном слове
    boolean isCorrect = secretWord.contains(String.valueOf(upperLetter));

    // 5. Вычисляем новые ошибки
    int newErrors = isCorrect ? errors : errors + 1;

    // 6. Определяем статус
    GuessStatus status = isCorrect ? GuessStatus.CORRECT : GuessStatus.WRONG;

    // 7. Создаем новую игру
    HangmanGame newGame =
        new HangmanGame(secretWord, Set.copyOf(newGuessedLetters), newErrors, maxErrors, state);

    return new GuessResult(status, newGame);
  }
}
