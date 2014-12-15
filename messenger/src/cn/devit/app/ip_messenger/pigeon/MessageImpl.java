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

import java.io.Serializable;
import java.util.List;

/**
 * Default {@link PigeonMessage} implementation.
 * 
 * @author lxb
 *
 */
public class MessageImpl implements PigeonMessage, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    String content;

    List<AttachementLink> attachements;

    UserData from;
    
    String packetNumber;

    public MessageImpl(String packetNumber,UserData from, String content,
            List<AttachementLink> attaches) {
        super();
        this.packetNumber = packetNumber;
        this.from = from;
        this.content = content;
        this.attachements = attaches;
    }

    @Override
    public UserData getSender() {
        return from;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean hasAttachements() {
        return attachements != null && attachements.size() > 0;
    }

    @Override
    public List<AttachementLink> getAttachements() {
        return attachements;
    }

	@Override
	public String getId() {
		return this.packetNumber;
	}

}
