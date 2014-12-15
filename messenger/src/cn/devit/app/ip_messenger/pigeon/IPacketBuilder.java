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

import java.nio.charset.Charset;

public interface IPacketBuilder {

    public static interface PacketSetupStep1 {
    	
    	public long getPacketNumber();

        /**
         * set username and hostname from user data.
         * 
         * @param user
         * @return this, to set other properties.
         */
        public abstract PacketSetupStep2 user(UserData user);
    }

    public interface PacketSetupStep2 {

        /**
         * predefined command.
         * 
         * @param command
         * @return this, to continue set message.
         */
        public abstract PacketSetupStep3 command(int command);

    }

    public interface PacketSetupStep3 {

        /**
         * Append detail message associate with command and append NUL
         * automatic.
         * 
         * @param message
         * @return final message build.
         */
        public abstract PacketSetupStep3 message(String message);

        /**
         * @return string of packet content.
         */
        public abstract String toString();

        /**
         * convert {@link #string()} to byte array with charset.
         * 
         * @param charset
         * @return
         */
        public abstract byte[] bytes(Charset charset);

    }
}
