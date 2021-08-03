(ns kyllyukambot.net.polling
  (:require [clojure.core.async :as as :refer [<! >!]]
            [kyllyukambot.net.telegram :as tg]))

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
  [token stop opts]
  (let [updates (as/chan)]
    (as/go-loop [offset 0]
      (let [timed-out (as/timeout timeout)
            res (tg/call token "getUpdates" (merge opts {:offset offset}))]
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
           (if-let [e (:error response)]
             (do (throw e)
                 (as/close! stop))
             (let [data (:result response)]
               (doseq [update data] (>! updates update))
               (recur (next-offset data offset))))))))
    updates))

(defn start
  "Start polling."
  [token handle opts]
  (let [stop (as/chan)
        updates (poll token stop opts)]
    (as/go-loop []
      (when-let [update (<! updates)]
        (when-let [{path :method :as opts} (<! (handle update))]
          (tg/call token path (dissoc opts :method)))
        (recur)))
    stop))

(def stop "Stop polling." as/close!)