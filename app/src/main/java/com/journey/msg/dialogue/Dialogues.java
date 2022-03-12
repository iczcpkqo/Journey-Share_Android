package com.journey.msg.dialogue;

import com.journey.entity.Dialogue;
import com.journey.entity.User;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class Dialogues implements DialogueContainer {
    private final ArrayList<Dialogue> diaBox = new ArrayList<Dialogue>();
    private String hostUserName;

    public Dialogues(User user) throws ParseException {
        setHostUserName(user.getUsername());

        //获取用户的所有聊天对象
        // ...

        //聊天对象先写死
        this.diaBox.add(new Dialogue(new User("test_name_111", "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0),
                new User("test_name_222", "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0)));

        this.diaBox.add(new Dialogue(new User("test_name_333", "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0,0),
                new User("test_name_444", "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0)));

        this.diaBox.add(new Dialogue(new User("test_name_555", "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0),
                new User("test_name_6666", "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0)));

        this.diaBox.add(new Dialogue(new User("test_name_777", "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0),
                new User("test_name_888", "11111", new Date(), "1994", "male", "13555555", "xiang.mao@outlook.com", 0.0, 0)));
    }

    public void setHostUserName(String name){ this.hostUserName = name; }

    public String getHostUserName(){ return this.hostUserName; }

    @Override
    public DialogueIterator getIterator(){
        return new DiaIter(this.diaBox);
    }

    private class DiaIter implements DialogueIterator {
        private int index;
        private final ArrayList<Dialogue> diaBox;

        public DiaIter(ArrayList<Dialogue> diaBox) {
            this.index = 0;
            this.diaBox = diaBox;
        }

        @Override
        public boolean hasNext(){ return index < this.diaBox.size(); }

        @Override
        public Dialogue next(){
            return hasNext() ? this.diaBox.get(index++) : null;
        }
    }
}
