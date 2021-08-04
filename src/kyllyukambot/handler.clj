(ns kyllyukambot.handler
  (:require [clojure.core.match :refer [match]]
            [clojure.string :as str]
            [selmer.parser :refer [render-file]]
            [telegrambot-lib.core :as tbot]
            [kyllyukambot.api :as api]))

(def bot (tbot/create))

(defn lookup [word lang]
  (if (nil? word)
    {:text (render-file "word-not-specified.md" {:lang lang})}
    (let [data (api/get-defs word (keyword lang))]
      (if (seq data)
        {:text (str/join "\n\n" data)
         :parse_mode "HTML"}
        {:text (render-file "not-found.md" {:lang lang})
         :parse_mode "Markdown"}))))

(defn handle [update]
  (match [update]
    [{:message {:text msg, :chat {:id id}}}]
    (if-let [tokens (seq (str/split msg #"\s+"))]
      (case (-> tokens first (str/split #"@") first)
        "/start"
        {:method "sendMessage"
         :chat_id id
         :text (render-file "start.md" {})
         :parse_mode "Markdown"}

        "/help"
        {:method "sendMessage"
         :chat_id id
         :text (render-file "help.md" {})
         :parse_mode "Markdown"}

        "/udm"
        (merge (lookup (second tokens) "udm")
               {:method "sendMessage"
                :chat_id id})

        "/ru"
        (merge (lookup (second tokens) "ru")
               {:method "sendMessage"
                :chat_id id})

        (merge-with #(str/join "\n\n" [%1 %2])
                    {:method "sendMessage"
                     :chat_id id
                     :text (render-file "ru-default.md" {})}
                    (lookup (first tokens) "ru"))))

    :else nil))