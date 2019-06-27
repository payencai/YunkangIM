package com.yunkang.chat.mine.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunkang.chat.R;
import com.yunkang.chat.mine.adapter.OrderAdapter;
import com.yunkang.chat.mine.model.Order;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaitResultFragment extends Fragment {
    OrderAdapter orderAdapter;
    List<Order> mOrder;
    List<Order.OrderProductsBean> childList;
    @BindView(R.id.rv_order)
    RecyclerView rv_order;

    public WaitResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wait_result, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
       // return inflater.inflate(R.layout.fragment_wait_result, container, false);
    }
    private void initView() {
        mOrder = new ArrayList<>();
        childList = new ArrayList<>();

        orderAdapter = new OrderAdapter(R.layout.item_mine_order, mOrder);
        rv_order.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_order.setAdapter(orderAdapter);
    }
}
