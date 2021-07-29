(ns kyllyukambot.core
  (:require [kyllyukambot.api :as api]
            [clojure.core.async :refer [<!!]]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [morse.handlers :as h]
            [morse.api :as t]
            [morse.polling :as p]
            [environ.core :refer [env]]
            [selmer.parser :refer [render-file]]))

(def token (env :telegram-token))

(defn lookup-cmd [lang]
  (fn [{{id :id} :chat, msg :text}]
    (let [word (get (str/split msg #"\s+") 1)]
      (if (nil? word)
        (t/send-text token id
                     (render-file "word-not-specified.md" {:lang lang}))
        (let [data (api/get-defs word (keyword lang))]
          (if (seq data)
            (->> (map api/as-tghtml data)
                 (map (partial t/send-text token id {:parse_mode "HTML"}))
                 doall)
            (t/send-text token id {:parse_mode "Markdown"}
                         (render-file "not-found.md" {:lang lang}))))))))

(h/defhandler bot-api
  (h/command "start" {{id :id} :chat}
             (t/send-text token id
                          {:parse_mode "Markdown"}
                          (render-file "start.md" {})))

  (h/command "help" {{id :id} :chat}
             (t/send-text token id
                          {:parse_mode "Markdown"}
                          (render-file "help.md" {})))

  (h/command-fn "udm" (lookup-cmd "udm"))
  (h/command-fn "ru" (lookup-cmd "ru")))

(defn -main []
  (when (str/blank? token)
    (println "Please provide token in TELEGRAM_TOKEN")
    (System/exit 1))

  (println "Starting the bot...")
  (<!! (p/start token bot-api)))
