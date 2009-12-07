tile = gr.material({0.8, 0.8, 0.8}, {0, 0, 0}, 0)
tile:set_bumpmap("checker_bump.png")

water_mat = gr.material({0.2, 0.2, 0.6}, {0.3, 0.3, 0.3}, 5)
--water_mat:set_texture("mann2.png")
water_mat:set_bumpmap("ripple.png")
water_mat:set_reflect(0.3)

sink_mat = gr.material({0.7, 0.7, 0.7}, {0.3, 0.3, 0.3}, 5)
sink_mat:set_reflect(0.2)
sink_mat:set_gloss(0.15)

mirror = gr.material({0, 0, 0}, {0, 0, 0}, 0)
mirror:set_reflect(0.7)

stream_mat = gr.material({0.3, 0.3, 1.0}, {0, 0, 0}, 0)
stream_mat:set_texture("stream.png")
stream_mat:set_bumpmap("stream_bump.png")
stream_mat:set_reflect(0.2)

glass = gr.material({1, 0, 0}, {0, 0, 0}, 0)
glass:set_refract(1.9)

tb_mat = gr.material({0, 0, 0}, {0, 0, 0}, 0)
tb_bristle = gr.material({0, 0, 0}, {0, 0, 0}, 0)
tb_tip = gr.material({1, 0, 0}, {0, 0, 0}, 0)
tb_mat:set_texture("toothbrush.png")
tb_mat:set_bumpmap("toothbrush_bump.png")
tb_bristle:set_texture("tb_end.png")
tb_tip:set_texture("tb_tip.png")

stopper_mat = gr.material({1, 1, 0.8}, {0, 0, 0}, 0)
metal = gr.material({0.4, 0.4, 0.4}, {0.8, 0.8, 0.8}, 10)
--metal:set_reflect(0.1)
--metal:set_gloss(0.5)

donut = gr.material({0, 0, 0}, {0, 0, 0}, 0)
bite = gr.material({0.25, 0, 0}, {0, 0, 0}, 0)
donut:set_texture("donut_tex.png")
donut:set_bumpmap("donut_bump.png")

door_mat = gr.material({0.4, 0, 0}, {0, 0, 0}, 0)
handle_mat = gr.material({0.6, 0.6, 0}, {1, 1, 1}, 5)
portrait = gr.material({0, 0, 0}, {0, 0, 0}, 0)
portrait:set_texture("mann.png")

sr = gr.node('root')
sr:rotate('x', 30)
sr:rotate('y', -30)

--------------
-- The Room --
--------------

room = gr.node('room')

wall = gr.cube('wall')
wall:set_material(tile)
wall:scale(20, 20, 0.1)
wall:translate(-0.5, -0.5, -0.5)

back = gr.node('back')
back:add_child(wall)
back:translate(0, 0, -2.5)
room:add_child(back)

left = gr.node('left')
left:add_child(wall)
left:translate(-10, 0, 7.5)
left:rotate('y', 90)
room:add_child(left)

right = gr.node('right')
right:add_child(wall)
right:translate(10, 0, 7.5)
right:rotate('y', 90)
room:add_child(right)

behind = gr.node('behind')
behind:add_child(wall)
behind:translate(0, 0, 17.5)
room:add_child(behind)

floor = gr.node('floor')
floor:add_child(wall)
floor:translate(0, -10, 7.5)
floor:rotate('x', 90)
room:add_child(floor)

top = gr.node('top')
top:add_child(wall)
top:translate(0, 10, 7.5)
top:rotate('x', 90)
room:add_child(top)

sr:add_child(room)

-- Mirror
m = gr.cube('mirror')
m:set_material(mirror)
m:translate(0, 2, -2.4)
m:scale(10, 5, 0.001)
m:translate(-0.5, 0, 1)
sr:add_child(m)

-- Portrait
mann = gr.cube('mann')
mann:set_material(portrait)
mann:translate(-9.9, 4, 6)
mann:rotate('y', 90)
mann:rotate('z', 180)
mann:scale(4, 4, 1)
mann:scale(0.8, 1, 0.01)
mann:translate(-0.5, -0.5, -0.5)
sr:add_child(mann)

-- Door
door = gr.cube('door')
door:set_material(door_mat)
door:translate(0, 0, 17.4)
door:scale(10, 20, 0.1)
door:translate(-0.5, -0.5, -0.5)
sr:add_child(door)

-- Handle
handle = gr.sphere('handle')
handle:set_material(handle_mat)
handle:translate(-3.5, 0, 16.4)
handle:scale(0.5, 0.5, 0.5)
sr:add_child(handle)



--------------
-- The Sink --
--------------

basin_box = gr.cube('basin_box')
basin_box:set_material(sink_mat)
basin_box:scale(2, 2, 2)
basin_box:translate(-0.5, -0.5, -0.5)

basin_cutout = gr.sphere('basin_cutout')
basin_cutout:set_material(sink_mat)
basin_cutout:translate(0, 1, 0.2)
basin_cutout:scale(0.7, 0.7, 0.7)

