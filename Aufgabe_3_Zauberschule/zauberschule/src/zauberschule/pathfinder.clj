(ns zauberschule.pathfinder
  (:require [zauberschule.utils :as utils]
            [zauberschule.file-reader :as reader]))

(defn- left-of
  [pos]
  (let [floor (first pos)
        row (second pos)
        column (last pos)]
    (list floor row (dec column))))

(defn- right-of
  [pos]
  (let [floor (first pos)
        row (second pos)
        column (last pos)]
    (list floor row (inc column))))

(defn- above-of
  [pos]
  (let [floor (first pos)
        row (second pos)
        column (last pos)]
    (list floor (dec row) column)))

(defn- below-of
  [pos]
  (let [floor (first pos)
        row (second pos)
        column (last pos)]
    (list floor (inc row) column)))

(defn- going-up-from
  [pos]
  (let [floor (first pos)
        row (second pos)
        column (last pos)]
    (list (inc floor) row column)))

(defn- going-down-from
  [pos]
  (let [floor (first pos)
        row (second pos)
        column (last pos)]
    (list (dec floor) row column)))

(defn- step-ignoring-walls
  [pos direction floor-map]
  (let [dest-pos (direction pos)]
    (when-some [_ (get floor-map dest-pos)]
      dest-pos)))

(defn- step
  [pos direction floor-map]
  (let [dest-pos (step-ignoring-walls pos direction floor-map)]
    (cond
      (nil? dest-pos)
      nil

      (utils/is-wall? (get floor-map dest-pos))
      nil

      :else
      dest-pos)))

(defn- get-goal-position
  [floor-map]
  (->> floor-map
       (filter utils/is-goal?)
       (first)
       (first)))

(defn- extend-path-by-one-step
  [path goal-pos floor-map]
  (into #{}
        (for [direction [left-of
                         right-of
                         above-of
                         below-of
                         going-up-from
                         going-down-from]]
          (when-let [next-pos (step (last path) direction floor-map)]
            (when-not (some #{next-pos} path)
              (conj path next-pos))))))

(defn- extend-all-paths-by-one-step
  [paths goal-pos floor-map]
  (->> (for [path paths]
         (extend-path-by-one-step path goal-pos floor-map))
       (reduce concat)
       (remove nil?)
       (into (sorted-set-by #(compare (vec (last %1)) (vec (last %2)))))
       ;; To prevent long execution times for paths of length ~20 and more
       ;; this last line is used to filter out paths that have the same end point.
       ;; It is not relevant *how* we got there in that exact number of steps,
       ;; it is just relevant *that* we got there.
       ;; You can see a huge difference in execution time if you comment out the
       ;; above line and take the following one instead:
       #_(into #{})
))

(defn- get-start-position
  [floor-map]
  (->> floor-map
       (filter utils/is-start?)
       (first)
       (first)))

(defn- is-solution?
  [path floor-map]
  (utils/is-goal? (get floor-map (last path))))

(defn- get-instruction
  [coords]
  (let [from (first coords)
        to (second coords)]
    (cond
      (= (left-of from) to) "<"
      (= (right-of from) to) ">"
      (= (above-of from) to) "^"
      (= (below-of from) to) "v"
      (= (going-up-from from) to) "!"
      (= (going-down-from from) to) "!")))

(defn- zip-with-instructions
  [solution-coords]
  (->> solution-coords
       (partition 2 1)
       (map get-instruction)
       (map vector solution-coords)
       (into {})))
       
(defn- extract-solution
  [paths floor-map]
  (->> paths
       (filter #(is-solution? % floor-map))
       (first)
       (zip-with-instructions)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API
       
(defn optimize-path
  [floor-map {:keys [print-progress?]}]
  (let [_start-position (get-start-position floor-map)
        goal-position (get-goal-position floor-map)
        initial-path-set #{[_start-position]}]
    (reduce (fn [cur-path-set _upcoming-limit]
              (if print-progress? (println "Using path limit" _upcoming-limit))
              (if (some #(is-solution? % floor-map) cur-path-set)
                (reduced (extract-solution cur-path-set floor-map))
                (extend-all-paths-by-one-step cur-path-set goal-position floor-map)))
            initial-path-set
            (range))))
