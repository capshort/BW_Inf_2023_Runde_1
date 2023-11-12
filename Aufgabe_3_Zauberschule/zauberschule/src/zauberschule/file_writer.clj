(ns zauberschule.file-writer
  (:require
   [clojure.string :as str]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API

(defn write-blueprint
  [blueprint solution]
  (let [updated-blueprint (merge blueprint solution)
        num-floors (inc (reduce max (map first (keys blueprint))))
        num-rows (inc (reduce max (map second (keys blueprint))))
        num-columns (inc (reduce max (map last (keys blueprint))))]
    (println num-rows num-columns)
    (->> (for [i (range num-floors)
               j (range num-rows)
               k (range num-columns)]
           (get updated-blueprint (list i j k)))
         (partition num-columns)
         (map (partial str/join ""))
         (partition num-rows)
         (map (partial str/join "\n"))
         (str/join "\n\n")
         (println))))

