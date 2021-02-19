(ns datomic.db
  (:use clojure.pprint)
  (:require [datomic.api :as d]))

(def db-uri "datomic-dev://localhost:4334/ecommerce")

(defn abre-conexao []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn apaga-banco []
  (d/delete-database db-uri))

(def schema [{:db/ident       :pedido/key
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/doc         "Identificador do pedido"}
             {:db/ident       :pedido/cliente
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "CPF do cliente"}
             {:db/ident       :pedido/valor
              :db/valueType   :db.type/double
              :db/cardinality :db.cardinality/one
              :db/doc         "Valor do pedido"}])

(defn cria-schema [conn]
  (d/transact conn schema))