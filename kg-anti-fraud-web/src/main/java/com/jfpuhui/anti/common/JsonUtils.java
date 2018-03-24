package com.jfpuhui.anti.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-21-14:45
 */
public class JsonUtils {


    /**object 转成 json
     * @param object
     * @return
     */
    public static String object2Json(Object object) {
        try {
            ObjectMapper om = new ObjectMapper();
            //不含null的属性, i.e.忽略null的属性, !注意: 特地设置null值后, 会被解析!
            om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            //解决No serializer found for class...and no properties discovered to create BeanSerializer
            om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            StringWriter w = new StringWriter();
            om.writeValue(w, object);
            return w.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
