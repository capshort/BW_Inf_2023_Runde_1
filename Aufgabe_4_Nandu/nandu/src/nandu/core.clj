(ns nandu.core
  (:require
   [nandu.file-reader :as file-reader]
   [nandu.lighter :as lighter]
   #_[nandu.file-writer :as file-writer])
  (:gen-class))

(defn -main
  [& args]
  (let [file-name (str "resources/nandu" (first args) ".txt")
        construction (file-reader/read-construction file-name)
        solution (lighter/light-components-in-all-combinations construction)]
    solution #_(file-writer/write construction solution)))
