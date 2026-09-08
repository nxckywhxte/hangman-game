package io.github.nxckywhxte.hangman.game.model.guess;

import io.github.nxckywhxte.hangman.game.model.HangmanGame;

public record GuessResult(GuessStatus status, HangmanGame newGame) {}
