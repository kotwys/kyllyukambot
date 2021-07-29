(defproject kyllyukambot "0.1.0-SNAPSHOT"
  :description "Udmurt dictionary bot for Telegram"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "1.3.618"]
                 [org.clojure/core.match "1.0.0"]
                 [org.clojure/data.xml "0.2.0-alpha6"]
                 [clj-http "3.12.3"]
                 [cheshire "5.10.1"]
                 [selmer "1.12.44"]
                 [telegrambot-lib "1.0.0"]]
  :plugins [[lein-cljfmt "0.8.0"]]
  :main kyllyukambot.core
  :uberjar-name "kyllyukambot.jar"
  :profiles {:uberjar {:aot :all}})
