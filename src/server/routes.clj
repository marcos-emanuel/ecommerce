(ns server.routes
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [kafka.producer :as kp]
            [kafka.consumer :as kc]))

(defn respond-get [_]
  {:status 200 :body @kc/db})

(defn respond-post [request]
  (let [value (:json-params request)]
    (kp/produce value)
    (-> value
        http/json-response
        (assoc :status 201))))

(def routes
  (route/expand-routes
    #{["/get" :get respond-get :route-name :get-data]
      ["/post" :post respond-post :route-name :post-data]}))