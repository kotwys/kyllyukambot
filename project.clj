(defproject kyllyukambot "0.1.0"
  :description "Udmurt dictionary bot for Telegram"
  :url "https://github.com/kotwys/kyllyukambot"
  :license {:name "Eclipse Public License - v 2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "1.3.618"]
                 [org.clojure/core.match "1.0.0"]
                 [org.clojure/data.xml "0.2.0-alpha6"]
                 [clj-http "3.12.3"]
                 [cheshire "5.10.1"]
                 [environ "1.2.0"]
                 [http-kit "2.3.0"]
                 [selmer "1.12.44"]
                 [telegrambot-lib "1.0.0"]]
  :plugins [[lein-cljfmt "0.8.0"]]
  :uberjar-name "kyllyukambot.jar"
  :profiles {:uberjar {:aot :all}})
