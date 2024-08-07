/*
    This file is part of the HeavenMS MapleStory Server, commands OdinMS-based
    Copyleft (L) 2016 - 2019 RonanLana

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
   @Author: Arthur L - Refactored command content into modules
*/
package org.gms.client.command.commands.gm3;

import org.gms.client.Character;
import org.gms.client.Client;
import org.gms.client.command.Command;
import io.netty.buffer.Unpooled;
import org.gms.net.PacketHandler;
import org.gms.net.PacketProcessor;
import org.gms.net.packet.ByteBufInPacket;
import org.gms.net.packet.InPacket;
import org.gms.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gms.util.HexTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PeCommand extends Command {
    {
        setDescription(I18nUtil.getMessage("PeCommand.message1"));
    }

    private static final Logger log = LoggerFactory.getLogger(PeCommand.class);

    @Override
    public void execute(Client c, String[] params) {
        Character player = c.getPlayer();
        String packet = "";
        try (BufferedReader br = Files.newBufferedReader(Path.of("pe.txt"))) {
            Properties packetProps = new Properties();
            packetProps.load(br);
            packet = packetProps.getProperty("pe");
        } catch (IOException ex) {
            ex.printStackTrace();
            player.yellowMessage(I18nUtil.getMessage("PeCommand.message2"));
            return;

        }

        byte[] packetContent = HexTool.toBytes(packet);
        InPacket inPacket = new ByteBufInPacket(Unpooled.wrappedBuffer(packetContent));
        short packetId = inPacket.readShort();
        final PacketHandler packetHandler = PacketProcessor.getProcessor(0, c.getChannel()).getHandler(packetId);
        if (packetHandler != null && packetHandler.validateState(c)) {
            try {
                player.yellowMessage(I18nUtil.getMessage("PeCommand.message3", packet));
                packetHandler.handlePacket(inPacket, c);
            } catch (final Throwable t) {
                final String chrInfo = player != null ? player.getName() : "?";
                log.warn(I18nUtil.getLogMessage("PeCommand.execute.warn1"), packetHandler.getClass().getSimpleName(),
                        chrInfo, c.getAccountName(), packet, t);
            }
        }
    }
}
