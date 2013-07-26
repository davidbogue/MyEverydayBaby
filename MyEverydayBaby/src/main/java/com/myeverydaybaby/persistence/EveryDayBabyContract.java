package com.myeverydaybaby.persistence;

import android.provider.BaseColumns;

public final class EveryDayBabyContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_BABIES =
            "CREATE TABLE " + Babies.TABLE_NAME + " (" +
                    Babies._ID + " INTEGER PRIMARY KEY," +
                    Babies.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    Babies.COLUMN_NAME_BIRTHDAY + INTEGER_TYPE + COMMA_SEP +
                    Babies.COLUMN_NAME_PICTURE + TEXT_TYPE +
            " )";

    public static final String SQL_CREATE_FEEDINGS =
            "CREATE TABLE " + Feedings.TABLE_NAME + " (" +
                    Feedings._ID + " INTEGER PRIMARY KEY," +
                    Feedings.COLUMN_NAME_BABY_ID + TEXT_TYPE + COMMA_SEP +
                    Feedings.COLUMN_NAME_FEEDING_TYPE + INTEGER_TYPE + COMMA_SEP +
                    Feedings.COLUMN_NAME_BOOB + INTEGER_TYPE + COMMA_SEP +
                    Feedings.COLUMN_NAME_AMOUNT + REAL_TYPE + COMMA_SEP +
                    Feedings.COLUMN_NAME_START_TIME + INTEGER_TYPE + COMMA_SEP +
                    Feedings.COLUMN_NAME_END_TIME + INTEGER_TYPE +
                    " )";

    public static final String SQL_CREATE_DIAPERS =
            "CREATE TABLE " + Diapers.TABLE_NAME + " (" +
                    Diapers._ID + " INTEGER PRIMARY KEY," +
                    Diapers.COLUMN_NAME_BABY_ID + TEXT_TYPE + COMMA_SEP +
                    Diapers.COLUMN_NAME_TIME + INTEGER_TYPE + COMMA_SEP +
                    Diapers.COLUMN_NAME_TYPE+ INTEGER_TYPE + COMMA_SEP +
                    Diapers.COLUMN_NAME_CONSISTENCY+ INTEGER_TYPE + COMMA_SEP +
                    Diapers.COLUMN_NAME_COLOR + TEXT_TYPE +
                    " )";

    public static final String SQL_CREATE_SLEEP =
            "CREATE TABLE " + Sleep.TABLE_NAME + " (" +
                    Sleep._ID + " INTEGER PRIMARY KEY," +
                    Sleep.COLUMN_NAME_BABY_ID + TEXT_TYPE + COMMA_SEP +
                    Sleep.COLUMN_NAME_START_TIME + INTEGER_TYPE + COMMA_SEP +
                    Sleep.COLUMN_NAME_END_TIME+ INTEGER_TYPE +
                    " )";

    public static final String SQL_CREATE_STATISTICS =
            "CREATE TABLE " + Statistics.TABLE_NAME + " (" +
                    Statistics._ID + " INTEGER PRIMARY KEY," +
                    Statistics.COLUMN_NAME_BABY_ID + TEXT_TYPE + COMMA_SEP +
                    Statistics.COLUMN_NAME_DATE + INTEGER_TYPE + COMMA_SEP +
                    Statistics.COLUMN_NAME_LENGTH + REAL_TYPE + COMMA_SEP +
                    Statistics.COLUMN_NAME_WEIGHT + REAL_TYPE + COMMA_SEP +
                    Statistics.COLUMN_NAME_HEAD + REAL_TYPE +
                    " )";

    // To prevent someone from accidentally instantiating the contract class, give it an empty constructor.
    public EveryDayBabyContract() {}

    /* Inner class that defines the table contents */
    public static abstract class Babies implements BaseColumns {
        public static final String TABLE_NAME = "babies";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_BIRTHDAY = "birthday";
        public static final String COLUMN_NAME_PICTURE = "picture";
    }

    /* Inner class that defines the table contents */
    public static abstract class Feedings implements BaseColumns {
        public static final String TABLE_NAME = "feedings";
        public static final String COLUMN_NAME_BABY_ID = "baby_id";
        public static final String COLUMN_NAME_FEEDING_TYPE = "feeding_type"; // breast or bottle
        public static final String COLUMN_NAME_BOOB = "boob"; // right or left .. only for breast
        public static final String COLUMN_NAME_AMOUNT = "amount"; // only for bottle
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_END_TIME = "end_time";

        public static final int FEEDING_TYPE_BREAST = 1;
        public static final int FEEDING_TYPE_BOTTLE = 2;
        public static final int BOOB_LEFT= 1;
        public static final int BOOB_RIGHYT= 2;
    }

    /* Inner class that defines the table contents */
    public static abstract class Diapers implements BaseColumns {
        public static final String TABLE_NAME = "diapers";
        public static final String COLUMN_NAME_BABY_ID = "baby_id";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_TYPE = "type"; // pee, poop, or both
        public static final String COLUMN_NAME_CONSISTENCY = "consistency";
        public static final String COLUMN_NAME_COLOR = "color";

        public static final int PEE_DIAPER_TYPE = 1;
        public static final int POOP_DIAPER_TYPE = 2;
        public static final int BOTH_DIAPER_TYPE = 3;

        public static final int STOOL_CONSISTENCY_ONE = 1;
        public static final int STOOL_CONSISTENCY_TWO = 2;
        public static final int STOOL_CONSISTENCY_THREE = 3;
        public static final int STOOL_CONSISTENCY_FOUR = 4;
        public static final int STOOL_CONSISTENCY_FIVE= 5;

    }

    /* Inner class that defines the table contents */
    public static abstract class Sleep implements BaseColumns {
        public static final String TABLE_NAME = "sleep";
        public static final String COLUMN_NAME_BABY_ID = "baby_id";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_END_TIME = "end_time";
    }

    /* Inner class that defines the table contents */
    public static abstract class Statistics implements BaseColumns {
        public static final String TABLE_NAME = "statistics";
        public static final String COLUMN_NAME_BABY_ID = "baby_id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_LENGTH = "length";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_HEAD = "head";
    }
}
