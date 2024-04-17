package org.example.utils.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

public class GenderConverter implements Converter<Integer> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 这里读的时候会调用，将Excel中的字段汉字转换成Java的Integer对象
     *
     * @param context context
     * @return Java中的Integer对象
     */
    @Override
    public Integer convertToJavaData(ReadConverterContext<?> context) {
        return context.getReadCellData().getStringValue().equals("男") ? 0 : 1;
    }

    /**
     * 这里是写的时候会调用，将Java的Integer对象转换成Excel中的字符串
     *
     * @return Excel中要存储的字符串
     */
    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) {
        String gender = context.getValue() == 0 ? "男" : "女";
        return new WriteCellData<String>(gender);
    }
}
