(ns zauberschule.utils)


(def ^:private start-symbol "A")

(def ^:private goal-symbol "B")

(def ^:private wall-symbol "#")

(def ^:private empty-field-symbol ".")


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API

(defn is-start?
  [s]
  (or (= s start-symbol)
      (= (second s) start-symbol)))

(defn is-goal?
  [s]
  (or (= s goal-symbol)
      (= (second s) goal-symbol)))

(defn is-wall?
  [s]
  (or (= s wall-symbol)
      (= (second s) wall-symbol)))

(defn is-empty?
  [s]
  (or (= s empty-field-symbol)
      (= (second s) empty-field-symbol)))
