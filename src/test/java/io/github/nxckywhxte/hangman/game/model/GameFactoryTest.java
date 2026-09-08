package io.github.nxckywhxte.hangman.game.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.nxckywhxte.hangman.game.exception.WordSourceException;
import io.github.nxckywhxte.hangman.game.model.word.InMemoryWordSource;
import io.github.nxckywhxte.hangman.game.model.word.WordSource;

class GameFactoryTest {
  private final Category animals = new Category("animals");
  private final Category countries = new Category("countries");

  @Test
  @DisplayName("Should create game with word from specified category")
  void shouldCreateGameWithWordFromSpecifiedCategory() throws WordSourceException {
    WordSource wordSource =
        new InMemoryWordSource(Map.of(animals, List.of("CAT", "DOG", "ELEPHANT")));
    GameFactory factory = new GameFactory(wordSource, new Random(42));
    HangmanGame game = factory.createGame(animals, Difficulty.MEDIUM);
    assertThat(game.secretWord()).isIn("CAT", "DOG", "ELEPHANT");
    assertThat(game.errors()).isZero();
    assertThat(game.guessedLetters()).isEmpty();
    assertThat(game.maxErrors()).isEqualTo(Difficulty.MEDIUM.maxErrors());
    assertThat(game.state()).isInstanceOf(InProgress.class);
  }

  @Test
  @DisplayName("Should select different words on multiple calls with different seeds")
  void shouldSelectDifferentWordsOnMultipleCalls() throws WordSourceException {
    WordSource wordSource =
        new InMemoryWordSource(Map.of(animals, List.of("CAT", "DOG", "ELEPHANT", "LION", "TIGER")));
    WordSource countryWordSource =
        new InMemoryWordSource(Map.of(countries, List.of("FRANCE", "GERMANY", "JAPAN", "BRAZIL")));
    HangmanGame game1 =
        new GameFactory(wordSource, new Random(1)).createGame(animals, Difficulty.MEDIUM);
    HangmanGame game2 =
        new GameFactory(countryWordSource, new Random(100))
            .createGame(countries, Difficulty.MEDIUM);
    HangmanGame game3 =
        new GameFactory(wordSource, new Random(1000)).createGame(animals, Difficulty.MEDIUM);
    assertThat(game1.secretWord()).isIn("CAT", "DOG", "ELEPHANT", "LION", "TIGER");
    assertThat(game2.secretWord()).isIn("FRANCE", "GERMANY", "JAPAN", "BRAZIL");
    assertThat(game3.secretWord()).isIn("CAT", "DOG", "ELEPHANT", "LION", "TIGER");
  }

  @Test
  @DisplayName("Should throw exception for non-existent category")
  void shouldThrowExceptionForNonExistentCategory() {
    WordSource wordSource = new InMemoryWordSource(Map.of(animals, List.of("CAT")));
    GameFactory factory = new GameFactory(wordSource);
    Category unknown = new Category("unknown");

    assertThatThrownBy(() -> factory.createGame(unknown, Difficulty.MEDIUM))
        .isInstanceOf(WordSourceException.class)
        .hasMessageContaining("Category not found");
  }

  @Test
  @DisplayName("Should throw exception for empty category")
  void shouldThrowExceptionForEmptyCategory() {
    WordSource wordSource = new InMemoryWordSource(Map.of(animals, List.of()));
    GameFactory factory = new GameFactory(wordSource);

    assertThatThrownBy(() -> factory.createGame(animals, Difficulty.MEDIUM))
        .isInstanceOf(WordSourceException.class)
        .hasMessageContaining("No words found");
  }
}
