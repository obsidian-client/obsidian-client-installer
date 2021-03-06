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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.obsidianclient.installer.utils.DialogUtil;
import com.obsidianclient.installer.utils.MavenMetadata;
import com.obsidianclient.installer.utils.MavenUtil;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The main 'engine' for all the real installing stuff.
 */
public class Engine {

    /**
     * Installs Obsidian Client for Minecraft Forge.
     * @param obsidianClientVersion The version of Obsidian Client to install.
     */
    public void installForForge(String obsidianClientVersion) {

        System.out.println("[Obsidian Client - Installer] Installing for Minecraft Forge!");

        final URL forgeJarUrl = this.getJarUrl(obsidianClientVersion);

        //Creating and showing the select file dialog:
        FileChooser dialog = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Java Archive", "*.jar");
        dialog.getExtensionFilters().add(filter);
        dialog.setSelectedExtensionFilter(filter);
        dialog.setTitle("Please save the extracted mod in your 'mods' folder.");
        dialog.setInitialFileName(new File(forgeJarUrl.getFile()).getName());
        dialog.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = dialog.showSaveDialog(Installer.getInstance().getPrimaryStage());

        if (file == null) {
            System.err.println("[Obsidian Client - Installer] User requested invalid file path!");
            System.err.println("[Obsidian Client - Installer] Installation failed!");
            DialogUtil.showInvalidFilePathDialog();
            return;
        }

        if (!file.getName().endsWith(".jar")) {
            file = new File(file.getPath() + ".jar");
        }

        //Downloading and saving the file:
        try {

            //Downloading:
            FileUtils.copyURLToFile(forgeJarUrl, file);

            //Showing a message when it's done:
            DialogUtil.showInstalledSuccessfullyDialog("Installed successfully!", "Obsidian Client for Minecraft Forge was installed successfully!\nYou can close the installer now or install Obsidian Client for another platform.");

            //Return to GuiChoosePlatform:
            Installer.getInstance().getPrimaryStage().setScene(Installer.getInstance().getGuiChoosePlatform());

        } catch (IOException e) {
            System.err.println("[Obsidian Client - Installer] Can't download file (" + forgeJarUrl + "):");
            System.err.println("[Obsidian Client - Installer] Installation failed!");
            DialogUtil.showFailedDownloadDialog(forgeJarUrl);
            e.printStackTrace();
            return;
        }

        System.out.println("[Obsidian Client - Installer] Installed successfully!");

    }

    /**
     * Installs Obsidian Client for the standard Minecraft Launcher.
     * @param obsidianClientVersion The version of Obsidian Client to install.
     * @param mcFolderPath The target Minecraft installation.
     */
    public void installForVanilla(String obsidianClientVersion, String mcFolderPath) {

        System.out.println("[Obsidian Client - Installer] Installing for the standard Minecraft Launcher!");

        final URL vanillaJsonUrl = this.getJsonUrl(obsidianClientVersion);
        final String vanillaProfileIcon = this.getVanillaProfileIcon();
        final File mcFolder = new File(mcFolderPath);
        final File mcVersionsFolder = new File(mcFolder, "versions");
        final File mcLauncherProfilesFile = new File(mcFolder, "launcher_profiles.json");
        final String installDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date());

        //Inform the user to close the Minecraft Launcher:
        DialogUtil.showCloseMinecraftLauncherDialog();

        //Downloading and saving the JSON file:
        try {
            FileUtils.copyURLToFile(vanillaJsonUrl, new File(mcVersionsFolder, "ObsidianClient-" + obsidianClientVersion + File.separator + new File(vanillaJsonUrl.getFile()).getName()));
        } catch (IOException e) {
            System.err.println("[Obsidian Client - Installer] Can't download file (" + vanillaJsonUrl + "):");
            System.err.println("[Obsidian Client - Installer] Installation failed!");
            DialogUtil.showFailedDownloadDialog(vanillaJsonUrl);
            return;
        }

        //Creating a profile entry in the Minecraft launcher:
        ObjectMapper mapper = new ObjectMapper();

        //Creating the new profile:
        ObjectNode profileEntry = mapper.createObjectNode();
        profileEntry.put("name", "Obsidian Client");
        profileEntry.put("type", "custom");
        profileEntry.put("created", installDate);
        profileEntry.put("lastUsed", installDate);
        profileEntry.put("icon", vanillaProfileIcon);
        profileEntry.put("lastVersionId", "ObsidianClient-" + obsidianClientVersion);

