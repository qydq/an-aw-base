package com.an.base.model;

import java.io.Serializable;

/**
 * Created by stary on 2016/11/26.
 * 莳萝花，晴雨荡气，sunshuntao，qydq
 * Contact : qyddai@gmail.com
 * 说明：Model的参考类。
 * 最后修改：on 2016/11/26.
 */

public class ResponseBaseModel implements Serializable{
    private String ret;
    private String body;
    private boolean hasmore;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isHasmore() {
        return hasmore;
    }

    public void setHasmore(boolean hasmore) {
        this.hasmore = hasmore;
    }
}
