package com.yunkang.chat.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunkang.chat.R;
import com.yunkang.chat.home.model.Msg;
import com.yunkang.chat.mine.view.DeleteRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：凌涛 on 2019/1/21 16:37
 * 邮箱：771548229@qq..com
 */
public class MyMsgAdapter extends RecyclerView.Adapter<MyMsgAdapter.MyHolder> {
     Context mContext;
     List<Msg> list ;

    public MyMsgAdapter(Context context, List<Msg> list) {
        mContext = context;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(View.inflate(mContext, R.layout.item_home_msg, null));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        // holder.mText.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends DeleteRecyclerView.DeleteViewHolder {

        private TextView mText;

        public MyHolder(View itemView) {
            super(itemView);
           // mText = (TextView) itemView.findViewById(R.id.text);
        }
    }
}

