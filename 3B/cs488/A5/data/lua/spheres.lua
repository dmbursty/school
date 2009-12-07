l = gr.material({0.7, 0.7, 0}, {1, 1, 1}, 3)
r = gr.material({0, 0, 1}, {1, 1, 1}, 3)
fl = gr.material({0.8, 0.8, 0.8}, {1, 1, 1}, 3)

l:set_reflect(1)
l:set_gloss(0.3)
r:set_reflect(1)

scene_root = gr.node('root')
scene_root:rotate('x', 40)

w = gr.mesh('w', {
  {-1, 0, 1},
  {1, 0, 1},
  {1, 0, -1},
  {-1, 0, -1},
    }, {
  {0, 1, 2, 3},
} )
w:set_material(fl)
w:scale(10, 10, 10)
scene_root:add_child(w)

s1 = gr.sphere('s1')
s1:translate(-1.3, 1, 0)
s1:set_material(l)
scene_root:add_child(s1)

s2 = gr.sphere('s2')
s2:translate(1.3, 1, 0)
s2:set_material(r)
scene_root:add_child(s2)

white_light = gr.light({0, 5, 2}, {0.7, 0.7, 0.7}, {1, 0, 0})

gr.render(scene_root, 'simple.png', 500, 500,
	  {0, 1, 5}, {0, 0, -1}, {0, 1, 0}, 50,
	  {0.3, 0.3, 0.3}, {white_light})
