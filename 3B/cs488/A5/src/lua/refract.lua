mat2 = gr.material({0.7, 1.0, 0.7}, {0.5, 0.7, 0.5}, 0, 0, 0, "wood.png", "")

mat3 = gr.material({0.7, 1.0, 0.7}, {0.5, 0.7, 0.5}, 0, 0, 2.5, "", "")

mat1  = gr.material({0.8, 0, 0}, {0.5, 0.7, 0.5}, 0, 0, 0, "", "")

scene = gr.node('node');

plane = gr.cube( 'cube' );
z = 6
for x = -10, 10, 1 do
  for y = -10, 10, 1 do
     plane = gr.cube( 'cube' )
     plane:set_material( mat2 )
     plane:translate( x, y, z )
     plane:rotate( 'z', '180' )
     scene:add_child( plane )
     end
end


for x = -0, 2, 2 do
  for y = -0, 2, 2 do
     cube = gr.cube( 'cube' )
     cube:translate( x, y + 1, 3 )
     cube: set_material( mat1 )
     scene:add_child( cube )
     sphere = gr.sphere( 'sphere' )
     sphere:translate( x, y, 0 )
     sphere:set_material( mat3 )
     scene:add_child( sphere )
  end
end

gr.render(scene,
         'refract.png', 512, 512,
         {0, 1, -5}, {0, 0, 1}, {0, 1, 0}, 50,
         {0.3, 0.3, 0.3}, {gr.light({0, 200, -100}, {0.6, 0.6, 0.6}, {1, 0, 0})})
