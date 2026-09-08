package io.github.nxckywhxte.hangman.game.model.word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.nxckywhxte.hangman.game.exception.WordSourceException;
import io.github.nxckywhxte.hangman.game.model.Category;

public class JsonWordSource implements WordSource {
  private final Map<Category, List<String>> wordsByCategory;

  private JsonWordSource(Map<Category, List<String>> wordsByCategory) {
    this.wordsByCategory = wordsByCategory;
  }

  public static JsonWordSource fromResource(String resourcePath) throws WordSourceException {
    String jsonContent = loadResource(resourcePath);
    JsonParser parser = new JsonParser();
    Map<String, List<String>> parsedData;

    try {
      parsedData = parser.parse(jsonContent);
    } catch (IllegalArgumentException e) {
      throw new WordSourceException("Failed to parse JSON: " + e.getMessage(), e);
    }

    Map<Category, List<String>> categoryMap = convertToCategoryMap(parsedData);
    return new JsonWordSource(categoryMap);
  }

  private static String loadResource(String resourcePath) throws WordSourceException {
    try (InputStream is = JsonWordSource.class.getClassLoader().getResourceAsStream(resourcePath)) {
      if (is == null) {
        throw new WordSourceException("Resource not found: " + resourcePath);
      }

      try (BufferedReader reader =
          new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
        return reader.lines().collect(Collectors.joining("\n"));
      }
    } catch (IOException e) {
      throw new WordSourceException("Failed to read resource: " + resourcePath, e);
    }
  }

  private static Map<Category, List<String>> convertToCategoryMap(
      Map<String, List<String>> parsedData) {
    return parsedData.entrySet().stream()
        .collect(
            Collectors.toUnmodifiableMap(
                entry -> new Category(entry.getKey()), entry -> List.copyOf(entry.getValue())));
  }

  @Override
  public Set<Category> getCategories() {
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
