package com.jfpuhui.anti.common;

import com.jfpuhui.anti.exception.CustomException;
import org.apache.commons.lang.StringUtils;

public interface Profile {


    //客户类型和客户类型代码映射关系
    enum CustType {
        WHITE(0), GRAY(1), BLACK(2);

        private Integer code;

        CustType(Integer code) {
            this.code = code;
        }

        /**
         * 还有'白'字, 则匹配为 白客户 code 为0,...
         *
         * @param desc
         * @return
         */
        public static Integer matchDescToCode(String desc) throws CustomException {
            Integer retCode;
            if (desc.contains("白")) {
                retCode = 0;
            } else if (desc.contains("灰")) {
                retCode = 1;
            } else if (desc.contains("黑")) {
                retCode = 2;
            } else {
                throw new CustomException("请选择正确的[客户类型]");
            }

            return retCode;
        }

    }


    enum EdgeType {
        /**
         * 通话记录
         */
        CALL_HISTORY(1)/**
         * 紧急联系人
         */
        , EMERGENCY(2)/**
         * 设备
         */
        , DEVICE(3)/**信用卡
         * 银行卡, 借记卡,
         */
        , BANK_CARD(4);

        private Integer code;

        EdgeType(Integer code) {
            this.code = code;
        }

        public static Integer matchDescToCode(String desc) throws CustomException {
            Integer retCode;
            desc = StringUtils.trim(desc);
            if (desc.contains("通话记录")) {
                retCode = 1;
            } else if (desc.contains("电话")) {
                retCode = 2;
            } else if (desc.contains("设备")) {
                retCode = 3;
            } else if (desc.contains("银行卡")) {
                retCode = 4;
            } else {
                throw new CustomException("请选择正确的[边类型]");
            }

            return retCode;
        }


    }


}

