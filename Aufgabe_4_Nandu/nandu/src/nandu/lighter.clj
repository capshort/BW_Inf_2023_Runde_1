(ns nandu.lighter
  (:require [clojure.math.combinatorics :as combo]))

(defn- light-components
  [construction lighting-combination]
  lighting-combination) ;; TODO

(defn- lighting-combinations
  [construction]
  (let [torches (->> construction
                     (first)
                     (filter (partial re-matches #"Q\d"))
                     (map keyword))]
    (->> (for [torch torches]
           (combo/cartesian-product [torch] [true false]))
         (reduce combo/cartesian-product)
         (flatten)
         (partition 2)
         (map vec)
         (partition (count torches))
         (map (partial into {})))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API

(defn light-components-in-all-combinations
  [setup]
  (let [combinations (lighting-combinations (:construction setup))]
    (for [combination combinations]
      (light-components (:construction setup) combination))))
