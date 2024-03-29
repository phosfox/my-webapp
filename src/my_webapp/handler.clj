(ns my-webapp.handler
  (:require [my-webapp.views :as views] ; add this require
            [ring.adapter.jetty :as jetty]
            [compojure.core :refer :all]
            [my-webapp.util :as util]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:gen-class))

(defroutes app-routes ; replace the generated app-routes with this
  (GET "/"
    []
    (views/home-page))
  (GET "/add-location"
    []
    (views/add-location-page))
  (POST "/add-location"
    {params :params}
    (let [coords (vals (dissoc params :__anti-forgery-token))
          parsed (map util/parse-int coords)]
      (if (every? number? parsed)
        (views/add-location-results-page params)
        (views/add-location-page "Location must be a number"))))
  (GET "/location/:loc-id"
    [loc-id]
    (views/location-page loc-id))
  (GET "/all-locations"
    []
    (views/all-locations-page))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

(defn -main
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           5000))]
    (jetty/run-jetty app {:port  port
                          :join? false})))
