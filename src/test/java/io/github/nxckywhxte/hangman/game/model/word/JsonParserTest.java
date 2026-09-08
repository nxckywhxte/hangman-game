package io.github.nxckywhxte.hangman.game.model.word;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonParserTest {
  private final JsonParser parser = new JsonParser();

  @Test
  @DisplayName("Should parse empty JSON object")
  void shouldParseEmptyJsonObject() {
    Map<String, List<String>> result = parser.parse("{}");
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Should parse object with single key-value pair")
  void shouldParseObjectWithSingleKeyValuePair() {
    Map<String, List<String>> result = parser.parse("{\"animals\": [\"CAT\"]}");

    assertThat(result).hasSize(1).containsKey("animals");
    assertThat(result.get("animals")).containsExactly("CAT");
  }

  @Test
  @DisplayName("Should parse multiple key-value pairs and array elements")
  void shouldParseMultipleKeyValuePairsAndArrayElements() {
    String json = "{ \"animals\": [\"CAT\", \"DOG\"], \"countries\": [\"FRANCE\"] }";
    Map<String, List<String>> result = parser.parse(json);

    assertThat(result).hasSize(2);
    assertThat(result.get("animals")).containsExactly("CAT", "DOG");
    assertThat(result.get("countries")).containsExactly("FRANCE");
  }

  @Test
  @DisplayName("Should throw exception for invalid JSON - missing closing brace")
  void shouldThrowExceptionForMissingClosingBrace() {
    assertThatThrownBy(() -> parser.parse("{\"animals\": [\"CAT\"]"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Expected '}'");
  }

  @Test
  @DisplayName("Should throw exception for invalid JSON - missing colon")
  void shouldThrowExceptionForMissingColon() {
    assertThatThrownBy(() -> parser.parse("{\"animals\" [\"CAT\"]}"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Expected ':'");
  }

  @Test
  @DisplayName("Should throw exception for invalid JSON - missing closing bracket")
  void shouldThrowExceptionForMissingClosingBracket() {
    assertThatThrownBy(() -> parser.parse("{\"animals\": [\"CAT\"}"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Expected ']'");
  }

  @Test
  @DisplayName("Should handle whitespace and newlines")
  void shouldHandleWhitespaceAndNewlines() {
    String json =
        """
            {
                "animals": [
                    "CAT",
                    "DOG"
                ],
                "countries": ["FRANCE"]
            }
            """;

    Map<String, List<String>> result = parser.parse(json);

    assertThat(result).hasSize(2);
    assertThat(result.get("animals")).containsExactly("CAT", "DOG");
    assertThat(result.get("countries")).containsExactly("FRANCE");
  }
}
