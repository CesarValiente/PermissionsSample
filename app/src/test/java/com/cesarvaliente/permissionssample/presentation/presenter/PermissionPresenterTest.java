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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cesarvaliente.permissionssample.action.PermissionAction;
import com.cesarvaliente.permissionssample.presentation.presenter.PermissionPresenter.PermissionCallbacks;

import static android.Manifest.permission.READ_CONTACTS;
import static com.cesarvaliente.permissionssample.presentation.presenter.PermissionPresenter.ACTION_READ_CONTACTS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Cesar on 17/09/15.
 */
@RunWith(JUnit4.class)
public class PermissionPresenterTest {
    PermissionPresenter permissionPresenter;
    @Mock
    PermissionAction permissionAction;
    @Mock
    PermissionCallbacks permissionCallbacks;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        permissionPresenter = new PermissionPresenter(permissionAction, permissionCallbacks);
    }

    @Test
    public void shouldCallAcceptedPermission() {
        when(permissionAction.hasSelfPermission(READ_CONTACTS)).thenReturn(true);

        permissionPresenter.requestReadContactsPermission(ACTION_READ_CONTACTS);
        verify(permissionCallbacks).permissionAccepted(ACTION_READ_CONTACTS);
    }

    @Test
    public void shouldCallShowRationale() {
        when(permissionAction.hasSelfPermission(READ_CONTACTS)).thenReturn(false);
        when(permissionAction.shouldShowRequestPermissionRationale(READ_CONTACTS)).thenReturn(true);

        permissionPresenter.requestReadContactsPermission(ACTION_READ_CONTACTS);
        verify(permissionCallbacks).showRationale(ACTION_READ_CONTACTS);
    }

    @Test
    public void shouldRequestPermission() {
        when(permissionAction.hasSelfPermission(READ_CONTACTS)).thenReturn(false);
        when(permissionAction.shouldShowRequestPermissionRationale(READ_CONTACTS)).thenReturn(false);

        permissionPresenter.requestReadContactsPermission(ACTION_READ_CONTACTS);
        verify(permissionAction).requestPermission(READ_CONTACTS, ACTION_READ_CONTACTS);
    }
}
