package com.example.testapplication.main_screen.list_components;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testapplication.R;
import com.example.testapplication.models.RepoInfo;
import com.squareup.picasso.Picasso;

/**
 * Created by vlad on 11/30/16.
 */

public class ListItem extends CardView{
    private ImageView avatarImage;
    private TextView nameText;
    private TextView descriptionText;
    private TextView ownerLoginText;
    private TextView languageText;

    public ListItem(Context context) {
        super(context);
    }

    public ListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        nameText = (TextView)findViewById(R.id.nameText);
        descriptionText = (TextView)findViewById(R.id.descriptionText);
        ownerLoginText = (TextView)findViewById(R.id.ownerLoginText);
        languageText = (TextView)findViewById(R.id.languageText);
    }

    public void setData(RepoInfo repoInfo){
        nameText.setText(repoInfo.getName());
        descriptionText.setText(repoInfo.getDescription());
        ownerLoginText.setText(repoInfo.getOwner().getLogin());
        languageText.setText(repoInfo.getLanguage());

        Picasso.with(getContext())
                .load(repoInfo.getOwner().getAvatarPath())
                .placeholder(R.drawable.avatar_placeholder)
                .into(avatarImage);
    }
}
