(ns kyllyukambot.utils
  (:require [clojure.zip :as zip]))

(defn traverse
  "Traverse tree through a zipper applying a function."
  [tree f]
  (loop [loc tree]
    (if (zip/end? loc)
      (zip/node loc)
      (recur (zip/next (f loc))))))
