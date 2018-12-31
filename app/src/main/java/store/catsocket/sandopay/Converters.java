package store.catsocket.sandopay;


import java.util.Date;

import androidx.room.TypeConverter;

/*Converter for date related information for BankAccountDataBase*/

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}

