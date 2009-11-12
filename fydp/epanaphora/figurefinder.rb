#

require 'sentence'
require 'paragraph'

module RhetoricalFigures
	class FigureFinder
		attr_reader :buffer, :epanaphora, :valid
		
		def parse( s )
			@epanaphora=false
			@valid=true
			@buffer=""
			
			mnl		=1
			mxl		=2
			mnt		=2
			mxt		=4
			dis		=false
			wh		=true
			id		=false
			glue	=false
			icase	=false
			
			s.split( /\n\s*/ ).map{ |x| Paragraph.new( x ) }.each{ |a|
				@valid=a.valid? && @valid
				break unless @valid
				@epanaphora = true if a.epanaphora?( mnl, mxl, mnt, mxt, dis, wh, id, glue, icase )
				@buffer+=a.to_html( mnt, mxt )
			}
			return @valid
		end
	end
end



