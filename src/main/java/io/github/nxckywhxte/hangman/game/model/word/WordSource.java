package io.github.nxckywhxte.hangman.game.model.word;

import java.util.List;
import java.util.Set;

import io.github.nxckywhxte.hangman.game.exception.WordSourceException;
import io.github.nxckywhxte.hangman.game.model.Category;

public interface WordSource {
  Set<Category> getCategories() throws WordSourceException;

  List<String> getWords(Category category) throws WordSourceException;
}
