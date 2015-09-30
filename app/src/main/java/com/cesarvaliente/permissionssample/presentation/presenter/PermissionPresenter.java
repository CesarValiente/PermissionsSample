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
package com.cesarvaliente.permissionssample.presentation.presenter;

import android.Manifest;
import android.content.pm.PackageManager;

import com.cesarvaliente.permissionssample.action.PermissionAction;

/**
 * Created by Cesar on 15/09/15.
 */
public class PermissionPresenter {

    public static final int ACTION_READ_CONTACTS = 1;
    public static final int ACTION_SAVE_IMAGE = 2;
    public static final int ACTION_SEND_SMS = 3;

    private PermissionAction permissionAction;
    private PermissionCallbacks permissionCallbacks;

    public PermissionPresenter(PermissionAction permissionAction, PermissionCallbacks permissionCallbacks) {
        this.permissionAction = permissionAction;
        this.permissionCallbacks = permissionCallbacks;
    }

    public void requestReadContactsPermission(int action) {
        checkAndRequestPermission(action, Manifest.permission.READ_CONTACTS);
    }

    public void requestReadContactsPermissionAfterRationale(int action) {
        permissionAction.requestPermission(Manifest.permission.READ_CONTACTS, action);
    }

    public void requestWriteExternalStorangePermission(int action) {
        checkAndRequestPermission(action, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void requestWriteExternalStorangePermissionAfterRationale(int action) {
        permissionAction.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, action);
    }

    public void requestSendSMS(int action) {
        checkAndRequestPermission(action, Manifest.permission.SEND_SMS);
    }

    public void requestSendSMSAfterRationale(int action) {
        permissionAction.requestPermission(Manifest.permission.SEND_SMS, action);
    }

    private void checkAndRequestPermission(int action, String permission) {
        if (permissionAction.hasSelfPermission(permission)) {
            permissionCallbacks.permissionAccepted(action);
        } else {
            if (permissionAction.shouldShowRequestPermissionRationale(permission)) {
                permissionCallbacks.showRationale(action);
            } else {
                permissionAction.requestPermission(permission, action);
            }
        }
    }

    public boolean verifyGrantedPermission(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public interface PermissionCallbacks {
        void permissionAccepted(int action);

        void permissionDenied(int action);

        void showRationale(int action);
    }
}
