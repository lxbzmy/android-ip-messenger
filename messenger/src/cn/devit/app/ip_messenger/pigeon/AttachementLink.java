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

/**
 * 附件的链接模型。
 * <p>
 * 附件在报文中的要与正文文本隔开，一次可以传输多个附件信息。<br/>
 * 附件消息段 = 附件(0x7附件)+\0 <br/>
 * 附件 = {序号（从0开始）}:{文件名}:{文件大小HEX}:{日期HEX}:{类别}<br/>
 * TODO change to attachementDescriptor?
 * @author lxb
 *
 */
public class AttachementLink {

    AttachementLink() {
        super();
    }

    /**
     * @param id
     * @param filename
     * @param length
     * @param lastModified
     * @param type
     */
    public AttachementLink(int id, String filename, long length,
            long lastModified, int type) {
        super();
        this.id = id;
        this.filename = filename;
        this.length = length;
        this.type = type;
        this.lastModified = lastModified;
    }

    /**
     * 序号。
     */
    int id;

    /**
     * 文件名。
     */
    String filename;

    /**
     * 文件长度。
     */
    long length;

    /**
     * 类别
     */
    int type;

    /**
     * 最后修改日期。
     */
    long lastModified;

    /**
     * 序号，在报文的序号。
     * 
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * 文件名。
     * 
     * @return
     */
    public String getFilename() {
        return filename;
    }

    /**
     * 文件长度。
     * 
     * @return
     */
    public long getLength() {
        return length;
    }

    /**
     * 文件类型。
     * 
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     * 最后修改时间。
     * 
     * @return
     */
    public long getLastModified() {
        return lastModified;
    }

}
