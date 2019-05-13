package cc.hisens.hardboiled.patient.websocket;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageModel {

    public long from;   //用户id
    public long to;   //给谁发送信息的对象id
    public byte type;   //
    public byte event;
    public byte accessory;
    public long time;    //时间
    public Content content;

    JSONObject messageObject = new JSONObject();

    public MessageModel() {

    }
    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
        try {
            messageObject.put("from", from);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
        try {
            messageObject.put("to", to);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
        try {
            messageObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public byte getEvent() {
        return event;
    }

    public void setEvent(byte event) {
        this.event = event;
        try {
            messageObject.put("event", event);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public byte getAccessory() {
        return accessory;
    }

    public void setAccessory(byte accessory) {
        this.accessory = accessory;
        try {
            messageObject.put("accessory", accessory);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
        try {
            messageObject.put("time", time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
        try {
            messageObject.putOpt("content", content.content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class Content {

        public String text;     //文字
        public String title;     //标题
        public String intro;     //介绍
        public String url;      //链接
        public String thumbUrl;   //缩略图
        private String voicePath; //语音消息，语音消息url
        private double voiceTime;   //语音时长

        public JSONObject  content = new JSONObject();

        public Content() {
            try {
                if(!TextUtils.isEmpty(text))
                content.put("text",text);
                if(!TextUtils.isEmpty(title))
                content.put("title",title);
                if(!TextUtils.isEmpty(intro))
                content.put("intro",intro);
                if(!TextUtils.isEmpty(url))
                content.put("url",url);
                if(!TextUtils.isEmpty(thumbUrl))
                content.put("thumb_url",thumbUrl);
                if(!TextUtils.isEmpty(voicePath))
                 content.put("voicePath",voicePath);
                if(!TextUtils.isEmpty(voiceTime+""))
                    content.put("voicePath",voicePath);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
            try {
                content.put("text", text);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
            try {
                content.put("title", title);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
            try {
                content.put("intro", intro);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
            try {
                content.put("url", url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {

            this.thumbUrl = thumbUrl;

            try {
                content.put("thumb_url", thumbUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public String toString() {

        try {
            messageObject.put("from", from);
            messageObject.put("to", to);
            messageObject.put("type", type);
            messageObject.put("event", event);
            messageObject.put("accessory", accessory);
            messageObject.put("time", time);

            if (content == null) {
                content = new Content();
            }
            messageObject.putOpt("content", content.content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return messageObject.toString();
    }
}
