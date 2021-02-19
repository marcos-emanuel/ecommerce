(ns kafka.producer-test
  (:require [clojure.test :refer [deftest is testing]]
            [kafka.producer :as kp]))

(deftest json->byte-test
  (let [data {"name" "marcos" "age" "30"}
        out (kp/json->byte data)]
    ;(prn (type out))
    (is (bytes? out))))