(ns fibu.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui] :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [om.dom :as dom]))

(enable-console-print!)

(def app-state
  (atom
   {:bookings/transactions
    #{{:created-at #inst "2016-08-19T17:21:29.102-00:00"
       :amount 666
       :transaction :expense
       :description "Bienenstock"}
      {:created-at #inst "2016-08-19T17:19:59.226-00:00"
       :amount 42
       :transaction :expense
       :description "Aldi"}
      {:created-at #inst "2016-08-19T17:20:14.523-00:00"
       :amount 1 
       :transaction :income
       :description "Contract"}}}))

;; ----- PARSING -----

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state] :as env} key params]
  (let [st @state]
    (if-let [[_ value] (find st key)]
      {:value value}
      {:value :not-found})))

(defmethod read :bookings/transactions
  [{:keys [state query]} _ _]
  {:value (sort-by :created-at > (:bookings/transactions @state))})

(defmethod read :bookings/income
  [{:keys [state query]} _ _]
  {:value (sort-by :created-at > (filter #(= (:transaction %) :income) (:bookings/list @state)))})

(defmethod read :bookings/expenses
  [{:keys [state query]} _ _]
  {:value (sort-by :created-at > (filter #(= (:transaction %) :expense) (:bookings/list @state)))})



;; -----  COMPONENTS -----

(defui Ledger
  static om/IQuery
  (query [this]
    '[:bookings/transactions])
  Object
  (render [this]
    (letfn [(amount-color [tx]
              (case tx
                :expense "red"
                :income "green"
                "black"))]
      (let [{:keys [bookings/transactions]} (om/props this)]
        (html
         [:div
          [:h2 "Transactions"]
          [:table
           [:tr
            [:th "Date"]
            [:th "Description"]
            [:th "Amount"]]
           (map (fn [{:keys [created-at amount transaction description]}]
                [:tr 
                 [:td (.toLocaleDateString created-at)]
                 [:td description]
                 [:td {:style {:color (amount-color transaction)}} amount]])
              transactions)]])))))

(defui Capture
  Object
  (render [this]
    (html
     [:div
      [:input {:type "text" :placeholder "Description"}]
      [:input {:type "number" :placeholder "Amount"}]
      [:input {:type "radio" :id "expense"}]])))


(def reconciler
  (om/reconciler
   {:state app-state
    :parser (om/parser {:read read})}))

(om/add-root! reconciler Ledger (gdom/getElement "app"))
