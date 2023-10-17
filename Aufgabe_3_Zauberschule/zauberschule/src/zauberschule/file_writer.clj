(ns zauberschule.file-writer
  (:require
   [clojure.string :as str]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API

(defn write-blueprint
  [blueprint solution]
  (let [updated-blueprint (merge blueprint solution)
        num-floors (reduce max (map first (keys blueprint)))
        num-rows (reduce max (map second (keys blueprint)))
        num-columns (reduce max (map last (keys blueprint)))]
    (->> (for [i (range (inc num-floors))
               j (range (inc num-rows))
               k (range (inc num-columns))]
           (get updated-blueprint (list i j k)))
;         (partition num-columns)
;         (str/join "")
;         (partition num-rows)
;         (str/join "\n")
;         (partition num-floors)
;         (str/join "\n")
         )))
