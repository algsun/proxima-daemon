package com.microwise.proxima.bean;

import com.microwise.proxima.bean.base.NumberEnum;

/**
 * 枚举星期几或者全部
 *
 * @author gaohui
 * @date 2012-7-31
 */
public enum DayType implements NumberEnum {
    /**
     * 星期一 MONDAY
     */
    MON(1),
    /**
     * 星期二 TUESDAY
     */
    TUES(2),
    /**
     * 星期三 WEDNESDAY
     */
    WED(3),
    /**
     * 星期四 THURSDAY
     */
    THURS(4),
    /**
     * 星期五 FRIDAY
     */
    FRI(5),
    /**
     * 星期六 SATURDAY
     */
    SAT(6),
    /**
     * 星期天 SUNDAY
     */
    SUN(7),
    /**
     * 全部(包括所有七天)
     */
    ALL(8);

    private int number;

    private DayType(int number) {
        this.number = number;
    }

    @Override
    public int number() {
        return number;
    }

}
