package pl.crystalek.budgetapp.controller;

import javafx.scene.Parent;

public record ControllerData<T>(T classController, Parent pane) {
}
