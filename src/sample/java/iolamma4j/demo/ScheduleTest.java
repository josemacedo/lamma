package iolamma4j.demo;

import com.google.common.collect.Lists;
import io.lamma.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * this class covers all java code used in Tutorial 3: Advanced Schedule Generation
 */
public class ScheduleTest {

    @Test
    public void testTwoDatesSchedule() {
        List<Date> expectedCouponDates = Lists.newArrayList(
                Dates.newDate(2015, 6, 30),
                Dates.newDate(2015, 12, 31),
                Dates.newDate(2016, 6, 30),
                Dates.newDate(2016, 12, 30));
        List<Date> expectedSettlementDates = Lists.newArrayList(
                Dates.newDate(2015, 7, 2),
                Dates.newDate(2016, 1, 4),
                Dates.newDate(2016, 7, 4),
                Dates.newDate(2017, 1, 3));

        DateDef couponDate = DateDefs.of("CouponDate", Anchors.periodEnd(), Selectors.modifiedFollowing(HolidayRules.weekends()));
        DateDef settlementDate = DateDefs.of("SettlementDate", Anchors.otherDate("CouponDate"), Shifters.byWorkingDays(2, HolidayRules.weekends()));

        Schedule4j result = Schedule4j.schedule(
                Dates.newDate(2015, 1, 1),
                Dates.newDate(2016, 12, 31),
                Patterns.monthly(6, DayOfMonths.lastDay()),
                Lists.newArrayList(couponDate, settlementDate));

        assertEquals(result.get("CouponDate"), expectedCouponDates);
        assertEquals(result.get("SettlementDate"), expectedSettlementDates);
    }

    @Test
    public void testDefaultStubHandling() {
        List<Date> expectedCouponDates = Lists.newArrayList(
                Dates.newDate(2015, 6, 30),
                Dates.newDate(2015, 12, 31),
                Dates.newDate(2016, 6, 30),
                Dates.newDate(2016, 12, 30),
                Dates.newDate(2017, 1, 31));
                
        DateDef couponDate = DateDefs.of("CouponDate", Anchors.periodEnd(), Selectors.modifiedFollowing(HolidayRules.weekends()));
        Schedule4j result = Schedule4j.schedule(
                Dates.newDate(2015, 1, 1),
                Dates.newDate(2017, 1, 31),
                Patterns.monthly(6, DayOfMonths.lastDay()),
                Lists.newArrayList(couponDate));

        assertEquals(result.get("CouponDate"), expectedCouponDates);
    }

    @Test
    public void testStubRules() {
        List<Date> expectedCouponDates = Lists.newArrayList(
                Dates.newDate(2015, 6, 30),
                Dates.newDate(2015, 12, 31),
                Dates.newDate(2016, 6, 30),
                Dates.newDate(2017, 1, 31));

        DateDef couponDate = DateDefs.of("CouponDate", Anchors.periodEnd(), Selectors.modifiedFollowing(HolidayRules.weekends()));
        Schedule4j result = Schedule4j.schedule(
                Dates.newDate(2015, 1, 1),
                Dates.newDate(2017, 1, 31),
                Patterns.monthly(6, DayOfMonths.lastDay()),
                StubRulePeriodBuilders.of(StubRulePeriodBuilders.Rules.longEnd(270)),
                Lists.newArrayList(couponDate));

        assertEquals(result.get("CouponDate"), expectedCouponDates);
    }
}
