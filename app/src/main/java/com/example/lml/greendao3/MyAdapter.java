package com.example.lml.greendao3;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lml.greendao3.Greendao.ArticleDao;

/**
 * Created by 小莫 on 2016/8/4.
 *
 * 数据库 && ListView 的适配器 —— （ImageView的数据是以byte[]形式存在数据库中的，取出时需要注意！）
 *
 * 这部分代码参考了Stackoverflow上的回答，在此贴出地址：http://stackoverflow.com/questions/6710565/images-in-simplecursoradapter
 *
 */

public class MyAdapter extends SimpleCursorAdapter{

    private Cursor cursor;
    private Context context;

    public MyAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
        this.cursor = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.layout_item, null);
        }
        cursor.moveToPosition(position);

        String title = cursor.getString(cursor.getColumnIndex(ArticleDao.Properties.Title.columnName));
        String subtitle = cursor.getString(cursor.getColumnIndex(ArticleDao.Properties.Content.columnName));
        String date = cursor.getString(cursor.getColumnIndex(ArticleDao.Properties.Date.columnName));
        byte[] icon = cursor.getBlob(cursor.getColumnIndexOrThrow(ArticleDao.Properties.Icon.columnName));

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_1);
        if (icon != null){
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(icon,0,icon.length));
        }else {
            imageView.setImageResource(R.drawable.ic_android_black_24dp);
        }

        TextView titleText = (TextView) view.findViewById(R.id.tv_title);
        titleText.setText(title);

        TextView contentText = (TextView) view.findViewById(R.id.tv_content);
        contentText.setText(subtitle);

        TextView dateText = (TextView)view.findViewById(R.id.tv_date);
        dateText.setText(date);

        return view;
    }
}
