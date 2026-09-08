package io.github.nxckywhxte.hangman.game.model.word;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.nxckywhxte.hangman.game.exception.WordSourceException;
import io.github.nxckywhxte.hangman.game.model.Category;

public class InMemoryWordSource implements WordSource {
  private final Map<Category, List<String>> wordsByCategory;

  public InMemoryWordSource(Map<Category, List<String>> wordsByCategory) {
    this.wordsByCategory =
        wordsByCategory.entrySet().stream()
            .collect(
                Collectors.toUnmodifiableMap(
                    Map.Entry::getKey, entry -> List.copyOf(entry.getValue())));
  }

  @Override
  public Set<Category> getCategories() throws WordSourceException {
    return wordsByCategory.keySet();
  }

  @Override
  public List<String> getWords(Category category) throws WordSourceException {
    if (!wordsByCategory.containsKey(category)) {
      throw new WordSourceException("Category not found: " + category.name());
    }
    return wordsByCategory.get(category);
  }
}
