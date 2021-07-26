(defproject formdump "0.0.1"
  :description ""
  :url "https://github.com/hueyy/formdump"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [http-kit "2.5.3"]
                 [metosin/jsonista "0.3.3"]
                 [compojure "1.6.2"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler formdump.core/app
         :nrepl {:start true}}
  :main ^:skip-aot formdump.app
  :target-path "target/%s"
  :profiles {:uberjar {:aot :alll}
             :dev {}})