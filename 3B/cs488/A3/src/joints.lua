--
-- CS488 -- Introduction to Computer Graphics
-- 
-- a3mark.lua
--
-- A very simple scene creating a trivial puppet.  The TAs will be
-- using this scene as part of marking your assignment.  You'll
-- probably want to make sure you get reasonable results with it!

rootnode = gr.node('root')

red = gr.material({1.0, 0.0, 0.0}, {0.1, 0.1, 0.1}, 10)
blue = gr.material({0.0, 0.0, 1.0}, {0.1, 0.1, 0.1}, 10)
green = gr.material({0.0, 1.0, 0.0}, {0.1, 0.1, 0.1}, 10)
white = gr.material({1.0, 1.0, 1.0}, {0.1, 0.1, 0.1}, 10)

org = gr.sphere('org')
rootnode:add_child(org)
org:translate(0.0, -2.0, 0.0)
org:scale(0.3, 0.3, 0.3)
org:set_material(white)

j1 = gr.joint('j1', {-90, 0, 90}, {-90, 0, 90})
j1:translate(0.0, -2.0, 0.0)
rootnode:add_child(j1)
j2 = gr.joint('j2', {-90, 0, 90}, {-90, 0, 90})
j2:translate(0.0, -2.0, 0.0)
rootnode:add_child(j2)
j3 = gr.joint('j3', {-90, 0, 90}, {-90, 0, 90})
j3:translate(0.0, -2.0, 0.0)
rootnode:add_child(j3)

s1 = gr.sphere('s1')
j1:add_child(s1)
s1:translate(0.0, 2.0, 0.0)
s1:scale(0.1, 2.0, 0.1)
s1:set_material(red)

s2 = gr.sphere('s2')
j2:add_child(s2)
s2:translate(2.0, 0.0, 0.0)
s2:rotate('z', -90.0)
s2:scale(0.1, 2.0, 0.1)
s2:set_material(blue)

s3 = gr.sphere('s3')
j3:add_child(s3)
s3:scale(0.1, 0.1, 2.0)
s3:translate(0.0, 0.0, 1.0)
s3:set_material(green)

j = gr.joint('j', {-90, 0, 90}, {-90, 0, 90})
j:translate(0.0, 0.0, 4.0)
j3:add_child(j)

o = gr.sphere('o')
j:add_child(o)
o:scale(0.4, 0.2, 0.2)
o:set_material(green)

rootnode:translate(-0.75, 0.25, -10.0)
rootnode:rotate('y', -20.0)

return rootnode
