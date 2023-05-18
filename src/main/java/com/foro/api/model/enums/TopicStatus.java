package com.foro.api.model.enums;

public enum TopicStatus {

    NOT_ANSWERED,
    NOT_SOLVED,
    SOLVED,
    CLOSED;

    public static boolean isValidStatus(String status){
        for (TopicStatus ts: TopicStatus.values()){
            if (ts.name().equals(status)){
                return true;
            }
        }
        return false;
    }

}
