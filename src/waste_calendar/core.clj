(ns waste-calendar.core
  (:require
   [clojure.data.json :as json]
   [clojure.string :as string]
   [hickory.core :as h]
   [hickory.select :as s])
  (:import
   [java.io FileOutputStream]
   [java.util UUID]
   [net.fortuna.ical4j.model Calendar Date DateList]
   [net.fortuna.ical4j.model.component VEvent]
   [net.fortuna.ical4j.model.property CalScale ProdId Uid Version RDate]
   [net.fortuna.ical4j.data CalendarOutputter]))

(def resource
  {:origin "https://apps.london.ca"
   :path "/ZoneFinder/CalendarRequests/Create"})

(defn document
  [resource]
  (-> (slurp (format "%s%s?zoneIn=%s&fullAddressIn=_&postalCodeIn=_"
                     (:origin resource)
                     (:path resource)
                     (:zone resource)))
      h/parse
      h/as-hickory))

(defn model
  [doc]
  (let [model-match "var model = "
        js (->> (s/select (s/child (s/tag :script)) doc)
                last
                :content
                first
                string/split-lines
                (drop-while (fn [s]
                              (not (string/starts-with? (string/triml s)
                                                        model-match))))
                first)]
    (->> (string/split js #" = ")
         second
         json/read-str)))

(defn pickup-dates
  [model]
  (->> (get model "PickUpDateList")
       (map (comp #(Date. %)
                  #(Long/parseLong %)
                  #(re-find #"[0-9]+" %)
                  #(get % "PickUpsDate")))))

(defn garbage-calendar
  [dates]
  (let [cal (Calendar.)
        props (.getProperties cal)
        vevent (VEvent. (first dates) "Recycling & Garbage Collection")
        vevent-props (.getProperties vevent)]
    (doto props
      (.add (ProdId. "-//London, ON Garbage Calendar//iCal4j 2.0//EN"))
      (.add Version/VERSION_2_0)
      (.add CalScale/GREGORIAN))
    (.add vevent-props (Uid. (str (UUID/randomUUID))))
    (doseq [d (rest dates)]
      (.add vevent-props (RDate. (doto (DateList.)
                                   (.addAll (rest dates))))))
    (.add (.getComponents cal) vevent)
    cal))

(defn save-calendar!
  [calendar]
  (let [out (CalendarOutputter.)]
    (.output out
             calendar
             (FileOutputStream. "Garbage Calendar.ics"))))

(defn generate!
  [{:keys [zone]}]
  (->> (document (assoc resource :zone (or zone "A")))
       model
       pickup-dates
       garbage-calendar
       save-calendar!))
