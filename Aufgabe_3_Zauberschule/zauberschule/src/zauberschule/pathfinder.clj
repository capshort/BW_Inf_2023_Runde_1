(ns zauberschule.pathfinder
  (:require
   [zauberschule.utils :as utils])) ;; Import

(defn- left-of
  "Gibt die Koordinaten der Zielposition, wenn man auf Position `pos` einen Schritt nach links ausführt."
  [pos]
  (let [floor (first pos)  ;; first gibt das erste Element aus der Liste pos,
        row (second pos)   ;; second das zweite,
        column (last pos)] ;; last das letzte.
    (list floor row (dec column)))) ;; (dec column) ist gleichwertig zu column minus 1
                                    ;; bzw. in Clojure-Schreibweise (- column 1)
;; Beispiel:
;; (left-of (list 0 0 0))
;; ergibt:
;; (list 0 0 -1)
;; In Clojure gilt: Code is data / Data is Code. Beides sieht gleich aus.
;; D.h. "(list ...)" ist eine Liste und ist auch der *Code* um eine Liste zu bauen.

(defn- right-of
  "Gibt die Koordinaten der Zielposition, wenn man auf Position `pos` einen Schritt nach rechts ausführt."
  [pos]
  (let [floor (first pos)
        row (second pos)
        column (last pos)]
    (list floor row (inc column))))
;; Beispiel:
;; (right-of (list 0 0 0))
;; ergibt:
;; (list 0 0 1)

(defn- above-of
  "Gibt die Koordinaten der Zielposition, wenn man auf Position `pos` einen Schritt nach oben ausführt."
  [pos]
  (let [floor (first pos)
        row (second pos)
        column (last pos)]
    (list floor (dec row) column)))
;; Beispiel:
;; (above-of (list 0 0 0))
;; ergibt:
;; (list 0 -1 0)

(defn- below-of
  "Gibt die Koordinaten der Zielposition, wenn man auf Position `pos` einen Schritt nach unten ausführt."
  [pos]
  (let [floor (first pos)
        row (second pos)
        column (last pos)]
    (list floor (inc row) column)))
;; Beispiel:
;; (below-of (list 0 0 0))
;; ergibt:
;; (list 0 1 0)

(defn- going-up-from
  "Gibt die Koordinaten der Zielposition, wenn man auf Position `pos` ein Stockwerk nach oben geht."
  [pos]
  (let [floor (first pos)
        row (second pos)
        column (last pos)]
    (list (inc floor) row column)))
;; Beispiel:
;; (going-up-from (list 0 0 0))
;; ergibt:
;; (list 1 0 0)

(defn- going-down-from
  "Gibt die Koordinaten der Zielposition, wenn man auf Position `pos` ein Stockwerk nach unten geht."
  [pos]
  (let [floor (first pos)
        row (second pos)
        column (last pos)]
    (list (dec floor) row column)))
;; Beispiel:
;; (going-down-from (list 0 0 0))
;; ergibt:
;; (list -1 0 0)

(defn- step-ignoring-walls
  "Gibt die Koordinaten der Zielposition zurück, wenn man auf Position `pos`
  einen Schritt in der gegebenen `direction` ausführt.
  Falls die Zielposition außerhalb der möglichen Positionen (gegeben durch `floor-map`)
  liegt, wird nil zurückgegeben."
  [pos direction floor-map]
  (let [dest-pos (direction pos)] ;; in Python wäre das: dest-post = direction(pos)
    (when-some [_ (get floor-map dest-pos)] ;; sucht aus den möglichen Positionen den Wert an der Zielposition, z.B. "#" für eine Wand
      dest-pos))) ;; gibt die Zielposition zurück *falls* der Wert an der Zielposition nicht nil ist (dafür sorgt das "some" oben drüber)
