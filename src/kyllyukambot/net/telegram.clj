(ns kyllyukambot.net.telegram
  (:require [clojure.core.async :as as]
            [cheshire.core :as json]
            [clj-http.client :as http]))

(def api-url "https://api.telegram.org/bot")

(defn call
  "Makes a call to the Telegram API."
  [token method opts]
  (let [endpoint (str api-url token "/" method)
        ch (as/chan 1)]
    (http/post endpoint
               {:async? true
                :form-params opts
                :content-type :json}
               (fn [response]
                 (as/put! ch (json/parse-string (:body response) true)))
               (fn [e]
                 (as/put! ch
                          (merge (some-> e
                                         :data
                                         :body
                                         (json/parse-string true))
                                 {:error e}))))
    ch))