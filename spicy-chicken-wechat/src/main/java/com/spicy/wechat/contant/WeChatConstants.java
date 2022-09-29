package com.spicy.wechat.contant;

public class WeChatConstants {

    /**
     * QrCode Type Temporary Or Permanent
     */
    public static class QrCodeType {

        /**
         * Temporary integer parameter value
         */
        public static final String QR_SCENE = "QR_SCENE";

        /**
         * Temporary string parameter value
         */
        public static final String QR_STR_SCENE = "QR_STR_SCENE";

        /**
         * Permanent integer parameter value
         */
        public static final String QR_LIMIT_SCENE = "QR_LIMIT_SCENE";

        /**
         * Persistent string parameter value
         */
        public static final String QR_LIMIT_STR_SCENE = "QR_LIMIT_STR_SCENE";
    }

    /**
     * WeChat Scanning code CallBack
     */
    public static class CALL_BACK_TYPE {

        /**
         * Scan Code
         */
        public static final String SCAN = "SCAN";

        /**
         * Subscribe WeChat Accounts
         */
        public static final String SUBSCRIBE = "subscribe";

        /**
         * Unsubscribe WeChat Accounts
         */
        public static final String UN_SUBSCRIBE = "unsubscribe";
    }

    /**
     * Some WeChat Result KeyWord
     */
    public static class WeChatKeyWord {

        /**
         * WeChat only identifier
         */
        public static final String OPEN_ID = "openId";

        /**
         * EventKey prefix
         */
        public static final String EVENT_KEY_PREFIX = "qrscene_";
    }

    /**
     * Sync Something KeyWord
     */
    public static class Sync {

        /**
         * When SysUser does not exist
         */
        public static final String CREATE_USER = "ScanAutoCreated";
    }
}
