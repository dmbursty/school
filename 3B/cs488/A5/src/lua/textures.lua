mat1 = gr.material({0.7, 0.0, 0.0}, {0.0, 0.0, 0.0}, 5, "swirl.png", "")
mat2 = gr.material({0.7, 0.0, 0.0}, {0.0, 0.0, 0.0}, 5, "checker1.png", "")
mat3 = gr.material({0.7, 0.0, 0.0}, {0.0, 0.0, 0.0}, 5, "checker3.png", "")

scene_root = gr.node('root')
--scene_root:translate(-0.5, -0.5, 0)

-- Cubes
sa = gr.cube('sa', 0.3)
scene_root:add_child(sa)
sa:translate(-3, 5, 0)
sa:rotate("y", -45)
sa:rotate("x", -45)
sa:scale(1.3, 1.3, 1.3)
sa:set_material(mat1)

sb = gr.cube('sb', 0.3)
scene_root:add_child(sb)
sb:translate(0, 5, 0)
sb:rotate("x", -45)
sb:scale(1.3, 1.3, 1.3)
sb:set_material(mat2)

sc = gr.cube('sc', 0.3)
scene_root:add_child(sc)
sc:translate(3, 5, 0)
sc:rotate("y", 45)
sc:rotate("x", -45)
sc:scale(1.3, 1.3, 1.3)
sc:set_material(mat3)

-- Spheres
s1 = gr.sphere('s1', 0.3)
scene_root:add_child(s1)
s1:translate(-3, 3, 0)
s1:rotate("y", -45)
s1:rotate("x", -45)
s1:set_material(mat1)

s2 = gr.sphere('s2', 0.3)
scene_root:add_child(s2)
s2:translate(0, 3, 0)
s2:rotate("x", -45)
s2:set_material(mat2)

s3 = gr.sphere('s3', 0.3)
scene_root:add_child(s3)
s3:translate(3, 3, 0)
s3:rotate("y", 45)
s3:rotate("x", -45)
s3:set_material(mat3)

-- Cylinders
s4 = gr.cylinder('s4', 0.3)
scene_root:add_child(s4)
s4:translate(-3, -1, 0)
s4:rotate("x", -30)
s4:rotate("y", -30)
s4:scale(1, 2, 1)
s4:set_material(mat1)

s5 = gr.cylinder('s5', 0.3)
scene_root:add_child(s5)
s5:translate(0, -1, 0)
s5:scale(1, 2, 1)
s5:set_material(mat2)

s6 = gr.cylinder('s6', 0.3)
scene_root:add_child(s6)
s6:translate(3, -1, 0)
s6:rotate("x", 30)
s6:rotate("y", 30)
s6:scale(1, 2, 1)
s6:set_material(mat3)

-- Cone
s7 = gr.cone('s7', 0.3)
scene_root:add_child(s7)
s7:translate(-3, -3, 0)
s7:rotate("y", -10)
s7:rotate("x", 45)
s7:rotate("x", 180)
s7:set_material(mat1)

s8 = gr.cone('s8', 0.3)
scene_root:add_child(s8)
s8:translate(0, -3, 0)
s8:rotate("x", 45)
s8:rotate("x", 180)
s8:set_material(mat2)

s9 = gr.cone('s9', 0.3)
scene_root:add_child(s9)
s9:translate(3, -3, 0)
s9:rotate("y", 10)
s9:rotate("x", 45)
s9:rotate("x", 180)
s9:set_material(mat3)

-- Torus
s10 = gr.torus('s10', 0.3)
scene_root:add_child(s10)
s10:translate(-3, -6, 0)
s10:rotate("y", -15)
s10:rotate("x", 15)
s10:set_material(mat1)

s11 = gr.torus('s11', 0.3)
scene_root:add_child(s11)
s11:translate(0, -6, 0)
s11:rotate("x", 15)
s11:set_material(mat2)

s12 = gr.torus('s12', 0.3)
scene_root:add_child(s12)
s12:translate(3, -6, 0)
s12:rotate("y", 15)
s12:rotate("x", 15)
s12:set_material(mat3)

white_light = gr.light({-800.0, 0.0, 800.0}, {0.5, 0.5, 0.5}, {1, 0, 0})
light = gr.light({0.0, 0.0, 800.0}, {0.2, 0.2, 0.2}, {1, 0, 0})

gr.render(scene_root, 'simple.png', 500, 750,
	  {0, 0, 10}, {0, 0, -1}, {0, 1, 0}, 50,
	  {0.3, 0.3, 0.3}, {white_light, light})
