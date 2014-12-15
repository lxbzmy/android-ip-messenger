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

import java.util.List;

/**
 * 消息正文的模型。
 * 
 * TODO add sequence number
 * 
 * @author lxb
 *
 */
public interface PigeonMessage {

	/**
	 * 发送的人。
	 * 
	 * @return
	 */
	public UserData getSender();

	/**
	 * 得到消息文本正文。
	 * 
	 * @return
	 */
	public String getContent();

	/**
	 * 是否带有附件信息。
	 * 
	 * @return
	 */
	public boolean hasAttachements();

	/**
	 * 附件列表。
	 */
	public List<AttachementLink> getAttachements();

	/**
	 * Relation packet NO.
	 * 
	 * @return Integer represent as string.
	 */
	public String getId();
}
