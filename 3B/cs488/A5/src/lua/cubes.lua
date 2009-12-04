mat1 = gr.material({0.7, 0.0, 0.0}, {0.5, 0.7, 0.5}, 5, "checker2.png", "")

scene_root = gr.node('root')
scene_root:translate(-0.5, -0.5, 0)

s1 = gr.cube('s1')
scene_root:add_child(s1)
s1:rotate("y", -10)
s1:rotate("x", -10)
s1:translate(-2, 2, 0)
s1:set_material(mat1)

s2 = gr.cube('s2')
scene_root:add_child(s2)
s2:rotate("x", -10)
s2:translate(0, 2, 0)
s2:set_material(mat1)

s3 = gr.cube('s3')
scene_root:add_child(s3)
s3:rotate("y", 10)
s3:rotate("x", -10)
s3:translate(2, 2, 0)
s3:set_material(mat1)

s4 = gr.cube('s4')
scene_root:add_child(s4)
s4:rotate("y", -10)
s4:translate(-2, 0, 0)
s4:set_material(mat1)

s5 = gr.cube('s5')
scene_root:add_child(s5)
s5:set_material(mat1)

s6 = gr.cube('s6')
scene_root:add_child(s6)
s6:rotate("y", 10)
s6:translate(2, 0, 0)
s6:set_material(mat1)

s7 = gr.cube('s7')
scene_root:add_child(s7)
s7:rotate("y", -10)
s7:rotate("x", 10)
s7:translate(-2, -2, 0)
s7:set_material(mat1)

s8 = gr.cube('s8')
scene_root:add_child(s8)
s8:rotate("x", 10)
s8:translate(0, -2, 0)
s8:set_material(mat1)

s9 = gr.cube('s9')
scene_root:add_child(s9)
s9:rotate("y", 10)
s9:rotate("x", 10)
s9:translate(2, -2, 0)
s9:set_material(mat1)

white_light = gr.light({-800.0, 0.0, 800.0}, {0.5, 0.5, 0.5}, {1, 0, 0})
light = gr.light({0.0, 0.0, 800.0}, {0.2, 0.2, 0.2}, {1, 0, 0})

gr.render(scene_root, 'simple.png', 500, 500,
	  {0, 0, 7}, {0, 0, -1}, {0, 1, 0}, 50,
	  {0.3, 0.3, 0.3}, {white_light, light})
