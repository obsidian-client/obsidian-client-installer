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

package com.obsidianclient.installer.gui;

import com.obsidianclient.installer.Installer;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public abstract class GuiNext extends Gui {

    public abstract Scene createGui(Gui nextGui);

    @Override
    public Scene createGui() {
        return createGui(new Gui() {
            @Override
            public Scene createGui() {
                return new Scene(new VBox(), Installer.WINDOW_WIDTH, Installer.WINDOW_HEIGHT);
            }
        });
    }

}
