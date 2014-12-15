/*
 * Copyright (c) 2014 lxb<lxbzmy@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.devit.app.ip_messenger.pigeon;

public class PigeonPacket {

    String version;
    String packetNumber;
    String username;
    String hostname;
    int command;
    String message;

    public static PigeonPacket parse(String bin) {
        PigeonPacket packet = new PigeonPacket();
        String[] segments = bin.split(":",6);
        packet.version      = segments[0];
        packet.packetNumber = segments[1];
        packet.username     = segments[2];
        packet.hostname     = segments[3];
        packet.command      = Integer.parseInt(segments[4]);
        packet.message      = segments.length>5?segments[5]:null;
        return packet;
    }

    public String getVersion() {
        return version;
    }

    public String getUsername() {
        return username;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPacketNumber() {
        return packetNumber;
    }

    public int getCommand() {
        return command;
    }

    public String getMessage() {
        return message;
    }
    
}
