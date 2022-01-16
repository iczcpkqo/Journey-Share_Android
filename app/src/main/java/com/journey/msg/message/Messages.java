package com.journey.msg.message;

import com.journey.entity.Message;
import com.journey.entity.User;

import java.util.ArrayList;
import java.util.Date;

public class Messages implements MessageContainer {
    private final ArrayList<Message> msgBox = new ArrayList<Message>();
    private String hostUserName;
    private String guestUserName;

    public Messages(User user) {
        setHostUserName(user.getUsername());

        //获取两人之间
        // ...

        //聊天消息写死
        this.msgBox.add(new Message(
                new User("test_name_111", "1231231", new Date()),
                new User("test_name_222", "1231231", new Date()),
            "sdfsdfsdf", new Date() ));

        this.msgBox.add(new Message(
                new User("test_name_222", "1231231", new Date()),
                new User("test_name_111", "1231231", new Date()),
                "222222", new Date() ));
    }

    public void setHostUserName(String name){ this.hostUserName = name; }

    public String getHostUserName(){ return this.hostUserName; }

    @Override
    public MsgIter getIterator(){
        return new MsgIter(this.msgBox);
    }

    private class MsgIter implements MessageIterator {
        private int index;
        private final ArrayList<Message> msgBox;

        public MsgIter(ArrayList<Message> msgBox) {
            this.index = 0;
            this.msgBox = msgBox;
        }

        @Override
        public boolean hasNext(){ return index < this.msgBox.size(); }

        @Override
        public Message next(){
            return hasNext() ? this.msgBox.get(index++) : null;
        }
    }
}