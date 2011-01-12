<%inherit file="/sbig.mako" />
<%namespace name="helper" file="/helper.mako" />

<%def name="more_head_tags()">
  <script type="text/javascript" 
    src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js">
  </script>
  <script type="text/javascript" src="scripts/highcharts.js"></script>
  <script type="text/javascript">
    var chart;
    $(document).ready(function() {
      chart = new Highcharts.Chart({
        chart: {
          renderTo: 'container',
          margin: [0,10,0,0]
        },
        title: {
          text: null,
        },
        plotArea: {
          shadow: null,
          borderWidth: null,
          backgroundColor: null
        },
        tooltip: {
          formatter: function() {
            return '<b>'+ this.point.name +'</b>: '+ this.y +' results';
          }
        },
        plotOptions: {
          pie: {
            borderColor: '#000000',
            cursor: 'pointer',
            dataLabels: {
              enabled: true,
              formatter: function() {
                if (this.y > 5) return this.point.name;
              },
              color: 'white',
              style: {
                font: '13px Trebuchet MS, Verdana, sans-serif'
              }
            }
          }
        },
        legend: {
          enabled: true,
          layout: 'vertical',
          style: {
            left: 'auto',
            bottom: 'auto',
            right: '0px',
            top: '0px'
          }
        },
        series: [{
          type: 'pie',
          name: 'Good vs. Bad',
          data: [
            {
              name: 'Bad',
              y: ${len(c.badResults)},
              color: '#F95353'
            },
            {
              name: 'Good',  
              sliced: true,
              y: ${len(c.goodResults)},
              color: '#49BD43'
            }
          ]
        }]
      });
    });
  </script>

</%def>


<table align="center">
  ## Headers
  <tr>
    <td colspan=2>
      <table>
        <tr>
          <td>
            <div id="container" style="width: 300px; height: 200px; margin: 0 auto"></div>
          </td>
          <td class="tagcloud">
            ${helper.tagCloud(c.goodTerms, c.badTerms)}
          </td>
        </tr>
        <tr>
        <td colspan="2" align="right" style="font-size: 8pt">
          <a href="analysis?query=${c.query.replace(" ", "+")}">More analysis</a>
        </td>
      </table>
    </td>
  </tr>
  <tr>
    <td class="goodhead"></td>
    <td class="badhead"></td>
  </tr>
  ## Results
  <tr>
    <td class="goodresults">
      % for result in c.goodResults:
        ${helper.printResult(result)}
      % endfor
    </td>
    <td class="badresults">
      % for result in c.badResults:
        ${helper.printResult(result)}
      % endfor
    </td>
  </tr>
</table>
