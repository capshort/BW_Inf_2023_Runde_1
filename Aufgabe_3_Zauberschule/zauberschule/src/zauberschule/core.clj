(ns zauberschule.core
  (:require
   [zauberschule.file-reader :as reader]
   [zauberschule.pathfinder :as pathfinder]
   [zauberschule.file-writer :as writer])
  (:gen-class))

(defn -main
  [& args]
  (let [file (last args)
        options {:print-progress? (= "-v" (first args))}
        blueprint (reader/read-blueprint file)
        solution (pathfinder/optimize-path blueprint options)]
    (writer/write-blueprint blueprint solution)))
;; Benutzung:
;; (-main 0)
;; (-main 1)
;; ...
;; (-main 5)
