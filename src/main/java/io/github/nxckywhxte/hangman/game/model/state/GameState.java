package io.github.nxckywhxte.hangman.game.model.state;

public sealed interface GameState permits InProgress, Won, Lost {}
