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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class FileUtils {

    public static void copyURLToFile(URL url, File file) throws IOException {
        InputStream inputStream = url.openStream();
        FileOutputStream outputStream = new FileOutputStream(file);
        ReadableByteChannel inputChannel = Channels.newChannel(inputStream);
        FileChannel outputChannel = outputStream.getChannel();
        outputChannel.transferFrom(inputChannel, 0, Long.MAX_VALUE);
    }

}
