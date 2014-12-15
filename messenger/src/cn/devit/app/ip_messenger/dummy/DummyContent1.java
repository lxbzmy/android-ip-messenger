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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent1 {

    public static List<Map<String, String>> ITEMS = new ArrayList<Map<String, String>>(
            10);
    static DateFormat sdf ;

    static {
    	sdf = SimpleDateFormat.getDateInstance();
        // Add 3 sample items.
        addItem("1","lxbzmy：","你好");
        addItem("2","scp","收到");
        addItem("3","lee","https://mobilegateway.hazw.gov.cn/api/scim/v1/ServiceProviderConfigs");
        addItem("4","lxb","星星");

    }

    public static void addItem(String id, String text1, String text2) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("text1", text1);
        map.put("text2", text2);
        map.put("timestamp", sdf.format(new Date()));
        ITEMS.add(map);
    }
}
