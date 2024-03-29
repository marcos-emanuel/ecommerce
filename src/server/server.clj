(ns server.server
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-param]
            [server.routes :as routes]))

(def service-map
  (-> {::http/routes routes/routes
       ::http/port 8000
       ::http/type :jetty}
      http/default-interceptors
      (update ::http/interceptors conj (body-param/body-params))))
(defonce server (atom nil))
(defn go []
  (reset! server
          (http/start (http/create-server
                        (assoc service-map ::http/join? false))))
  (prn "Server started on localhost:8000")
  :started)

(defn halt []
  (http/stop @server))