(ns kyllyukambot.polling
  (:require [clojure.core.async :as as :refer [<! >!]]
            [telegrambot-lib.core :as tbot]
            [telegrambot-lib.http :as thttp]))

(def timeout (* 60 1000))

(defn next-offset [data from]
  (if (seq data)
    (->> (map :update_id data)
         (apply max)
         inc)
    from))

(defn rethrow [response]
  (if-let [error (:error response)]
    (throw error)))

(defn poll
  "Poll Telegram API as [bot]."
  [bot stop opts]
  (let [bot (assoc bot :async true)
        updates (as/chan)]
    (as/go-loop [offset 0]
      (let [timed-out (as/timeout timeout)
            res (tbot/get-updates bot (merge opts {:offset offset}))]
        (as/alt!
          timed-out
          (do (as/close! res)
              (as/close! stop))
          
          stop
          (do (as/close! res)
              (as/close! timed-out))

          res
          ([response]
            (as/close! timed-out)
            (rethrow response)
            (let [data (:result response)]
              (doseq [update data] (>! updates update))
              (recur (next-offset data offset)))))))
    updates))

(defn start
  "Start polling."
  [bot handle opts]
  (let [stop (as/chan)
        updates (poll bot stop opts)]
    (as/go-loop []
      (when-let [update (<! updates)]
        (when-let [{path :method :as opts} (handle update)]
          (thttp/request bot path
                         (dissoc opts :method)))
        (recur)))
    stop))

(def stop "Stop polling." as/close!)