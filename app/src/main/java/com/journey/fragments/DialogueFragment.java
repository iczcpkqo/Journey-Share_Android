package com.journey.fragments;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journey.R;
import com.journey.adapter.DialogueAdapter;
import com.journey.entity.Dialogue;
import com.journey.entity.User;
import com.journey.entity.diaTest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Xiang Mao
 * @date: 2022-03-26-04:00
 * @tag: Dialogue
 */
public class DialogueFragment extends Fragment {

    List<Dialogue> dialogueList = new ArrayList<>();
    RecyclerView dialogueRecycler;
    DialogueAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View diaFrame = inflater.inflate(R.layout.fragment_dialogue, container, false);
        // TODO: 获取当前用户信息
        // TODO: 系统消息
        // TODO: 移除测试数据
        try {
            initDialogueTestData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // TODO: 刷新会话列表
        // TODO: 新消息标记

        dialogueRecycler = (RecyclerView) diaFrame.findViewById(R.id.dialogue_recycler_view);
        adapter = new DialogueAdapter(dialogueList, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        dialogueRecycler.setLayoutManager(layoutManager);
        dialogueRecycler.setAdapter(adapter);

        return diaFrame;
    }

    public void initDialogueTestData() throws ParseException {
        for (int i=0; i<10; i++) {
            dialogueList.add(new Dialogue(
                    new User("test_Sender" + i, "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0),
                    new User("test_Receiver" + i, "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0)
            ));
        }
    }

}