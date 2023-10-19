(ns nandu.file-reader
  (:require [clojure.string :as str]))

(defn- interpret-content
  [content]
  (let [_lines (str/split-lines content)
        
        _dimensions (str/split (first lines) " ")
        width (first dimensions)
        height (second dimensions)

        construction (for [i (range width)]
                       (->> _lines
                            (drop 1)
                            (map (partial into vector))
                            (into vector)))]
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
