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
 * predefined IP messenger protocol command .
 * 
 * @author lxb
 */
public class PigeonCommand {
    /**
     * VERSION Number
     */
    public static final int VERSION = 1;
    /**
     * Default UDP port to listen.
     */
    public static final int PORT = 2425;

    /**
     * NOP
     */
    public static final int IPMSG_NOOPERATION = 0x00000000;
    /**
     * Notify join .
     * <p>
     * 当新用户开始监听端口时要发送的第一个命令。
     */
    public static final int IPMSG_BR_ENTRY = 0x00000001;

    /**
     * Notify logout .
     * <p>
     * Send this command when a user off network.
     */
    public static final int IPMSG_BR_EXIT = 0x00000002;

    /**
     * State online notify.
     */
    public static final int IPMSG_ANSENTRY = 0x00000003; // 通报在线

    public static final int IPMSG_BR_ABSENCE = 0x00000004; // 改为缺席模式

    public static final int IPMSG_BR_ISGETLIST = 0x00000010; // 寻找有效的可以发送用户列表的成员
    public static final int IPMSG_OKGETLIST = 0x00000011; // 通知用户列表已经获得
    public static final int IPMSG_GETLIST = 0x00000012; // 用户列表发送请求
    public static final int IPMSG_ANSLIST = 0x00000013; // 应答用户列表发送请求
    public static final int IPMSG_FILE_MTIME = 0x00000014; //
    public static final int IPMSG_FILE_CREATETIME = 0x00000016; //
    public static final int IPMSG_BR_ISGETLIST2 = 0x00000018; //

    /**
     * 发送普通消息指令。
     * <p>
     * 注意，当接受到对方的消息后，需要回复收到指令。
     */
    public static final int IPMSG_SENDMSG = 0x00000020;

    /**
     * 通报收到消息。
     * <p>
     * 当收到对方的消息后，使用此指令回复对方。
     */
    public static final int IPMSG_RECVMSG = 0x00000021;

    public static final int IPMSG_READMSG = 0x00000030; // 消息打开通知
    public static final int IPMSG_DELMSG = 0x00000031; // 消息丢弃通知
    public static final int IPMSG_ANSREADMSG = 0x00000032; // 消息打开确认通知（version-8中添加）

    public static final int IPMSG_GETINFO = 0x00000040; // 获得IPMSG版本信息
    public static final int IPMSG_SENDINFO = 0x00000041; // 发送IPMSG版本信息

    public static final int IPMSG_GETABSENCEINFO = 0x00000050; // 获得缺席信息
    public static final int IPMSG_SENDABSENCEINFO = 0x00000051; // 发送缺席信息

    public static final int IPMSG_GETFILEDATA = 0x00000060; // 文件传输请求
    public static final int IPMSG_RELEASEFILES = 0x00000061; // 丢弃附加文件
    public static final int IPMSG_GETDIRFILES = 0x00000062; // 附着统计文件请求

    public static final int IPMSG_GETPUBKEY = 0x00000072; // 获得RSA公钥
    public static final int IPMSG_ANSPUBKEY = 0x00000073; // 应答RSA公钥

    /* option for all command */
    public static final int IPMSG_ABSENCEOPT = 0x00000100; // 缺席模式
    public static final int IPMSG_SERVEROPT = 0x00000200; // 服务器（保留）
    public static final int IPMSG_DIALUPOPT = 0x00010000; // 发送给个人

    /**
     * 附件标记，报文中包含附件的链接。
     * <p>
     * 当指令中此位置1表示报文包含附件。
     */
    public static final int IPMSG_FILEATTACHOPT = 0x00200000; // 附加文件
    public static final int IPMSG_ENCRYPTOPT = 0x00400000; // 加密

    /* option for send command */
    /**
     * 消息传输验证标记，要求收到消息的人发送应答报文以验证发送正确。
     * <p>
     * 当收到的消息中包含此标记时，需要使用 {@link #IPMSG_ANSENTRY}来回复对方。
     */
    public static final int IPMSG_FLAG_SENDCHECKOPT = 0x00000100;

