m = gr.material({1, 0, 0}, {0, 0, 0}, 0, 0, 0, "", "")
mat = gr.material({1, 0, 0}, {1, 1, 1}, 5, 0, 1.5, "", "")
fl = gr.material({0, 0, 0}, {0, 0, 0}, 0, 0, 0, "wood.png", "")
back = gr.material({0, 0, 0}, {0, 0, 0}, 0, 0, 0, "checker2.png", "")

scene_root = gr.node('root')
scene_root:rotate("x", 30)

bg = gr.mesh('bg', {
  {-1, -1, 0},
  {1, -1, 0},
  {1, 1, 0},
  {-1, 1, 0},
    }, {
  {0, 1, 2, 3},
} )
bg:set_material(back)
bg:translate(0, 0, -30)
bg:scale(30, 30, 30)
--scene_root:add_child(bg)

s = gr.sphere('s')
s:translate(0, 1, 0)
s:scale(1, 1, 1)
s:set_material(mat)
scene_root:add_child(s)

c = gr.sphere('c')
c:translate(0, 0, -4)
c:set_material(m)
scene_root:add_child(c)

floor = gr.mesh('floor', {
  {-1, 0, -1},
  {1, 0, -1},
  {1, 0, 1},
  {-1, 0, 1},
    }, {
  {0, 3, 2, 1},
} )
floor:translate(0, -0.01, -5)
floor:scale(10, 10, 10)
floor:set_material(fl)
scene_root:add_child(floor)

l = 1
room = gr.mesh('room', {
  {-l, -l, -l},
  {-l, -l, l},
  {-l, l, -l},
  {-l, l, l},
  {l, -l, -l},
  {l, -l, l},
  {l, l, -l},
  {l, l, l},
    }, {
  {0, 4, 6, 2},
  --{1, 5, 4, 0},
  --{4, 5, 7, 6},
  --{1, 0, 2, 3},
  --{2, 6, 7, 3},
  --{5, 1, 3, 7},
} )
room:set_material(mat)
room:translate(0, 1, 0)
--scene_root:add_child(room)

white_light = gr.light({0, 5, 5}, {1, 1, 1}, {1, 0, 0})

gr.render(scene_root, 'simple.png', 500, 500,
	  {0, 0, 10}, {0, 0, -1}, {0, 1, 0}, 50,
	  {0.3, 0.3, 0.3}, {white_light})
