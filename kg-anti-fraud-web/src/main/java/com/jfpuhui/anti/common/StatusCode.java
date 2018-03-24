package com.jfpuhui.anti.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 状态码规范
 *
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-21-9:32
 */
public interface StatusCode {

    //2点和边相关 // 0-超限 1-未匹配 // 0-点 1-边 // 0,1,2对应度
    public final static Status TOO_MANY_CORE_NODES = new Status(2000,"核心节点数量超限");
    public final static Status TOO_MANY_1D_NODES = new Status(2001,"一度节点数量超限");
    public final static Status TOO_MANY_2D_NODES = new Status(2002,"二度节点数量超限");
    public final static Status TOO_MANY_EDGES = new Status(2010,"边数量超限");
    public final static Status TOO_MANY_1D_EDGES = new Status(2011,"一度边数量超限");
    public final static Status TOO_MANY_2D_EDGES = new Status(2012,"二度边数量超限");

    public final static Status NO_MATCHED_CORE_NODES = new Status(2100,"未匹配到任何核心节点");
    public final static Status NO_MATCHED_CORE_EDGES = new Status(2110,"未匹配到任何核心边");

    public final static Status NO_MATCHES = new Status(4000,"未匹配到任何数据");



    class Status implements Serializable{
        private Integer code;
        private String msg;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Status(Integer code, String msg) {
            this.code = code;
            this.msg = msg;

        }
    }


}
