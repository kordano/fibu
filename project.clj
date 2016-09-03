(defproject fibu "0.1.0-SNAPSHOT"
  :description "simple accounting system written in om-next and using replikativ"
  :src-paths ["src/clj" "src/cljs"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [org.omcljs/om "1.0.0-alpha34"]
                 [datascript "0.13.1"]
                 [cljsjs/react "15.2.1-1"]
                 [cljsjs/react-dom "15.2.1-1"]
                 [io.replikativ/replikativ "0.2.0-SNAPSHOT"]
                 [sablono "0.7.4"]
                 [figwheel-sidecar "0.5.0-SNAPSHOT" :scope "test"]])
