(ns nandu.file-reader
  (:require [clojure.string :as str]))

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

(defn- blue-component
  [sensor-left-active? sensor-right-active?]
  [sensor-left-active? sensor-right-active?])

(defn match-blocks
  [cur-block next-block]
  (cond
    (= [cur-block next-block] ["W" "W"])
    white-component

    (= [cur-block next-block] ["R" "r"])
    red-component-sensor-left

    (= [cur-block next-block] ["r" "R"])
    red-component-sensor-right

    (= [cur-block next-block] ["B" "B"])
    blue-component))

(defn recognize-components
  [result input-to-process]
  (let [current-block (first input-to-process)
        next-block (second input-to-process)]
    (if (nil? current-block)
      result
      (if-let [match (match-blocks current-block next-block)]
        (recognize-components (conj result match) (drop 2 input-to-process))
        (recognize-components (conj result current-block) (drop 1 input-to-process))))))

(defn- interpret-content
  [content]
  (let [_lines (str/split-lines content)
        
        _dimensions (str/split (first _lines) #" ")
        width (Integer. (first _dimensions))
        height (Integer. (second _dimensions))

        construction (->> _lines
                          (drop 1)
                          (map #(str/split % #"\s+"))
                          (map #(recognize-components [] %)))]
    {:width width
     :height height
     :construction construction}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API

(defn read-setup
  [file-name]
  (-> file-name
      (slurp)
      (interpret-content)))
