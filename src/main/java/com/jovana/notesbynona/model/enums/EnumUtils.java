package com.jovana.notesbynona.model.enums;

public class EnumUtils {
    public static <T extends Enum<T>> boolean isInEnum(String value, Class<T> enumClass) {
        for (T enumValue : enumClass.getEnumConstants()) {
            if (enumValue.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
