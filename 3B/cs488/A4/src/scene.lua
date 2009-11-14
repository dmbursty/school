mat1 = gr.material({1.0, 0.0, 0.0}, {1.0, 1.0, 1.0}, 25)
mat2 = gr.material({0.0, 1.0, 0.0}, {1.0, 1.0, 1.0}, 25)
mat3 = gr.material({0.0, 0.0, 1.0}, {1.0, 1.0, 1.0}, 25)

scene_root = gr.node('root')

s1 = gr.nh_sphere('s1', {50, 50, -600}, 100)
scene_root:add_child(s1)
s1:set_material(mat1)

s2 = gr.nh_sphere('s2', {-50, -50, -600}, 100)
scene_root:add_child(s2)
s2:set_material(mat2)

s3 = gr.nh_sphere('s3', {50, -50, -600}, 100)
scene_root:add_child(s3)
s3:set_material(mat3)

white_light = gr.light({-100.0, 150.0, 400.0}, {0.9, 0.9, 0.9}, {1, 0, 0})

gr.render(scene_root, 'simple.png', 256, 256,
	  {0, 0, 800}, {0, 0, -800}, {0, -1, 0}, 50,
	  {0.3, 0.3, 0.3}, {white_light})
