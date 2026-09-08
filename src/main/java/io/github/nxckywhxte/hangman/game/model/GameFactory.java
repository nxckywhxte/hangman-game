package io.github.nxckywhxte.hangman.game.model;

import java.util.List;
import java.util.Random;

import io.github.nxckywhxte.hangman.game.exception.WordSourceException;
import io.github.nxckywhxte.hangman.game.model.word.WordSource;

public class GameFactory {
  private final WordSource wordSource;
  private final Random random;

  public GameFactory(WordSource wordSource) {
    this(wordSource, new Random());
  }

  GameFactory(WordSource wordSource, Random random) {
    this.wordSource = wordSource;
    this.random = random;
  }

  public HangmanGame createGame(Category category, Difficulty difficulty)
      throws WordSourceException {
    List<String> words = wordSource.getWords(category);
    if (words.isEmpty()) {
      throw new WordSourceException("No words found for category: " + category.name());
    }

    String randomWord = words.get(random.nextInt(words.size()));
    return HangmanGame.create(randomWord, difficulty);
  }
}
