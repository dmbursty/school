<%inherit file="/sbig.mako" />
<%namespace name="helper" file="/helper.mako" />

<%def name="more_head_tags()">
  <link rel="stylesheet" type="text/css" href="styles/smoothness/jquery-ui-1.8.2.custom.css" />

  <script type="text/javascript"
  src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.0/jquery.min.js"></script>
  <script type="text/javascript" src="scripts/jquery-ui-1.8.2.custom.min.js"></script>

  <script type="text/javascript">
    $(document).ready(function() {
      $(".slider").slider({
        range: true,
        min: 0,
        max: 1,
        step: 0.1,
        values: [${c.start}, ${c.end}],
        slide: function(event, ui) {
          $(".slider-display").html("&nbsp;to view sentiments "
                  + ui.values[0] + " to " + ui.values[1]);
        }
      });

      $(".range-go").bind('click', function(){
        window.location="custom?query=${c.query}&start=" +
                        $(".slider").slider("values")[0] +
                        "&end=" +
                        $(".slider").slider("values")[1];
      });
    });
  </script>
</%def>

<input class="range-go" type="button" value="Click"><div class="slider-display">
  &nbsp;to view sentiments ${c.start} to ${c.end}</div>
<div class="slider"></div>
<br />
<div class="current-range">Currently viewing results for sentiment ${c.start} to
${c.end}</div><br/>

% for result in c.results:
  ${helper.printResult(result)}
% endfor
