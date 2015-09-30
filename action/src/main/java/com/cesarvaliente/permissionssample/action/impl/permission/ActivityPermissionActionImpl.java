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
import android.content.pm.PackageManager;

import com.cesarvaliente.permissionssample.action.PermissionAction;
import com.cesarvaliente.permissionssample.action.utils.CommonUtils;

/**
 * Created by Cesar on 24/09/15.
 */
class ActivityPermissionActionImpl implements PermissionAction {

    private Activity activity;

    public ActivityPermissionActionImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean hasSelfPermission(String permission) {
        if (!CommonUtils.isMarshmallowOrHigher()) {
            return true;
        }
        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestPermission(String permission, int requestCode) {
        activity.requestPermissions(new String[] { permission }, requestCode);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(String permission) {
        return activity.shouldShowRequestPermissionRationale(permission);
    }
}
