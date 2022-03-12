package com.journey.msg.dialogue;

import com.journey.msg.base.Container;

public interface DialogueContainer extends Container {
    DialogueIterator getIterator();
}
