package com.xie.java.withannotation;

/**
 * Created by xieyang on 18/7/14.
 */
public class TopicTag {

    private String topic;
    private String tag;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return topic+":"+tag;
    }
}
