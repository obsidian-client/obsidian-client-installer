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
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

public class GuiMainMenu extends Gui {

    @Override
    public Scene createGui() {

        System.out.println("[Obsidian Client - Installer] Creating Scene: GuiMainMenu");

        //The main container:
        HBox container = new HBox();

        //The left (empty, with dummy item) container:
        VBox leftContainer = new VBox();
        leftContainer.setPadding(new Insets(12));
        container.getChildren().add(leftContainer);

        //Dummy item for the left container:
        Pane leftDummy = new Pane();
        leftDummy.setMinWidth(40);
        leftDummy.setMinHeight(40);
        leftContainer.getChildren().add(leftDummy);

        //The middle (real main) container:
        VBox middleContainer = new VBox();
        middleContainer.setSpacing(50);
        middleContainer.setPadding(new Insets(50));
        container.getChildren().add(middleContainer);
        HBox.setHgrow(middleContainer, Priority.ALWAYS);

        //The Obsidian Client Logo:
        Image img = new Image(Installer.OBSIDIAN_CLIENT_LOGO, 385, 84, true, true);
        ImageView logo = new ImageView(img);
        HBox logoBox = new HBox();
        logoBox.setAlignment(Pos.CENTER);
        logoBox.getChildren().add(logo);
        middleContainer.getChildren().add(logoBox);

        //The welcome message in the center of the screen:
        Label label = new Label();
        label.setText("Thanks for downloading Obsidian Client!" + "\n" + "Click the button below to start the setup process!");
        label.setTextAlignment(TextAlignment.CENTER);
        HBox labelBox = new HBox();
        labelBox.setAlignment(Pos.CENTER);
        labelBox.getChildren().add(label);
        middleContainer.getChildren().add(labelBox);
        VBox.setVgrow(labelBox, Priority.ALWAYS);

        //The "Start Setup" button:
        Button btn = new Button();
        btn.setText("Start Setup");
        btn.setOnAction(event -> {
            Installer.getInstance().getPrimaryStage().setScene(Installer.getInstance().getGuiChoosePlatform());
        });
        HBox btnBox = new HBox();
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().add(btn);
        middleContainer.getChildren().add(btnBox);

        //The right container (info button):
        VBox rightContainer = new VBox();
        rightContainer.setPadding(new Insets(12));
        container.getChildren().add(rightContainer);

        //The "info" button:
        Image infoImg = new Image("InfoOutlined.png", 40, 40, true, true);
        ImageView infoBtn = new ImageView(infoImg);
        Pane infoBtnBox = new Pane();
        infoBtnBox.setCursor(Cursor.HAND);
        infoBtnBox.setOnMouseClicked(event -> {
            Installer.getInstance().getPrimaryStage().setScene(Installer.getInstance().getGuiAbout());
        });
        infoBtnBox.getChildren().add(infoBtn);
        rightContainer.getChildren().add(infoBtnBox);

        //Creating the scene object:
        Scene scene = new Scene(container, Installer.WINDOW_WIDTH, Installer.WINDOW_HEIGHT);
        scene.getStylesheets().add(Installer.OBSIDIAN_CLIENT_STYLESHEET);

        //Returning it:
        return scene;
    }

}
