(ns kyllyukambot.utils
  (:require [clojure.core.async :as as]
            [clojure.zip :as zip]))

(defn instant
  "Returns a channel with instant value."
  [v]
  (as/to-chan [v]))

(defn traverse
  "Traverse tree through a zipper applying a function."
  [tree f]
  (loop [loc tree]
    (if (zip/end? loc)
      (zip/node loc)
      (recur (zip/next (f loc))))))