        try {

            //Putting the new profile into the file:
            JsonNode json = mapper.readTree(mcLauncherProfilesFile);
            ((ObjectNode) json.get("profiles")).set("ObsidianClient", profileEntry);

            //Writing the file:
            mapper.writerWithDefaultPrettyPrinter().writeValue(mcLauncherProfilesFile, json);

        } catch (IOException e) {
            System.err.println("[Obsidian Client - Installer] Can't modify file " + mcLauncherProfilesFile.getAbsolutePath() + ": The user has to create it's own Minecraft Launcher profile!");
            DialogUtil.showCantAccessLauncherProfilesDialog();
            e.printStackTrace();
        }

        DialogUtil.showInstalledSuccessfullyDialog("Installed successfully!", "Obsidian Client was successfully installed for the standard Minecraft Launcher!\nYou can close the installer now or install Obsidian Client for another platform.");
        System.out.println("[Obsidian Client - Installer] Installed successfully!");
        Installer.getInstance().getPrimaryStage().setScene(Installer.getInstance().getGuiChoosePlatform());

    }

    public void installObsidianLauncher() {
        //Coming Soon!
    }

    /**
     * Gets all available versions of Obsidian Client.
     * The version numbers are returned unmodified.
     * Example: 1.8.9-1.0.0 -- Obsidian Client 1.0.0 for Minecraft 1.8.9
     * @return A list of the versions.
     */
    public List<String> getAllVersions() throws IOException {
        MavenMetadata metadata = MavenUtil.getArtifactMetadata("archive.obsidian-client.com", "com.obsidianclient", "ObsidianClient");
        return metadata.versioning.versions.versionList;
    }

    /**
     * Gets all available Minecraft versions Obsidian Client supports.
     * @return A list of the versions.
     */
    public List<String> getAllMinecraftVersions() throws IOException {
        List<String> list = getAllVersions();
        list.forEach(s -> {
            int index = list.indexOf(s);
            String newString = s.split("-")[0];
            list.set(index, newString);
        });
        return list;
    }

    /**
     * Gets all available versions of Obsidian Client.
     * @return A list of the versions.
     */
    public List<String> getAllObsidianClientVersions(String minecraftVersion) throws IOException {
        List<String> list = getAllVersions();
        list.forEach(s -> {
            int index = list.indexOf(s);
            if (s.startsWith(minecraftVersion)) {
                String newString = s.split("-")[1];
                list.set(index, newString);
            } else {
                list.remove(index);
            }
        });
        return list;
    }

    /**
     * Gets the URL of the Obsidian Client JAR file.
     * @param version The version for the JAR file.
     * @return The URL of the file.
     */
    public URL getJarUrl(String version) {
        try {
            return new URL("https://archive.obsidian-client.com/com/obsidianclient/ObsidianClient/" + version + "/ObsidianClient-" + version + ".jar");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the URL of the Obsidian Client JSON file.
     * @param version The version for the JSON file.
     * @return The URL of the file.
     */
    public URL getJsonUrl(String version) {
        try {
            return new URL("https://archive.obsidian-client.com/com/obsidianclient/ObsidianClient/" + version + "/ObsidianClient-" + version + ".json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads the vanilla profile icon out of 'ObsidianClientProfileIcon.txt' (base64).
     * @return The icon, base64 encoded.
     */
    public String getVanillaProfileIcon() {

        InputStream stream = this.getClass().getResourceAsStream(File.separator + "ObsidianClientProfileIcon.txt");
        if (stream == null) {
            System.err.println("[Obsidian Client - Installer] Can't find internal file 'ObsidianClientProfileIcon.txt': This installer is broken!");
            return "";
        }

        try {
            return "data:image/png;base64," + IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("[Obsidian Client - Installer] Can't read internal file 'ObsidianClientProfileIcon.txt': This installer is broken!");
            e.printStackTrace();
            return "";
        }

    }

    /**
     * @return Returns the default '.minecraft' folder location.
     *         Returns "" if the platform isn't Windows, macOS or Linux.
     */
    public File getDefaultMCFolder() {

        String path;
        String os = System.getProperty("os.name").toLowerCase();
        String user = System.getProperty("user.home");
        if (!user.endsWith(File.separator)) {
            user = user + File.separator;
        }

        if (os.contains("windows")) {
            path = user + "AppData" + File.separator + "Roaming" + File.separator + ".minecraft";

        } else if (os.contains("mac")) {
            path = user + "Library" + File.separator + "Application Support" + File.separator + "minecraft";

        } else if (os.contains("linux")) {
            path = user + ".minecraft";

        } else {
            System.err.println("[Obsidian Client - Installer] Can't find preconfigured '.minecraft' path for OS: " + os);
            path = "";
        }

        return new File(path);
    }

}
