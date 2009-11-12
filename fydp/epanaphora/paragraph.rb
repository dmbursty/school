#

require 'sentence'

module RhetoricalFigures
	class Paragraph < Array
		
		attr_reader :valid, :solo
		
		def valid?( ) return @valid.nil? ? self.validate( ) : @valid end
		
		def initialize( s )
			super( s.split( /([\.|\!|\?]\s+)/ ).map{ |x| Sentence.new( x ) } )
		end
		
		def to_s
			buf="<p>\n"
			self.each{ |x|
				buf+= x.to_html( 1, false )
			}
			buf+="</p>\n"
			return buf
		end
		
		def to_html( m, n )
			buf="<p>\n"
			self.each{ |x|
				buf+= x.to_html( m, n )
			}
			buf+="</p>\n"
			return buf
		end

		def validate
			@valid=true
			@solo=!(self.length>2)
			self.each{ |x| 
				x.solo = @solo
				@valid = x.valid? && @valid
			}
			return @valid
		end
		
		
		def idfilter( s )
			return true if s.eql?( "i" ) or s.eql?( "you" ) or s.eql?( "he" ) or s.eql?( "she" ) or s.eql?( "it" ) or s.eql?( "we" ) or s.eql?( "they" )
			return false
		end
		

		def whfilter( s )
			return true if s.eql?( "who" ) or s.eql?( "what" ) or s.eql?( "where" ) or s.eql?( "why" ) or s.eql?( "when" ) or s.eql?( "how" )
			return false
		end
		
		
		def gluefilter( s )
			return true if s.eql?( "and" ) or s.eql?( "or" )
			return false
		end
		
		
		## inputs:
        # minl      - minimum length of ngram to find
		# maxl      - maximum length of ngram to find (infinite if false)
		# mintuple	- minimum number of tuples per match
		# maxtuple	- maximum number of tuples per match (infinite if false)
		# distance  - maximum distance between closest elements of tuple (infinite if false)
		# wh        - whether the matched sentences should be Wh- questions
		# id        - whether to filter for personal pronouns
		# icase     - whether to ignore case when matching (e.g. downcase all strings before parsing) 
		def epanaphora?( minlen=1, maxlen=false, mintuple=1, maxtuple=false, distance=false, wh=false, id=false, gl=false, icase=false )
			ret=false
			return false if @solo
			return false if maxtuple and mintuple>maxtuple
			return false if maxlen   and minlen>maxlen
			
			self.each_index{ |x|
				tmp=self[x].strip
				next if tmp.eql?( "." ) or tmp.eql?( "!" ) or tmp.eql?( "?" )
				d = self.length - x - 1
				d = distance*2 if distance and d>(distance*2)
				self.slice( x+1..x+1+d ).each_index{ |y|
					if icase then
						ax=self[x	 ].downcase.split
						ay=self[x+y+1].downcase.split
					else
						ax=self[x	 ].split
						ay=self[x+y+1].split
					end
					l=ax.length<ay.length ? ax.length : ay.length
					next if l<minlen
					next if wh and not self.whfilter( ax[0].downcase )
					next if id and not self.idfilter( ax[0].downcase )
					next if gl and not self.gluefilter( ax[0].downcase )
					c=0
					(0..l).each{ |i|
						break unless ax[i]==ay[i]
						c+=1
					}
					if c>=minlen then
						# exceeded maximum ngram length
						return false if maxlen and c>maxlen
						self[x].pairings<<[ self[x], self[x+y+1] ]
						self[x+y+1].pairings=self[x].pairings
						
						# Exceeded maximum tuple size
						return false if maxtuple and self[x].pairings.length>maxtuple
						ret=true if self[x].pairings.length>=mintuple
						break
					end
				}
			}
			return ret
		end
	end
end



