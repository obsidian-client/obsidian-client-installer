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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling Maven stuff.
 */
public class MavenUtils {

    public static MavenMetadata getArtifactMetadata(String repoUrl, String groupId, String artifactId) throws IOException, ParserConfigurationException, SAXException {
        URL url = new URL("https://" + repoUrl + "/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/maven-metadata.xml");
        InputStream stream = url.openStream();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(stream);

        List<String> versions = new ArrayList<>();
        NodeList rawVersions = doc.getElementsByTagName("version");
        for (int i = 0; i < rawVersions.getLength(); i++) {
            Node current = rawVersions.item(i);
            versions.add(current.getTextContent());
        }

        return new MavenMetadata(
            doc.getElementsByTagName("groupId").item(0).getTextContent(),
            doc.getElementsByTagName("artifactId").item(0).getTextContent(),
            new MavenMetadata.VersionMetadata(
                    doc.getElementsByTagName("latest").item(0).getTextContent(),
                    doc.getElementsByTagName("release").item(0).getTextContent(),
                    doc.getElementsByTagName("lastUpdated").item(0).getTextContent(),
                    versions
            )
        );
    }

    public static class MavenMetadata {

        private final String groupId;
        private final String artifactId;
        private final VersionMetadata versioning;

        public MavenMetadata(String groupId, String artifactId, VersionMetadata versioning) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.versioning = versioning;
        }

        public String getGroupId() { return groupId; }
        public String getArtifactId() { return artifactId; }
        public VersionMetadata getVersioning() { return versioning; }

        public static class VersionMetadata {

            private final String latest;
            private final String release;
            private final String lastUpdated;
            private final List<String> versions;

            public VersionMetadata(String latest, String release, String lastUpdated, List<String> versions) {
                this.latest = latest;
                this.release = release;
                this.lastUpdated = lastUpdated;
                this.versions = versions;
            }

            public String getLatest() { return latest; }
            public String getRelease() { return release; }
            public String getLastUpdated() { return lastUpdated; }
            public List<String> getVersions() { return versions; }

        }

    }

}
