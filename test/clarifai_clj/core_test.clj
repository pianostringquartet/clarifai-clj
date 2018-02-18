(ns clarifai-clj.core-test
  (:require [clojure.test :refer :all]
            [clarifai-clj.core :refer :all]))

(def valid-image-url
  "https://images.unsplash.com/photo-1490118121063-d12f8c4464ce?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=1711e7df3b2e49a9582b8040c6a3aafe&auto=format&fit=crop&w=2534&q=80")

(def expected-concepts
  '({:name "travel", :value 0.98999643} {:name "people", :value 0.98932767} {:name "vehicle", :value 0.989195} {:name "light", :value 0.9800554} {:name "transportation system", :value 0.9760855} {:name "seat", :value 0.97569263} {:name "car", :value 0.9676071} {:name "adult", :value 0.9641167} {:name "airplane", :value 0.95818424} {:name "water", :value 0.95588475} {:name "one", :value 0.93720055} {:name "aircraft", :value 0.9344635} {:name "outdoors", :value 0.93127537} {:name "landscape", :value 0.9270297} {:name "indoors", :value 0.9225382} {:name "woman", :value 0.91233695} {:name "girl", :value 0.8763349} {:name "mirror", :value 0.85588} {:name "man", :value 0.8526882} {:name "tourism", :value 0.8517755}))

(def invalid-image-url
  "https://images.unsplash.com/photo-123456789")

; you will need to put this api-key in your env,
; as clj-sendgrid's test suite does...
(def api-key "ebc31d071d23414ab7e369c003e3c3bf")

; hold on -- are you going to do that actual api request?
; furthermore, what happens if the Clarifai API starts calculating
; the concepts differently later?
(deftest valid-image-url-concepts
  (testing "Get concepts for a valid image url?"
    (is (= expected-concepts
           (concepts api-key :general valid-image-url)))))

(deftest invalid-image-url-exception
  (testing "Throw exception on invalid image url?"
    (is (thrown-with-msg?
          Exception
          (re-pattern invalid-image-url-error-message)
          (concepts api-key :general invalid-image-url)))))
