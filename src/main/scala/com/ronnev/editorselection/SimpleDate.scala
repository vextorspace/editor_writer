package com.ronnev.editorselection

import scala.beans.BeanProperty

class SimpleDate() extends Comparable[SimpleDate] {
    @BeanProperty var year: Int = 0
    @BeanProperty var month: Int = 0
    @BeanProperty var day: Int = 0


    override def compareTo(o: SimpleDate): Int = {
        if (year == o.year) {
            if (month == o.month) {
                return day.compareTo(o.day)
            }

            return month.compareTo(o.month)
        }

        return year.compareTo(o.year)
    }

    override def toString: String = {
        f"$year%4d-$month%2d-$day%2d"
    }


    def canEqual(other: Any): Boolean = other.isInstanceOf[SimpleDate]

    override def equals(other: Any): Boolean = other match {
        case that: SimpleDate =>
            (that canEqual this) &&
                year == that.year &&
                month == that.month &&
                day == that.day
        case _ => false
    }

    override def hashCode(): Int = {
        val state = Seq(year, month, day)
        state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
    }
}

object SimpleDate {
    def apply() = new SimpleDate()
    def apply(year: Int, month: Int, day: Int) : SimpleDate = {
        val sd = new SimpleDate
        sd.setYear(year)
        sd.setMonth(month)
        sd.setDay(day)

        sd
    }

    def apply(dateString: String) : Option[SimpleDate] = {
        def parseDate(separator: String) : Option[SimpleDate] = {
            val pieces = dateString.split(separator)
            if (pieces.length != 3) {
                return Option.empty
            }
            val year = Integer.parseInt(pieces(0))
            val month = Integer.parseInt(pieces(1))
            val day = Integer.parseInt(pieces(2))
            return Option.apply(SimpleDate(year, month, day))
        }

        if (dateString.contains("-")) {
            return parseDate("-")
        } else if (dateString.contains("/")) {
            return parseDate("/")
        } else if (dateString.contains(" ")) {
            return parseDate(" ")
        }

        return Option.empty
    }
}
