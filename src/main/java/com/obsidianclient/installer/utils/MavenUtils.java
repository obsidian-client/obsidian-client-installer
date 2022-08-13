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

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.net.URL;

/**
 * Utility class for handling Maven stuff.
 */
public class MavenUtils {

    public static MavenMetadata getArtifactMetadata(String repoUrl, String groupId, String artifactId) throws IOException {
        URL url = new URL("https://" + repoUrl + "/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/maven-metadata.xml");
        XmlMapper mapper = new XmlMapper();
        return mapper.readValue(url, MavenMetadata.class);
    }

}
