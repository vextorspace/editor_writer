package com.ronnev.editorselection.dates

import org.scalatest.FeatureSpec

class SimpleDateSpec extends FeatureSpec {
    feature("dates can be made from strings") {
        scenario("A date made from a string") {

            val oDate = SimpleDate("2017-09-08")

            assert(oDate.isDefined)

            val date = oDate.get

            assert(date.year == 2017)

            assert(date.month == 9)

            assert(date.day == 8)

            assert(date.toString == "2017-09-08")
        }
    }

    feature("dates are comparable") {
        scenario("two dates that are the same") {
            val date1 = SimpleDate("2017-09-08").get
            val date2 = SimpleDate("2017-09-08").get

            assert(date1.compareTo(date2) == 0)
        }

        scenario("two dates whose days are not the same") {
            val date1 = SimpleDate("2017-09-08").get
            val date2 = SimpleDate("2017-09-07").get

            assert(date1.compareTo(date2) == 1)
        }

        scenario("two dates whose months are not the same") {
            val date1 = SimpleDate("2017-02-07").get
            val date2 = SimpleDate("2017-09-07").get

            assert(date1.compareTo(date2) == -1)
        }

        scenario("two dates whose years are not the same") {
            val date1 = SimpleDate("2012-09-08").get
            val date2 = SimpleDate("2017-09-08").get

            assert(date1.compareTo(date2) == -1)
        }
    }

    feature("dates can be copied and modifications to the copy don't affect the original") {
        val date1 = SimpleDate("2012-08-09").get
        val date2 = date1.copy()

        date2.day = 12

        assert(date2.day == 12)
        assert(date1.day == 9)

        date2.month = 1

        assert(date2.month == 1)
        assert(date1.month == 8)

        date2.year = 2013

        assert(date2.year == 2013)
        assert(date1.year == 2012)
    }
}
