require('readobj')

-- Materials
grass = gr.material({0.1, 0.7, 0.1}, {0.0, 0.0, 0.0}, 0)
hide = gr.material({0.84, 0.6, 0.53}, {0.3, 0.3, 0.3}, 20)
barnwood = gr.material({0.7, 0, 0}, {0, 0, 0}, 0)
grey = gr.material({0.2, 0.2, 0.2}, {0, 0, 0}, 0)
white = gr.material({1, 1, 1}, {0, 0, 0}, 0)

yellow = gr.material({1, 1, 0}, {1, 1, 1}, 30)
silver = gr.material({0.7, 0.7, 0.7}, {1, 1, 1}, 30)

-- ##############################################
-- the scene
-- ##############################################

scene = gr.node('scene')
scene:rotate('X', 23)

-- the floor

plane = gr.mesh('plane', {
		   { -1, 0, -1 }, 
		   {  1, 0, -1 }, 
		   {  1,  0, 1 }, 
		   { -1, 0, 1  }
		}, {
		   {3, 2, 1, 0}
		})
scene:add_child(plane)
plane:set_material(grass)
plane:scale(40, 30, 30)

-- ##############################################
-- Some Cows - Being ubducted
-- ##############################################

-- Unfortunate cow :(
cow = gr.mesh('cow', readobj('cow.obj'))
cow:set_material(hide)
cow:translate(2, 6, 13)
cow:rotate('z', 60)
cow:rotate('x', -20)
cow:scale(0.8, 0.8, 0.8)
scene:add_child(cow)

-- Curious cows
cow1 = gr.mesh('cow1', readobj('cow.obj'))
cow1:set_material(hide)
cow1:translate(-8, 3, 13)
cow1:scale(0.8, 0.8, 0.8)
scene:add_child(cow1)

cow2 = gr.mesh('cow2', readobj('cow.obj'))
cow2:set_material(hide)
cow2:translate(7, 3, 20)
cow2:rotate('y', 120)
cow2:scale(0.8, 0.8, 0.8)
scene:add_child(cow2)


-- ##############################################
-- A UFO
-- ##############################################

ufo = gr.node('ufo')
ufo:scale(2, 2, 2)
ufo:translate(3, 9, 10)
ufo:rotate('z', -20)
ufo:rotate('x', 10)
scene:add_child(ufo)

main = gr.sphere('main')
main:set_material(yellow)
ufo:add_child(main)

rim = gr.sphere('rim')
rim:set_material(silver)
rim:translate(0, -0.35, 0)
rim:scale(3, 0.5, 3)
ufo:add_child(rim)


-- ##############################################
-- A Barn
-- ##############################################
barn = gr.node('barn')
barn:scale(20, 20, 20)
barn:translate(-1.2, 0, -0.8)
barn:rotate('y', 30)
scene:add_child(barn)

box = gr.cube('box')
box:set_material(barnwood)
box:scale(1, 0.5, 1)
barn:add_child(box)

-- Barn Corners

c1 = gr.cube('c1')
c1:set_material(white)
c1:translate(0.975, 0, 0.975)
c1:scale(0.05, 0.5, 0.05)
barn:add_child(c1)

c2 = gr.cube('c2')
c2:set_material(white)
c2:translate(-0.025, 0, 0.975)
c2:scale(0.05, 0.5, 0.05)
barn:add_child(c2)

c3 = gr.cube('c3')
c3:set_material(white)
c3:translate(-0.025, 0, -0.025)
c3:scale(0.05, 0.5, 0.05)
barn:add_child(c3)

c4 = gr.cube('c4')
c4:set_material(white)
c4:translate(0.975, 0, -0.025)
c4:scale(0.05, 0.5, 0.05)
barn:add_child(c4)

-- The Barn Roof
rooffront = gr.mesh('rooffront', {
    { 0, 0.5, 1},
    { 0.35, 0.7, 1},
    { 0.35, 0.85, 1},
    { 0.5, 0.95, 1},
    { 0.65, 0.85, 1},
    { 0.65, 0.7, 1},
    { 1, 0.5, 1},
    { 0, 0.5, 0},
    { 0.35, 0.7, 0},
    { 0.35, 0.85, 0},
    { 0.5, 0.95, 0},
    { 0.65, 0.85, 0},
    { 0.65, 0.7, 0},
    { 1, 0.5, 0},
      }, {
    { 0, 6, 5, 1},
    { 1, 5, 4, 2},
    { 4, 3, 2},
    { 7, 8, 12, 13},
    { 8, 9, 11, 12},
    { 9, 10, 11},
    { 1, 2, 9, 8},
    { 5, 12, 11, 4},
} )
rooffront:set_material(barnwood)
barn:add_child(rooffront)

rooftop = gr.mesh('rooftop', {
    { 0, 0.5, 1},
    { 0.35, 0.7, 1},
    { 0.35, 0.85, 1},
    { 0.5, 0.95, 1},
    { 0.65, 0.85, 1},
    { 0.65, 0.7, 1},
    { 1, 0.5, 1},
    { 0, 0.5, 0},
    { 0.35, 0.7, 0},
    { 0.35, 0.85, 0},
    { 0.5, 0.95, 0},
    { 0.65, 0.85, 0},
    { 0.65, 0.7, 0},
    { 1, 0.5, 0},
      }, {
    { 0, 1, 8, 7},
    { 5, 6, 13, 12},
    { 2, 3, 10, 9},
    { 3, 4, 11, 10},
} )
rooftop:set_material(grey)
barn:add_child(rooftop)

-- The Barn Door
z = 1.01
d = 0.1
w = 1
h = 1
door1 = gr.mesh('door1', {
    {0, 0, z}, {w, 0, z}, {w, h, z}, {0, h, z},
    {0, d, z}, {d, 0, z}, {w-d, 0, z}, {w, d, z},
    {w, h-d, z}, {w-d, h, z}, {d, h, z}, {0, h-d, z},
      }, {
    { 0, 5, 10, 3}, { 0, 1, 7, 4}, { 1, 2, 9, 6}, { 2, 3, 11, 8},
    { 5, 8, 9, 4}, { 6, 7, 10, 11},
} )
door1:set_material(white)
door1:scale(0.25, 0.35, 1)
door1:translate(1, 0, 0)
barn:add_child(door1)

door2 = gr.mesh('door2', {
    {0, 0, z}, {w, 0, z}, {w, h, z}, {0, h, z},
    {0, d, z}, {d, 0, z}, {w-d, 0, z}, {w, d, z},
    {w, h-d, z}, {w-d, h, z}, {d, h, z}, {0, h-d, z},
      }, {
    { 0, 5, 10, 3}, { 0, 1, 7, 4}, { 1, 2, 9, 6}, { 2, 3, 11, 8},
    { 5, 8, 9, 4}, { 6, 7, 10, 11},
} )
door2:set_material(white)
door2:scale(0.25, 0.35, 1)
door2:translate(2, 0, 0)
barn:add_child(door2)

window = gr.mesh('window', {
    {0, 0, z}, {w, 0, z}, {w, h, z}, {0, h, z},
    {0, d, z}, {d, 0, z}, {w-d, 0, z}, {w, d, z},
    {w, h-d, z}, {w-d, h, z}, {d, h, z}, {0, h-d, z},
      }, {
    { 0, 5, 10, 3}, { 0, 1, 7, 4}, { 1, 2, 9, 6}, { 2, 3, 11, 8},
    { 5, 8, 9, 4}, { 6, 7, 10, 11},
} )
window:set_material(white)
window:scale(0.2, 0.2, 1)
window:translate(2, 3, 0)
barn:add_child(window)

global_light = gr.light({-200, 202, 430}, {0.8, 0.8, 0.8}, {1, 0, 0})
ufo_light = gr.light({5.2, 15.5, 19.3}, {1.0, 1.0, 1.0}, {1, 0, 0})

gr.render(scene,
	  'sample.png', 500, 500,
	  {2, 2, 45}, {0, 0, -1}, {0, 1, 0}, 50,
	  {0.4, 0.4, 0.4}, {global_light, ufo_light})
