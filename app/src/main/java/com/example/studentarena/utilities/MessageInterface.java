package com.example.studentarena.utilities;
import com.example.studentarena.model.Message;
import com.parse.ParseException;

import java.util.List;

public interface MessageInterface {

    void getProcessFinish(List<Message> output);
    void postProcessFinish(ParseException e);

}