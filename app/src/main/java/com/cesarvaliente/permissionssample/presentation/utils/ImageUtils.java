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
package com.cesarvaliente.permissionssample.presentation.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.cesarvaliente.permissionssample.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Cesar on 10/09/15.
 */
public class ImageUtils {

    public static void loadImage(Context context, Uri uri, ImageView imageView, int width, int height) {
        Picasso.with(context).load(uri).placeholder(R.mipmap.ic_launcher).resize(width, height).into(imageView);
    }

}
