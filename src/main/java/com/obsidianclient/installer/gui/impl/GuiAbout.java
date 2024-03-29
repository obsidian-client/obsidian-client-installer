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
import com.obsidianclient.installer.utils.IOUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.io.InputStream;

public class GuiAbout extends Gui {

    @Override
    public Scene createGui() {

        //The main container:
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50, 150, 50, 175));
        container.setSpacing(30);

        //The Obsidian Client Logo:
        Image img = new Image(Installer.OBSIDIAN_CLIENT_LOGO);
        ImageView logo = new ImageView(img);
        logo.setPreserveRatio(true);
        logo.setSmooth(true);
        logo.setFitWidth(385);
        logo.setFitHeight(84);
        container.getChildren().add(logo);

        //The installer version:
        Label label = new Label();
        label.setText("Version " + Installer.INSTALLER_VERSION);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setPadding(new Insets(-15));
        container.getChildren().add(label);

        //The main content in the center:
        TextArea txtArea = new TextArea();
        txtArea.setEditable(false);
        txtArea.setContextMenu(new ContextMenu());
        txtArea.setWrapText(true);
        txtArea.setPrefWidth(Integer.MAX_VALUE);
        txtArea.setPrefHeight(Integer.MAX_VALUE);
        txtArea.setText(this.getAboutInformation());
        container.getChildren().add(txtArea);

        //The "Go Back" button:
        Button btn = new Button();
        btn.setText("Go Back");
        btn.setOnAction(event -> {
            Installer.getInstance().getPrimaryStage().setScene(new GuiMainMenu().createGui());
        });
        container.getChildren().add(btn);

        //Creating the scene object:
        Scene scene = new Scene(container, Installer.WINDOW_WIDTH, Installer.WINDOW_HEIGHT);
        scene.getStylesheets().add(Installer.OBSIDIAN_CLIENT_STYLESHEET);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.BACK_SPACE) {
                Installer.getInstance().getPrimaryStage().setScene(new GuiMainMenu().createGui());
            }
        });

        //Returning it:
        return scene;
    }

    private String getAboutInformation() {

        InputStream copyrightStream = this.getClass().getResourceAsStream("/COPYRIGHT");
        InputStream licenseStream = this.getClass().getResourceAsStream("/LICENSE");
        InputStream thirdPartyStream = this.getClass().getResourceAsStream("/THIRD-PARTY");
        if (copyrightStream == null || licenseStream == null || thirdPartyStream == null) {
            System.err.println("[Obsidian Client - Installer] Can't read internal files 'COPYRIGHT', 'LICENSE', 'THIRD-PARTY': This installer is broken!");
            return "";
        }

        try {

            return IOUtils.convertStreamToString(copyrightStream) +
                    "\n\n" +
                    IOUtils.convertStreamToString(licenseStream) +
                    "\n\n" +
                    IOUtils.convertStreamToString(thirdPartyStream);

        } catch (IOException e) {
            System.err.println("[Obsidian Client - Installer] Can't read internal files 'COPYRIGHT', 'LICENSE', 'THIRD-PARTY': This installer is broken!");
            e.printStackTrace();
            return "";
        }

    }

}
