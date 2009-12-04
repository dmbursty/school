mat = gr.material({0.3, 0.3, 1}, {0.3, 0.3, 0.3}, 5, 0, 0, "", "")
mirror = gr.material({1.0, 0.1, 0.1}, {0, 0, 0}, 0, 0, 1.5, "", "")
checker = gr.material({0.1, 0.1, 0.1}, {0, 0, 0}, 0, 0, 0, "checker2.png", "")


scene_root = gr.node('root')
--scene_root:rotate("x", 45)
--
l = 7
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
room:set_material(checker)
scene_root:add_child(room)

s1 = gr.cube('s1', 0.4)
--s1:translate(2, -5, 0)
--s1:rotate('x', 45)
--s1:rotate('y', 30)
s1:rotate('x', 30)
s1:rotate('y', 30)
--s1:rotate('y', 90)
s1:scale(5, 5, 3)
s1:translate(-0.5, -0.5, 1)
s1:set_material(mirror)
scene_root:add_child(s1)

white_light = gr.light({0.0, 0.0, 7}, {0.5, 0.5, 0.5}, {1, 0, 0})
light = gr.light({0.0, 300.0, 800.0}, {0.7, 0.7, 0.7}, {1, 0, 0})

gr.render(scene_root, 'simple.png', 500, 500,
	  {0, 0, 20}, {0, 0, -1}, {0, 1, 0}, 50,
	  {0.3, 0.3, 0.3}, {white_light})
