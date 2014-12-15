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
package cn.devit.app.ip_messenger;

import java.net.SocketException;

import cn.devit.app.ip_messenger.pigeon.PigeonMessage;
import cn.devit.app.ip_messenger.pigeon.UserData;

/**
 * @author lxb
 *
 */
public interface NetworkActivityListener {

    /**
     * fire on eth port bind.
     */
    public void onBindOk();

    /**
     * fire on socket exception.
     * 
     * @param exception
     */
    public void onBindFailure(SocketException exception);

    /**
     * fire when receive user online event.
     * 
     * @param user
     */
    public void onAddUser(UserData user);

    /**
     * Receiver a message.
     * 
     * @param message
     */
    public void onMessage(PigeonMessage message);
}
