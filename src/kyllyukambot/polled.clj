(ns kyllyukambot.polled
  (:require [clojure.core.async :refer [<!!]]
            [kyllyukambot.handler :refer [bot handle]]
            [kyllyukambot.net.polling :refer [start]])
  (:gen-class))

(defn -main [& args]
  (when (nil? (:bot-token bot))
    (println "Please provide token in BOT_TOKEN")
    (System/exit 1))

  (println "Starting the bot...")
  (<!! (start bot handle
              {:timeout 1
               :allowed_updates ["message"]})))
