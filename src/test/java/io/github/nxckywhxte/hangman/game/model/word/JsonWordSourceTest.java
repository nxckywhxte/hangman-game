package io.github.nxckywhxte.hangman.game.model.word;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.nxckywhxte.hangman.game.exception.WordSourceException;
import io.github.nxckywhxte.hangman.game.model.Category;

class JsonWordSourceTest {
  private JsonWordSource wordSource;

  @BeforeEach
  void setUp() throws WordSourceException {
    wordSource = JsonWordSource.fromResource("words.json");
  }

  @Test
  @DisplayName("Should load categories from JSON file")
  void shouldLoadCategoriesFromJsonFile() {
    Set<Category> categories = wordSource.getCategories();

    assertThat(categories).hasSize(3);
    assertThat(categories)
        .extracting(Category::name)
        .containsExactlyInAnyOrder("animals", "countries", "programming");
  }

  @Test
  @DisplayName("Should load words for specific category")
  void shouldLoadWordsForSpecificCategory() throws WordSourceException {
    Category animals = new Category("animals");
    List<String> words = wordSource.getWords(animals);

    assertThat(words).isNotEmpty().contains("CAT", "DOG", "ELEPHANT");
  }

  @Test
  @DisplayName("Should throw exception for non-existent category")
  void shouldThrowExceptionForNonExistentCategory() {
    Category unknown = new Category("unknown");

    assertThatThrownBy(() -> wordSource.getWords(unknown))
        .isInstanceOf(WordSourceException.class)
        .hasMessageContaining("Category not found: unknown");
  }

  @Test
  @DisplayName("Should throw exception when resource not found")
  void shouldThrowExceptionWhenResourceNotFound() {
    assertThatThrownBy(() -> JsonWordSource.fromResource("nonexistent.json")) // ← Фабричный метод
        .isInstanceOf(WordSourceException.class)
        .hasMessageContaining("Resource not found");
  }
}
