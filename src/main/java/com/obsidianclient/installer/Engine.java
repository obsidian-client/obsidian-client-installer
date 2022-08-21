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

import com.obsidianclient.installer.gui.impl.GuiChoosePlatform;
import com.obsidianclient.installer.utils.DialogUtils;
import com.obsidianclient.installer.utils.IOUtils;
import com.obsidianclient.installer.utils.MavenUtils;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            DialogUtils.showInvalidFilePathDialog();
            return;
        }

        if (!file.getName().endsWith(".jar")) {
            file = new File(file.getPath() + ".jar");
        }

        //Downloading and saving the file:
        try {

            //Downloading:
            IOUtils.copyURLToFile(forgeJarUrl, file);

            //Showing a message when it's done:
            DialogUtils.showInstalledSuccessfullyDialog("Installed successfully!", "Obsidian Client for Minecraft Forge was installed successfully!\nYou can close the installer now or install Obsidian Client for another platform.");

            //Return to GuiChoosePlatform:
            Installer.getInstance().getPrimaryStage().setScene(new GuiChoosePlatform().createGui());

        } catch (IOException e) {
            System.err.println("[Obsidian Client - Installer] Can't download file (" + forgeJarUrl + "):");
            System.err.println("[Obsidian Client - Installer] Installation failed!");
            DialogUtils.showFailedDownloadDialog(forgeJarUrl);
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
        DialogUtils.showCloseMinecraftLauncherDialog();

        //Downloading and saving the JSON file:
        try {
            IOUtils.copyURLToFile(vanillaJsonUrl, new File(mcVersionsFolder, "ObsidianClient-" + obsidianClientVersion + File.separator + new File(vanillaJsonUrl.getFile()).getName()));
        } catch (IOException e) {
            System.err.println("[Obsidian Client - Installer] Can't download file (" + vanillaJsonUrl + "):");
            System.err.println("[Obsidian Client - Installer] Installation failed!");
            DialogUtils.showFailedDownloadDialog(vanillaJsonUrl);
            return;
        }

        //Creating a profile entry in the Minecraft launcher:
        try {
            FileInputStream jsonStreamIn = new FileInputStream(mcLauncherProfilesFile);
            JsonReader jsonReader = Json.createReader(jsonStreamIn);
            JsonObject json = jsonReader.readObject();
            JsonObject profilesJson = json.getJsonObject("profiles");

            //Creating the new profile:
            JsonObjectBuilder profileEntryBuilder = Json.createObjectBuilder();
            profileEntryBuilder.add("name", "Obsidian Client");
            profileEntryBuilder.add("type", "custom");
            profileEntryBuilder.add("created", installDate);
            profileEntryBuilder.add("lastUsed", installDate);
            profileEntryBuilder.add("icon", vanillaProfileIcon);
            profileEntryBuilder.add("lastVersionId", "ObsidianClient-" + obsidianClientVersion);
            JsonObject profileEntry = profileEntryBuilder.build();

            //Putting it into the profiles list:
            JsonObjectBuilder profilesListBuilder = Json.createObjectBuilder(profilesJson);
            profilesListBuilder.add("ObsidianClient", profileEntry);
            JsonObject profilesList = profilesListBuilder.build();

            //Putting the profiles list into the json file:
            JsonObjectBuilder mainBuilder = Json.createObjectBuilder(json);
            mainBuilder.add("profiles", profilesList);
            JsonObject newJson = mainBuilder.build();

            FileOutputStream jsonStreamOut = new FileOutputStream(mcLauncherProfilesFile);
            Map<String, Boolean> config = new HashMap<>();
            config.put(JsonGenerator.PRETTY_PRINTING, true);
            JsonWriterFactory jsonWriterFactory = Json.createWriterFactory(config);
            JsonWriter jsonWriter = jsonWriterFactory.createWriter(jsonStreamOut);
            jsonWriter.writeObject(newJson);

        } catch (FileNotFoundException e) {
            System.err.println("[Obsidian Client - Installer] Can't modify file " + mcLauncherProfilesFile.getAbsolutePath() + ": The user has to create it's own Minecraft Launcher profile!");
            DialogUtils.showCantAccessLauncherProfilesDialog();
            e.printStackTrace();
        }

        DialogUtils.showInstalledSuccessfullyDialog("Installed successfully!", "Obsidian Client was successfully installed for the standard Minecraft Launcher!\nYou can close the installer now or install Obsidian Client for another platform.");
        System.out.println("[Obsidian Client - Installer] Installed successfully!");
        Installer.getInstance().getPrimaryStage().setScene(new GuiChoosePlatform().createGui());

    }

    public void installObsidianLauncher() {
        //Coming Soon!
    }

    /**
     * Gets all available versions of Obsidian Client.
     * The version numbers are returned unmodified.
     * Example: 1.8.9-1.0.0 --> Obsidian Client 1.0.0 for Minecraft 1.8.9
     * @return A list of the versions.
     */
    public List<String> getAllVersions() throws IOException {
        try {
            MavenUtils.MavenMetadata metadata = MavenUtils.getArtifactMetadata("archive.obsidian-client.com", "com.obsidianclient", "ObsidianClient");
            return metadata.getVersioning().getVersions();
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("Can't download the Obsidian Client version information file!", e);
        }
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
            return "data:image/png;base64," + IOUtils.convertStreamToString(stream);
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
