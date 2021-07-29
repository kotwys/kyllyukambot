(ns kyllyukambot.api-test
  (:require [kyllyukambot.api :as api]
            [clojure.test :refer :all]))

(deftest as-tghtml
  (testing "basic"
    (is (= "<b>bold</b> <i>italic</i> plain"
           (api/as-tghtml "<span class=\"b\">bold</span> <span class=\"i\">italic</span> plain"))))
  (testing "whitespace"
    (is (= "<i>this </i>is<i> interesting</i>"
           (api/as-tghtml "<span class=\"i\">this </span>is<span class=\"i\"> interesting</span>"))))
  (testing "newlines"
    (is (= "line1\nline2\nline3"
           (api/as-tghtml "line1<span class=\"m1\">line2</span><span class=\"m1\">line3</span>"))))
  (testing "strips empty elements"
    (is (= "hello world"
           (api/as-tghtml "hello <b></b>world"))))
  (testing "preserves nesting"
    (is (= "<b>bold</b>\nand then"
           (api/as-tghtml "<span class=\"b\">bold</span><span class=\"m1\">and then</span>")))
    (is (= "ю́ра"
           (api/as-tghtml "<span class=\"apos\">ю</span>ра"))))
  (testing "accent mark"
    (is (= "ударе́ние"
           (api/as-tghtml "удар<span class=\"apos\">е</span>ние")))))
