package io.github.nxckywhxte.hangman.game.exception;

import java.io.Serial;

public class WordSourceException extends Exception {
  @Serial private static final long serialVersionUID = 1L;

  public WordSourceException(String message) {
    super(message);
  }

  public WordSourceException(String message, Throwable cause) {
    super(message, cause);
  }
}
