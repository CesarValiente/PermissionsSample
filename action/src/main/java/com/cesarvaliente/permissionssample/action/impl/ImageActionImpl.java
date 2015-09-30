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
package com.cesarvaliente.permissionssample.action.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;

import com.cesarvaliente.permissionssample.action.ImageAction;


/**
 * Created by Cesar on 24/09/15.
 */
public class ImageActionImpl implements ImageAction {

    private Context context;

    public ImageActionImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean saveImage(Uri uri, String contactName) throws IOException {
        String root = Environment.getExternalStorageDirectory().toString();
        File permissionsFolder = new File(root + "/PermissionsSample");
        permissionsFolder.mkdirs();
        String fileName = contactName + ".jpg";
        File file = new File(permissionsFolder, fileName);
        if (file.exists()) {
            file.delete();
        }

        Bitmap bitmap = getBitmap(context, uri);
        if (bitmap != null) {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return true;
        }
        return false;
    }

    public Bitmap getBitmap(Context context, Uri uri) throws IOException {
        InputStream stream = ContactsContract.Contacts.openContactPhotoInputStream(
                context.getContentResolver(), uri);
        return BitmapFactory.decodeStream(stream);
    }
}
