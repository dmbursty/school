donut = gr.material({0, 0, 0}, {0, 0, 0}, 0)
bite = gr.material({0.25, 0, 0}, {0, 0, 0}, 0)
donut:set_texture("donut_tex.png")
donut:set_bumpmap("donut_bump.png")

scene_root = gr.node('root')
--scene_root:rotate('x', 90)
--scene_root:rotate('y', 180)
scene_root:rotate('x', 130)
scene_root:rotate('y', 20)
scene_root:rotate('z', 110)

d = gr.torus('donut', 0.4)
d:set_material(donut)
--scene_root:add_child(d)
--

b1 = gr.cylinder('b1')
b1:set_material(bite)

b2 = gr.cylinder('b2')
b2:set_material(bite)
b2:translate(0.5, 0, 1.2)

b3 = gr.cylinder('b3')
b3:set_material(bite)
b3:translate(0.5, 0, -1.2)

ba = gr.union(b1, b2)

b = gr.union(ba, b3)
b:translate(1.2, 0, -0.5)
b:rotate('x', 90)
b:scale(0.4, 1, 0.4)


s = gr.difference(d, b)
scene_root:add_child(s)

white_light = gr.light({0, 1, 5}, {0.7, 0.7, 0.7}, {1, 0, 0})

gr.render(scene_root, 'donut_a.png', 500, 500,
	  {0, 0, 5}, {0, 0, -1}, {0, 1, 0}, 50,
	  {0.3, 0.3, 0.3}, {white_light})
