(ns kafka.consumer
  (:require
    [jackdaw.client :as jc]
    [jackdaw.client.log :as jl]
    [jsonista.core :as json])
  (:import java.util.UUID)
  )

(def consumer-config
  {"bootstrap.servers"  "localhost:9092"
   "key.deserializer"   "org.apache.kafka.common.serialization.StringDeserializer"
   "value.deserializer" "org.apache.kafka.common.serialization.ByteArrayDeserializer"
   "group.id"           "consumer.reader-trx-service1"
   "auto.offset.reset"  "earliest"})

(def topic-consumer {:topic-name "ECOMMERCE"})

(def db (atom {}))

(defn byte->json [value]
  (-> value
      (json/read-value json/default-object-mapper)))

#_(defn run-consumer []
    (with-open [my-consumer (-> (jc/consumer consumer-config)
                                (jc/subscribe [topic-consumer]))]

      (doseq [{:keys [value]} (jl/log my-consumer 1000)]
        (swap! db assoc (java.util.UUID/randomUUID) (byte->json value)))))

(defn consumer []
  (jc/consumer consumer-config))

(defn save-db [{:keys [value]}]
  (swap! db assoc (UUID/randomUUID) (byte->json value)))

(defn do-in-another-thread [f]
  (future
    (let [c (jc/subscribe (consumer)
                          [topic-consumer])]
      (while true
        (->> (jc/poll c 1000)
             (run! f))
        (.commitSync c)))))

(def f (do-in-another-thread save-db))