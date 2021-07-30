(ns kyllyukambot.webhooks
  (:require [compojure.core :refer [defroutes POST]]
            [compojure.route :as route]
            [org.httpkit.server :refer :all]
            [environ.core :refer [env]]
            [telegrambot-lib.core :as tbot]
            [kyllyukambot.handler :refer [bot handle]]))

(defroutes app
  (POST (str "/" (:bot-token bot))
       [& update]
       {:status 200
        :headers {"Content-Type" "application/json; charset=utf-8"}
        :body (handle update)})
  (route/not-found "Not found"))

(defn -main [& args]
  (when (nil? (:bot-token bot))
    (println "Please provide token in BOT_TOKEN")
    (System/exit 1))

  (tbot/set-webhook bot
                    (str "https://"
                         (env :heroku-app-name)
                         ".herokuapp.com/"
                         (:bot-token bot))
                    {:allowed_updates ["message"]})
  (println "Starting server...")
  (run-server app {:port (env :port)}))