/**
 * Copyright (C) 2015 Cesar Valiente based in the code of Fernando Cejas
 * https://github.com/android10/Android-CleanArchitecture
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
package com.cesarvaliente.permissionsample.domain.usecase.impl;

import java.util.List;

import com.cesarvaliente.permissionsample.domain.action.ListContactsAction;
import com.cesarvaliente.permissionsample.domain.action.ListContactsAction.DataCallback;
import com.cesarvaliente.permissionsample.domain.entity.ContactEntity;
import com.cesarvaliente.permissionsample.domain.executor.PostExecutionThread;
import com.cesarvaliente.permissionsample.domain.executor.ThreadExecutor;
import com.cesarvaliente.permissionsample.domain.usecase.ListContactsUseCase;

/**
 * Created by Cesar on 19/09/15.
 */
public class ListContactsUseCaseImpl implements ListContactsUseCase {

    private ListContactsAction listContactsAction;
    private ThreadExecutor threadExecutor;
    private PostExecutionThread postExecutionThread;
    private UiCallbacks uiUiCallbacks;
    private DataCallback dataCallback;

    public ListContactsUseCaseImpl(ListContactsAction listContactsAction, ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread) {
        this.listContactsAction = listContactsAction;
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(final UiCallbacks uiCallbacks) {
        this.uiUiCallbacks = uiCallbacks;

        dataCallback = new DataCallback() {
            @Override
            public void onDataLoaded(final List<ContactEntity> contactEntityList) {
                notifyUiOnSuccess(contactEntityList);
            }
        };
        threadExecutor.execute(this);
    }

    @Override
    public void run() {
        listContactsAction.getPhoneBookContacts(dataCallback);
    }

    private void notifyUiOnSuccess(final List<ContactEntity> contactEntityList) {
        postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                uiUiCallbacks.onComplete(contactEntityList);
            }
        });
    }
}
