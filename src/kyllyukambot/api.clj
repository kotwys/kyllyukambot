(ns kyllyukambot.api
  (:require [clj-http.client :as html]
            [cheshire.core :as json]
            [clojure.data.xml :as xml]
            [clojure.zip :as zip]
            [kyllyukambot.utils :as utils])
  (:import (clojure.data.xml.node Element)))

(defn- transform [loc]
  (let [node (zip/node loc)]
    (cond
      (not (instance? Element node)) loc
      (empty? (:content node)) (zip/remove loc)
      :else (case (:class (:attrs node))
              "b"
              (zip/edit loc merge {:tag "b", :attrs {}})

              "i"
              (zip/edit loc merge {:tag "i", :attrs {}})

              "apos"
              (-> (zip/insert-right loc (:content node))
                  (zip/remove)
                  (zip/next)
                  (zip/insert-right (str (char 769)))
                  (zip/right))

              "m1"
              (as-> loc loc'
                (zip/insert-right loc' "\n")
                (zip/remove loc')
                (zip/next loc')
                (reduce zip/insert-right loc' (reverse (:content node))))

              loc))))

(defn as-tghtml
  "Adjust markup to the standard."
  [s]
  (-> (xml/parse-str (str "<div>" s "</div>"))
      zip/xml-zip
      (utils/traverse transform)
      xml/emit-str
      (#(subs % 43 (- (count %) 6)))))

(def endpoint "http://udmcorpus.udman.ru/api/public/dictionary/search")

(defn lang-id
  "Makes language keyword into ID."
  [lang]
  (case lang
    :udm 1
    :ru 2))

(defn get-defs
  "Retrieves definitions for the word as lazy sequence."
  [word lang]
  (let [query {:word word
               :lang {:id (lang-id lang)}}
        res (html/post endpoint
                       {:form-params query
                        :content-type :json})]
    (->> (json/parse-string (:body res) true)
         (map (comp as-tghtml :body)))))
