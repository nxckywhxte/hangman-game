package io.github.nxckywhxte.hangman.game.model;

public record Difficulty(DifficultyName name, int maxErrors, String description) {
  public static final Difficulty EASY =
      new Difficulty(DifficultyName.EASY, 10, "10 errors allowed");
  public static final Difficulty MEDIUM =
      new Difficulty(DifficultyName.MEDIUM, 7, "7 errors allowed");
  public static final Difficulty HARD = new Difficulty(DifficultyName.HARD, 5, "5 errors allowed");
}
