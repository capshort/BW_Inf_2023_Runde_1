(ns zauberschule.core
  (:require
   [zauberschule.file-reader :as reader]
   [zauberschule.pathfinder :as pathfinder]
   [zauberschule.file-writer :as writer]))

(defn -main
  [& args]
  (let [file (str "resources/zauberschule" (first args) ".txt")
        blueprint (reader/read-blueprint file)
        solution (pathfinder/optimize-path blueprint)]
    #_(writer/write-blueprint blueprint solution)
    solution))
;; Benutzung:
;; (-main 0)
;; (-main 1)
;; ...
;; (-main 5)
