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
package com.cesarvaliente.permissionssample.action.usecase;

import java.io.IOException;

import android.net.Uri;

import com.cesarvaliente.permissionsample.domain.executor.PostExecutionThread;
import com.cesarvaliente.permissionsample.domain.executor.ThreadExecutor;
import com.cesarvaliente.permissionssample.action.ImageAction;

/**
 * Created by Cesar on 24/09/15.
 */
public class ImageUseCaseImpl implements ImageUseCase {

    private ImageAction imageAction;
    private ThreadExecutor threadExecutor;
    private PostExecutionThread postExecutionThread;
    private Callbacks callbacks;
    private Uri imageUri;
    private String contactName;

    public ImageUseCaseImpl(ImageAction imageAction, ThreadExecutor threadExecutor, PostExecutionThread
            postExecutionThread) {
        this.imageAction = imageAction;
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(Uri imageUri, String contactName, Callbacks callbacks) {
        this.imageUri = imageUri;
        this.contactName = contactName;
        this.callbacks = callbacks;
        threadExecutor.execute(this);
    }

    @Override
    public void run() {
        try {
            final boolean result = imageAction.saveImage(imageUri, contactName);
            postExecutionThread.post(new Runnable() {
                @Override
                public void run() {
                    callbacks.onComplete(result);
                }
            });
        } catch (final IOException e) {
            postExecutionThread.post(new Runnable() {
                @Override
                public void run() {
                    callbacks.onError(e.toString());
                }
            });
        }
    }
}
