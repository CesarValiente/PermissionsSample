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
package com.cesarvaliente.permissionssample.presentation.view.contactlist.recyclerview;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.cesarvaliente.permissionssample.R;
import com.cesarvaliente.permissionssample.presentation.model.ContactModel;
import com.cesarvaliente.permissionssample.presentation.utils.ImageUtils;

/**
 * Created by Cesar on 06/09/15.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ContactViewHolder> {

    private Context context;
    private List<ContactModel> contactsList;

    public Adapter(Context context, List<ContactModel> contactsList) {
        this.context = context;
        this.contactsList = contactsList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_cardview, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        ContactModel contact = contactsList.get(position);
        holder.name.setText(contact.getName());
        holder.email.setText(contact.getEmail());
        holder.phone.setText(contact.getPhone());
        int width = context.getResources().getDimensionPixelOffset(R.dimen.contacts_photo_width);
        int height = context.getResources().getDimensionPixelOffset(R.dimen.contacts_photo_height);
        ImageUtils.loadImage(context, contact.getImageUri(), holder.photo, width, height);
    }

    @Override
    public int getItemCount() {
        if (contactsList != null && !contactsList.isEmpty()) {
            return contactsList.size();
        } else {
            return 0;
        }
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.email)
        TextView email;
        @Bind(R.id.phone)
        TextView phone;
        @Bind(R.id.photo)
        ImageView photo;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setClickable(true);
        }
    }
}
