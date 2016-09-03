(ns fibu.core
  (:require [full.async :refer [<??]]
            [kabel.http-kit :refer [start stop]]
            [konserve
             [filestore :refer [new-fs-store]]
             [memory :refer [new-mem-store]]]
            [replikativ
             [peer :refer [server-peer client-peer]]
             [stage :refer [create-stage!]]]
            [replikativ.crdt.simple-gset.stage :as gs]))


(defn start-server []
  (let [user "mail:fibu@replikativ.io"
        gset-id #uuid "d8ed7342-2dee-4881-a886-be38e0303794"
        store (<?? (new-mem-store))
        peer (<?? (server-peer store "ws://127.0.0.1:9090"))
        stage (<?? (create-stage! user peer))
        _ (<?? (gs/create-simple-gset! stage
                                :id gset-id
                                :description "booking transactions"
                                :public false))]
    {:peer peer
     :stage stage
     :store store}))

(client-peer)
