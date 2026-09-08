package io.github.nxckywhxte.hangman.game.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HangmanGameTest {
  private HangmanGame game;

  @BeforeEach
  void setUp() {
    game = createDefaultGame();
  }

  @Test
  @DisplayName("Initial errors count should be zero")
  void initialErrorsCountShouldBeZero() {
    assertThat(game.errors()).isZero();
  }

  @Test
  @DisplayName("Initial secret word should not be null")
  void initialSecretWordShouldNotBeNull() {
    assertThat(game.secretWord()).isNotNull();
  }

  @Test
  @DisplayName("Initial secret word should not be empty")
  void initialSecretWordShouldNotBeEmpty() {
    assertThat(game.secretWord()).isNotEmpty();
  }

  @Test
  @DisplayName("Initial guessed letters should be empty")
  void initialGuessedLettersShouldBeEmpty() {
    assertThat(game.guessedLetters()).isEmpty();
  }

  @Test
  @DisplayName("The initial state of the game must be an instance of the InProgress class.")
  void initialGameStateShouldBeAnInstanceOfTheInProgressClass() {
    assertThat(game.state()).isInstanceOf(InProgress.class);
  }

  @Test
  @DisplayName("Guessing correct letter should return CORRECT status")
  void guessCorrectLetterShouldReturnCorrectStatus() {
    GuessResult result = game.guessLetter('A');
    assertThat(result.status()).isEqualTo(GuessStatus.CORRECT);
    assertThat(result.newGame().guessedLetters()).contains('A');
    assertThat(result.newGame().errors()).isZero();
  }

  @Test
  @DisplayName("Guessing wrong letter should increase errors and return WRONG status")
  void guessWrongLetterShouldIncreaseErrorsAndReturnWrongStatus() {
    GuessResult result = game.guessLetter('Z');
    assertThat(result.status()).isEqualTo(GuessStatus.WRONG);
    assertThat(result.newGame().errors()).isEqualTo(1);
    assertThat(result.newGame().guessedLetters()).contains('Z');
  }

  @Test
  @DisplayName("Guessing already guessed letter should return ALREADY_GUESSED status")
  void guessAlreadyGuessedLetterShouldReturnAlreadyGuessedStatus() {
    game = game.guessLetter('A').newGame(); // Угадываем 'A'
    GuessResult result = game.guessLetter('A'); // Пытаемся снова
    assertThat(result.status()).isEqualTo(GuessStatus.ALREADY_GUESSED);
    assertThat(result.newGame().errors()).isZero(); // Ошибки не увеличились
  }

  @Test
  @DisplayName("Letter guessing should be case-insensitive")
  void letterGuessingShouldBeCaseInsensitive() {
    GuessResult result = game.guessLetter('j'); // Маленькая 'j'
    assertThat(result.status()).isEqualTo(GuessStatus.CORRECT);
    assertThat(result.newGame().guessedLetters()).contains('J'); // Должна быть 'J'
  }

  private HangmanGame createDefaultGame() {
    return HangmanGame.create("JAVA", Difficulty.MEDIUM);
  }
}
