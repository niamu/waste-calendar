(ns waste-calendar.cli
  (:gen-class)
  (:require
   [clojure.string :as string]
   [clojure.tools.cli :as cli]
   [waste-calendar.core :as waste-calendar]))

(def cli-options
  ;; An option with a required argument
  [["-z" "--zone (A-G)" "https://apps.london.ca/ZoneFinder/"
    :parse-fn #(string/upper-case %)
    :validate [#(re-matches #"[A-G]" %)
               "Must be a valid collection zone (A-G)"]]
   ["-h" "--help"]])

(defn usage
  [options-summary]
  (->> ["waste-calendar outputs an iCalendar file of your London, ON "
        "Garbage Calendar by providing your collection zone (https://apps.london.ca/ZoneFinder/)."
        ""
        "Usage: waste-calendar [options]"
        ""
        "Options:"
        options-summary
        ""]
       (string/join \newline)))

(defn -main
  [& args]
  (cli/parse-opts args cli-options))

(defn -main
  [& args]
  (let [{:keys [options arguments errors summary]}
        (cli/parse-opts args cli-options)
        [arguments] arguments]
    (cond
      (:help options) (println (usage summary))
      (:zone options) (do (waste-calendar/generate! options)
                          (println "Generated \"Garbage Calendar.ics\"."))
      :else (println (usage summary)))))
