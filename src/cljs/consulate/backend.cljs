(ns consulate.backend
  (:require [ajax.core :refer [GET POST]]
            [consulate.utils :refer [log]]))

(defn datacenters-path []
  "test/datacenters.edn")

(defn get-datacenters [handler]
  (GET (datacenters-path) {:handler handler
                           :error-handler log
                           :format :edn}))
