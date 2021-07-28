(ns formdump.core
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.cors :refer [wrap-cors]]
            [org.httpkit.server :refer [run-server]]
            [org.httpkit.client :as http]
            [org.httpkit.sni-client :as sni-client]
            [jsonista.core :as json]
            [tick.alpha.api :as t]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [environ.core :refer [env]])
  (:gen-class))

(def OUTPUT_FILE (env :output-file "data.csv"))
(def PORT (Integer. (env :port 8000)))

(alter-var-root #'org.httpkit.client/*default-client* (fn [_] sni-client/default-client))

(defn to-json [data]
  (json/write-value-as-string data))

(defn iso-date-string []
  (let [date (t/format :iso-instant (t/zoned-date-time))]
    (str (subs date 0 23) (subs date 26 27))))

(defn save-to-csv
  "takes an vector and writes it to a CSV file"
  [data headers]
  (if (.exists (io/file OUTPUT_FILE))
    (with-open [writer (io/writer OUTPUT_FILE :append true)]
      (csv/write-csv writer [data]))
    (with-open [writer (io/writer OUTPUT_FILE)]
      (csv/write-csv writer [headers])
      (csv/write-csv writer [data]))))

(defn build-answers [field-id data]
  (to-json [{:questionId field-id
             :answer1 (to-json data)}]))

(defn build-ms-form-payload [field-id data]
  (let [date (iso-date-string)
        answers (build-answers field-id data)]
    {:startDate date
     :submitDate date
     :answers answers}))

(defn ms-form-proxy [form-url field-id data]
  (let [payload (build-ms-form-payload field-id data)
        {:keys [status body error]} @(http/post form-url {:body (to-json payload)                                                  :headers {"Content-Type" "application/json"}})]
    (if error
      (do
        (println "Failed" error body)
        false)
      (do
        (println status body)
        true))))

(defn handle-form-submission [req]
  (let [{:keys [body]} req
        url (get-in body ["url"])
        field-id (get-in body ["field_id"])
        data (get-in body ["data"])]
    (save-to-csv
     [url, field-id (to-json data)]
     ["form_url", "field_id" "data"])
    (if (or (nil? url) (nil? field-id) (nil? data))
      (do
        (println url field-id data)
        {:status 400
         :body "Bad request"})
      (do
        (ms-form-proxy url field-id data)
        {:body {:status "OK"}}))))

(defroutes app-routes
  (GET "/" [] {:body {:status "OK"}})
  (POST "/" [] handle-form-submission)
  (route/not-found "404 Not Found"))

(def app
  (-> app-routes
      (wrap-json-body)
      (wrap-json-response)
      (wrap-cors :access-control-allow-origin [#".*"]
                 :access-control-allow-methods [:get :post])))

(defn -main []
  (println "formdump running on port" PORT)
  (run-server app {:port PORT}))