/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.extension.registry;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.internal.registry.AbstractRegistry;

/**
 * A registry of known {@link Mask}s. Provides methods to instantiate
 * new masks from input.
 * </p>
 * Instances of this class can be taken from
 * {@link WorldEdit#getMaskRegistry()}.
 */
public final class MaskRegistry extends AbstractRegistry<Mask> {

    /**
     * Create a new mask registry.
     *
     * @param worldEdit the WorldEdit instance
     */
    public MaskRegistry(WorldEdit worldEdit) {
        super(worldEdit);

        parsers.add(new DefaultMaskParser(worldEdit));
    }

}
