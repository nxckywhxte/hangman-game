package io.github.nxckywhxte.hangman.game.model.word;

import java.util.List;
import java.util.Map;

public class JsonParser {
  private String json = "";
  private int pos = 0;
  private static final char EOF = '\0';

  public Map<String, List<String>> parse(String json) {
    this.json = json;
    this.pos = 0;
    skipWhitespace();
    Map<String, List<String>> result = parseObject();
    skipWhitespace();
    if (pos != json.length()) {
      throw new IllegalArgumentException("Unexpected characters after JSON object");
    }
    return result;
  }

  private void skipWhitespace() {
    while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
      pos++;
    }
  }

  private Map<String, List<String>> parseObject() {
    expect('{');
    skipWhitespace();
    Map<String, List<String>> result = new java.util.LinkedHashMap<>();
    if (peek() == '}') {
      pos++;
      return result;
    }
    String key = parseString();
    skipWhitespace();
    expect(':');
    skipWhitespace();
    List<String> value = parseArray();
    result.put(key, value);
    skipWhitespace();
    while (peek() == ',') {
      pos++;
      skipWhitespace();
      String nextKey = parseString();
      skipWhitespace();
      expect(':');
      skipWhitespace();
      List<String> nextValue = parseArray();
      result.put(nextKey, nextValue);
      skipWhitespace();
    }
    expect('}');
    return result;
  }

  private List<String> parseArray() {
    expect('[');
    skipWhitespace();
    List<String> result = new java.util.ArrayList<>();
    if (peek() == ']') {
      pos++;
      return result;
    }
    result.add(parseString());
    skipWhitespace();
    while (peek() == ',') {
      pos++;
      skipWhitespace();
      result.add(parseString());
      skipWhitespace();
    }
    expect(']');
    return result;
  }

  private String parseString() {
    expect('"');
    StringBuilder sb = new StringBuilder();
    while (pos < json.length() && json.charAt(pos) != '"') {
      sb.append(json.charAt(pos));
      pos++;
    }
    expect('"');
    return sb.toString();
  }

  private void expect(char expected) {
    char actual = peek();
    if (actual != expected) {
      if (actual == EOF) {
        throw new IllegalArgumentException(
            "Expected '" + expected + "' but reached end of input at position " + pos);
      }
      throw new IllegalArgumentException(
          "Expected '" + expected + "' but found '" + actual + "' at position " + pos);
    }
    pos++;
  }

  private char peek() {
    if (pos >= json.length()) {
      return EOF;
    }
    return json.charAt(pos);
  }
}
