(ns datomic.model)

(defn novo-pedido [key cliente valor]
  {:pedido/key key
   :pedido/cliente cliente
   :pedido/valor valor})