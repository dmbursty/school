rootnode = gr.node('root')

red = gr.material({1.0, 0.0, 0.0}, {0.1, 0.1, 0.1}, 10)
blue = gr.material({0.0, 0.0, 1.0}, {0.1, 0.1, 0.1}, 10)
green = gr.material({0.0, 1.0, 0.0}, {0.1, 0.1, 0.1}, 10)
grey = gr.material({0.7, 0.7, 0.7}, {0.1, 0.1, 0.1}, 10)

-- Body
body = gr.sphere('body')
body:set_material(red)
rootnode:add_child(body)
body:scale(0.8, 1.4, 0.8);

-- Left Shoulder
lshoulder = gr.sphere('lshoulder')
lshoulder:set_material(red)
rootnode:add_child(lshoulder)
lshoulder:translate(-0.7, 0.8, 0);
lshoulder:scale(0.5, 0.5, 0.5);

-- Right Shoulder
rshoulder = gr.sphere('rshoulder')
rshoulder:set_material(red)
rootnode:add_child(rshoulder)
rshoulder:translate(0.7, 0.8, 0);
rshoulder:scale(0.5, 0.5, 0.5);

-- Neck
neckj = gr.joint('neckj', {0, 0, 90}, {0, 0, 0})
neckj:translate(0, 1.0, 0)
rootnode:add_child(neckj)
neck = gr.sphere('neck')
neck:set_material(grey)
neck:translate(0, 0.45, 0)
neck:scale(0.3, 0.3, 0.3)
neckj:add_child(neck)

-- Head
headj = gr.joint('headj', {-50, 0, 30}, {-90, 0, 90})
headj:translate(0, 0.6, 0)
neckj:add_child(headj)
head = gr.sphere('head')
head:set_material(grey)
head:scale(0.6, 0.6, 0.6)
head:translate(0, 0.5, 0)
headj:add_child(head)

nose = gr.sphere('nose')
nose:set_material(blue)
nose:scale(0.1, 0.1, 0.5)
nose:translate(0, 0.5, 0.5)
headj:add_child(nose)

leye = gr.sphere('leye')
leye:set_material(blue)
leye:translate(0.2, 0.4, 0.55)
leye:scale(0.1, 0.15, 0.1)
headj:add_child(leye)
reye = gr.sphere('leye')
reye:set_material(blue)
reye:translate(-0.2, 0.4, 0.55)
reye:scale(0.1, 0.15, 0.1)
headj:add_child(reye)



-- Left Arm
larmj = gr.joint('larmj', {-90, 0, 90}, {0, 0, 0})
larmj:translate(-0.8, 0.8, 0)
rootnode:add_child(larmj)
larm = gr.sphere('larm')
larm:set_material(grey)
larm:scale(0.25, 0.5, 0.25)
larm:translate(0, -1, 0)
larmj:add_child(larm)

-- Left Forearm
lfarmj = gr.joint('lfarmj', {-90, 0, 20}, {0, 0, 0})
lfarmj:translate(0, -0.8, 0)
larmj:add_child(lfarmj)
lfarm = gr.sphere('lfarm')
lfarm:set_material(grey)
lfarm:scale(0.25, 0.5, 0.25)
lfarm:translate(0, -0.8, 0)
lfarmj:add_child(lfarm)

-- Left Hand
lhandj = gr.joint('lhandj', {-90, 0, 90}, {0, 0, 0})
lhandj:translate(0, -0.8, 0)
lfarmj:add_child(lhandj)
lhand = gr.sphere('lhand')
lhand:set_material(grey)
lhand:scale(0.25, 0.25, 0.25)
lhand:translate(0, -0.5, 0)
lhandj:add_child(lhand)

-- Right Arm
rarmj = gr.joint('rarmj', {-90, 0, 90}, {0, 0, 0})
rarmj:translate(0.8, 0.8, 0)
rootnode:add_child(rarmj)
rarm = gr.sphere('rarm')
rarm:set_material(grey)
rarm:scale(0.25, 0.5, 0.25)
rarm:translate(0, -1, 0)
rarmj:add_child(rarm)

-- Right Forefarm
rfarmj = gr.joint('rfarmj', {-90, 0, 20}, {0, 0, 0})
rfarmj:translate(0, -0.8, 0)
rarmj:add_child(rfarmj)
rfarm = gr.sphere('rfarm')
rfarm:set_material(grey)
rfarm:scale(0.25, 0.5, 0.25)
rfarm:translate(0, -0.8, 0)
rfarmj:add_child(rfarm)

-- Right Hand
rhandj = gr.joint('rhandj', {-90, 0, 90}, {0, 0, 0})
rhandj:translate(0, -0.8, 0)
rfarmj:add_child(rhandj)
rhand = gr.sphere('rhand')
rhand:set_material(grey)
rhand:scale(0.25, 0.25, 0.25)
rhand:translate(0, -0.5, 0)
rhandj:add_child(rhand)


-- Left Leg
llegj = gr.joint('llegj', {-90, 0, 90}, {0, 0, 0})
llegj:translate(-0.4, -0.8, 0)
rootnode:add_child(llegj)
lleg = gr.sphere('lleg')
lleg:set_material(grey)
lleg:scale(0.3, 0.8, 0.3)
lleg:translate(0, -1, 0)
llegj:add_child(lleg)

-- Left lower leg
llolegj = gr.joint('llolegj', {-20, 0, 90}, {0, 0, 0})
llolegj:translate(0.0, -1.35, 0)
llegj:add_child(llolegj)
lloleg = gr.sphere('lloleg')
lloleg:set_material(grey)
lloleg:scale(0.3, 0.8, 0.3)
lloleg:translate(0, -1, 0)
llolegj:add_child(lloleg)

-- Left Foot
lfootj = gr.joint('lfootj', {-45, 0, 45}, {0, 0, 0})
lfootj:translate(0, -1.35, 0)
llolegj:add_child(lfootj)
lfoot = gr.sphere('lfoot')
lfoot:set_material(grey)
lfoot:scale(0.25, 0.25, 0.5)
lfoot:translate(0, -0.9, 0.55)
lfootj:add_child(lfoot)


-- Right Leg
rlegj = gr.joint('llegj', {-90, 0, 90}, {0, 0, 0})
rlegj:translate(0.4, -0.8, 0)
rootnode:add_child(rlegj)
rleg = gr.sphere('lleg')
rleg:set_material(grey)
rleg:scale(0.3, 0.8, 0.3)
rleg:translate(0, -1, 0)
rlegj:add_child(rleg)

-- Right lower leg
rlolegj = gr.joint('rlolegj', {-20, 0, 90}, {0, 0, 0})
rlolegj:translate(0.0, -1.35, 0)
rlegj:add_child(rlolegj)
rloleg = gr.sphere('rloleg')
rloleg:set_material(grey)
rloleg:scale(0.3, 0.8, 0.3)
rloleg:translate(0, -1, 0)
rlolegj:add_child(rloleg)

-- Right Foot
rfootj = gr.joint('rfootj', {-45, 0, 45}, {0, 0, 0})
rfootj:translate(0, -1.35, 0)
rlolegj:add_child(rfootj)
rfoot = gr.sphere('rfoot')
rfoot:set_material(grey)
rfoot:scale(0.25, 0.25, 0.5)
rfoot:translate(0, -0.9, 0.55)
rfootj:add_child(rfoot)



rootnode:translate(0, 0.5, -12.0)
rootnode:rotate('y', 20.0)

return rootnode
