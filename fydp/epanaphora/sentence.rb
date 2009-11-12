# Copyright (c) 2009 Claus Strommer
#
# All rights reserved
#

module RhetoricalFigures
	class Sentence < String
		attr_reader :valid
		attr_accessor :pairings, :solo
		
		def valid?( ) 
			@pairings=[] unless @pairings
			return @valid.nil? ? self.validate( ) : @valid 
		end
		
		def validate( )
			@valid = self.validateCharacters( 4 )
			return @valid
		end
		
		def validateCharacters( threshold )
			## strict rule, ASCII only
			#return s =~ /^[[:print:]]*$/
			
			# Reject if there are a certain amount of garbage characters
			c=0
			self.unpack( 'C'*self.length ).each{ |b|
				case b
					when 195 then c+=1
					when 194 then c+=1
				end
			}
			return c>threshold ? false : true
		end
		
		def to_html( m, n )
			return self.colorize( "#000000", "#FFFF66" ) unless self.valid?
			# anything below this already passes @valid
			return self.colorize( "#CCCCCC", "#FFFFFF" ) if @solo
			return self.to_s if n and @pairings.length>n
			return self.colorize( "#000000", "#FF6666" ) if @pairings.length>=m
			return self.to_s
		end
		
		def colorize( cf, cb )
			return "<span style=\"background-color: " + cb + "; color: "+ cf + "\">" + self.to_s + "</span>\n"
		end

		
		def repair( )
			unless self.validate( ) then
				a = self.unpack( 'C'*self.length )
				a.map!{|b|
					case b
						when 187 then 62 #>>, >
						when 171 then 60 #<<, <
						when 163 then 36 #pound, $
						when 183 then 46 #mdot, .
						when 169 then 67 #(c), C
						when 239 then 105 #&iuml, i
						when 174 then 82 #(R), R
						when 250 then 117 #%utilde, u
						when 215 then 120 #times, x
						when 172 then 32 #not, ' '
						when 233 then 101 #etilde, e
						when 243 then 111 #ouml,o
						when 246 then 111 #otilde,o
						when 216 then 48 #empty set,0
						when 194 then 32 #degree, ' '
						else b
					end
				}
				x=a.pack( 'c'*a.length )
				unless self.validate( x ) then
					puts "[-] " + x
					return false
				end
			end
			return x
		end
	end
end
