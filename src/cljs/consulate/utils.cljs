(ns consulate.utils)


(defn log [obj]
  (.dir js/console (clj->js obj)))

(def color-states {"Failing" "red"
                   "Healthy" "green"})

(def color-op-states {"Down" "red"
                      "Running" "green"
                      "Test" "yellow"})

(def operational-states {"Down" "down"
                         "Running" "up"
                         "Test" "test"})
