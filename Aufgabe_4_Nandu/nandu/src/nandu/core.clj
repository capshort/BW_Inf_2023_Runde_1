(ns nandu.core
  (:require
   [nandu.file-reader :as file-reader]
   [nandu.lighter :as lighter]
   [nandu.file-writer :as file-writer]))

(defn -main
  [& args]
  (let [file-name (str "resources/nandu" (first args) ".txt")
        construction (file-reader/read-setup)
        solution (lighter/light-components construction)]
    (file-writer/write construction solution)))
