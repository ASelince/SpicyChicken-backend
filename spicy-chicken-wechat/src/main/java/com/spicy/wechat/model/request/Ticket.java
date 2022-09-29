package com.spicy.wechat.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    private String action_name;
    private Integer expire_seconds;
    private ActionInfo action_info;

    @Data
    public static class ActionInfo {

        private Scene scene;
    }

    @Data
    public static class Scene {

        private String scene_str;
        private Integer scene_id;
    }
}
