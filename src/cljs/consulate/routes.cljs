(ns consulate.routes
  (:require [bidi.bidi :refer [match-route path-for]]))



(def routes ["/"
             {"" :index-page
              "detail" :detail-page}])


(defn get-handler [path]
  (or (match-route routes path)
      {:handler :index-page :message "Path not recognized"}))
