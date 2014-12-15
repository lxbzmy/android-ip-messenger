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

import java.nio.charset.Charset;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // BOARD 主板
        // String phoneInfo = "BOARD: " + android.os.Build.BOARD;
        // phoneInfo += ", BOOTLOADER: " + android.os.Build.BOOTLOADER;
        // // BRAND 运营商
        // phoneInfo += ", BRAND: " + android.os.Build.BRAND;
        // phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI;
        // phoneInfo += ", CPU_ABI2: " + android.os.Build.CPU_ABI2;
        // // DEVICE 驱动
        // phoneInfo += ", DEVICE: " + android.os.Build.DEVICE;
        // // DISPLAY 显示
        // phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY;
        // // 指纹
        // phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT;
        // // HARDWARE 硬件
        // phoneInfo += ", HARDWARE: " + android.os.Build.HARDWARE;
        // phoneInfo += ", HOST: " + android.os.Build.HOST;
        // phoneInfo += ", ID: " + android.os.Build.ID;
        // // MANUFACTURER 生产厂家
        // phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER;
        // // MODEL 机型
        // phoneInfo += ", MODEL: " + android.os.Build.MODEL;
        // phoneInfo += ", PRODUCT: " + android.os.Build.PRODUCT;
        // phoneInfo += ", RADIO: " + android.os.Build.RADIO;
        // phoneInfo += ", RADITAGSO: " + android.os.Build.TAGS;
        // phoneInfo += ", TIME: " + android.os.Build.TIME;
        // phoneInfo += ", TYPE: " + android.os.Build.TYPE;
        // phoneInfo += ", USER: " + android.os.Build.USER;
        // // VERSION.RELEASE 固件版本
        // phoneInfo += ", VERSION.RELEASE: " +
        // android.os.Build.VERSION.RELEASE;
        // phoneInfo += ", VERSION.CODENAME: " +
        // android.os.Build.VERSION.CODENAME;
        // // VERSION.INCREMENTAL 基带版本
        // phoneInfo += ", VERSION.INCREMENTAL: "
        // + android.os.Build.VERSION.INCREMENTAL;
        // // VERSION.SDK SDK版本
        // phoneInfo += ", VERSION.SDK: " + android.os.Build.VERSION.SDK;
        // phoneInfo += ", VERSION.SDK_INT: " +
        // android.os.Build.VERSION.SDK_INT;
        // System.out.println(phoneInfo);
        preferenceCheck();
    }

    /**
     * 验证首选项的值是合理的，有BUG就覆盖成hardcode。
     */
    public void preferenceCheck() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        String value = preferences.getString("display_name", null);
        if (TextUtils.isEmpty(value)) {
            preferences.edit().putString("display_name", Build.BRAND).commit();
        }
        value = preferences.getString("host_name", null);
        if (TextUtils.isEmpty(value)) {
            preferences.edit().putString("host_name", Build.MODEL).commit();
        }
        value = preferences.getString("charset", null);
        if (TextUtils.isEmpty(value)) {
            preferences.edit()
                    .putString("charset", Charset.defaultCharset().name())
                    .commit();
        }

    }

}
