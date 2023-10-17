(ns zauberschule.file-reader
  (:require
   [clojure.string :as str]))

(defn- coordinate-map
  [floors-coll]
  (reduce merge
          (for [floor (map-indexed list floors-coll)
                row (map-indexed list (second floor))
                field (map-indexed list (second row))]
            {(list (first floor)
                   (first row)
                   (first field))
             (str (second field))})))

(defn- interpret-content
  [content]
  (let [_file-lines (str/split-lines content)
        
        _dimensions (str/split (first _file-lines) #" ")
        _rows (Integer. (first _dimensions))
        _columns (Integer. (second _dimensions))
        
        _floor-contents (drop 1 _file-lines)
        _lower-floor (->> _floor-contents
                          (take _rows))
        _upper-floor (->> _floor-contents
                          (drop (inc _rows)))
        floors (coordinate-map (list _lower-floor _upper-floor))]
    floors))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API

(defn read-blueprint
  [path-to-file]
  (-> path-to-file
      (slurp)
      (interpret-content)))
