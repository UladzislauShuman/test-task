package com.example.java_ifortex_test_task.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DeviceTypeConverter implements AttributeConverter<DeviceType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DeviceType deviceType) {
        if (deviceType == null) {
            return null;
        }
        return deviceType.getCode();
    }

    @Override
    public DeviceType convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }
        return DeviceType.fromCode(code);
    }
}