m = gr.material({1, 0, 0}, {0, 0, 0}, 0, 0, 0, "", "")
n = gr.material({0, 0, 1}, {0, 0, 0}, 0, 0, 0, "", "")

scene_root = gr.node('root')

l = gr.sphere('l')
l:set_material(m)
l:translate(-0.5, 0, 0)
--scene_root:add_child(l)

r = gr.sphere('r')
r:set_material(n)
r:translate(0.5, 0, 0)
--scene_root:add_child(r)

c = gr.union(l, r)
scene_root:add_child(c)

white_light = gr.light({0, 5, 5}, {1, 1, 1}, {1, 0, 0})

gr.render(scene_root, 'simple.png', 500, 500,
	  {0, 0, 5}, {0, 0, -1}, {0, 1, 0}, 50,
	  {0.3, 0.3, 0.3}, {white_light})
