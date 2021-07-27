(defproject formdump "0.0.1"
  :description "dumps form field input into a CSV and optionally to a Microsoft Forms spreadsheet "
  :url "https://github.com/hueyy/formdump"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [http-kit "2.5.3"]
                 [metosin/jsonista "0.3.3"]
                 [compojure "1.6.2"]
                 [tick "0.4.32"]
                 [ring/ring-json "0.5.1"]
                 [org.clojure/data.csv "1.0.0"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler formdump.core/app
         :nrepl {:start true
                 :port "8888"}}
  :main ^:skip-aot formdump.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {}})