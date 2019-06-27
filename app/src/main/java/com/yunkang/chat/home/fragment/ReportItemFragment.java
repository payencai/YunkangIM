package com.yunkang.chat.home.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunkang.chat.R;
import com.yunkang.chat.home.adapter.OpenTicketsAdapter;
import com.yunkang.chat.home.model.OpenTickets;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportItemFragment extends Fragment {
    OpenTicketsAdapter mOpenTicketsAdapter;
    List<OpenTickets> mOpenTickets;

    @BindView(R.id.rv_shopitem)
    RecyclerView rv_checkitem;
    @BindView(R.id.tv_open)
    TextView tv_open;
    public ReportItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_item, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
      //  return inflater.inflate(R.layout.fragment_shop_item, container, false);
    }
    private void initView() {
        tv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getContext(), TicketsActivity.class));
            }
        });
        mOpenTickets = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            OpenTickets openTickets = new OpenTickets();
            if(i==0)
                openTickets.setCheck(true);
            mOpenTickets.add(openTickets);
        }
        mOpenTicketsAdapter = new OpenTicketsAdapter(R.layout.item_home_opentickets, mOpenTickets);
        rv_checkitem.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_checkitem.setAdapter(mOpenTicketsAdapter);
    }
}
