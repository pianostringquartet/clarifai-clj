(ns clarifai-clj.models)

;; ------------------------------------------------
;; Clarifai Predict API public models:
;; https://www.clarifai.com/developer/guide/public-models#public-models
;; ------------------------------------------------

;; As of Feb 2018, Predict API video supports only a subset of models.
;; https://www.clarifai.com/developer/guide/predict#videos
(def video-models
   {:apparel "e0be3b9d6a454f0493ac3a30784001ff"
    :food "bd367be194cf45149e75f01d59f77ba7"
    :general "aaa03c23b3724a16a56b629203edc62c"
    :nsfw "e9576d86d2004ed1a38ba0cf39ecb4b1"
    :travel "eee28c313d69466f836ab83287a54ed9"
    :wedding "c386b7a870114f4a87477c0824499348"})

; Apparel, Food, General, NSFW, Travel, and Wedding

(def models
  {:apparel "e0be3b9d6a454f0493ac3a30784001ff"
   :celebrity "e466caa0619f444ab97497640cefc4dc"
   :color "eeed0b6733a644cea07cf4c60f87ebb7"
   :demographics "c0c0ac362b03416da06ab3fa36fb58e3"
   :general "aaa03c23b3724a16a56b629203edc62c"
   :general-embedding "bbb5f41425b8468d9b7a554ff10f8581"
   :logo "c443119bf2ed4da98487520d01a0b1e3"
   :portrait-quality "de9bd05cfdbf4534af151beb2a5d0953"
   :travel "eee28c313d69466f836ab83287a54ed9"
   :moderation "d16f390eb32cad478c7ae150069bd2c6"
   :nsfw "e9576d86d2004ed1a38ba0cf39ecb4b1"
   :food "bd367be194cf45149e75f01d59f77ba7"
   :face-detection "a403429f2ddf4b49b307e318f00e528b-detection"
   :face-embedding "d02b4508df58432fbb84e800597b8959"
   :landscape-quality "bec14810deb94c40a05f1f0eb3c91403"
   :wedding "c386b7a870114f4a87477c0824499348"
   :focus "c2cf7cecd8a6427da375b9f35fcd2381"
   :textures-patterns "fbefb47f9fdb410e8ce14f24f54b47ff"})
