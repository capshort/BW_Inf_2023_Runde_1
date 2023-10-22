(ns nandu.lighter
  (:require [clojure.math.combinatorics :as combo]))

(defn- light-first-row
  [construction lighting-combination]
  (->> construction
       first
       (map #(get lighting-combination % false))))

(defn- light-next-row
  [current-row previous-row result]
  #_(println "Partial result:" result)
  (cond
    (empty? current-row)
    result
    
    (= "X" (first current-row))
    (light-next-row (drop 1 current-row)
                    (drop 1 previous-row)
                    (conj result false))

    (and (string? (first current-row))
         (re-matches #"L\d" (first current-row)))
    (light-next-row (drop 1 current-row)
                    (drop 1 previous-row)
                    (conj result (first current-row) (first previous-row)))

    :else
    (let [block (first current-row)]
      (light-next-row (drop 1 current-row)
                      (drop 2 previous-row)
                      (-> result
                          (conj (block (vec (take 2 previous-row))))
                          (flatten)
                          (vec))))))

(defn- light-components
  [construction lighting-combination]
  (let [first-row-lighted (light-first-row construction lighting-combination)]
    (reduce #(light-next-row %2 %1 [])
            first-row-lighted
            (drop 1 construction))))


(defn- lighting-combinations
  [construction]
  (let [torches (->> construction
                     (first)
                     (filter (partial re-matches #"Q\d")))]
    (->> (for [torch torches]
           (combo/cartesian-product [torch] [true false]))
         (reduce combo/cartesian-product)
         (flatten)
         (partition 2)
         (map vec)
         (partition (count torches))
         (map (partial into {})))))

(defn- sparse-result
  [result-row sparse-row]
  (if (empty? result-row)
    (remove empty? sparse-row)
    (as-> result-row r
      (drop-while (complement string?) r)
      (sparse-result (drop 2 r)
                     (conj sparse-row (take 2 r))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API

(defn light-components-in-all-combinations
  [construction]
  (let [combinations (lighting-combinations construction)]
    (for [combination combinations]
      (vector combination
              (sparse-result (light-components construction combination) [])))))
