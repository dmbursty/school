mat1 = gr.material({0.7, 1.0, 0.7}, {0.5, 0.7, 0.5}, 25)

scene_root = gr.node('root')
scene_root:rotate("y", 30)
scene_root:rotate("x", 30)

t1 = gr.torus('t1', 0.4)
scene_root:add_child(t1)
t1:translate(0, 0, -1.5)
t1:set_material(mat1)

t2 = gr.torus('t2', 0.2)
scene_root:add_child(t2)
t2:translate(0, 0, 0.3)
t2:set_material(mat1)

t3 = gr.torus('t3', 0.05)
scene_root:add_child(t3)
t3:translate(0, 0, 1.3)
t3:set_material(mat1)

c1 = gr.cone('c1')
scene_root:add_child(c1)
c1:rotate("x", -90)
c1:translate(0, -2, 0)
c1:scale(0.8, 6, 0.8)
c1:set_material(mat1)

cy = gr.cylinder('cy')
scene_root:add_child(cy)
cy:translate(-2.05, 1, 0)
cy:scale(0.15, 0.15, 2.5)
cy:rotate("x", 90)
cy:set_material(mat1)

white_light = gr.light({-800.0, 0.0, 800.0}, {0.5, 0.5, 0.5}, {1, 0, 0})
light = gr.light({0.0, 0.0, 800.0}, {0.2, 0.2, 0.2}, {1, 0, 0})

gr.render(scene_root, 'new_primitives.png', 500, 500,
	  {0, 0, 5}, {0, 0, -1}, {0, 1, 0}, 50,
	  {0.3, 0.3, 0.3}, {white_light, light})
