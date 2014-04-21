(ns invaders.client.textures)

(defn create-texture [path]
  (js/PIXI.Texture.fromImage path))


(def tiles-textures {:w (create-texture "/images/water.png")
                     :g (create-texture "/images/grass.png")
                     :s (create-texture "/images/sand.png") })

(def units-textures {:marsman (create-texture "/images/marsman.png")})
