(ns nandu.core
  (:require
   [nandu.file-reader :as file-reader]
   [nandu.lighter :as lighter]
   #_[nandu.file-writer :as file-writer]))

(defn -main
  [& args]
  (let [file-name (str "resources/nandu" (first args) ".txt")
        setup (file-reader/read-setup file-name)
        solution (lighter/light-components-in-all-combinations setup)]
    solution #_(file-writer/write construction solution)))
