/**
 * This file is part of Obsidian Client Installer,
 * in the following referred to as "this program".
 * Copyright (C) 2022  Alexander Richter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.obsidianclient.installer.gui.impl;

import com.obsidianclient.installer.Installer;
import com.obsidianclient.installer.gui.Gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GuiChoosePlatform extends Gui {

    @Override
    public Scene createGui() {

        System.out.println("[Obsidian Client - Installer] Creating Scene: GuiChoosePlatform");

        //The main container:
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);

        //The title:
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(50));
        Label title = new Label();
        title.setText("Install Obsidian Client ...");
        title.setStyle("-fx-font-size: 18px");
        titleBox.getChildren().add(title);
        container.getChildren().add(titleBox);

        //The first option: Install as a forge mod
        HBox forgeBox = new HBox();
        forgeBox.setAlignment(Pos.CENTER);
        forgeBox.setPadding(new Insets(25));
        Label forgeLabel = new Label();
        forgeLabel.setText("... as a Forge Mod.");
        Button forgeButton = new Button();
        forgeButton.setText(">>");
        forgeButton.setStyle("-fx-padding: 12px");
        forgeButton.setOnAction(event -> {
            GuiChooseVersion.nextScene = Installer.getInstance().getGuiInstallForge();
            Installer.getInstance().getPrimaryStage().setScene(Installer.getInstance().getGuiChooseVersion());
        });
        forgeBox.getChildren().add(forgeLabel);
        forgeBox.getChildren().add(forgeButton);
        HBox.setMargin(forgeButton, new Insets(0, 0, 0, 25));
        container.getChildren().add(forgeBox);

        //The second option: Install for the standard Minecraft Launcher
        HBox vanillaBox = new HBox();
        vanillaBox.setAlignment(Pos.CENTER);
        vanillaBox.setPadding(new Insets(25));
        Label vanillaLabel = new Label();
        vanillaLabel.setText("... for the standard Minecraft Launcher.");
        Button vanillaButton = new Button();
        vanillaButton.setText(">>");
        vanillaButton.setStyle("-fx-padding: 12px");
        vanillaButton.setOnAction(event -> {
            GuiChooseVersion.nextScene = Installer.getInstance().getGuiInstallVanilla();
            Installer.getInstance().getPrimaryStage().setScene(Installer.getInstance().getGuiChooseVersion());
        });
        vanillaBox.getChildren().add(vanillaLabel);
        vanillaBox.getChildren().add(vanillaButton);
        HBox.setMargin(vanillaButton, new Insets(0, 0, 0, 25));
        container.getChildren().add(vanillaBox);

        //The third option: Install using the Obsidian Launcher
        HBox launcherBox = new HBox();
        launcherBox.setAlignment(Pos.CENTER);
        launcherBox.setPadding(new Insets(25));
        Label launcherLabel = new Label();
        launcherLabel.setText("... using the Obsidian Launcher.");
        Button launcherButton = new Button();
        launcherButton.setText(">>");
        launcherButton.setStyle("-fx-padding: 12px");
        launcherButton.setOnAction(event -> {
            Installer.getInstance().getPrimaryStage().setScene(Installer.getInstance().getGuiInstallLauncher());
        });
        launcherButton.setDisable(true); //Temporally disabling the button!
        launcherBox.getChildren().add(launcherLabel);
        launcherBox.getChildren().add(launcherButton);
        HBox.setMargin(launcherButton, new Insets(0, 0, 0, 25));
        container.getChildren().add(launcherBox);

        //Creating the scene object:
        Scene scene = new Scene(container, Installer.WINDOW_WIDTH, Installer.WINDOW_HEIGHT);
        scene.getStylesheets().add(Installer.OBSIDIAN_CLIENT_STYLESHEET);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.BACK_SPACE) {
                Installer.getInstance().getPrimaryStage().setScene(Installer.getInstance().getGuiMainMenu());
            }
        });

        //Returning it:
        return scene;
    }

}
