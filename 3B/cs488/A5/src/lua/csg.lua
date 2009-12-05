m = gr.material({1, 0, 0}, {0, 0, 0}, 0, 0, 0, "checker1.png", "")
n = gr.material({0, 0, 1}, {0, 0, 0}, 0, 0, 0, "checker1.png", "")
b = gr.material({0, 0, 1}, {0, 0, 0}, 0, 0, 0, "", "")
checker = gr.material({0, 1, 0}, {0, 0, 0}, 0, 0, 0, "checker1.png", "")

scene_root = gr.node('root')
scene_root:rotate('x', 15)
scene_root:rotate('y', 15)

w = gr.mesh('w', {
  {-1, -1, 0},
  {1, -1, 0},
  {1, 1, 0},
  {-1, 1, 0},
    }, {
  {0, 1, 2, 3},
} )
w:set_material(checker)
w:translate(0, 0, -5)
w:scale(5, 5, 5)
--scene_root:add_child(w)

l = gr.sphere('l')
l:set_material(m)
--l:translate(-0.5, 0, 0)
--scene_root:add_child(l)

r = gr.cylinder('r', 0.4)
r:set_material(n)
r:translate(0.5, 0, 0.5)
--r:rotate('x', 90)
--scene_root:add_child(r)

s = gr.sphere('s')
s:set_material(b)
s:translate(0, 0.2, 0.2)
s:scale(0.1, 0.1, 0.1)
scene_root:add_child(s)

t = gr.torus('t', 0.3)
t:set_material(checker)

c = gr.difference(r, t)
c:translate(-0.1, 0, 0)
--scene_root:add_child(c)

d = gr.difference(l, c)
scene_root:add_child(d)


white_light = gr.light({0, 1, 5}, {0.7, 0.7, 0.7}, {1, 0, 0})

gr.render(scene_root, 'simple.png', 500, 500,
	  {0, 0, 5}, {0, 0, -1}, {0, 1, 0}, 50,
	  {0.3, 0.3, 0.3}, {white_light})
