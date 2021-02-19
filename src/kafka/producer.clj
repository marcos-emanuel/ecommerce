(ns kafka.producer
  (:require [jackdaw.client :as jc]
            [jsonista.core :as json])

  (:import java.util.UUID))

(def producer-config
  {"bootstrap.servers" "localhost:9092"
   "key.serializer" "org.apache.kafka.common.serialization.UUIDSerializer"
   "value.serializer" "org.apache.kafka.common.serialization.ByteArraySerializer"
   "acks" "all"
   "cliente.id" "producer-trx"})

(def topic "ECOMMERCE")

(defn get-uuid []
  (UUID/randomUUID))

(defn json->byte [value]
  (-> value
      (json/write-value-as-bytes json/default-object-mapper)))

(defn send-message [topic key value]
  (with-open [my-producer (jc/producer producer-config)]
    @(jc/produce! my-producer {:topic-name topic} key value)))

(defn produce [data]
  (let [key (get-uuid)
        trx (assoc data :pedido key)
        value (json->byte trx)]
    (prn data)
    (send-message topic key value)))