basin = gr.difference(basin_box, basin_cutout)

water = gr.cube('water')
water:set_material(water_mat)
water:translate(0, 0, 0.2)
water:scale(1.8, 1.3, 1.8)
water:translate(-0.5, -0.5, -0.5)

water_clip = gr.intersect(water, basin_cutout)

sink = gr.union(basin, water_clip)
sink:scale(5, 1, 3)

sr:add_child(sink)

------------
-- Faucet --
------------

thick = 0.15

neck = gr.torus('neck', thick)
neck:set_material(mirror)

neck_co = gr.cube('neck_co')
neck_co:set_material(mirror)
neck_co:scale(2, 2, 1)
neck_co:translate(0, 0, -0.5)

f_top = gr.sphere('f_top')
f_top:set_material(mirror)
f_top:translate(0, 1, 0)
f_top:scale(thick, thick, thick)

f_spout = gr.cylinder('f_spout')
f_spout:set_material(mirror)
f_spout:translate(0, 1-thick, 0)
f_spout:scale(thick, thick, thick)

stream = gr.cylinder('stream')
stream:set_material(stream_mat)
stream:translate(0, -0.5, 0)
stream:scale(thick/2, 1.5, thick/2)

faucet = gr.intersect(neck, neck_co)
faucet = gr.union(faucet, f_top)
faucet = gr.union(faucet, f_spout)
faucet = gr.union(faucet, stream)
faucet:translate(0, 0, 0.5)
faucet:scale(2.5, 2.5, 2.5)
faucet:rotate('y', 90)
sr:add_child(faucet)

---------
-- Cup --
---------

cthick = 0.1

cout = gr.cylinder('cout')
cout:set_material(glass)

cin = gr.cylinder('cin')
cin:translate(0, cthick/2, 0)
cin:scale(1-cthick, 1, 1-cthick)
cin:set_material(glass)

cup = gr.difference(cout, cin)
cup:scale(0.6, 2, 0.6)

-- Toothbrush

tbrod = gr.cylinder('toothbrush rod')
tbrod:set_material(tb_mat)
tbrod:scale(0.07, 2.5, 0.07)

tbend = gr.cube('tb_end')
tbend:set_material(tb_bristle)
tbend:translate(0, 2.5, 0)
tbend:scale(0.4, 0.4, 0.1)
tbend:translate(0, -1, -0.5)

tbtip = gr.cube('tb_tip')
tbtip:set_material(tb_tip)
tbtip:translate(0.3, 2.55, 0)
tbtip:scale(0.5, 0.5, 0.2)
tbtip:translate(0, -1, -0.5)

tbbris = gr.difference(tbend, tbtip)
tb = gr.union(tbrod, tbbris)
tb:translate(0.4, 0.1, 0)
tb:rotate('z', 25)
tb:rotate('y', -120)

c = gr.node('cup')
c:translate(3.9, 1.001, -1.2)
c:add_child(cup)
c:add_child(tb)

sr:add_child(c)

------------------
-- Sink Stopper --
------------------

ssbody = gr.cone('ssbody')
ssbody:set_material(stopper_mat)
ssbody:translate(0, 1, 0)
ssbody:scale(0.5, 2, 0.5)
ssbody:translate(0, -1, 0)

ss_co = gr.cube('ss_co')
ss_co:translate(0, 0.5, 0)
ss_co:scale(1, 2, 1)
ss_co:translate(-0.5, -1, -0.5)
ss_co:set_material(stopper_mat)

ssmain = gr.difference(ssbody, ss_co)

ss_ring = gr.torus('ss_ring', 0.4)
ss_ring:set_material(metal)
ss_ring:translate(0, 1.1, 0)
ss_ring:scale(0.15, 0.15, 0.15)

stopper = gr.union(ssmain, ss_ring)
stopper:translate(-3.7, 0.401, -1.7)

sr:add_child(stopper)

-----------
-- Donut --
-----------

d = gr.torus('donut', 0.4)
d:set_material(donut)

b1 = gr.cylinder('b1')
b1:set_material(bite)

b2 = gr.cylinder('b2')
b2:set_material(bite)
b2:translate(0.5, 0, 1.2)

b3 = gr.cylinder('b3')
b3:set_material(bite)
b3:translate(0.5, 0, -1.2)

ba = gr.union(b1, b2)

b = gr.union(ba, b3)
b:translate(1.2, 0, -0.5)
b:rotate('x', 90)
b:scale(0.4, 1, 0.4)

s = gr.difference(d, b)
s:translate(4, 1.2, 2)
s:scale(0.6, 0.6, 0.6)
s:rotate('y', -45)
s:rotate('x', 90)
sr:add_child(s)


white_light = gr.light({1, 5, 10}, {1, 1, 1}, {1, 0, 0})

gr.render(sr, 'final_scene.png', 500, 500,
	  {1, 1, 10}, {0, 0, -1}, {0, 1, 0}, 50,
	  {0.3, 0.3, 0.3}, {white_light})
