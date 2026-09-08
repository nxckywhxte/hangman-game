package io.github.nxckywhxte.hangman.game.model.word;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.nxckywhxte.hangman.game.exception.WordSourceException;
import io.github.nxckywhxte.hangman.game.model.Category;

class InMemoryWordSourceTest {
  private InMemoryWordSource wordSource;
  private final Category animals = new Category("animals");
  private final Category programming = new Category("programming");

  @BeforeEach
  void setUp() {
    wordSource =
        new InMemoryWordSource(
            Map.of(
                animals, List.of("CAT", "DOG"),
                programming, List.of("JAVA", "PYTHON")));
  }

  @Test
  @DisplayName("Should return all available categories")
  void shouldReturnAllAvailableCategories() throws WordSourceException {
    Set<Category> categories = wordSource.getCategories();
    assertThat(categories).containsExactlyInAnyOrder(animals, programming);
  }

  @Test
  @DisplayName("Should return words for existing category")
  void shouldReturnWordsForExistingCategory() throws WordSourceException {
    List<String> words = wordSource.getWords(animals);
    assertThat(words).containsExactly("CAT", "DOG");
  }

  @Test
  @DisplayName("Should throw exception for non-existent category")
  void shouldThrowExceptionForNonExistentCategory() {
    Category unknown = new Category("unknown");

    assertThatThrownBy(() -> wordSource.getWords(unknown))
        .isInstanceOf(WordSourceException.class)
        .hasMessage("Category not found: unknown");
  }
}
