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

package com.obsidianclient.installer;

import com.obsidianclient.installer.gui.impl.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Installer extends Application {

    /**
     * The Obsidian Client Installer instance.
     */
    private static Installer instance;

    public static final String INSTALLER_VERSION = "1.0.0";

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 480;

    public static final String OBSIDIAN_CLIENT_STYLESHEET = "ObsidianClientStyle.css";
    public static final String OBSIDIAN_CLIENT_LOGO = "ObsidianClientLogo.png";
    public static final String OBSIDIAN_CLIENT_LOGO_SMALL = "ObsidianClientLogoSmall.png";

    private Stage primaryStage;

    private Scene guiMainMenu;
    private Scene guiAbout;
    private Scene guiChoosePlatform;
    private Scene guiChooseVersion;
    private Scene guiInstallForge;
    private Scene guiInstallLauncher;
    private Scene guiInstallVanilla;

    /**
     * The main 'engine' for all the real installing stuff.
     */
    private Engine engine;

    public Installer() {
        instance = this;
        this.primaryStage = null;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println("[Obsidian Client - Installer] Starting ... (Version: " + Installer.INSTALLER_VERSION + ")");

        this.primaryStage = primaryStage;

        this.engine = new Engine();

        this.guiMainMenu = new GuiMainMenu().createGui();
        this.guiAbout = new GuiAbout().createGui();
        this.guiChoosePlatform = new GuiChoosePlatform().createGui();
        this.guiChooseVersion = new GuiChooseVersion().createGui();
        this.guiInstallForge = new GuiInstallForge().createGui();
        this.guiInstallLauncher = new GuiInstallLauncher().createGui();
        this.guiInstallVanilla = new GuiInstallVanilla().createGui();

        primaryStage.setTitle("Obsidian Client Installer");
        primaryStage.getIcons().add(new Image(Installer.OBSIDIAN_CLIENT_LOGO_SMALL));

        primaryStage.setScene(this.guiMainMenu);

        primaryStage.show();

        System.out.println("[Obsidian Client - Installer] Started successfully!");

    }

    public static Installer getInstance() {
        return instance;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Engine getEngine() {
        return engine;
    }

    public Scene getGuiMainMenu() {
        return guiMainMenu;
    }

    public Scene getGuiAbout() {
        return guiAbout;
    }

    public Scene getGuiChoosePlatform() {
        return guiChoosePlatform;
    }

    public Scene getGuiChooseVersion() {
        return guiChooseVersion;
    }

    public Scene getGuiInstallForge() {
        return guiInstallForge;
    }

    public Scene getGuiInstallLauncher() {
        return guiInstallLauncher;
    }

    public Scene getGuiInstallVanilla() {
        return guiInstallVanilla;
    }

}
