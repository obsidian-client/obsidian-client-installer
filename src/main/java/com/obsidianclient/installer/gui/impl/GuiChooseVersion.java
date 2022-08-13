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
import com.obsidianclient.installer.gui.GuiNext;
import com.obsidianclient.installer.utils.DialogUtils;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.List;

public class GuiChooseVersion extends GuiNext {

    @Override
    public Scene createGui(Gui nextGui) {

        //The main container:
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);

        //The button description:
        Label label = new Label();
        label.setText("Please select the Minecraft version and " + "\n" + "Obsidian Client version to install.");
        label.setTextAlignment(TextAlignment.CENTER);
        container.getChildren().add(label);

        ComboBox<String> selectMcVersion = new ComboBox<>();
        ComboBox<String> selectOcVersion = new ComboBox<>();

        //The Minecraft version ComboBox:
        selectMcVersion.setPromptText("Minecraft Version");
        //Download list of Obsidian Client versions:
        SortedList<String> sortedMcVersionsList;
        try {
            List<String> mcVersionsList = Installer.getInstance().getEngine().getAllMinecraftVersions();
            sortedMcVersionsList = FXCollections.observableList(mcVersionsList).sorted();
        } catch (IOException e) {
            System.err.println("[Obsidian Client - Installer] Can't download list of Obsidian Client versions!");
            DialogUtils.showFailedDownloadMetadataDialog("https://archive.obsidian-client.com/com/obsidianclient/ObsidianClient/maven-metadata.xml");
            e.printStackTrace();
            sortedMcVersionsList = null;
        }
        selectMcVersion.setItems(sortedMcVersionsList);
        selectMcVersion.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            //Download list of Obsidian Client versions:
            SortedList<String> newSortedOcVersionsList;
            try {
                List<String> newOcVersionsList = Installer.getInstance().getEngine().getAllObsidianClientVersions(newValue);
                newSortedOcVersionsList = FXCollections.observableList(newOcVersionsList).sorted();
            } catch (IOException e) {
                System.err.println("[Obsidian Client - Installer] Can't download list of Obsidian Client versions!");
                DialogUtils.showFailedDownloadMetadataDialog("https://archive.obsidian-client.com/com/obsidianclient/ObsidianClient/maven-metadata.xml");
                e.printStackTrace();
                newSortedOcVersionsList = null;
            }
            selectOcVersion.setItems(newSortedOcVersionsList);

        });
        container.getChildren().add(selectMcVersion);
        VBox.setMargin(selectMcVersion, new Insets(35, 0, 0, 0));

        //The Obsidian Client version ComboBox:
        selectOcVersion.setPromptText("Obsidian Client Version");
        container.getChildren().add(selectOcVersion);
        VBox.setMargin(selectOcVersion, new Insets(17.5, 0, 0, 0));

        //The "Select" button:
        Button button = new Button();
        button.setText("Select");
        button.setOnAction(event -> {

            if (selectMcVersion.getValue() != null) {
                if (selectOcVersion.getValue() != null) {
                    String minecraftVersion = selectMcVersion.getValue();
                    String obsidianClientVersion = selectOcVersion.getValue();

                    if (nextGui instanceof GuiInstallForge) {
                        Installer.getInstance().getPrimaryStage().setScene(((GuiInstallForge) nextGui).createGui(minecraftVersion, obsidianClientVersion));

                    } else if (nextGui instanceof GuiInstallVanilla) {
                        Installer.getInstance().getPrimaryStage().setScene(((GuiInstallVanilla) nextGui).createGui(minecraftVersion, obsidianClientVersion));

                    } else {
                        System.err.println("[Obsidian Client - Installer] Cannot open next gui, 'nextScene' variable has weird content!");
                        DialogUtils.showInternalErrorDialog();
                    }

                }
            }

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
