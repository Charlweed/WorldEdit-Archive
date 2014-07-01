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

package com.sk89q.worldedit.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.LocalWorld.KillFlags;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.command.UtilityCommands.FlagContainer;
import com.sk89q.worldedit.command.tool.BrushTool;
import com.sk89q.worldedit.command.tool.brush.*;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.function.mask.BlockMask;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.util.command.binding.Switch;
import com.sk89q.worldedit.util.command.parametric.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Commands to set brush shape.
 */
public class BrushCommands {

    private final WorldEdit worldEdit;

    /**
     * Create a new instance.
     *
     * @param worldEdit reference to WorldEdit
     */
    public BrushCommands(WorldEdit worldEdit) {
        checkNotNull(worldEdit);
        this.worldEdit = worldEdit;
    }

    @Command(
        aliases = { "sphere", "s" },
        usage = "<pattern> [radius]",
        flags = "h",
        desc = "Choose the sphere brush",
        help =
            "Chooses the sphere brush.\n" +
            "The -h flag creates hollow spheres instead.",
        min = 1,
        max = 2
    )
    @CommandPermissions("worldedit.brush.sphere")
    public void sphereBrush(Player player, LocalSession session, EditSession editSession, Pattern fill,
                            @Optional("2") double radius, @Switch('h') boolean hollow) throws WorldEditException {
        worldEdit.checkMaxBrushRadius(radius);

        BrushTool tool = session.getBrushTool(player.getItemInHand());
        tool.setFill(fill);
        tool.setSize(radius);

        if (hollow) {
            tool.setBrush(new HollowSphereBrush(), "worldedit.brush.sphere");
        } else {
            tool.setBrush(new SphereBrush(), "worldedit.brush.sphere");
        }

        player.print(String.format("Sphere brush shape equipped (%.0f).", radius));
    }

    @Command(
        aliases = { "cylinder", "cyl", "c" },
        usage = "<block> [radius] [height]",
        flags = "h",
        desc = "Choose the cylinder brush",
        help =
            "Chooses the cylinder brush.\n" +
            "The -h flag creates hollow cylinders instead.",
        min = 1,
        max = 3
    )
    @CommandPermissions("worldedit.brush.cylinder")
    public void cylinderBrush(Player player, LocalSession session, EditSession editSession, Pattern fill,
                              @Optional("2") double radius, @Optional("1") int height, @Switch('h') boolean hollow) throws WorldEditException {
        worldEdit.checkMaxBrushRadius(radius);
        worldEdit.checkMaxBrushRadius(height);

        BrushTool tool = session.getBrushTool(player.getItemInHand());
        tool.setFill(fill);
        tool.setSize(radius);

        if (hollow) {
            tool.setBrush(new HollowCylinderBrush(height), "worldedit.brush.cylinder");
        } else {
            tool.setBrush(new CylinderBrush(height), "worldedit.brush.cylinder");
        }

        player.print(String.format("Cylinder brush shape equipped (%.0f by %d).", radius, height));
    }

    @Command(
        aliases = { "clipboard", "copy" },
        usage = "",
        flags = "a",
        desc = "Choose the clipboard brush",
        help =
            "Chooses the clipboard brush.\n" +
            "The -a flag makes it not paste air.",
        min = 0,
        max = 0
    )
    @CommandPermissions("worldedit.brush.clipboard")
    public void clipboardBrush(Player player, LocalSession session, EditSession editSession, @Switch('a') boolean ignoreAir) throws WorldEditException {

        CuboidClipboard clipboard = session.getClipboard();

        if (clipboard == null) {
            player.printError("Copy something first.");
            return;
        }

        Vector size = clipboard.getSize();

        worldEdit.checkMaxBrushRadius(size.getBlockX());
        worldEdit.checkMaxBrushRadius(size.getBlockY());
        worldEdit.checkMaxBrushRadius(size.getBlockZ());

        BrushTool tool = session.getBrushTool(player.getItemInHand());
        tool.setBrush(new ClipboardBrush(clipboard, ignoreAir), "worldedit.brush.clipboard");

        player.print("Clipboard brush shape equipped.");
    }

    @Command(
        aliases = { "smooth" },
        usage = "[size] [iterations]",
        flags = "n",
        desc = "Choose the terrain softener brush",
        help =
            "Chooses the terrain softener brush.\n" +
            "The -n flag makes it only consider naturally occurring blocks.",
        min = 0,
        max = 2
    )
    @CommandPermissions("worldedit.brush.smooth")
    public void smoothBrush(Player player, LocalSession session, EditSession editSession,
                            @Optional("2") double radius, @Optional("4") int iterations, @Switch('n')
                            boolean naturalBlocksOnly) throws WorldEditException {

        worldEdit.checkMaxBrushRadius(radius);

        BrushTool tool = session.getBrushTool(player.getItemInHand());
        tool.setSize(radius);
        tool.setBrush(new SmoothBrush(iterations, naturalBlocksOnly), "worldedit.brush.smooth");

        player.print(String.format("Smooth brush equipped (%.0f x %dx, using " + (naturalBlocksOnly ? "natural blocks only" : "any block") + ").",
                radius, iterations));
    }

