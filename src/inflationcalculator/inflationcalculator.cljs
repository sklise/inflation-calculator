(ns inflationcalculator.inflationcalculator
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [cljs.core.async :refer [<! put! chan]])
  (:import [goog.net Jsonp]
           [goog Uri]))

; Buying Power
; d0 = dollar amount in starting year
; d1 = dollar amount in desired year
; c0 = cpi of starting year
; c1 = cpi of desired year
; t = number of years between starting year and desired year

; d0 is in 0$ and d1 is in 1$ units.
; d0 / c0 gets it to base year, times c1 is then hopefully the answer.

(def cpis {
           2014 233.069
           2013 233.069
           2012 229.594
           2011 224.939
           2010 218.056
           2009 214.537
           2008 215.303
           2007 207.342
           2006 201.6
           2005 195.3
           2004 188.9
           2003 184.0
           2002 179.9
           2001 177.1
           2000 172.2})

(defn listen [el type]
  (let [out (chan)]
    (events/listen el type
      (fn [e] (put! out e)))
    out))

(defn get-value [id]
  (.-value (dom/getElement id)))

(defn discount
  ([table amount target_year]
   (discount amount (.getFullYear (js/Date.)) target_year table))
  ([table amount base_year target_year]
   (* amount (/ (get table target_year) (get table base_year)))))

(defn init []
  (let [clicks (listen (dom/getElement (str "key-" 1)) "click")
        output (dom/getElement "input-val")]
    (go (while true
          (<! clicks)
          (set! (.-value output) (str (.-value output) "1") ))))

  (let [clicks (listen (dom/getElement (str "key-" 2)) "click")
        output (dom/getElement "input-val")]
    (go (while true
          (<! clicks)
          (set! (.-value output) (str (.-value output) "2") ))))
)
(init)