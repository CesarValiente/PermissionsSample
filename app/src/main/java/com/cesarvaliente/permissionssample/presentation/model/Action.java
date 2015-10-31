/**
 * Copyright (C) 2015 Luis G. Valle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cesarvaliente.permissionssample.presentation.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.Manifest;
import android.support.annotation.IntDef;

/**
 * Created by lgvalle on 27/10/15.
 */
public class Action {

    @IntDef({ ACTION_CODE_READ_CONTACTS, ACTION_CODE_SAVE_IMAGE, ACTION_CODE_SEND_SMS })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionCode {
    }

    public static final int ACTION_CODE_READ_CONTACTS = 0;
    public static final int ACTION_CODE_SAVE_IMAGE = 1;
    public static final int ACTION_CODE_SEND_SMS = 2;

    public static final Action READ_CONTACTS = new Action(ACTION_CODE_READ_CONTACTS, Manifest.permission
            .READ_CONTACTS);
    public static final Action SAVE_IMAGE = new Action(ACTION_CODE_SAVE_IMAGE, Manifest.permission
            .WRITE_EXTERNAL_STORAGE);
    public static final Action SEND_SMS = new Action(ACTION_CODE_SEND_SMS, Manifest.permission.SEND_SMS);

    private int code;
    private String permission;

    private Action(@ActionCode int value, String name) {
        this.code = value;
        this.permission = name;
    }

    @ActionCode
    public int getCode() {
        return code;
    }

    public String getPermission() {
        return permission;
    }
}
