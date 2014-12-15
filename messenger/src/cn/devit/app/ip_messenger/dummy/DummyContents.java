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
package cn.devit.app.ip_messenger.dummy;

import java.util.ArrayList;
import java.util.List;

import cn.devit.app.ip_messenger.pigeon.UserData;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContents {

    /**
     * An array of sample (dummy) items.
     */
    public static List<UserData> USER_ITEMS = new ArrayList<UserData>();

    static {
        addItem(new UserData("lxb", "localhost"));
        addItem(new UserData("lxb1", "localhost"));
        addItem(new UserData("lxb2", "localhost"));
    }

    private static void addItem(UserData item) {
        USER_ITEMS.add(item);
    }
}
