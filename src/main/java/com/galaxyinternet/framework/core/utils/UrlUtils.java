package com.galaxyinternet.framework.core.utils;

import com.galaxyinternet.framework.core.constants.Constants;

import java.util.Properties;

public class UrlUtils {

    static Properties property = PropertiesUtils.getProperties(Constants.IDENTY_URL_FILE);

    /**
     *
     * @param urlId 从Constants中取值,三类模板MAIL_URGE_CONTENT,MAIL_RESTPWD_CONTENT,MAIL_FILESHARE_CONTENT
     * @return
     */
    public static String getUrl(String urlId) {
        return property.getProperty(urlId);
    }
}

