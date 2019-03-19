package com.ruoyi.wx.mp.config;

import lombok.Data;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;

@Data
public class WxConfigStorage extends WxMpInMemoryConfigStorage {
    protected volatile String webPath;
    protected volatile String thirdUrl;

    public String getWebPath() {
        return webPath;
    }

    public void setWebPath(String webPath) {
        this.webPath = webPath;
    }

    public String getThirdUrl() {
        return thirdUrl;
    }

    public void setThirdUrl(String thirdUrl) {
        this.thirdUrl = thirdUrl;
    }
}
