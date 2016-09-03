(ns fibu.replicate
  (:require [konserve.memory :refer [new-mem-store]]
            [replikativ
             [peer :refer [client-peer]]
             [stage :refer [connect! create-stage!]]
             [crdt.simple-gset.stage :as gs]]
            [cljs.core.async :refer [>! chan timeout]]
            [full.cljs.async :refer [throw-if-throwable]])
  (:require-macros [full.cljs.async :refer [go-try <? go-loop-try]]
                   [cljs.core.async.macros :refer [go-loop]]))

(defn start-local []
  (got-try
   (let [user "mail:prototype@your-domain.com"
         gset-id #uuid "d8ed7342-2dee-4881-a886-be38e0303794"
         store (<? (new-mem-store))
         peer (<? (client-peer store))
         stage (<? (create-stage! user peer))]
     (<? (gs/create-simple-gset! stage :id gset-id))
     (<? (connect! stage "ws://127.0.0.1:9090"))
     {:peer peer
      :stage stage
      :store store})))