    @Command(
        aliases = { "ex", "extinguish" },
        usage = "[radius]",
        desc = "Shortcut fire extinguisher brush",
        min = 0,
        max = 1
    )
    @CommandPermissions("worldedit.brush.ex")
    public void extinguishBrush(Player player, LocalSession session, EditSession editSession, @Optional("5") double radius) throws WorldEditException {
        worldEdit.checkMaxBrushRadius(radius);

        BrushTool tool = session.getBrushTool(player.getItemInHand());
        Pattern fill = new BlockPattern(new BaseBlock(0));
        tool.setFill(fill);
        tool.setSize(radius);
        tool.setMask(new BlockMask(editSession, new BaseBlock(BlockID.FIRE)));
        tool.setBrush(new SphereBrush(), "worldedit.brush.ex");

        player.print(String.format("Extinguisher equipped (%.0f).", radius));
    }

    @Command(
            aliases = { "gravity", "grav" },
            usage = "[radius]",
            flags = "h",
            desc = "Gravity brush",
            help =
                "This brush simulates the affect of gravity.\n" +
                "The -h flag makes it affect blocks starting at the world's max y, " +
                    "instead of the clicked block's y + radius.",
            min = 0,
            max = 1
    )
    @CommandPermissions("worldedit.brush.gravity")
    public void gravityBrush(Player player, LocalSession session, EditSession editSession, @Optional("5") double radius, @Switch('h') boolean fromMaxY) throws WorldEditException {
        worldEdit.checkMaxBrushRadius(radius);

        BrushTool tool = session.getBrushTool(player.getItemInHand());
        tool.setSize(radius);
        tool.setBrush(new GravityBrush(fromMaxY), "worldedit.brush.gravity");

        player.print(String.format("Gravity brush equipped (%.0f).",
                radius));
    }
    
    @Command(
            aliases = { "butcher", "kill" },
            usage = "[radius] [command flags]",
            desc = "Butcher brush",
            help = "Kills nearby mobs within the specified radius.\n" +
                   "Any number of 'flags' that the //butcher command uses\n" +
                   "may be specified as an argument",
            min = 0,
            max = 2
    )
    @CommandPermissions("worldedit.brush.butcher")
    public void butcherBrush(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
        LocalConfiguration config = worldEdit.getConfiguration();

        double radius = args.argsLength() > 0 ? args.getDouble(0) : 5;
        double maxRadius = config.maxBrushRadius;
        // hmmmm not horribly worried about this because -1 is still rather efficient,
        // the problem arises when butcherMaxRadius is some really high number but not infinite
        // - original idea taken from https://github.com/sk89q/worldedit/pull/198#issuecomment-6463108
        if (player.hasPermission("worldedit.butcher")) {
            maxRadius = Math.max(config.maxBrushRadius, config.butcherMaxRadius);
        }
        if (radius > maxRadius) {
            player.printError("Maximum allowed brush radius: " + maxRadius);
            return;
        }

        FlagContainer flags = new FlagContainer(player);
        if (args.argsLength() == 2) {
            String flagString = args.getString(1);
            // straight from the command, using contains instead of hasflag
            flags.or(KillFlags.FRIENDLY      , flagString.contains("f")); // No permission check here. Flags will instead be filtered by the subsequent calls.
            flags.or(KillFlags.PETS          , flagString.contains("p"), "worldedit.butcher.pets");
            flags.or(KillFlags.NPCS          , flagString.contains("n"), "worldedit.butcher.npcs");
            flags.or(KillFlags.GOLEMS        , flagString.contains("g"), "worldedit.butcher.golems");
            flags.or(KillFlags.ANIMALS       , flagString.contains("a"), "worldedit.butcher.animals");
            flags.or(KillFlags.AMBIENT       , flagString.contains("b"), "worldedit.butcher.ambient");
            flags.or(KillFlags.TAGGED        , flagString.contains("t"), "worldedit.butcher.tagged");
            flags.or(KillFlags.WITH_LIGHTNING, flagString.contains("l"), "worldedit.butcher.lightning");
        }
        BrushTool tool = session.getBrushTool(player.getItemInHand());
        tool.setSize(radius);
        tool.setBrush(new ButcherBrush(flags.flags), "worldedit.brush.butcher");

        player.print(String.format("Butcher brush equipped (%.0f).",
                radius));
    }
}
