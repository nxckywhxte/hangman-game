package io.github.nxckywhxte.hangman.game.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.nxckywhxte.hangman.game.model.guess.GuessResult;
import io.github.nxckywhxte.hangman.game.model.guess.GuessStatus;
import io.github.nxckywhxte.hangman.game.model.state.GameState;
import io.github.nxckywhxte.hangman.game.model.state.InProgress;
import io.github.nxckywhxte.hangman.game.model.state.Lost;
import io.github.nxckywhxte.hangman.game.model.state.Won;

public record HangmanGame(
    String secretWord, Set<Character> guessedLetters, int errors, int maxErrors, GameState state) {
  public HangmanGame {
    if (secretWord == null || secretWord.isEmpty()) {
      throw new IllegalArgumentException("Secret word cannot be empty");
    }
    if (secretWord.contains(" ")) {
      throw new IllegalArgumentException("Secret word cannot contain spaces");
    }
    guessedLetters = Set.copyOf(guessedLetters);
  }

  public static HangmanGame create(String word, Difficulty difficulty) {
    return new HangmanGame(word, Set.of(), 0, difficulty.maxErrors(), new InProgress());
  }

  public GuessResult guessLetter(char letter) {
    // 1. Проверка: игра уже закончена?
    if (!(state instanceof InProgress)) {
      return new GuessResult(GuessStatus.GAME_ALREADY_FINISHED, this);
    }

    // 2. Нормализация регистра
    char upperLetter = Character.toUpperCase(letter);

    // 3. Проверяем, была ли буква уже угадана
    if (guessedLetters.contains(upperLetter)) {
      return new GuessResult(GuessStatus.ALREADY_GUESSED, this);
    }

    // 4. Добавляем букву в список угаданных
    Set<Character> newGuessedLetters = new HashSet<>(guessedLetters);
    newGuessedLetters.add(upperLetter);

    // 5. Проверяем есть ли буква в секретном слове
    boolean isCorrect = secretWord.contains(String.valueOf(upperLetter));

    // 6. Вычисляем новые ошибки
    int newErrors = isCorrect ? errors : errors + 1;

    // 7. Определяем статус
    GuessStatus status = isCorrect ? GuessStatus.CORRECT : GuessStatus.WRONG;

    // 8. Проверка поражения и победы
    GameState newState;
    if (newErrors >= maxErrors) {
      newState = new Lost();
    } else if (secretWord.chars().allMatch(c -> newGuessedLetters.contains((char) c))) {
      newState = new Won();
    } else {
      newState = state;
    }

    // 9. Создаем новую игру
    HangmanGame newGame =
        new HangmanGame(secretWord, Set.copyOf(newGuessedLetters), newErrors, maxErrors, newState);

    return new GuessResult(status, newGame);
  }

  public String getMaskedWord() {
    return secretWord
        .chars()
        .mapToObj(c -> (char) c)
        .map(c -> guessedLetters.contains(c) ? c.toString() : "_")
        .collect(Collectors.joining(" "));
  }
}