    public static final int IPMSG_SECRETOPT = 0x00000200; // 密封的消息
    public static final int IPMSG_BROADCASTOPT = 0x00000400; // 广播
    public static final int IPMSG_MULTICASTOPT = 0x00000800; // 多播
    public static final int IPMSG_NOPOPUPOPT = 0x00001000; // （不再有效）
    public static final int IPMSG_AUTORETOPT = 0x00002000; // 自动应答(Ping-pong
                                                           // protection)
    public static final int IPMSG_RETRYOPT = 0x00004000; // 重发标识（用于请求用户列表时）
    public static final int IPMSG_PASSWORDOPT = 0x00008000; // 密码
    public static final int IPMSG_NOLOGOPT = 0x00020000; // 没有日志文件
    public static final int IPMSG_NEWMUTIOPT = 0x00040000; // 新版本的多播（保留）
    public static final int IPMSG_NOADDLISTOPT = 0x00080000; // 不添加用户列表 Notice
                                                             // to the members
                                                             // outside of
                                                             // BR_ENTRY
    public static final int IPMSG_READCHECKOPT = 0x00100000; // 密封消息验证（version8中添加）
    public static final int IPMSG_SECRETEXOPT = (IPMSG_READCHECKOPT | IPMSG_SECRETOPT);

    /* encryption flags for encrypt command */
    public static final int IPMSG_RSA_512 = 0x00000001;
    public static final int IPMSG_RSA_1024 = 0x00000002;
    public static final int IPMSG_RSA_2048 = 0x00000004;
    public static final int IPMSG_RC2_40 = 0x00001000;
    public static final int IPMSG_RC2_128 = 0x00004000;
    public static final int IPMSG_RC2_256 = 0x00008000;
    public static final int IPMSG_BLOWFISH_128 = 0x00020000;
    public static final int IPMSG_BLOWFISH_256 = 0x00040000;
    public static final int IPMSG_SIGN_MD5 = 0x10000000;

    /* file types for fileattach command */
    public static final int IPMSG_FILE_REGULAR = 0x00000001;
    public static final int IPMSG_FILE_DIR = 0x00000002;
    public static final int IPMSG_FILE_RETPARENT = 0x00000003; // return parent
                                                               // directory
    public static final int IPMSG_FILE_SYMLINK = 0x00000004;
    public static final int IPMSG_FILE_CDEV = 0x00000005; // for UNIX
    public static final int IPMSG_FILE_BDEV = 0x00000006; // for UNIX
    public static final int IPMSG_FILE_FIFO = 0x00000007; // for UNIX
    public static final int IPMSG_FILE_RESFORK = 0x00000010; // for Mac

    /* file attribute options for fileattach command */
    public static final int IPMSG_FILE_RONLYOPT = 0x00000100;
    public static final int IPMSG_FILE_HIDDENOPT = 0x00001000;
    public static final int IPMSG_FILE_EXHIDDENOPT = 0x00002000; // for MacOS X
    public static final int IPMSG_FILE_ARCHIVEOPT = 0x00004000;
    public static final int IPMSG_FILE_SYSTEMOPT = 0x00008000;

    /* extend attribute types for fileattach command */
    public static final int IPMSG_FILE_UID = 0x00000001;
    public static final int IPMSG_FILE_USERNAME = 0x00000002; // uid by string
    public static final int IPMSG_FILE_GID = 0x00000003;
    public static final int IPMSG_FILE_GROUPNAME = 0x00000004; // gid by string
    public static final int IPMSG_FILE_PERM = 0x00000010; // for UNIX
    public static final int IPMSG_FILE_MAJORNO = 0x00000011; // for UNIX devfile
    public static final int IPMSG_FILE_MINORNO = 0x00000012; // for UNIX devfile
    public static final int IPMSG_FILE_CTIME = 0x00000013; // for UNIX
    public static final int IPMSG_FILE_ATIME = 0x00000015;
    public static final int IPMSG_FILE_CREATOR = 0x00000020; // for Mac
    public static final int IPMSG_FILE_FILETYPE = 0x00000021; // for Mac
    public static final int IPMSG_FILE_FINDERINFO = 0x00000022; // for Mac
    public static final int IPMSG_FILE_ACL = 0x00000030;
    public static final int IPMSG_FILE_ALIASFNAME = 0x00000040; // alias fname
    public static final int IPMSG_FILE_UNICODEFNAME = 0x00000041; // UNICODE
                                                                  // fname

}
