-- A simple scene with five spheres

mat1 = gr.material({0.7, 1.0, 0.7}, {0.5, 0.7, 0.5}, 25)

scene_root = gr.node('root')

thing = gr.node('thing')
thing:rotate("x", 30)
thing:rotate("y", 30)
scene_root:add_child(thing)

s1 = gr.cone('s1')
s1:rotate("z", 0)
s1:scale(100, 200, 100)
thing:add_child(s1)
s1:set_material(mat1)

s2 = gr.cone('s2')
s2:rotate("z", 90)
s2:scale(100, 200, 100)
thing:add_child(s2)
s2:set_material(mat1)

s3 = gr.cone('s3')
s3:rotate("z", 180)
s3:scale(100, 200, 100)
thing:add_child(s3)
s3:set_material(mat1)

s4 = gr.cone('s4')
s4:rotate("z", 270)
s4:scale(100, 200, 100)
thing:add_child(s4)
s4:set_material(mat1)

s5 = gr.cone('s5')
s5:rotate("x", 90)
s5:scale(100, 200, 100)
thing:add_child(s5)
s5:set_material(mat1)

s6 = gr.cone('s6')
s6:rotate("x", 270)
s6:scale(100, 200, 100)
thing:add_child(s6)
s6:set_material(mat1)

white_light = gr.light({-800.0, 0.0, 800.0}, {0.7, 0.7, 0.7}, {1, 0, 0})
light = gr.light({0.0, 0.0, 800.0}, {0.5, 0.5, 0.5}, {1, 0, 0})

gr.render(scene_root, 'simple.png', 500, 500,
	  {0, 0, 800}, {0, 0, -800}, {0, 1, 0}, 50,
	  {0.3, 0.3, 0.3}, {white_light, light})
