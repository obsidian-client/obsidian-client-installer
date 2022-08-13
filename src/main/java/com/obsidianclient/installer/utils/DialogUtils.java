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

package com.obsidianclient.installer.utils;

import com.obsidianclient.installer.Installer;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;

import java.net.URL;

/**
 * Utility class containing pre-made dialogs.
 */
public class DialogUtils {

    public static void showInstalledSuccessfullyDialog(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        DialogPane dialog = alert.getDialogPane();
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        dialog.getStylesheets().add(Installer.OBSIDIAN_CLIENT_STYLESHEET);
        dialog.setMinWidth(550);
        dialog.setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Info");
        alert.show();
    }

    public static void showInternalErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane dialog = alert.getDialogPane();
        dialog.setHeaderText("Internal error occurred!");
        dialog.setContentText("An internal error occurred, please restart the installer!");
        dialog.getStylesheets().add(Installer.OBSIDIAN_CLIENT_STYLESHEET);
        dialog.setMinWidth(550);
        dialog.setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Error");
        alert.show();
    }

    public static void showFailedDownloadDialog(URL file) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane dialog = alert.getDialogPane();
        dialog.setHeaderText("Download failed!");
        dialog.setContentText("Failed to download Obsidian Client!" + "\n(" + file.toString() + ")\n" + "Please check your internet connection and try again.");
        dialog.getStylesheets().add(Installer.OBSIDIAN_CLIENT_STYLESHEET);
        dialog.setMinWidth(550);
        dialog.setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Error");
        alert.show();
    }

    public static void showFailedDownloadMetadataDialog(String url) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane dialog = alert.getDialogPane();
        dialog.setHeaderText("Metadata download failed!");
        dialog.setContentText("Failed to download Obsidian Client metadata!" + "\n(" + url + ")\n" + "Please check your internet connection and restart the installer.");
        dialog.getStylesheets().add(Installer.OBSIDIAN_CLIENT_STYLESHEET);
        dialog.setMinWidth(550);
        dialog.setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Error");
        alert.show();
    }

    public static void showInvalidFilePathDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane dialog = alert.getDialogPane();
        dialog.setHeaderText("Invalid file path!");
        dialog.setContentText("The chosen file path is invalid!\nThis can have multiple reasons, as Example missing rights.\nPlease try again with a different file path.");
        dialog.getStylesheets().add(Installer.OBSIDIAN_CLIENT_STYLESHEET);
        dialog.setMinWidth(550);
        dialog.setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Error");
        alert.show();
    }

    public static void showCantAccessLauncherProfilesDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        DialogPane dialog = alert.getDialogPane();
        dialog.setHeaderText("Can't access 'launcher_profiles.json'!");
        dialog.setContentText("The Minecraft Launcher profiles configuration file isn't accessible!\nThis means you will have to create a profile for Obsidian Client on your own.");
        dialog.getStylesheets().add(Installer.OBSIDIAN_CLIENT_STYLESHEET);
        dialog.setMinWidth(550);
        dialog.setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Warning");
        alert.showAndWait();
    }

    public static void showCloseMinecraftLauncherDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        DialogPane dialog = alert.getDialogPane();
        dialog.setHeaderText("Close Minecraft Launcher!");
        dialog.setContentText("Please make sure the Minecraft Launcher is not running!\nThe launcher has to be closed for the installation to work.");
        dialog.getStylesheets().add(Installer.OBSIDIAN_CLIENT_STYLESHEET);
        dialog.setMinWidth(550);
        dialog.setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Info");
        alert.showAndWait();
    }

}
