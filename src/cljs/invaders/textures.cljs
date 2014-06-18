(ns invaders.client.textures)

(defn create-texture [path]
  (js/PIXI.Texture.fromImage path))


(def textures { :w (create-texture "/images/water.png")
                :g (create-texture "/images/grass.png")
                :s (create-texture "/images/sand.png")
                :marsman (create-texture "/images/marsman.png") })

; hit-area coordinates are top-down, left-right
(def hit-areas {
                 :w (array 0 26 0 66 40 83 80 66 80 26 40 8)
                 :g (array 0 17 0 66 40 83 80 66 80 17 40 0)
                 :s (array 0 17 0 66 40 83 80 66 80 17 40 0)
                 :marsman (array 33 0 20 15 20 28 25 38 25 67 38 83 51 77 54 62
                                 55 37 60 28 60 15 47 0)})