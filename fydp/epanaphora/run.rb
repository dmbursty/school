#!/usr/bin/ruby

require 'figurefinder'
require 'socket'


def processDir( s )
ff = RhetoricalFigures::FigureFinder.new( )
	Dir.foreach( s ){ |f|
		if f =~ /\.txt$/ then
			f = s+ "/" + f
			buf = ""
			File.open( f, "r" ){ |fin| buf = fin.read }
			ff.parse( buf )
			unless ff.valid then
				#puts f.gsub( /lynx/, "reject" )+".html"
				#File.open( f.gsub( /lynx/, "reject" )+".html", "w"  ){ |fout| writeHtml( ff.buffer, fout ) }
			else
				if ff.epanaphora then 
					puts f.gsub( /lynx/, "epanaphora" )+".html"
					File.open( f.gsub( /lynx/, "epanaphora" )+".html", "w"  ){ |fout| writeHtml( ff.buffer, fout ) }
				else
					#puts f.gsub( /lynx/, "out" )+".html"
					#File.open( f.gsub( /lynx/, "out" )+".html", "w"  ){ |fout| writeHtml( ff.buffer, fout ) }

				end
			end
		end
	}
	#return Dir.entries( s ).select{ |e| e=~/lynx$/ }.length
end


def writeHtml( s, f )
	f.write( "<head></head>\n" )
	f.write( "<body bgcolor=\" #FFFFFF\">\n" )
	f.write( s )
	f.write( "</body>" )
end


Datafolder = ENV['HOME'] + "/Code/blogdata/lynx/"

#buf=""
#File.open( ENV['HOME'] + "/Code/blogdata/20051206-bloghps-000-chunk00001.lynx", "r" ){ |fin|
#	buf = fin.read
#}
#ff = RhetoricalFigures::FigureFinder.new( )
#ff.parse( buf )

#processDir( ENV["HOME"] + "/Code/Thesis/test" )

#(6..6).each { |i| 
(6..31).each { |i|
	processDir( Datafolder + sprintf( "200512%02d", i ) ) 
}


(1..31).each { |i|
	processDir( Datafolder + sprintf( "200601%02d", i ) ) 
}
