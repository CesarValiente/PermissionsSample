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

import android.content.pm.PackageManager;

import com.cesarvaliente.permissionssample.action.PermissionAction;
import com.cesarvaliente.permissionssample.presentation.model.Action;

/**
 * Created by Cesar on 15/09/15.
 */
public class PermissionPresenter {

    private PermissionAction permissionAction;
    private PermissionCallbacks permissionCallbacks;

    public PermissionPresenter(PermissionAction permissionAction, PermissionCallbacks permissionCallbacks) {
        this.permissionAction = permissionAction;
        this.permissionCallbacks = permissionCallbacks;
    }

    public void requestReadContactsPermission() {
        checkAndRequestPermission(Action.READ_CONTACTS);
    }

    public void requestReadContactsPermissionAfterRationale() {
        requestPermission(Action.READ_CONTACTS);
    }

    public void requestWriteExternalStorangePermission() {
        checkAndRequestPermission(Action.SAVE_IMAGE);
    }

    public void requestWriteExternalStorangePermissionAfterRationale() {
        requestPermission(Action.SAVE_IMAGE);
    }

    public void requestSendSMS() {
        checkAndRequestPermission(Action.SEND_SMS);
    }

    public void requestSendSMSAfterRationale() {
        requestPermission(Action.SEND_SMS);
    }

    private void checkAndRequestPermission(Action action) {
        if (permissionAction.hasSelfPermission(action.getPermission())) {
            permissionCallbacks.permissionAccepted(action.getCode());
        } else {
            if (permissionAction.shouldShowRequestPermissionRationale(action.getPermission())) {
                permissionCallbacks.showRationale(action.getCode());
            } else {
                permissionAction.requestPermission(action.getPermission(), action.getCode());
            }
        }
    }

    private void requestPermission(Action action) {
        permissionAction.requestPermission(action.getPermission(), action.getCode());
    }

    public void checkGrantedPermission(int[] grantResults, int requestCode) {
        if (verifyGrantedPermission(grantResults)) {
            permissionCallbacks.permissionAccepted(requestCode);
        } else {
            permissionCallbacks.permissionDenied(requestCode);
        }
    }

    private boolean verifyGrantedPermission(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public interface PermissionCallbacks {
        void permissionAccepted(@Action.ActionCode int actionCode);

        void permissionDenied(@Action.ActionCode int actionCode);

        void showRationale(@Action.ActionCode int actionCode);
    }
}
