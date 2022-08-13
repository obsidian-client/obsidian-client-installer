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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class GuiInstallLauncher extends Gui {

    @Override
    public Scene createGui() {

        //The main container:
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);

        //The button description:
        Label label = new Label();
        label.setText("Please click the button below" + "\n" + "to start installing the Obsidian Launcher.");
        label.setTextAlignment(TextAlignment.CENTER);
        container.getChildren().add(label);

        //The "Extract Mod" button:
        Button button = new Button();
        button.setText("Start Installation");
        button.setOnAction(event -> {

        });
        container.getChildren().add(button);
        VBox.setMargin(button, new Insets(35, 0, 0, 0));

        //Creating the scene object:
        Scene scene = new Scene(container, Installer.WINDOW_WIDTH, Installer.WINDOW_HEIGHT);
        scene.getStylesheets().add(Installer.OBSIDIAN_CLIENT_STYLESHEET);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.BACK_SPACE) {
                Installer.getInstance().getPrimaryStage().setScene(new GuiChoosePlatform().createGui());
            }
        });

        //Returning it:
        return scene;
    }

}
