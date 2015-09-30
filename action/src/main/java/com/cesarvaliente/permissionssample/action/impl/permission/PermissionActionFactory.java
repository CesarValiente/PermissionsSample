/**
 * Copyright (C) 2015 Cesar Valiente (cesar.valiente@gmail.com)
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
package com.cesarvaliente.permissionssample.action.impl.permission;

import android.app.Activity;

import com.cesarvaliente.permissionssample.action.PermissionAction;

/**
 * Created by Cesar on 27/09/15.
 */
public class PermissionActionFactory {

    public static final int ACTIVITY_IMPL = 1;
    public static final int SUPPORT_IMPL = 2;

    private Activity activity;

    public PermissionActionFactory(Activity activity) {
        this.activity = activity;
    }

    public PermissionAction getPermissionAction(int type) {
        if (type == SUPPORT_IMPL) {
            return new SupportPermissionActionImpl(activity);
        } else {
            return new ActivityPermissionActionImpl(activity);
        }
    }
}
