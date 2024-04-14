package org.example.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class ServiceImplUtil {
    public static void shortContent(JSONArray ops, StringBuilder previewText, Consumer<Object> imageHandler){
        for (Object op : ops) {
            Object insert = JSONObject.from(op).get("insert");
            if (insert instanceof String text){
                if (previewText.length() >= ConstUtil.TASK_PREVIEW_CONTENT_LENGTH) continue;      //300
                previewText.append(text);
            }else if (insert instanceof Map<?, ?> map){
                Optional.ofNullable(map.get("image"))
                        .ifPresent(imageHandler);
            }
        }
    }
    public static <T> T setTextAndImages(T target, JSONArray ops){
        List<String> images = new ArrayList<>();
        StringBuilder text = new StringBuilder();
        ServiceImplUtil.shortContent(ops, text, obj -> images.add(obj.toString()));
        TextAndImages tai = new TextAndImages();
        tai.setText(text.length() > ConstUtil.TASK_PREVIEW_CONTENT_LENGTH
                ? text.substring(0, ConstUtil.TASK_PREVIEW_CONTENT_LENGTH)
                : text.toString());
        tai.setImages(images);
        BeanUtils.copyProperties(tai, target);
        return target;
    }
    @Data
    public static class TextAndImages{
        String text;
        List<String> images;
    }

    public static boolean textLimitCheck(JSONObject object, int max){
        if (object == null) return true;
        long length = 0;
        for (Object op : object.getJSONArray("ops")){
            length += JSONObject.from(op).getString("insert").length();
            if (length > max) return true;
        }
        return false;
    }
}
