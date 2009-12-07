mat = gr.material({0, 0, 0}, {0, 0, 0}, 0)
mat:set_texture("wood.png")

r = gr.material({1, 0, 0}, {0, 0, 0}, 0)
r:set_refract(1.5)
b = gr.material({0, 0, 1}, {0, 0, 0}, 0)

shine = gr.material({0, 0, 0}, {0, 0, 0}, 0)
shine:set_reflect(1)

sr = gr.node('root')
sr:rotate("x", 45)

l = 5
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
-- Front
  --{0, 4, 6, 2},
-- Bottom
  {1, 5, 4, 0},
-- Right
  --{4, 5, 7, 6},
-- Left
  --{1, 0, 2, 3},
-- Top
  --{2, 6, 7, 3},
-- Back
  --{5, 1, 3, 7},
} )
room:translate(0, l-0.01, 0)
room:set_material(mat)
sr:add_child(room)

s1 = gr.cylinder('s1')
s1:translate(-3, 0, 0)
s1:scale(0.5, 3, 0.5)
s1:set_material(r)
sr:add_child(s1)

s2 = gr.sphere('s2')
s2:translate(3, 1, 0)
s2:set_material(r)
sr:add_child(s2)

c1 = gr.cylinder('c1')
c1:set_material(shine)
c1:scale(1, 0.5, 1)
--sr:add_child(c1)

c2 = gr.cylinder('c2')
c2:set_material(shine)
c2:scale(0.9, 0.6, 0.9)
c2:translate(0, -0.05, 0)
--sr:add_child(c2)

cy = gr.difference(c1, c2)
cy:scale(1.2, 1, 1.2)
sr:add_child(cy)

white_light = gr.light({25, 25, 10}, {1, 1, 1}, {1, 0, 0})

gr.render(sr, 'caustics.png', 500, 500,
	  {0, 1, 10}, {0, 0, -1}, {0, 1, 0}, 50,
	  {0.3, 0.3, 0.3}, {white_light})
