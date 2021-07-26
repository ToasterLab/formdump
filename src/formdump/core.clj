(ns formdump.core
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [org.httpkit.server :refer [run-server]]
            [jsonista.core :as j])
  (:gen-class))

(defroutes app
  (GET "/" [] "Hello World")
  (route/not-found "404 Not Found"))

(defn -main []
  (run-server app {:port 8000}))