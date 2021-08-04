(ns kyllyukambot.webhooks
  (:require [clojure.core.match :refer [match]]
            [clojure.java.io :as io]
            [cheshire.core :as json]
            [org.httpkit.server :refer :all]
            [environ.core :refer [env]]
            [telegrambot-lib.core :as tbot]
            [kyllyukambot.handler :refer [bot handle]]))

(defn app [uri req]
  (match [req]
    [{:uri uri, :request-method :post, :body body}]
    (with-open [reader (io/reader body)]
      (let [update (json/parse-stream reader true)]
        {:status 200
         :headers {"Content-Type" "application/json; charset=utf-8"}
         :body (-> (handle update)
                   json/generate-string)}))

    :else
    {:status 404
     :body "Not found"}))

(defn -main [& args]
  (when (nil? (:bot-token bot))
    (println "Please provide token in BOT_TOKEN")
    (System/exit 1))

  (println "Starting server...")
  (run-server (partial app (str "/" (:bot-token bot)))
              {:port (-> :port env Integer/parseInt)})
  (println "Setting webhook...")
  (tbot/set-webhook bot
                    (str "https://"
                         (env :heroku-app-name)
                         ".herokuapp.com/"
                         (:bot-token bot))
                    {:allowed_updates ["message"]}))