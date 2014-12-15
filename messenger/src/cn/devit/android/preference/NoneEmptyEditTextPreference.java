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
package cn.devit.android.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class NoneEmptyEditTextPreference extends EditTextPreference {

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                AlertDialog dialog = (AlertDialog) getDialog();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(
                        false);
            } else {
                AlertDialog dialog = (AlertDialog) getDialog();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(
                        true);
            }

        }
    };;

    public NoneEmptyEditTextPreference(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    public NoneEmptyEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoneEmptyEditTextPreference(Context context) {
        super(context);
    }

    @Override
    protected void onAddEditTextToDialogView(View dialogView, EditText editText) {
        super.onAddEditTextToDialogView(dialogView, editText);
        editText.addTextChangedListener(watcher);
    }

}
