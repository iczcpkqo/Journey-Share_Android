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
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogueFragment# newInstance} factory method to
 * create an instance of this fragment.
 */

public class DialogueFragment extends Fragment {

    List<Dialogue> dialogueList = new ArrayList<>();
    RecyclerView dialogueRecycler;
    DialogueAdapter adapter;

    // test
    List<diaTest> diattt = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View diaFrame = inflater.inflate(R.layout.fragment_dialogue, container, false);

        // set Title
//        setHasOptionsMenu(true);
//        getActivity().setTitle("聊天");

        try {
            initDialogueTestData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dialogueRecycler = (RecyclerView) diaFrame.findViewById(R.id.dialogue_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dialogueRecycler.setLayoutManager(layoutManager);

        adapter = new DialogueAdapter(dialogueList, getActivity());
        dialogueRecycler.setAdapter(adapter);

        return diaFrame;
    }


    // set the style for Chat actionBar
    private void setChatBar(String tit) {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle(tit);
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);//display the back arrow icon
    }

    public void initDialogueTestData() throws ParseException {
        for (int i=0; i<10; i++) {
            dialogueList.add(new Dialogue(
                    new User("test_Sender" + i, "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0),
                    new User("test_Receiver" + i, "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0)
            ));
        }
    }

//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public DialogueFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment Dialogue.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static DialogueFragment newInstance(String param1, String param2) {
//        DialogueFragment fragment = new DialogueFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

}