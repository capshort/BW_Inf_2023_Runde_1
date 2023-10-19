(ns nandu.lighter)

(defn- white-component
  [[sensor-left-active? sensor-right-active?]]
  (if (and sensor-left-active? sensor-right-active?)
    [false false]
    [true true]))

(defn- red-component-sensor-left
  [[sensor-left-active? _sensor-right-active?]]
  (if sensor-left-active?
    [true true]
    [false false]))

(defn- red-component-sensor-right
  [[_sensor-left-active? sensor-right-active?]]
  (if sensor-right-active?
    [true true]
    [false false]))

(def ^:private blue-component identity)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API

(defn light-components
  [construction]
  (for [i (range 1 (count construction))]
    (->> construction
         (get i)
         (partition 2 1)
         )))