;; In Clojure kann man problemlos Funktionen als Parameter für Funktionen nutzen,
;; das passiert hier mit `direction``: dieser Parameter wird später beim Aufruf immer mit
;; einer der oben definierten Funktionen `left-of`, `right-of`, ..., `going-down-from` belegt.

(defn- step
  "Gibt die Koordinaten der Zielposition zurück, wenn man auf Position `pos`
  einen Schritt in der gegebenen `direction` ausführt.
  Falls die Zielposition außerhalb der möglichen Positionen liegt oder falls an der
  Zielposition eine Wand ist, wird nil zurückgegeben."
  [pos direction floor-map]
  (let [dest-pos (step-ignoring-walls pos direction floor-map)]
    (cond             ;; Fallunterscheidung
      (nil? dest-pos) ;; Ziel liegt außerhalb der möglichen Positionen
      nil

      (utils/is-wall? (get floor-map dest-pos)) ;; Ziel ist eine Wand
      nil

      :else
      dest-pos)))

(defn find-path-with-length
  "Gibt einen Pfad der Länge `length-limit` zurück, der zum Ziel führt.
  Falls kein Pfad dieser Länge zum Ziel existiert, wird nil zurückgegeben."
  [cur-pos path-taken-until-here floor-map length-limit]
  #_(println "Path taken:" path-taken-until-here) ;; #_ kommentiert einzelne Ausdrücke aus -
                                                  ;; das hier ist eine debug-Ausgabe die gerade
                                                  ;; ungenutzt ist
  (let [directions [left-of
                    right-of
                    above-of
                    below-of
                    going-down-from
                    going-up-from]] ;; Liste, die alle möglichen Richtungsänderungen enthält (als Funktionen)
    (cond                                       ;; Fallunterscheidung
      (utils/is-goal? (get floor-map cur-pos))  ;; a) Sind wir schon am Ziel? Dann ist der Weg den wir bis hierher
                                                ;; genommen haben die Lösung.
      path-taken-until-here

      (= 0 length-limit)                        ;; b) Ist die maximale Pfadlänge erreicht? Dann brechen wir hier
                                                ;; ab und geben nil - also keinen Pfad - zurück.
                                                ;; Das ist der Punkt, wo eingeschlagene Pfade komplett verworfen werden.
      nil

      :else                                     ;; c) Wenn die maximale Pfadlänge noch nicht erreicht ist, dann machen wir
                                                ;; jeweils *einen* Schritt...
      (some identity
       (for [direction directions]              ;; ... in jede der möglichen Richtungen...
         (when-let [next-pos (step cur-pos direction floor-map)] ;; ... aber nur wenn die Zielposition gültig ist (nicht außerhalb und keine Wand) ...
           (when (not (some #{next-pos} path-taken-until-here))  ;; ... und nur falls wir jetzt nirgendwo sind wo wir schon mal waren (hier wird geprüft ob `next-pos` in der Liste `path-taken-until-here` enthalten ist) ...
            (find-path-with-length next-pos                      ;; ... und von der neuen Position aus suchen wir einen Pfad...
                                   (conj path-taken-until-here next-pos) ;; ... mit der neuen Position als Teil des Gesamtwegs den wir nehmen (conj fügt `next-pos` der Liste hinzu) ...
                                   floor-map
                                   (dec length-limit)))))))))    ;; ... mit um 1 verringerter Maximallänge.

(defn- get-start-position
  "Sucht aus den möglichen Positionen die Koordinaten der Position heraus,
  die das Anfangsfeld darstellt."
  [floor-map]
  (->> floor-map
       (filter utils/is-start?)
       (first)
       (first)))

(defn- get-instruction
  "Sucht für eine fertige Liste `coords` von in Reihenfolge abgelaufenen Koordinaten
  die jeweils nötigen Schritte heraus, die zwischen den abgelaufenen Koordinaten
  gemacht werden mussten. Die Schritte werden als Textzeichen kodiert."
  [coords]
  (let [from (first coords)
        to (second coords)]
    (cond
      (= (left-of from) to) "<"
      (= (right-of from) to) ">"
      (= (above-of from) to) "^"
      (= (below-of from) to) "v"
      (= (going-up-from from) to) "!"
      (= (going-down-from from) to) "!")))
;; Beispiel:
;; (get-instruction (list (list 0 0 0) (list 0 0 1) (list 1 0 1)))
;; ergibt:
;; (list ">" "!")

(defn- zip-with-instructions
  "Kombiniert eine fertige Liste `solution-coords` von in Reihenfolge abgelaufenen
  Koordinaten mit den jeweils nötigen Schritten um von einer zur nächsten Koordinate
  zu gelangen. Die Schritte werden als Textzeichen kodiert."
  [solution-coords]
  (->> solution-coords
       (partition 2 1)
       (map get-instruction)
       (map vector solution-coords)
       #_(into {})))
;; Beispiel:
;; (zip-with-instructions (list (list 0 0 0) (list 0 0 1) (list 1 0 1)))
;; ergibt:
;; (list (list (list 0 0 0) ">")
      ;; (list (list 0 0 1) "!")
      ;; (list (list 1 0 1)))
;; Tatsächlich ist das etwas geschummelt: es sind nicht alles Listen, sondern auch Vektoren dabei -
;; das ist aber verständnistechnisch egal.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API

(defn optimize-path
  "Gibt für eine gegebene `floor-map`, die eine Abbildung von dreidimensionalen Koordinaten auf
  Textsymbole A . # oder B sein muss, den kürzesten Pfad von A nach B zurück.
  Wenn es keinen möglichen Pfad von A nach B gibt, bricht die Ausführung irgendwann ab und gibt
  nil zurück."
  [floor-map]
  (let [start-position (get-start-position floor-map) ;; sucht die Startposition aus der Abbildung heraus
        length-limits (range)                         ;; unendlich lange Liste: (list 0 1 2 3 4 5 ...)
        path-at-length (partial find-path-with-length ;; Funktion, die eine Zahl als Angabe für Maximallänge erwartet und
                                                      ;; einen Pfad dieser Länge zum Ziel zurückgibt (partial ist eine *teilweise*
                                                      ;; Ausführung einer Funktion, die weitere zu belegende Parameter offen lässt)
                                start-position
                                [start-position]
                                floor-map)]
    (->> length-limits                ;; für jede Maximallänge (beginnend bei 0):
         (map path-at-length)         ;; suche den Pfad mit dieser Maximallänge ...
         (some identity)              ;; übernimm den ersten Pfad beliebiger Maximallänge, der nicht nil ist (das ist der kürzeste, weil wir bei Maximallänge 0 starten)
         (zip-with-instructions))))   ;; und ergänze den gefundenen Pfad um die Anweisungen, die dabei ausgeführt wurden.
