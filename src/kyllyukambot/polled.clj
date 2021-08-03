(ns kyllyukambot.polled
  (:require [clojure.core.async :refer [<!!]]
            [environ.core :refer [env]]
            [kyllyukambot.handler :refer [handle]]
            [kyllyukambot.net.polling :refer [start]])
  (:gen-class))

(defn -main [& args]
  (when (nil? (env :bot-token))
    (println "Please provide token in BOT_TOKEN")
    (System/exit 1))

  (println "Starting the bot...")
  (<!! (start (env :bot-token) handle
              {:timeout 1
               :allowed_updates ["message"]})))
