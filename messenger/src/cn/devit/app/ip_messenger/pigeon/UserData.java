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

import java.net.InetSocketAddress;

/**
 * Hold endpoint user information.
 * <p>
 * equals:compare username+hostname.
 *  TODO change to address book item.
 * @author lxb
 */
public class UserData {

    /**
     * Endpoint username.
     */
    protected String username;

    /**
     * user's alias like display name.
     */
    protected String alias; // 别名

    /**
     * A label of user's group.
     */
    protected String group;

    /**
     * network hostname for dns.
     */
    protected String hostname;

    protected InetSocketAddress address;

    public InetSocketAddress getAddress() {
        return address;
    }

    /**
     * Set socket address user use.
     * 
     * @param address
     * @return
     */
    public void setAddress(InetSocketAddress socketAddress) {
        this.address = socketAddress;
    }

    protected UserData() {
        super();
    }

    public UserData(String username, String hostname) {
        super();
        this.username = username;
        this.hostname = hostname;
    }

    /**
     * Create user properties with username,group,hostname
     * 
     * @param username
     * @param hostname
     * @param group
     */
    public UserData(String username, String hostname, String group) {
        super();
        this.username = username;
        this.group = group;
        this.hostname = hostname;
    }

    /**
     * Gets the endpoint username.
     *
     * @return the endpoint username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the endpoint username.
     *
     * @param username
     *            the new endpoint username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the user's alias like display name.
     *
     * @return the user's alias like display name
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the user's alias like display name.
     *
     * @param alias
     *            the new user's alias like display name
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Gets the a label of user's group.
     *
     * @return the a label of user's group
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the a label of user's group.
     *
     * @param group
     *            the new a label of user's group
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Gets the network hostname for dns.
     *
     * @return the network hostname for dns
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Sets the network hostname for dns.
     *
     * @param hostname
     *            the new network hostname for dns
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public String toString() {
        return "<" + username + "@" + hostname + ">";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((hostname == null) ? 0 : hostname.hashCode());
        result = prime * result
                + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserData other = (UserData) obj;
        if (hostname == null) {
            if (other.hostname != null)
                return false;
        } else if (!hostname.equals(other.hostname))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

}
