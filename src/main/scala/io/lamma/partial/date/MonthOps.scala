package io.lamma.partial.date

import io.lamma._
import io.lamma.DayOfMonth._
import io.lamma.{JavaDateUtil, Month, DayOfMonth, Date}

import collection.JavaConverters._
import annotation.tailrec

private[lamma] trait MonthOps {
  this: Date =>

  lazy val month = Month.of(mm)

  /**
   * max day of this month, different month lengths and leap month are considered
   */
  lazy val maxDayOfMonth = JavaDateUtil.maxDayOfMonth(this)

  lazy val isLastDayOfMonth = dd == maxDayOfMonth

  lazy val dayOfMonth = dd

  lazy val quarter = month.quarter()

  /**
   * find the day of this month matching specified day-of-month. DSL can be used directly.
   *
   * Usage example:
   * {{{
   *    Date(2014, 5, 5).withDayOfMonth(lastDay)
   *    Date(2014, 5, 5).withDayOfMonth(3 rd Friday)
   *    Date(2014, 5, 5).withDayOfMonth(20 th day)
   * }}}
   */
  def withDayOfMonth(dom: DayOfMonth) = {
    val matched = daysOfMonth.filter(dom.isValidDOM)
    require(matched.size == 1, s"Invalid DayOfMonth: $dom. Matched dates: $matched")
    matched.head
  }

  /**
   * first day of current month
   */
  lazy val firstDayOfMonth = Date(yyyy, mm, 1)

  /**
   * last day of the month, different month end and leap month are handled properly
   */
  lazy val lastDayOfMonth = Date(yyyy, mm, maxDayOfMonth)

  /**
   * an iterable for every day in the month
   */
  lazy val daysOfMonth = firstDayOfMonth to lastDayOfMonth

  /**
   * <b>Java Friendly.</b> It is recommended to use [[daysOfMonth]] for Scala.
   *
   * an iterable for every day in the month
   */
  lazy val daysOfMonth4j = daysOfMonth.javaIterable

  /**
   * Every day in the same month with same dow <br>
   * eg, if this.dayOfWeek == Wednesday, then this is a list of all Wednesday in the same month
   */
  lazy val sameWeekdaysOfMonth = daysOfMonth.filter(_.dayOfWeek == dayOfWeek).toList

  /**
   * <b>Java Friendly.</b> It is recommended to use [[sameWeekdaysOfMonth]] for Scala.
   *
   * Every day in the same month with same dow <br>
   * eg, if this.dayOfWeek == Wednesday, then this is a list of all Wednesday in the same month
   */
  lazy val sameWeekdaysOfMonth4j = sameWeekdaysOfMonth.asJava

  /**
   * This method is an alias of [[nextOrSame(DayOfMonth)]] for Scala to prevent overloading when using DSL.
   *
   * Usage example:
   * {{{
   *   Date(2014, 5, 5).nextOrSameDayOfMonth(3 rd Friday)
   * }}}
   */
  def nextOrSameDayOfMonth(dom: DayOfMonth) = nextOrSame(dom)

  /**
   * <b>Java Friendly.</b> It is recommended to use [[nextOrSameDayOfMonth]] for Scala.
   *
   * coming day-of-month including this date
   */
  def nextOrSame(dom: DayOfMonth) = MonthOps.nextOrSame(this, dom)

  /**
   * This method is an alias of [[next(DayOfMonth)]] for Scala to prevent overloading when using DSL.
   *
   * Usage example:
   * {{{
   *   Date(2014, 5, 5).nextDayOfMonth(3 rd Friday)
   * }}}
   */
  def nextDayOfMonth(dom: DayOfMonth) = next(dom)

  /**
   * <b>Java Friendly.</b> It is recommended to use [[nextDayOfMonth]] for Scala.
   *
   * coming day-of-month excluding this date
   */
  def next(dom: DayOfMonth) = MonthOps.nextOrSame(this + 1, dom)

  /**
   * shorthand of comingDayOfMonth(LastDayOfMonth)<br>
   *   For example:
   *   {{{
   *   Date(2014, 7, 30).comingMonthEnd => Date(2014, 7, 31)
   *   Date(2014, 7, 31).comingMonthEnd => Date(2014, 8, 31)
   *   }}}
   *
   *   Note this is different from lastDayOfNextMonth
   */
  lazy val nextLastDayOfMonth = next(LastDayOfMonth)

  /**
   * shorthand of comingDayOfMonth(FirstDayOfMonth)<br>
   *   For example:
   *   {{{
   *   Date(2014, 7, 31).comingMonthBegin => Date(2014, 8, 1)
   *   Date(2014, 8,  1).comingMonthBegin => Date(2014, 9, 1)
   *   }}}
   */
  lazy val nextFirstDayOfMonth = next(FirstDayOfMonth)

  /**
   * This method is an alias of [[previousOrSame(DayOfMonth)]] for Scala to prevent overloading when using DSL.
   *
   * Usage example:
   * {{{
   *   Date(2014, 5, 5).previousOrSameDayOfMonth(3 rd Friday)
   * }}}
   */
  def previousOrSameDayOfMonth(dom: DayOfMonth) = previousOrSame(dom)

  /**
   * <b>Java Friendly.</b> It is recommended to use [[previousOrSameDayOfMonth]] for Scala.
   *
   * past day-of-month including this date
   */
  def previousOrSame(dom: DayOfMonth) = MonthOps.previousOrSame(this, dom)

  /**
   * This method is an alias of [[previous(DayOfMonth)]] for Scala to prevent overloading when using DSL.
   *
   * Usage example:
   * {{{
   *   Date(2014, 5, 5).previousDayOfMonth(3 rd Friday)
   * }}}
   */
  def previousDayOfMonth(dom: DayOfMonth) = previous(dom)

  /**
   * <b>Java Friendly.</b> It is recommended to use [[previousDayOfMonth]] for Scala.
   *
   * past day-of-month excluding this date
   */
  def previous(dom: DayOfMonth) = MonthOps.previousOrSame(this - 1, dom)

  /**
   * shorthand of pastDayOfMonth(LastDayOfMonth)<br>
   *  For example:<br>
   *    {{{
   *    Date(2014, 8, 5).pastMonthEnd => Date(2014, 7, 31)
   *    Date(2014, 7, 31).pastMonthEnd => Date(2014, 6, 30)
   *    }}}
   */
  lazy val previousLastDayOfMonth = previous(LastDayOfMonth)

  /**
   * shorthand of pastDayOfMonth(FirstDayOfMonth)<br>
   *  For example:<br>
   *    {{{
   *    Date(2014, 8, 2).pastMonthBegin => Date(2014, 8, 1)
   *    Date(2014, 8, 1).pastMonthBegin => Date(2014, 7, 1)
   *    }}}
   *
   *  Note this is different from firstDayOfPreviousMonth
   */
  lazy val previousFirstDayOfMonth = previous(FirstDayOfMonth)

  /**
   * day of next month. A shorthand of
   * {{{this + (1 month) dayOfMonth (dom)}}}
   */
  def dayOfNextMonth(dom: DayOfMonth) = this + (1 month) withDayOfMonth dom

  /**
   * shorthand of
   * {{{dayOfNextMonth(FirstDayOfMonth)}}}
   */
  lazy val firstDayOfNextMonth = dayOfNextMonth(FirstDayOfMonth)

  /**
   * shorthand of
   * {{{dayOfNextMonth(LastDayOfMonth)}}}
   */
  lazy val lastDayOfNextMonth = dayOfNextMonth(LastDayOfMonth)

  /**
   * find day-of-month for the previous month. A shorthand of
   * {{{this - (1 month) withDayOfMonth (dom)}}}
   */
  def dayOfPreviousMonth(dom: DayOfMonth) = this - (1 month) withDayOfMonth dom

  /**
   * shorthand of
   * {{{dayOfPreviousMonth(FirstDayOfMonth)}}}
   */
  lazy val firstDayOfPreviousMonth = dayOfPreviousMonth(FirstDayOfMonth)

  /**
   * shorthand of
   * {{{dayOfPreviousMonth(LastDayOfMonth)}}}
   */
  lazy val lastDayOfPreviousMonth = dayOfPreviousMonth(LastDayOfMonth)

  /**
   * <b>Java Friendly.</b> For Scala, it is recommended to use DSL with [[withDayOfMonth]] directly.
   *
   * Find nth occurrence of day-of-week in current month.
   *
   * For example:
   * {{{ new Date(2014, 5, 5).dayOfWeekInMonth(3, DayOfWeek.FRIDAY); }}}
   *
   * which is identical to
   * {{{ Date(2014, 5, 5).withDayOfMonth(3 rd Friday) }}}
   * in Scala
   *
   * @param n ordinal of the month, from 1 to 5
   * @param dow DayOfWeek
   * @return a new copy of the date
   */
  def dayOfWeekInMonth(n: Int, dow: DayOfWeek) = this.withDayOfMonth(n th dow)

  /**
   * <b>Java Friendly.</b> For Scala, it is recommended to use DSL with [[withDayOfMonth]] directly.
   *
   * Find first occurrence of day-of-week in current month.
   *
   * For example:
   * {{{ new Date(2014, 5, 5).firstInMonth(DayOfWeek.FRIDAY); }}}
   *
   * which is identical to
   * {{{ Date(2014, 5, 5).withDayOfMonth(1 st Friday) }}}
   * in Scala
   */
  def firstInMonth(dow: DayOfWeek) = this.withDayOfMonth(1 st dow)

  /**
   * <b>Java Friendly.</b> For Scala, it is recommended to use DSL with [[withDayOfMonth]] directly.
   *
   * Find last occurrence of day-of-week in current month.
   *
   * For example:
   * {{{ new Date(2014, 5, 5).lastInMonth(DayOfWeek.FRIDAY); }}}
   *
   * which is identical to
   * {{{ Date(2014, 5, 5).withDayOfMonth(lastFriday) }}}
   * in Scala
   */
  def lastInMonth(dow: DayOfWeek) = this.withDayOfMonth(LastWeekdayOfMonth(dow))
}

private object MonthOps {
  @tailrec
  private def nextOrSame(d: Date, dom: DayOfMonth): Date = {
    if (dom.isValidDOM(d)) {
      d
    } else {
      nextOrSame(d + 1, dom)
    }
  }

  @tailrec
  private def previousOrSame(d: Date, dom: DayOfMonth): Date = {
    if (dom.isValidDOM(d)) {
      d
    } else {
      previousOrSame(d - 1, dom)
    }
  }
}