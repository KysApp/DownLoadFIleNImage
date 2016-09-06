package com.example.download.how2use.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.download.how2use.ui.DownloadActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhl on 2016/8/30.
 */
public class MainAdapter extends BaseAdapter {
    private Context context;
    private List<String> datas = new ArrayList<>();
    private int datasCount = 0;

    public MainAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
        datasCount = datas.size();
    }

    @Override
    public int getCount() {
        return datasCount;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            String text = datas.get(position);
            viewHolder.textView.setText(text);
            convertView = viewHolder.textView;
            convertView.setOnClickListener(new Listeners(text));
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView textView;

        ViewHolder() {
            textView = new TextView(context);
            textView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setHeight(140);
            textView.setTextSize(24);
            textView.setGravity(Gravity.CENTER);
        }
    }

    private class Listeners implements View.OnClickListener {
        private String text;

        public Listeners() {
        }

        public Listeners( String text) {
            this.text = text;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DownloadActivity.class);
            intent.putExtra("type", text);
            context.startActivity(intent);
        }
    }
}
