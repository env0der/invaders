(ns invaders.client.textures)

(defn create-texture [path]
  (js/PIXI.Texture.fromImage path))


(def textures { :w (create-texture "/images/water.png")
                :g (create-texture "/images/grass.png")
                :s (create-texture "/images/sand.png")
                :marsman (create-texture "/images/marsman.png") })

(def hit-areas {
                 :w nil
                 :g nil
                 :s nil
                 :marsman (array 25 3 25 44 20 54 19 69 31 83 49 83 60 69 60 55
                                 55 47 54 21 48 1)})