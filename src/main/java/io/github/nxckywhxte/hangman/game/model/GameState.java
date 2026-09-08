package io.github.nxckywhxte.hangman.game.model;

public sealed interface GameState permits InProgress, Won, Lost {}
